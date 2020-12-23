package war;

import java.awt.Dimension;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Continente;
import model.Deck;
import model.Mapa;
import model.CartaTerritorio;

import com.google.gson.Gson;

public class Utilidades {
	class Territorios {
		List<TerritorioWar> territorios;

		public void setTerritorios(List<TerritorioWar> ts) {
			this.territorios = ts;
		}

		public List<TerritorioWar> getTerritorio() {
			return this.territorios;
		}
	}

	class TerritorioWar {
		String nome;
		Integer continente;
		Integer tipo;
		List<List<java.lang.Double>> pontosFronteira;

		public String getNome() {
			return this.nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public int getContinent() {
			return this.continente;
		}

		public void setContinent(int c) {
			this.continente = c;
		}

		public int getTipo() {
			return this.tipo;
		}

		public void setTipo(int c) {
			this.tipo = c;
		}

		public List<List<java.lang.Double>> getPontosFronteira() {
			return this.pontosFronteira;
		}

		public void setBoundsPoints(List<List<java.lang.Double>> pontosFronteira) {
			this.pontosFronteira = pontosFronteira;
		}
	}

	public static void carregarTerritorios(Mapa mapa, Deck deck) {
		String conteudoJson;
		try {
			conteudoJson = lerArquivo("imagens/territorios.json",
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Territorios ts = new Gson().fromJson(conteudoJson, Territorios.class);
		for (TerritorioWar t : ts.getTerritorio()) {
			List<Point2D.Double> pontos = new LinkedList<Point2D.Double>();
			for (List<java.lang.Double> bp : t.getPontosFronteira()) {
				Point2D.Double ponto = new Point2D.Double(bp.get(0), bp.get(1));
				pontos.add(ponto);
			}
			model.Territorio novoTerritorio = new model.Territorio(
					t.getNome(), pontos, Continente.getContinenteId(t.getContinent()));
			mapa.addTerritorio(novoTerritorio);
			CartaTerritorio c = new CartaTerritorio(novoTerritorio, t.getTipo());
			deck.addCarta(c);
		}
	}

	public static ArrayList<Line2D.Double> getSegmentosLinhas(GeneralPath p) {

		ArrayList<double[]> pontos = new ArrayList<>();
		ArrayList<Line2D.Double> segmentos = new ArrayList<>();

		double[] coordenadas = new double[6];

		for (PathIterator pi = p.getPathIterator(null); !pi.isDone(); pi.next()) {
			int tipo = pi.currentSegment(coordenadas);
			double[] pathIteratorCoordenadas = { tipo, coordenadas[0], coordenadas[1] };
			pontos.add(pathIteratorCoordenadas);
		}

		double[] inicioPoligono = new double[3]; 

		for (int i = 0; i < pontos.size(); i++) {
			double[] elementoAtual = pontos.get(i);

			double[] nextElement = { -1, -1, -1 };
			if (i < pontos.size() - 1) {
				nextElement = pontos.get(i + 1);
			}

			if (elementoAtual[0] == PathIterator.SEG_MOVETO) {
				inicioPoligono = elementoAtual;
			}

			if (nextElement[0] == PathIterator.SEG_LINETO) {
				segmentos.add(new Line2D.Double(elementoAtual[1],
						elementoAtual[2], nextElement[1], nextElement[2]));
			} else if (nextElement[0] == PathIterator.SEG_CLOSE) {
				segmentos.add(new Line2D.Double(elementoAtual[1],
						elementoAtual[2], inicioPoligono[1], inicioPoligono[2]));
			}
		}
		return segmentos;
	}

	public static String lerArquivo(String arquivo, Charset codificacao)
			throws IOException {
		byte[] bytesArq = Files.readAllBytes(Paths.get(arquivo));
		return new String(bytesArq, codificacao);
	}

	public static Dimension getTamanhoJogo() {
		Dimension x = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		if(System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0){
			x.setSize(x.width, (int)(x.height*0.9));
		}
		return x;
	}
}