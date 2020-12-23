package viewcontroller;

import java.awt.BorderLayout;   
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import war.JogoWar;
import model.Carta;
import model.Jogador;
import model.CartaTerritorio;

@SuppressWarnings("serial")
public class FrameSelecaoCarta extends JFrame implements MouseListener {
	private HashMap<JLabel, Carta> cartas = new HashMap<JLabel, Carta>();
	private List<Carta> cartasSelecionadas = new LinkedList<Carta>();
	private int numMaxCartas;
	private JPanel painelDisplayCartas;
	private JButton botaoTrocaCartas;
	private Jogador jogador;

	public FrameSelecaoCarta(Jogador p, int numMaxCartas, boolean trocaForcada) {
		this.jogador = p;
		this.numMaxCartas = numMaxCartas;

		if (trocaForcada) this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		else this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setTitle(String.format("Cartas de %s %s", p.getNome(), trocaForcada ? " (Você deve fazer a troca)" : ""));
		this.setSize(new Dimension(1100 + 2 * 2 * 5, 600));
		this.getContentPane().setLayout(new BorderLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.painelDisplayCartas = new JPanel();
		this.painelDisplayCartas.setLayout(new BoxLayout(painelDisplayCartas,
				BoxLayout.X_AXIS));

		for (Carta c : p.getCartas()) {
			JLabel labelCartas = new JLabel();
			labelCartas.setSize(220, 363);
			labelCartas.setAlignmentX(Component.CENTER_ALIGNMENT);
			labelCartas.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			labelCartas.addMouseListener(this);
			String arquivoImagem;
			if (c instanceof CartaTerritorio) {
				CartaTerritorio ct = (CartaTerritorio) c;
				arquivoImagem = String.format("imagens/cartas/war_carta_%s_%s.png", ct.getTerritorio().getContinente().defineContinente(), ct.getTerritorio().getNome().toLowerCase().replaceAll("\\s+", ""));
				arquivoImagem = Normalizer.normalize(arquivoImagem, Normalizer.Form.NFD);
				arquivoImagem = arquivoImagem.replaceAll("[^\\x00-\\x7F]", "");
			} else arquivoImagem = "imagens/cartas/war_carta_coringa.png";
			
			ImageIcon imagemCarta = new ImageIcon(arquivoImagem);
			Image imagemRedimensionada = imagemCarta.getImage().getScaledInstance( labelCartas.getWidth(), labelCartas.getHeight(), Image.SCALE_SMOOTH);
			labelCartas.setIcon(new ImageIcon(imagemRedimensionada));

			this.cartas.put(labelCartas, c);
			this.painelDisplayCartas.add(labelCartas);
		}
		String textoBotaoTrocaCartas = String.format("Ganhou %d exércitos na troca de cartas", JogoWar.getInstancia().getQtdExercitosTrocados());
		
		if (JogoWar.getInstancia().atacando()) 
			textoBotaoTrocaCartas = String.format("Você pegou essas cartas de %s", p.getNome());
		
		this.botaoTrocaCartas = new JButton(textoBotaoTrocaCartas);
		ActionListener listenerTrocaCartas = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				JogoWar.getInstancia().trocarCartas(cartasSelecionadas);
				dispose();
			}

		};
		this.botaoTrocaCartas.addActionListener(listenerTrocaCartas);
		this.botaoTrocaCartas.setEnabled(false);

		this.getContentPane().add(this.painelDisplayCartas, BorderLayout.CENTER);
		this.getContentPane().add(this.botaoTrocaCartas,
				BorderLayout.PAGE_END);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		JLabel cartaClicada = (JLabel) me.getSource();
		int index = this.cartasSelecionadas.indexOf(this.cartas.get(cartaClicada));
		if (index >= 0) {
			this.cartasSelecionadas.remove(index);
			cartaClicada.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		} else if (this.cartasSelecionadas.size() < this.numMaxCartas) {
			this.cartasSelecionadas.add(this.cartas.get(cartaClicada));
			cartaClicada.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		}
		if ((this.jogador.trocaValida(this.cartasSelecionadas) && 
		(JogoWar.getInstancia().posicionando()) || (JogoWar.getInstancia().atacando()) && 
		this.cartasSelecionadas.size() < this.numMaxCartas) && !jogador.getNome().equals( JogoWar.getInstancia().getJogadorAtual().getNome())) this.botaoTrocaCartas.setEnabled(true);
		else this.botaoTrocaCartas.setEnabled(false);
		}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
