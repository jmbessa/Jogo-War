package war;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import model.Carta;
import model.Continente;
import model.Deck;
import model.Mapa;
import model.Jogador;
import model.Territorio;
import model.CartaTerritorio;
import model.SituacoesWar;
import model.SituacoesWar.EstadoJogada;
import objective.ObjetivoConquistarContinentes;
import objective.ObjetivoConquistarTerritorios;
import objective.ObjetivoDestruirJogador;
import objective.WarObjetivo;
import serialize.DeserializadorWar;
import serialize.SerializadorWar;
import viewcontroller.FrameWar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JogoWar { 

	private static JogoWar instancia;

	private FrameWar warFrame;
	private SituacoesWar situacaoWar = null;
	private int salvarNome = Math.abs((new Random()).nextInt());

	private JogoWar() {
		this.warFrame = new FrameWar();
	}

	public static JogoWar getInstancia() {
		if (JogoWar.instancia == null) {
			JogoWar.instancia = new JogoWar();
		}
		return JogoWar.instancia;
	}

	public void carregarJogo(String arquivoSalvo) {
		String jsonContent;
		try {
			jsonContent = Utilidades.lerArquivo(String.format("saves/%s.warsave", arquivoSalvo), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(SituacoesWar.class, new DeserializadorWar());
		Gson gson = gsonBuilder.create();
		this.situacaoWar = gson.fromJson(jsonContent, SituacoesWar.class);
		for (Jogador j : this.getSituacaoAtual().getJogadores()) {
			for (Carta c : j.getCartas()) {
				this.getSituacaoAtual().getDeck().removeCarta(c);
			}
		}
		this.getSituacaoAtual().getDeck().addJoker(2);
		this.getSituacaoAtual().getDeck().embaralhar();
		this.getMapa().calculaVizinhos();
		this.getWarFrame().iniciarJogo();
		this.getSituacaoAtual().addObserver(this.getWarFrame().getPainelMapa());
		this.getSituacaoAtual().addObserver(this.getWarFrame().getPainelUI());
		if (this.getJogadorAtual().getCartas().size() >= 5) {
			this.mostrarCartas(true);
		}
	}

	public void comecarJogo(List<Jogador> jogadores) {
		Collections.shuffle(jogadores);
		this.situacaoWar = new SituacoesWar(jogadores, new Mapa(), new Deck());
		Utilidades.carregarTerritorios(this.getSituacaoAtual().getMapa(), this.getSituacaoAtual()
				.getDeck());
		this.getSituacaoAtual().getDeck().addJoker(2);
		this.getSituacaoAtual().getDeck().embaralhar();
		this.distribuirTerritorios();
		this.getMapa().calculaVizinhos();
		this.selecionarObjetivosJogadores();
		this.getJogadorAtual().forneceExercitos(
				LogicaWar.calculoGanharExercitos(this.getMapa(),
						this.getJogadorAtual()));
		this.getWarFrame().iniciarJogo();
		this.getSituacaoAtual().addObserver(this.getWarFrame().getPainelMapa());
		this.getSituacaoAtual().addObserver(this.getWarFrame().getPainelUI());
	}


	private SituacoesWar getSituacaoAtual() {
		return this.situacaoWar;
	}

	public List<Jogador> getJogadores() {
		return this.getSituacaoAtual().getJogadores();
	}

	public Jogador getNomeJogador(String nome) {
		for (Jogador p : this.getJogadores()) {
			if (p.getNome().equals(nome)) {
				return p;
			}
		}
		return null;
	}

	public Mapa getMapa() {
		return this.getSituacaoAtual().getMapa();
	}

	public Deck getDeck() {
		return this.getSituacaoAtual().getDeck();
	}

	public Jogador getJogadorAtual() {
		return this.getSituacaoAtual().getJogadorAtual();
	}

	public int getIndexJogadorAtual() {
		return this.getSituacaoAtual().getIndexJogadorAtual();
	}

	public EstadoJogada getEstadoAtual() {
		return this.getSituacaoAtual().getEstadoAtual();
	}

	public boolean atacando() {
		return this.getEstadoAtual().equals(EstadoJogada.ATACAR);
	}

	public boolean movendo() {
		return this.getEstadoAtual().equals(EstadoJogada.MOVER_EXERCITO);
	}

	public boolean posicionando() {
		return this.getEstadoAtual().equals(EstadoJogada.POSICIONAR_NOVO_EXERCITO);
	}

	public FrameWar getWarFrame() {
		return this.warFrame;
	}

	public int getQtdExercitosTrocados() {
		return this.getSituacaoAtual().getQtdExercitosTrocados();
	}

	public void focarPopUp() {
		if (warFrame.popupAtivado()) {
			warFrame.centralizarPopUp();
		}
	}

	private void setQtdCartasTrocaExercitos() {
		this.getSituacaoAtual().setQtdCartasTrocaExercitos(
				this.getQtdExercitosTrocados()
						+ LogicaWar.INCREMENTO_EXERCITO_TROCA_DE_CARTAS);
	}

	private void distribuirTerritorios() {
		List<Integer> indexes = new ArrayList<Integer>();
		Iterator<Jogador> pi = this.getJogadores().iterator();
		for (int i = 0; i < this.getMapa().getTerritorio().size(); i++) {
			indexes.add(i);
		}
		Collections.shuffle(indexes);
		for (Integer i : indexes) {
			if (!pi.hasNext()) {
				pi = this.getJogadores().iterator();
			}
			Jogador p = pi.next();
			Territorio t = this.getMapa().getTerritorio().get(i);
			t.setNomeDono(p.getNome());
		}
	}

	private void selecionarObjetivosJogadores() {
		List<WarObjetivo> objetivos = new ArrayList<WarObjetivo>();
		objetivos.add(new ObjetivoConquistarTerritorios(18, 2));
		objetivos.add(new ObjetivoConquistarTerritorios(24, 1));
		objetivos.add(new ObjetivoConquistarContinentes(Continente.EUROPA,
				Continente.OCEANIA, false));
		objetivos.add(new ObjetivoConquistarContinentes(Continente.ASIA,
				Continente.AMERICA_DO_SUL, false));
		objetivos.add(new ObjetivoConquistarContinentes(Continente.EUROPA,
				Continente.AMERICA_DO_SUL, true));
		objetivos.add(new ObjetivoConquistarContinentes(Continente.ASIA,
				Continente.AFRICA, false));
		objetivos.add(new ObjetivoConquistarContinentes(Continente.AMERICA_DO_NORTE,
				Continente.AFRICA, false));
		objetivos.add(new ObjetivoConquistarContinentes(Continente.AMERICA_DO_NORTE,
				Continente.OCEANIA, false));

		for (Jogador p : this.getJogadores()) {
			objetivos.add(new ObjetivoDestruirJogador(p.getNome()));
		}

		Random r = new Random();
		for (Jogador p : this.getJogadores()) {
			int index = 0;
			WarObjetivo obj;
			do {
				index = r.nextInt(objetivos.size());
				obj = objetivos.get(index);
				
			} while (obj instanceof ObjetivoDestruirJogador
					&& ((ObjetivoDestruirJogador) obj)
							.getNomeJogadorAlvo().equals(p.getNome()));
			p.setObjetivo(obj);

			objetivos.remove(index);
		}
	}


	public void proximaVez() {

		if (this.getWarFrame().popupAtivado()) {
			this.getWarFrame().centralizarPopUp();
			return;
		}
		Jogador vencedor = this.checarVencedor();
		if (vencedor != null) {
			this.encerrarJogada(vencedor);
			return;
		}
		if (this.getSituacaoAtual().getConquistadosNaRodada()) {
			this.selecionarCartaParaJogador(this.getJogadorAtual());
		}
		this.getMapa().resetExercitosMoveisQtd();
		this.getSituacaoAtual().proximaVez();
		this.getSituacaoAtual()
				.getJogadorAtual()
				.forneceExercitos(
						LogicaWar.calculoGanharExercitos(this.getMapa(),
								getSituacaoAtual().getJogadorAtual()));
		if (this.getJogadorAtual().getCartas().size() >= 5) {
			this.mostrarCartas(true);
		}
		try {
			this.salvarJogo(String.format("%s_%d", "jogo", salvarNome));
		} catch (IOException e) {

			e.printStackTrace();
		}

		if (this.getMapa().getTerritoriosPorDono(this.getJogadorAtual()).size() == 0) {
			this.proximaVez();
		}
		this.getSituacaoAtual().notifyObservers();
	}

	public void actionPerformed() {

		if (this.getWarFrame().popupAtivado()) {
			this.getWarFrame().centralizarPopUp();
			return;
		}
		switch (this.getEstadoAtual()) {
		case ATACAR:
			this.getSituacaoAtual().comecarMoverExercitos();
			break;
		case MOVER_EXERCITO:
			this.getSituacaoAtual().desfazerSelecao();
			break;
		case POSICIONAR_NOVO_EXERCITO:
			if (this.getTerritorioSelecionado() != null) {
				this.getWarFrame().frameNumeroExercitosPosicionar(
						this.getJogadorAtual().getExercitosNaoColocados(),
						String.format("Quantos exércitos você quer posicionar em %s?",
								this.getTerritorioSelecionado().getNome()));
			}
			break;
		default:
			break;
		}
		this.getSituacaoAtual().notifyObservers();
	}

	public void selecionarTerritorio(Territorio t) {

		if (this.getWarFrame().popupAtivado()) {
			getWarFrame().centralizarPopUp();
			return;
		}
		switch (this.getEstadoAtual()) {
		case ATACAR:
			if (this.getTerritorioSelecionado() == null) {

				if (t.getNomeDono().equals(this.getJogadorAtual().getNome())) {
					this.situacaoWar.selecionarTerritorio(t);
				}
			} else {

				if (t.getNomeDono().equals(this.getJogadorAtual().getNome())) {
					this.situacaoWar.selecionarTerritorio(t);

				} else if (this.getTerritorioSelecionado().podeAtacar(t)) {
					this.getSituacaoAtual().territorioAlvo(t);
					this.getWarFrame()
							.frameNumeroExercitosPosicionar(
									this.getTerritorioSelecionado()
											.getExercitoParaAtaque(),
									String.format(
											"Você quer atacar de %s em %s com quantos exércitos?",
											this.getTerritorioSelecionado()
													.getNome(), this
													.getTerritorioDesejado()
													.getNome()));
				}
			}
			break;
		case MOVER_EXERCITO:
			if (t.getNomeDono().equals(this.getJogadorAtual().getNome())) {

				if (this.getTerritorioSelecionado() == null) {
					this.situacaoWar.selecionarTerritorio(t);

				} else if (!this.getTerritorioSelecionado().podeMoverPara(t)) {
					this.situacaoWar.selecionarTerritorio(t);

				} else {
					this.situacaoWar.territorioAlvo(t);
					this.getWarFrame().frameNumeroExercitosPosicionar(
							this.getTerritorioSelecionado().getQtdExercitosMoveis(),
							String.format("Quantos exércitos deseja mover de %s para %s?",
									this.getTerritorioSelecionado().getNome(), this
											.getTerritorioDesejado().getNome()));
				}
			}
			break;
		case POSICIONAR_NOVO_EXERCITO:
			if (t.getNomeDono().equals(this.getJogadorAtual().getNome())) {
				this.getSituacaoAtual().selecionarTerritorio(t);
			}
			break;
		default:
			break;
		}
		this.getSituacaoAtual().notifyObservers();
	}

	public void selecionarNumero(int numero) {
		switch (this.getEstadoAtual()) {
		case ATACAR:

			if (this.getTerritorioSelecionado().getNomeDono()
					.equals(this.getTerritorioDesejado().getNomeDono())) {
				this.getMapa().moverExercito(this.getTerritorioSelecionado(),
						this.getTerritorioDesejado(), numero - 1, true);
				Jogador vencedor = this.checarVencedor();
				if (vencedor != null) {
					this.encerrarJogada(vencedor);
					return;
				}
				if (this.getSituacaoAtual().getTomarCartas() != null) {
					if (this.getJogadorAtual().getCartas().size() < 5) {
						Jogador jogadorDerrotado = this.getNomeJogador(this
								.getSituacaoAtual().getTomarCartas());
						this.getWarFrame().frameSelecaoDeCartas(
								jogadorDerrotado,
								5 - this.getJogadorAtual().getCartas().size(),
								false);
					}
				}
			} else {
				this.getWarFrame().gerarFrameAtaque(
						this.getTerritorioSelecionado(),
						this.getTerritorioDesejado(), numero);
			}
			break;
		case MOVER_EXERCITO:
			this.getMapa().moverExercito(this.getTerritorioSelecionado(),
					this.getTerritorioDesejado(), numero, false);
			this.getSituacaoAtual().desfazerSelecao();
			break;
		case POSICIONAR_NOVO_EXERCITO:
			this.getJogadorAtual().removerExercitos(numero);
			this.situacaoWar.getTerritorioSelecionado().addExercitos(numero);
			if (this.getJogadorAtual().getExercitosNaoColocados() <= 0) {
				this.situacaoWar.comecarAtaque();
			}
			break;
		default:
			break;
		}
		this.getSituacaoAtual().notifyObservers();
	}

	public void resultadoAtaque(int[] perdas) {
		if (this.atacando()) {
			this.getTerritorioSelecionado().removerExercitos(perdas[0]);
			this.getTerritorioDesejado().removerExercitos(perdas[1]);


			if (this.getTerritorioDesejado().getQtdExercito() == 0) {

				Jogador dono = this.getNomeJogador(this.getTerritorioDesejado()
						.getNomeDono());
				if (this.getMapa().getTerritoriosPorDono(dono).size() == 1) {
					this.getSituacaoAtual().setTomarCartas(dono.getNome());
				}
				int limiteParaMover = this.getMapa().conquistarTerritorio(
						this.getTerritorioSelecionado(),
						this.getTerritorioDesejado());
				this.getSituacaoAtual().setConquistadoNaRodada();
				Jogador vencedor = this.checarVencedor();
				if (vencedor != null) {
					this.encerrarJogada(vencedor);
					return;
				}
				this.warFrame.frameNumeroExercitosPosicionar(limiteParaMover, String.format(
						"Quantos exércitos deseja mover de %s para %s?", this
								.getTerritorioSelecionado().getNome(), this
								.getTerritorioDesejado().getNome()));
			}
		}
		this.getSituacaoAtual().notifyObservers();
	}

	public void mostrarObjetivo() {
		if (this.getWarFrame().popupAtivado()) {
			getWarFrame().centralizarPopUp();
			return;
		}
		this.getWarFrame().gerarFrameTexto(
				this.getJogadorAtual().getObjetivo().getDescricao());
	}

	public void trocarCartas(List<Carta> cartasSelecionadas) {
		if (this.posicionando()) {
			for (Carta c : cartasSelecionadas) {
				this.getJogadorAtual().removeCarta(c);

				if (c instanceof CartaTerritorio) {
					CartaTerritorio tc = (CartaTerritorio) c;
					if (tc.getTerritorio().getNomeDono()
							.equals(this.getJogadorAtual().getNome())) {
						tc.getTerritorio().addExercitos(
								LogicaWar.GANHAR_TERRITORIOS_CARTA_TERRITORIO);
					}
				}
			}
			this.getJogadorAtual().forneceExercitos(
					this.getSituacaoAtual().getQtdExercitosTrocados());
			this.setQtdCartasTrocaExercitos();

		} else {
			for (Carta c : cartasSelecionadas) {
				Jogador perdedor = this.getNomeJogador(this.getSituacaoAtual()
						.getTomarCartas());
				perdedor.removeCarta(c);
				this.getJogadorAtual().addCarta(c);
			}
		}
		this.getSituacaoAtual().notifyObservers();
	}



	public Territorio getTerritorioDesejado() {
		return this.situacaoWar.getTerritorioAlvo();
	}

	public Territorio getTerritorioSelecionado() {
		return this.situacaoWar.getTerritorioSelecionado();
	}

	public void selecionarCartaParaJogador(Jogador p) {
		Carta c = this.getDeck().pegarCarta();
		p.addCarta(c);
	}

	public void receberCartaDeJogador(Jogador p, CartaTerritorio c) {
		p.removeCarta(c);
		this.getDeck().retornaCarta(c);
	}

	public Jogador checarVencedor() {
		for (Jogador p : this.getJogadores()) {
			if (p.vitorioso()) {
				return p;
			}
		}
		return null;
	}

	private void encerrarJogada(Jogador p) {
		System.out.println("Jogo Encerrado!");
		System.out.println("O vencedor é " + p.getNome() + " "
				+ p.getObjetivo().getDescricao());
		this.getWarFrame().getPainelUI().mostrarPainelFimDeJogo(p);
		this.getSituacaoAtual().notifyObservers();
	}

	public void mostrarCartas(boolean trocaForcada) {
		this.getWarFrame().frameSelecaoDeCartas(this.getJogadorAtual(), 3,
				trocaForcada);
	}

	public void salvarJogo(String pastaArquivo) throws IOException {
		SerializadorWar s = new SerializadorWar();
		FileWriter arquivo = new FileWriter(String.format("saves/%s.warsave", pastaArquivo), false);
		try {
			arquivo.write(s.serialize(this.getSituacaoAtual(), null, null).toString());
			System.out.printf("Arquivo salvo em saves/%s.warsave\n", pastaArquivo);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			arquivo.flush();
			arquivo.close();
		}

	}
}
