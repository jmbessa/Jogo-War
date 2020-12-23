package serialize;

import java.awt.Color;  
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import war.Utilidades;
import model.Carta;
import model.Continente;
import model.Deck;
import model.Mapa;
import model.Jogador;
import model.Territorio;
import model.CartaTerritorio;
import model.SituacoesWar;
import objective.ObjetivoConquistarContinentes;
import objective.ObjetivoConquistarTerritorios;
import objective.ObjetivoDestruirJogador;
import objective.WarObjetivo;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DeserializadorWar implements JsonDeserializer<SituacoesWar> {

	@Override
	public SituacoesWar deserialize(JsonElement jsonElem, Type tipo,
			JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObj = jsonElem.getAsJsonObject();
		Mapa mapa = new Mapa();
		Deck deck = new Deck();
		Utilidades.carregarTerritorios(mapa, deck);
		this.deserializarMapa(jsonObj.get("mapa").getAsJsonObject(), mapa);
		List<Jogador> jogadores = this.deserializarJogadores(jsonObj.get("jogadores")
				.getAsJsonArray(), mapa);
		SituacoesWar warState = new SituacoesWar(jogadores, mapa, deck);
		if (jsonObj.get("conquistadosNaRodada").getAsBoolean()) {
			warState.setConquistadoNaRodada();
		}
		warState.setQtdCartasTrocaExercitos(jsonObj.get("qtdExercitosTrocados")
				.getAsInt());
		warState.setEstadoJogada(SituacoesWar.EstadoJogada.valueOf(jsonObj
				.get("estadoAtual").getAsString()));
		warState.setJogadorAtual(jsonObj.get("jogadorAtual").getAsString());
		try {
			warState.setTomarCartas(jsonObj.get("tomarCartas")
					.getAsString());
		} catch (UnsupportedOperationException uoe) {
		}
		return warState;
	}

	private List<Jogador> deserializarJogadores(JsonArray jsonArray, Mapa mapa) {
		List<Jogador> jogadores = new ArrayList<Jogador>();
		for (JsonElement jsonElem : jsonArray) {
			JsonObject jsonObj = jsonElem.getAsJsonObject();
			Color cor = new Color(jsonObj.get("cor").getAsInt(), false);
			Jogador jogador = new Jogador(jsonObj.get("nome").getAsString(), cor);
			jogador.setExercitosNaoColocados(jsonObj.get("exercitosNaoColocados").getAsInt());
			jogador.setObjetivo(this.deserializarObjetivo(jsonObj.get("objetivo")
					.getAsJsonObject()));
			JsonArray jsArray = jsonObj.get("cartas").getAsJsonArray();
			for (JsonElement jsElem : jsArray) {
				jogador.addCarta(this.deserializarCarta(jsElem.getAsJsonObject(), mapa));
			}
			jogadores.add(jogador);
		}
		return jogadores;
	}

	private Carta deserializarCarta(JsonObject jsonObj, Mapa mapa) {
		int tipo = jsonObj.get("tipo").getAsInt();
		if (tipo != 1) { 

			// if not joker
			return new CartaTerritorio(mapa.getNomeTerritorio(jsonObj.get(
					"nomeTerritorio").getAsString()), tipo);
		}

		return new Carta(1);
	}

	private WarObjetivo deserializarObjetivo(JsonObject jsonObj) {
		String nomeClasse = jsonObj.get("classe").getAsString();
		if (nomeClasse.equals(ObjetivoConquistarContinentes.class.getName())) {
			Continente primeiroContinente = Continente.getContinenteId(jsonObj.get("primeiroContinente")
					.getAsInt());
			Continente segundoContinente = Continente.getContinenteId(jsonObj.get("segundoContinente")
					.getAsInt());
			boolean possuiTerceiroContinente = jsonObj.get(
					"possuiTerceiroContinente").getAsBoolean();
			return new ObjetivoConquistarContinentes(primeiroContinente, segundoContinente,
					possuiTerceiroContinente);
		} else if (nomeClasse
				.equals(ObjetivoConquistarTerritorios.class.getName())) {
			int exercitosMinimosPorTerritorio = jsonObj.get("exercitosMinimosPorTerritorio")
					.getAsInt();
			int territoriosParaConquistar = jsonObj.get(
					"territoriosParaConquistar").getAsInt();
			return new ObjetivoConquistarTerritorios(
					territoriosParaConquistar, exercitosMinimosPorTerritorio);
		} else if (nomeClasse.equals(ObjetivoDestruirJogador.class.getName())) {
			return new ObjetivoDestruirJogador(jsonObj.get("jogadorAlvo")
					.getAsString());
		}
		return null;
	}

	private Mapa deserializarMapa(JsonObject jsonObj, Mapa mapa) {
		JsonArray jsonArray = jsonObj.get("territorios").getAsJsonArray();
		for (JsonElement jsonElem : jsonArray) {
			JsonObject jsObj = jsonElem.getAsJsonObject();
			Territorio territorio = mapa.getNomeTerritorio(jsObj.get("nomeTerritorio").getAsString());
			this.deserializarTerritorio(jsObj, territorio);
		}
		return mapa;
	}

	private Territorio deserializarTerritorio(JsonObject jsonObj, Territorio territorio) {
		territorio.setQtdExercitos(jsonObj.get("qtdExercito").getAsInt());
		territorio.setNomeDono(jsonObj.get("nomeDono").getAsString());
		territorio.setExercitosImoveis(jsonObj.get("qtdExercitosImoveis").getAsInt());
		return territorio;
	}

}
