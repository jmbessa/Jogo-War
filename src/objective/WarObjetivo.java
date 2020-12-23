package objective;

import model.Mapa;
import model.Jogador;

public abstract class WarObjetivo {
	private String descricao;
	
	public WarObjetivo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

	public abstract boolean checarVitoria(Mapa m, Jogador p);
}
