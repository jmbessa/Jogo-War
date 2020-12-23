package viewcontroller;

import java.awt.Component; 
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import war.JogoWar;
import war.LogicaWar;
import model.Territorio;


@SuppressWarnings("serial")
public class FrameAtaque extends JFrame {
	private int qtdDadosAtaque = 0;
	private int qtdDadosDefesa = 0;
	private int[] derrotas = new int[2];
	private JPanel painelAtaque;
	private JPanel painelDefesa;
	private JPanel painelResultado;
	private List<JLabel> dadoAtaque = new LinkedList<JLabel>();
	private List<JLabel> dadoDefesa = new LinkedList<JLabel>();
	private JLabel labelResultado;
	private JButton botaoAtaque;
	private JButton botaoDefesa;
	private JButton botaoConfirmar;
	private List<Integer> resultadoAtaque = new LinkedList<Integer>();
	private List<Integer> resultadoDefesa = new LinkedList<Integer>();

	public FrameAtaque(Territorio territorioDe, Territorio territorioPara, int num) {
		this.qtdDadosAtaque = num;
		this.qtdDadosDefesa = territorioPara.getQtdExercito() > LogicaWar.MAX_DADOS ? LogicaWar.MAX_DADOS : territorioPara.getQtdExercito();

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(String.format("%s está sendo atacado por %s",territorioPara.getNome(), territorioDe.getNome()));

		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(420, 400));
		this.setResizable(false);

		this.setLocationRelativeTo(null);

		this.painelAtaque = new JPanel();
		this.botaoAtaque = new JButton(String.format("%d dado(s) para atacar", this.qtdDadosAtaque));
		this.botaoAtaque.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.painelAtaque.setLayout(new BoxLayout(painelAtaque, BoxLayout.Y_AXIS));

		ActionListener actLisA = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				jogarDado(true);
				checaFinal();
			}
		};

		this.botaoAtaque.addActionListener(actLisA);
		this.painelAtaque.add(this.botaoAtaque);

		this.painelDefesa = new JPanel();
		this.painelDefesa.setLayout(new BoxLayout(painelDefesa, BoxLayout.Y_AXIS));
		this.botaoDefesa = new JButton(String.format("%d dado(s) para defender", this.qtdDadosDefesa));
		this.botaoDefesa.setAlignmentX(Component.CENTER_ALIGNMENT);
		ActionListener actLisB = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				jogarDado(false);
				checaFinal();
			}
		};
		this.botaoDefesa.addActionListener(actLisB);
		this.painelDefesa.add(this.botaoDefesa);

		this.gerarDado();

		this.painelResultado = new JPanel();
		this.labelResultado = new JLabel();
		this.botaoConfirmar = new JButton("confirm");
		this.painelResultado.add(this.labelResultado);
		this.painelResultado.add(this.botaoConfirmar);
		ActionListener actLisC = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
				JogoWar.getInstancia().resultadoAtaque(derrotas);

			}
		};

		this.botaoConfirmar.addActionListener(actLisC);
		this.painelResultado.setVisible(false);
		this.getContentPane().add(this.painelAtaque);
		this.getContentPane().add(this.painelDefesa);
		this.getContentPane().add(this.painelResultado);
	}

	private void gerarDado() {
		for (int i = 0; i < LogicaWar.MAX_DADOS; i++) {
			ImageIcon iconeA = new ImageIcon("imagens/dado/dado_ataque_1.png");
			ImageIcon iconeB = new ImageIcon("imagens/dado/dado_defesa_1.png");
			JLabel dadoA = new JLabel(iconeA);
			JLabel dadoB = new JLabel(iconeB);
			dadoA.setVisible(false);
			dadoB.setVisible(false);
			dadoA.setAlignmentX(Component.CENTER_ALIGNMENT);
			dadoB.setAlignmentX(Component.CENTER_ALIGNMENT);

			this.dadoAtaque.add(dadoA);
			this.painelAtaque.add(dadoA);
			this.dadoDefesa.add(dadoB);
			this.painelDefesa.add(dadoB);
		}
	}

	private void jogarDado(boolean ataque) {
		if ((ataque && this.resultadoAtaque.size() == this.qtdDadosAtaque)
				|| (!ataque && this.resultadoDefesa.size() == this.qtdDadosDefesa)) {
			return;
		}

		Random rand = new Random();
		int qtdDados = ataque ? this.qtdDadosAtaque : this.qtdDadosDefesa;
		for (int i = 0; i < qtdDados; i++) {
			int resultado = rand.nextInt(6) + 1;
			if (ataque) this.resultadoAtaque.add(resultado);
			else this.resultadoDefesa.add(resultado);
		}
		Collections.sort(this.resultadoAtaque, Collections.reverseOrder());
		Collections.sort(this.resultadoDefesa, Collections.reverseOrder());

		int i = 0;
		for (int resultado : ataque ? this.resultadoAtaque : this.resultadoDefesa) {
			ImageIcon imgX;
			imgX = new ImageIcon(String.format("imagens/dado/dado_%s_%d.png", ataque ? "ataque" : "defesa", resultado));
			JLabel dado = ataque ? dadoAtaque.get(i) : dadoDefesa.get(i);
			dado.setIcon(imgX);
			dado.setVisible(true);
			i++;
		}
	}

	private int[] calculaDerrotas() {
		int derrotasAtaque = 0;
		int derrotasDefesa = 0;
		for (int i = 0; i < this.qtdDadosDefesa; i++) {
			if (i >= this.resultadoAtaque.size()) break;
			if (this.resultadoAtaque.get(i) <= this.resultadoDefesa.get(i)) derrotasAtaque++;
			else derrotasDefesa++;
		}
		int[] resultado = { derrotasAtaque, derrotasDefesa };
		return resultado;
	}

	private void checaFinal() {
		if (this.resultadoAtaque.size() != this.qtdDadosAtaque || this.resultadoDefesa.size() != this.qtdDadosDefesa) return;
		this.derrotas = calculaDerrotas();
		this.labelResultado.setText(String.format("%d unidades perdidas pelo atacante e %d unidades perdidas pela defesa", derrotas[0], derrotas[1]));
		this.painelResultado.setVisible(true);
	}
}
