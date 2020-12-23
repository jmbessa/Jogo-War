package model;

import java.awt.geom.GeneralPath; 
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import war.LogicaWar;

public class Territorio extends Object {

	private String nome;
	private GeneralPath poligono;
	private Point2D.Double centro = new Point2D.Double(0., 0.);
	private String nomeDono = null;
	private int qtdExercito = 1;
	private Set<Territorio> vizinhos = new HashSet<Territorio>();
	private Continente continente;
	private int qtdExercitosImoveis = 0;

	public Territorio(String name, List<Point2D.Double> points, Continente c) {
		this.nome = name;
		this.continente = c;
		this.criarPoligono(points);
	}

	public void setNomeDono(String name) {
		this.nomeDono = name;
	}

	public String getNomeDono() {
		return this.nomeDono;
	}

	public Continente getContinente() {
		return this.continente;
	}

	public GeneralPath getPoligono() {
		return this.poligono;
	}

	public Point2D.Double getCentro() {
		return this.centro;
	}

	public int getQtdExercito() {
		return this.qtdExercito;
	}

	public int addExercitos(int exercitos) {
		this.qtdExercito += exercitos;
		return this.qtdExercito;
	}

	public int removerExercitos(int exercitos) {
		this.qtdExercito -= exercitos;
		return this.qtdExercito;
	}

	public void setQtdExercitos(int armies) {
		this.qtdExercito = armies;
	}

	public String getNome() {
		return this.nome;
	}

	public void addVizinho(Territorio t) {
		this.vizinhos.add(t);
	}

	public Set<Territorio> getVizinhos() {
		return this.vizinhos;
	}

	public boolean ehVizinho(Territorio t) {
		return this.vizinhos.contains(t);
	}

	public boolean podeAtacar(Territorio t) {
		return !(
			this.getQtdExercito() <= 1 || 
			t.getNomeDono().equals(this.getNomeDono()) || 
			!this.ehVizinho(t)
			);
	}

	public boolean podeMoverPara(Territorio t) {
		return !(
			!t.getNomeDono().equals(this.getNomeDono()) || 
			!this.ehVizinho(t) || 
			this.getQtdExercitosMoveis() < 1
		);
	}

	@Override
	public boolean equals(Object another) {
		return this.nome == ((Territorio) another).getNome();
	}

	private void criarPoligono(List<Point2D.Double> points) {
		GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		boolean primeiro = true;
		for (Point2D.Double point : points) {
			if (primeiro) {
				gp.moveTo(point.x, point.y);
				primeiro = false;
			} else {
				gp.lineTo(point.x, point.y);
			}
			this.centro.x += point.x;
			this.centro.y += point.y;
		}
		this.centro.x /= points.size();
		this.centro.y /= points.size();
		gp.closePath();
		this.poligono = gp;
	}

	public int getQtdExercitosMoveis() {
		return Math.max(0, this.getQtdExercito() - 1 - this.qtdExercitosImoveis);
	}

	public int getExercitoParaAtaque() {
		if (this.getQtdExercito() <= 1) {
			return 0;
		} else if (this.getQtdExercito() > LogicaWar.MAX_DADOS) {
			return LogicaWar.MAX_DADOS;
		} else {
			return this.getQtdExercito() - 1;
		}
	}

	public void addExercitosImoveis(int amount) {
		this.qtdExercitosImoveis += amount;
		this.addExercitos(amount);
	}

	public void resetQtdExercitosImoveis() {
		this.qtdExercitosImoveis = 0;
	}

	public void setExercitosImoveis(int unmovableArmiesCount) {
		this.qtdExercitosImoveis = unmovableArmiesCount;
	}

	public int getQtdExercitosImoveis() {
		return this.qtdExercitosImoveis;
	}
}