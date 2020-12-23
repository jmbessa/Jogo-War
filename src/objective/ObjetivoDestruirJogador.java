package objective;

import war.JogoWar; 
import model.Mapa;
import model.Jogador;

public class ObjetivoDestruirJogador extends WarObjetivo {

	String jogadorAlvo;

	public ObjetivoDestruirJogador(String jogadorAlvo) {
		super(String.format("Destrua %s completamente", jogadorAlvo));
		this.jogadorAlvo = jogadorAlvo;
	}

	@Override
	public boolean checarVitoria(Mapa m, Jogador p) {
		Jogador alvo = JogoWar.getInstancia().getNomeJogador(jogadorAlvo);
		if (m.getTerritoriosPorDono(alvo).size() != 0) {
			return false;
		}
		return true;
	}

	public String getNomeJogadorAlvo() {
		return this.jogadorAlvo; 
	}

}
