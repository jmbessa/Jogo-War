package model;

public enum TipoCarta {
	Trg(3), Sqr(4), Crc(0), Jk(1);
	
	private final int id;

	TipoCarta(int value) {
		this.id = value;
	}

	public int getId() {
		return this.id;
	}

	public static TipoCarta getCartaId(int tipoId) {
		TipoCarta tipoCarta = null;

		if (tipoId == 3) {
			tipoCarta = TipoCarta.Trg;
		}
		else if (tipoId == 4) {
			tipoCarta = TipoCarta.Sqr;
		}
		else if (tipoId == 0){
			tipoCarta = TipoCarta.Crc;
		}
		else if (tipoId == 1){
			tipoCarta = TipoCarta.Jk;
		}

		return tipoCarta ;
	}

	@Override
	public String toString() {
		if (this.id == 0) {
			return "\u25CF";
		}
		else if (this.id == 3) {
			return "\u25B2";
		}
		else if (this.id == 4) {
			return "\u25FC";
		}
		return null;
	}
}