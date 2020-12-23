package model;

public class CartaTerritorio extends Carta {
	Territorio territorio;

	public CartaTerritorio(Territorio t, TipoCarta tipo) {
		super(tipo);
		this.territorio = t;
	}

	public CartaTerritorio(Territorio t, int tipo) {
		super(tipo);
		this.territorio = t;
	}

	public Territorio getTerritorio() {
		return this.territorio;
	}

}
