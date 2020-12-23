package model;

public class Carta {
	TipoCarta tipo;
	
	public Carta(TipoCarta tipoDeCarta) {
		this.tipo = tipoDeCarta;
	}

	public Carta(int idCarta) {
		this.tipo = TipoCarta.getCartaId(idCarta);
	}

	public TipoCarta getTipo() {
		return this.tipo;
	}
}
