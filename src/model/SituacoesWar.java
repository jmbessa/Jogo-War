package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class SituacoesWar extends Observable {

	private Jogador jogadorAtual;
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private String tomarCartas = null;

	private EstadoJogada estadoAtual;

	private Territorio territorioSelecionado;
	private Territorio territorioAlvo;

	private boolean conquistadosNaRodada = false;
	private int qtdExercitosTrocados = 4;

	private Mapa mapa = null;

	private Deck deck;

	public enum EstadoJogada {
		POSICIONAR_NOVO_EXERCITO, ATACAR, MOVER_EXERCITO;
	}

	public SituacoesWar(List<Jogador> jogadores, Mapa mapa, Deck deck) {
		this.jogadores = jogadores;
		this.mapa = mapa;
		this.deck = deck;
		this.jogadorAtual = jogadores.get(0);
		this.estadoAtual = EstadoJogada.POSICIONAR_NOVO_EXERCITO;
	}

	public Mapa getMapa() {
		this.setChanged();
		return this.mapa;
	}

	public List<Jogador> getJogadores() {
		this.setChanged();
		return this.jogadores;
	}

	public Deck getDeck() {
		this.setChanged();
		return deck;
	}

	public EstadoJogada getEstadoAtual() {
		return this.estadoAtual;
	}

	public Jogador getJogadorAtual() {
		this.setChanged();
		return this.jogadorAtual;
	}

	public Territorio getTerritorioSelecionado() {
		this.setChanged();
		return this.territorioSelecionado;
	}

	public Territorio getTerritorioAlvo() {
		this.setChanged();
		return this.territorioAlvo;
	}

	public boolean getConquistadosNaRodada() {
		return this.conquistadosNaRodada;
	}

	public int getQtdExercitosTrocados() {
		return this.qtdExercitosTrocados;
	}

	public String getTomarCartas() {
		return this.tomarCartas;
	}
	
	public int getIndexJogadorAtual() {
		return this.getJogadores().indexOf(this.getJogadorAtual());
	}
	
	public void proximaVez() {
		int indexJogadorAtual = jogadores.indexOf(this.jogadorAtual);
		if (indexJogadorAtual == jogadores.size() - 1) {
			this.jogadorAtual = jogadores.get(0);
		} else {
			this.jogadorAtual = jogadores.get(indexJogadorAtual + 1);
		}
		this.desfazerSelecao();
		this.conquistadosNaRodada = false;
		this.tomarCartas = null;
		this.estadoAtual = EstadoJogada.POSICIONAR_NOVO_EXERCITO;
		this.setChanged();
	}

	public void comecarAtaque() {
		this.desfazerSelecao();
		this.estadoAtual = EstadoJogada.ATACAR;
		this.setChanged();
	}

	public void comecarMoverExercitos() {
		this.desfazerSelecao();
		this.estadoAtual = EstadoJogada.MOVER_EXERCITO;
		this.setChanged();
	}

	public void selecionarTerritorio(Territorio t) {
		this.territorioSelecionado = t;
		this.setChanged();
	}

	public void territorioNaoSelecionado() {
		this.territorioSelecionado = null;
		this.setChanged();
	}

	public void territorioAlvo(Territorio t) {
		this.territorioAlvo = t;
		this.setChanged();
	}

	public void untargetTerritory() {
		this.territorioAlvo = null;
		this.setChanged();
	}

	public void desfazerSelecao() {
		this.territorioSelecionado = null;
		this.territorioAlvo = null;
		this.setChanged();
	}

	public void setConquistadoNaRodada() {
		this.conquistadosNaRodada = true;
		this.setChanged();
	}

	public void setQtdCartasTrocaExercitos(int qtd) {
		this.qtdExercitosTrocados = qtd;
		this.setChanged();
	}

	public void setTomarCartas(String nomeJogador) {
		this.tomarCartas = nomeJogador;
		this.setChanged();
	}

	public void setEstadoJogada(EstadoJogada t) {
		this.estadoAtual = t;
		this.setChanged();
	}

	public void setJogadorAtual(String nomeJogador) {
		for (Jogador p :this.getJogadores()) {
			if (p.getNome().equals(nomeJogador)) {
				this.jogadorAtual = p;
			}
		}
		this.setChanged();
	}
}
