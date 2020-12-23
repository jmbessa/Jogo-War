package war;

import model.Continente;
import model.Mapa;
import model.Jogador;

public class LogicaWar {

	public final static int MAX_JOGADORES = 6; 
	public final static int MIN_JOGADORES = 3; 
	public final static int MAX_DADOS = 3; 
	public final static int GANHAR_TERRITORIOS_CARTA_TERRITORIO = 2; 
	public final static int NUMERO_MINIMO_EXERCITOS_POR_RODADA = 3; 
	public final static int INCREMENTO_EXERCITO_TROCA_DE_CARTAS= 2; 

	public static int calculoGanharExercitos(Mapa m, Jogador p) { 
		int qtdTerritoriosGanhos = m.getTerritoriosPorDono(p).size() / 2;
		int qtdContinentesGanhos = 0;

		for (Continente c : m.getContinenteJogador(p)) {
			System.out.println(String.format("Jogador %s possui %s e ganha %d territorios do mesmo", p.getNome(), c.toString(), c.conquistarTerritorios()));
			qtdContinentesGanhos += c.conquistarTerritorios();
		}
		

		return Math.max(qtdTerritoriosGanhos + qtdContinentesGanhos, NUMERO_MINIMO_EXERCITOS_POR_RODADA);
	}
}
