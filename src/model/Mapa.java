package model;

import java.awt.geom.Line2D;  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import war.Utilidades;

public class Mapa extends Object {

	private List<Territorio> territorios = new ArrayList<Territorio>();

	public void addTerritorio(Territorio t) {
		this.territorios.add(t);
	}

	public List<Territorio> getTerritorio() {
		return this.territorios;
	}

	public List<Continente> getContinenteJogador(Jogador p) {
		List<Continente> continentesConquistados = new LinkedList<Continente>();
		HashMap<Continente, Integer> territoriosConquistadosContinente = new HashMap<Continente, Integer>();
		HashMap<Continente, Integer> totalTerritoriosContinente = new HashMap<Continente, Integer>();
		for (Continente c : Continente.values()) {
			territoriosConquistadosContinente.put(c, 0);
			totalTerritoriosContinente.put(c, 0);
		}
		for (Territorio t : this.getTerritorio()) {
			totalTerritoriosContinente.put(t.getContinente(),
					totalTerritoriosContinente.get(t.getContinente()) + 1);
			if (t.getNomeDono().equals(p.getNome())) {
				territoriosConquistadosContinente
						.put(t.getContinente(), territoriosConquistadosContinente
								.get(t.getContinente()) + 1);
			}
		}

		for (Continente c : Continente.values()) {
			if (territoriosConquistadosContinente.get(c) == totalTerritoriosContinente
					.get(c)) {
				continentesConquistados.add(c);
			}
		}

		return continentesConquistados;
	}

	public void calculaVizinhos() {
		ArrayList<Line2D.Double> tLines = new ArrayList<>();
		ArrayList<Line2D.Double> uLines = new ArrayList<>();

		for (Territorio territorioA : territorios) {
			tLines = Utilidades.getSegmentosLinhas(territorioA.getPoligono());
			for (Territorio territorioB : territorios) {
				if (!territorioA.equals(territorioB)) {
					uLines = Utilidades.getSegmentosLinhas(territorioB.getPoligono());
					for (Line2D.Double territoryAfrontier : tLines) {
						for (Line2D.Double territorioVizinho : uLines) {
							if (territoryAfrontier.intersectsLine(territorioVizinho)) {
								territorioA.addVizinho(territorioB);
								continue;
							}
						}
					}
				}
			}
		}

		this.nomeTerritoriosVizinhos("Argélia", "Espanha");
		this.nomeTerritoriosVizinhos("Argélia", "Itália");
		this.nomeTerritoriosVizinhos("Alasca", "Sibéria");
		this.nomeTerritoriosVizinhos("Brasil", "Nigéria");
		this.nomeTerritoriosVizinhos("Austrália", "Indonésia");
		this.nomeTerritoriosVizinhos("Austrália", "Nova Zelândia");
		this.nomeTerritoriosVizinhos("Bangladesh", "Indonésia");
		this.nomeTerritoriosVizinhos("Egito", "Romênia");
		this.nomeTerritoriosVizinhos("Egito", "Jordânia");
		this.nomeTerritoriosVizinhos("França", "Reino Unido");
		this.nomeTerritoriosVizinhos("Groelândia", "Reino Unido");
		this.nomeTerritoriosVizinhos("Índia", "Indonésia");
		this.nomeTerritoriosVizinhos("Japão", "Cazaquistão");
		this.nomeTerritoriosVizinhos("Japão", "Coréia do Norte");
		this.nomeTerritoriosVizinhos("Japão", "Mongólia");
		this.nomeTerritoriosVizinhos("Somália", "Arábia Saudita");
		this.nomeTerritoriosVizinhos("Suécia", "França");
		this.nomeTerritoriosVizinhos("Suécia", "Itália");
		this.nomeTerritoriosVizinhos("Groelândia", "Quebec");
		this.nomeTerritoriosVizinhos("Nova Zelândia", "Indonésia");

	}

	private void nomeTerritoriosVizinhos(String nomeTerritorioX, String nomeTerritorioY) {
		Territorio x, y;
		x = this.getNomeTerritorio(nomeTerritorioX);
		y = this.getNomeTerritorio(nomeTerritorioY);
		if (x == null || y == null) {
			System.out.println("not found" + nomeTerritorioX + " " + nomeTerritorioY);
			return;
		}
		x.addVizinho(y);
		y.addVizinho(x);
	}

	public Territorio getNomeTerritorio(String name) {
		for (Territorio nomeTerritorio : this.territorios) {
			if (nomeTerritorio.getNome().equals(name)) {
				return nomeTerritorio;
			}
		}
		return null;

	}

	public List<Territorio> getTerritoriosPorContinente(Continente c) {
		List<Territorio> lt = new ArrayList<Territorio>();
		for (Territorio territorioPorContinente : this.territorios) {
			if (territorioPorContinente.getContinente() == c)
				lt.add(territorioPorContinente);
		}
		return lt;
	}


	public int conquistarTerritorio(Territorio origem, Territorio destino) {
		destino.setNomeDono(origem.getNomeDono());

		this.moverExercito(origem, destino, 1, true);

		if ((origem.getExercitoParaAtaque() + 1 )> 3) {
			return 3;
		} else {
			return origem.getExercitoParaAtaque() + 1;
		}
	}

	public boolean moverExercito(Territorio from, Territorio to, int quantidade,
			boolean movable) {
		if (!from.getNomeDono().equals(to.getNomeDono())) {
			return false;
		}
		if (from.getQtdExercito() - 1 < quantidade) {
			return false;
		}
		from.removerExercitos(quantidade);
		if (movable) {
			to.addExercitos(quantidade);
		} else {
			to.addExercitosImoveis(quantidade);
		}
		return true;
	}

	public void resetExercitosMoveisQtd() {
		for (Territorio t : this.getTerritorio()) {
			t.resetQtdExercitosImoveis();
		}
	}

	public List<Territorio> getTerritoriosPorDono(Jogador owner) {
		List<Territorio> territoriosGanhos = new LinkedList<Territorio>();
		for (Territorio t : this.getTerritorio()) {
			if (t.getNomeDono().equals(owner.getNome())) {
				territoriosGanhos.add(t);
			}
		}
		return territoriosGanhos;
	}

}
