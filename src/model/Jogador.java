package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import war.JogoWar;
import objective.WarObjetivo;

public class Jogador {

	private String nome;
	private Color cor;
	private List<Carta> cartas = new ArrayList<Carta>();
	private WarObjetivo objetivo;
	private static final Color AZUL = new Color(0, 0, 128, 255);
	private static final Color VERDE = new Color(0, 128, 0, 255);
	private static final Color VERMELHO = new Color(128, 0, 0, 255);
	private static final Color AZUL_CLARO = new Color(0, 128, 128, 255);
	private static final Color ROXO = new Color(128, 0, 128, 255);
	private static final Color MARROM = new Color(128, 128, 0, 255);
	public final static Color[] corJogadores = { VERDE, VERMELHO, AZUL, AZUL_CLARO,
			ROXO, MARROM };
	private int exercitosNaoColocados = 0;

	public Jogador(String nome, Color cor) {
		this.nome = nome;
		this.cor = cor;
	}

	public String getNome() {
		return this.nome;
	}

	public Color getCor() {
		return this.cor;
	}

	public static Color getCorPrimeiroPlano(Color c) {
		if (!(c.equals(AZUL) || c.equals(ROXO) || c.equals(VERMELHO))) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

	public int getExercitosNaoColocados() {
		return this.exercitosNaoColocados; //change
	}

	public void setExercitosNaoColocados(int number) {
		this.exercitosNaoColocados = number; //change
	}

	public void forneceExercitos(int number) {
		this.exercitosNaoColocados += number; //change
	}

	public void removerExercitos(int number) {
		this.exercitosNaoColocados -= number; //change
	}

	public void addCarta(Carta c) {
		this.cartas.add(c);
	}

	public void removeCarta(Carta c) {
		this.cartas.remove(c);
	}

	public List<Carta> getCartas() {
		return this.cartas;
	}

	public boolean trocarCartasObrigatorias() {
		return this.cartas.size() >= 5;
	}

	public boolean trocarCartasPossiveis() {
		return trocaValida(this.getCartas());
	}

	public boolean trocaValida(List<Carta> cards) {
		boolean ehValido = false;

		if (this.getCartas().size() >= 3) {
			HashMap<TipoCarta, Integer> qtdTipoCarta = new HashMap<TipoCarta, Integer>();
			boolean umDeCada = true;
			boolean tresIguais = false;

			for (TipoCarta ct : TipoCarta.values()) {
				qtdTipoCarta.put(ct, 0);
			}

			for (Carta c : cards) {
				qtdTipoCarta.put(c.getTipo(),
						qtdTipoCarta.get(c.getTipo()) + 1);
			}
			int qtdJoker = qtdTipoCarta.get(TipoCarta.Jk);
			int jokerGastos = 0;
			for (TipoCarta ct : TipoCarta.values()) {
				if (ct.equals(TipoCarta.Jk)) {
					continue;
				}

			
				if (qtdTipoCarta.get(ct) == 0) {

					
					if (qtdJoker - jokerGastos <= 0) {
						umDeCada = false;
					} 
					else { 
						jokerGastos++;
					}
				}
				if (qtdTipoCarta.get(ct) == 3 - qtdJoker) {
					tresIguais = true;
				}
			}
			ehValido = umDeCada || tresIguais;
		}

		return ehValido;
	}

	public boolean vitorioso() {
		return this.objetivo.checarVitoria(JogoWar.getInstancia().getMapa(), this);
	}

	public WarObjetivo getObjetivo() {
		return this.objetivo;
	}

	public void setObjetivo(WarObjetivo obj) {
		this.objetivo = obj;
	}

}
