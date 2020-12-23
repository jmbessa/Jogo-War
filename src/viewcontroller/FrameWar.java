package viewcontroller;

import javax.swing.BoxLayout; 
import javax.swing.JFrame;

import war.Utilidades;
import model.Jogador;
import model.Territorio;

@SuppressWarnings("serial")
public class FrameWar extends JFrame {

	private PainelUI painelUI;
	private PainelMapa painelMapa;
	private FrameAtaque frameAtaque;
	private FrameTexto frameTexto;
	private FrameEscolherNumero frameEscolherNumero;
	private FrameSelecaoCarta frameSelecaoCarta;

	public FrameWar() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Jogo War");
		this.setSize(Utilidades.getTamanhoJogo());
		this.getContentPane().setLayout(
				new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.addPaineis();
		this.setVisible(true);
	}

	private void addPaineis() {
		this.painelMapa = new PainelMapa();
		this.painelUI = new PainelUI();
		this.getContentPane().add(painelMapa);
		this.getContentPane().add(painelUI);
	}

	public PainelMapa getPainelMapa() {
		return this.painelMapa;
	}

	public PainelUI getPainelUI() {
		return this.painelUI;
	}

	public void iniciarJogo() {
		this.atualizar(true);
	}

	public void atualizar(boolean primeiro) {
		this.painelUI.atualizar(primeiro);
		this.painelMapa.atualizar(primeiro);
	}

	public void centralizarPopUp() {
		if (this.frameAtaqueAtivado()) {
			this.frameAtaque.toFront();
		} else if (this.frameEscolherNumeroAtivo()) {
			this.frameEscolherNumero.toFront();
		} else if (this.frameTextoAtivado()) {
			this.frameTexto.toFront();
		} else if (this.frameSelecaoCartasAtivo()) {
			this.frameSelecaoCarta.toFront();
		}
	}

	public boolean frameAtaqueAtivado() {
		return this.frameAtaque != null && this.frameAtaque.isVisible();
	}

	public boolean frameEscolherNumeroAtivo() {
		return this.frameEscolherNumero != null
				&& this.frameEscolherNumero.isVisible();
	}

	public boolean frameTextoAtivado() {
		return this.frameTexto != null && this.frameTexto.isVisible();
	}

	public boolean frameSelecaoCartasAtivo() {
		return this.frameSelecaoCarta != null
				&& this.frameSelecaoCarta.isVisible();
	}

	public boolean popupAtivado() {
		return this.frameAtaqueAtivado() || this.frameEscolherNumeroAtivo()
				|| this.frameTextoAtivado()
				|| this.frameSelecaoCartasAtivo();
	}

	public void gerarFrameAtaque(Territorio de, Territorio para, int numero) {
		if (!this.popupAtivado()) {
			this.frameAtaque = new FrameAtaque(de, para, numero);
			this.frameAtaque.setVisible(true);
		}
	}

	public void frameNumeroExercitosPosicionar(int numero, String mensagem) {
		if (!this.popupAtivado()) {
			this.frameEscolherNumero = new FrameEscolherNumero(numero, mensagem);
			this.frameEscolherNumero.setVisible(true);
		}
	}

	public void gerarFrameTexto(String mensagem) {
		if (!this.popupAtivado()) {
			this.frameTexto = new FrameTexto(mensagem);
			this.frameTexto.setVisible(true);
		}
	}

	public void frameSelecaoDeCartas(Jogador jogadorAtual,
			int numMaxCartas, boolean forcadoTrocar) {
		if (!this.popupAtivado()) {
			this.frameSelecaoCarta = new FrameSelecaoCarta(jogadorAtual,
					numMaxCartas, forcadoTrocar);
			this.frameSelecaoCarta.setVisible(true);
		}
	}

}
