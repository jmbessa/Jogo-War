package serialize;

import java.lang.reflect.Type;
import java.util.List;

import model.Carta;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SerializadorWar implements JsonSerializer<SituacoesWar> {

	@Override
	public JsonElement serialize(final SituacoesWar warState, final Type tipo,
			final JsonSerializationContext contexto) {
		JsonObject resultado = new JsonObject();
		resultado.addProperty("conquistadosNaRodada", warState.getConquistadosNaRodada());
		resultado.addProperty("qtdExercitosTrocados",
				warState.getQtdExercitosTrocados());
		resultado.addProperty("estadoAtual", warState.getEstadoAtual()
				.name());
		resultado.addProperty("jogadorAtual", warState.getJogadorAtual()
				.getNome());
		resultado.addProperty("tomarCartas", warState.getTomarCartas());
		JsonArray jsonArray = new JsonArray();
		for (Jogador j : warState.getJogadores()) {
			jsonArray.add(this.serializarJogador(j, tipo, contexto));
		}
		resultado.add("jogadores", jsonArray);
		resultado.add("mapa", this.serializarMapa(warState.getMapa(), tipo, contexto));
		return resultado;
	}

	public JsonElement serializarJogador(final Jogador j, final Type tipo,
			final JsonSerializationContext contexto) {
		JsonObject resultado = new JsonObject();
		resultado.addProperty("nome", j.getNome());
		resultado.add("exercitosNaoColocados", new JsonPrimitive(j.getExercitosNaoColocados()));
		resultado.addProperty("cor", j.getCor().getRGB());
		resultado.add("cartas", serializarCartas(j.getCartas()));
		resultado.add("objetivo",
				this.serializarObjetivo(j.getObjetivo(), tipo, contexto));

		return resultado;
	}

	public JsonElement serializarObjetivo(final WarObjetivo o,
			final Type tipo, final JsonSerializationContext contexto) {
		JsonObject resultado = new JsonObject();
		if (o instanceof ObjetivoConquistarContinentes) {
			ObjetivoConquistarContinentes occ = (ObjetivoConquistarContinentes) o;
			resultado.addProperty("classe", ObjetivoConquistarContinentes.class.getName());
			resultado.addProperty("primeiroContinente", occ.getPrimeiroAlvo()
					.getId());
			resultado.addProperty("segundoContinente", occ.getSegundoAlvo()
					.getId());
			resultado.addProperty("possuiTerceiroContinente",
					occ.conquistarTerceiroContinente());
		} else if (o instanceof ObjetivoConquistarTerritorios) {
			ObjetivoConquistarTerritorios oct = (ObjetivoConquistarTerritorios) o;
			resultado.addProperty("classe", ObjetivoConquistarTerritorios.class.getName());
			resultado.addProperty("exercitosMinimosPorTerritorio",
					oct.getNumeroExercitosCada());
			resultado.addProperty("territoriosParaConquistar",
					oct.getNumeroTerritoriosConquistar());
		} else if (o instanceof ObjetivoDestruirJogador) {
			ObjetivoDestruirJogador dpo = (ObjetivoDestruirJogador) o;
			resultado.addProperty("classe", ObjetivoDestruirJogador.class.getName());
			resultado.addProperty("jogadorAlvo", dpo.getNomeJogadorAlvo());
		}
		return resultado;
	}

	public JsonArray serializarCartas(List<Carta> l) {
		JsonArray jsonArray = new JsonArray();
		for (Carta c : l) {
			JsonObject jo = new JsonObject();
			int tipo = c.getTipo().getId();
			jo.addProperty("tipo", tipo);
			if (tipo != 1) { 

				// if is not the joker
				CartaTerritorio ct = (CartaTerritorio) c;
				jo.addProperty("nomeTerritorio", ct.getTerritorio().getNome());
			}
			jsonArray.add(jo);
		}
		return jsonArray;
	}

	public JsonElement serializarMapa(final Mapa m, final Type tipo,
			final JsonSerializationContext contexto) {
		JsonObject resultado = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		for (Territorio t : m.getTerritorio()) {
			jsonArray.add(this.serializarTerritorio(t, tipo, contexto));
		}
		resultado.add("territorios", jsonArray);
		return resultado;
	}

	public JsonElement serializarTerritorio(final Territorio t, final Type tipo,
			final JsonSerializationContext contexto) {
		JsonObject resultado = new JsonObject();
		resultado.addProperty("nomeTerritorio", t.getNome());
		resultado.addProperty("qtdExercito", t.getQtdExercito());
		resultado.addProperty("nomeDono", t.getNomeDono());
		resultado.addProperty("qtdExercitosImoveis", t.getQtdExercitosImoveis());
		return resultado;
	}

}
