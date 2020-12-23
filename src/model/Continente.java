package model;


public enum Continente {

	AMERICA_DO_NORTE(0), AMERICA_DO_SUL(1), AFRICA(2), EUROPA(3), ASIA(4), OCEANIA(
			5);
	private final int id;

	Continente(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static Continente getContinenteId(int id) {
		for (Continente c: Continente.values()) {
			if (c.id == id) {
				return c;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return super.toString().replace("_", " ");
	}

	public int conquistarTerritorios() {
		if(this == AMERICA_DO_NORTE){
			return 5;
		}
		
		if(this == AMERICA_DO_SUL){
			return 2;
		}
		
		if(this == AFRICA){
			return 3;
		}
		
		if(this == EUROPA){
			return 5;
		}
		
		if(this == ASIA){
			return 7;
		}
		
		if(this == OCEANIA){
			return 2;
		}

		// Default
		return 0;
	}
	
	public String defineContinente() {
		if(this == AMERICA_DO_NORTE){
			return "an";
		}
		
		if(this == AMERICA_DO_SUL){
			return "asl";
		}
		
		if(this == AFRICA){
			return "af";
		}
		
		if(this == EUROPA){
			return "eu";
		}
		
		if(this == ASIA){
			return "as";
		}
		
		return "oc";
	}
		
}
