package objective;

import model.Mapa;
import model.Jogador;
import model.Territorio;

public class ObjetivoConquistarTerritorios extends WarObjetivo {

	int territoriosParaConquistar;

	int exercitosMinimosPorTerritorio;

	public ObjetivoConquistarTerritorios(int territoriosParaConquistar,
			int exercitosMinimosPorTerritorio) {
		super(
				String.format(
						"Conquistar %d territorios e ocupá-los com, no mínimo, %d exércitos.",
						territoriosParaConquistar, exercitosMinimosPorTerritorio));
		this.territoriosParaConquistar = territoriosParaConquistar;
		this.exercitosMinimosPorTerritorio = exercitosMinimosPorTerritorio;
	}

	@Override
	public boolean checarVitoria(Mapa m, Jogador p) {
		int numeroTerritoriosGanhos = 0;
		for (Territorio t : m.getTerritorio()) {
			if (t.getNomeDono().equals(p.getNome())
					&& t.getQtdExercito() >= this.exercitosMinimosPorTerritorio) {
				numeroTerritoriosGanhos++;
			}
		}
		return numeroTerritoriosGanhos >= this.territoriosParaConquistar;
	}

	public int getNumeroExercitosCada() {
		return this.exercitosMinimosPorTerritorio; 
	}

	public int getNumeroTerritoriosConquistar() {
		return territoriosParaConquistar;
	}

}
