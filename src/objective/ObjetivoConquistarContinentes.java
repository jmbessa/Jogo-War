package objective;

import java.util.List;

import model.Continente;
import model.Mapa;
import model.Jogador;

public class ObjetivoConquistarContinentes extends WarObjetivo {

	Continente primeiroContinente;
	Continente segundoContinente;
	boolean possuiTerceiroContinente;

	public ObjetivoConquistarContinentes(Continente c1, Continente c2,
			boolean possuiTerceiroContinente) {
		super(String.format("Conquistar %s, %s %s", c1.toString(), c2.toString(),
				possuiTerceiroContinente ? "e mais algum continente" : ""));
		this.primeiroContinente = c1;
		this.segundoContinente = c2;
		this.possuiTerceiroContinente = possuiTerceiroContinente;
	}

	@Override
	public boolean checarVitoria(Mapa m, Jogador p) {
		List<Continente> continentesGanhos = m.getContinenteJogador(p);
		return continentesGanhos.contains(primeiroContinente)
				&& continentesGanhos.contains(segundoContinente)
				&& (continentesGanhos.size() >= 3 || !possuiTerceiroContinente);
	}

	public Continente getPrimeiroAlvo() {
		return primeiroContinente; 
	}

	public Continente getSegundoAlvo() {
		return segundoContinente; 
	}

	public boolean conquistarTerceiroContinente() {
		return possuiTerceiroContinente; 
	}

}
