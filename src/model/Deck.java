package model;

import java.util.Collections;
import java.util.Stack;

public class Deck {

	private Stack<Carta> cartas = new Stack<Carta>();

	public Deck() {
	}

	public void addCarta(CartaTerritorio c) {
		this.cartas.push(c);
	}

	public void retornaCarta(CartaTerritorio c) {
		this.cartas.add(0, c);
	}

	public Carta pegarCarta() {
		return this.cartas.pop();
	}

	public void embaralhar() {
		Collections.shuffle(this.cartas);
	}

	public void addJoker() {
		this.cartas.push(new Carta(TipoCarta.Jk));
	}

	public void addJoker(int contador) {
		int i = 0;
		while (i <= contador) {
			addJoker();
			i++;
		}
	}

	public void removeCarta(Carta c) {
		this.cartas.remove(c);
	}

}
