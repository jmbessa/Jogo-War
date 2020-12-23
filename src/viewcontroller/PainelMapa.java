package viewcontroller;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import war.Utilidades;
import war.JogoWar;
import model.Jogador;
import model.Territorio;

@SuppressWarnings("serial")
public class PainelMapa extends JPanel implements Observer  {

	private Image imagemFundo;
	public double coordMultiplicadorX;
	public double coordMultiplicadorY;
	private Dimension tamanhoMapa;
	private List<JLabel> labelExercitos = new LinkedList<JLabel>();
	

	public PainelMapa() {
		this.setImagemFundo("imagens/cartas/bgconfiguracao.png");
		this.ajustaTamanho(0.5,0.5);
		this.setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g){

		BufferedImage fundo;
		try {
			fundo = ImageIO.read(new File("imagens/mapa/war_tabuleiro_fundo.png"));
			Paint textura = new TexturePaint( fundo, new Rectangle(fundo.getWidth(), fundo.getHeight() ) );
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint( textura );
			g.fillRect( 0, 0, getWidth(), getHeight() );
		} catch (IOException e) {
			e.printStackTrace();
		}
		


		g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
	}

	public Dimension getTamanhoMapa() {
		return tamanhoMapa;
	}
	
	public void ajustaTamanho(double multiplierX, double multiplierY) {
		Dimension tamanhoJogo = Utilidades.getTamanhoJogo();
		this.tamanhoMapa = new Dimension((int) (tamanhoJogo.width * multiplierX),(int) (tamanhoJogo.height * multiplierY));
		this.setPreferredSize(tamanhoMapa);
		this.setSize(tamanhoMapa);
		this.setMaximumSize(tamanhoMapa);
		this.setMinimumSize(tamanhoMapa);
		this.coordMultiplicadorX = (tamanhoMapa.width / 1024.0);
		this.coordMultiplicadorY = (tamanhoMapa.height / 768.0);
	}

	public void atualizar(boolean primeiro) {
		if (primeiro) {
			this.ajustaTamanho(1, 0.85);
			this.setImagemFundo("imagens/mapa/war_tabuleiro_mapa copy.png");
			this.addMouseListener(new MapPanelMouseListener());
		}
		this.atualizarLabelExercitos(primeiro);

	}

	public void atualizarLabelExercitos(boolean primeiro) {
		int i = 0;
		Territorio territorioSelecionado = JogoWar.getInstancia().getTerritorioSelecionado();
		for (Territorio t : JogoWar.getInstancia().getMapa().getTerritorio()) {
			Jogador dono = JogoWar.getInstancia().getNomeJogador(t.getNomeDono());
			JLabel labelCentral;
			Color corFronteira = Color.BLACK;
			int width = 130;
			int zOrder = 2;
			Color corFundo = dono.getCor();
			String texto = String.format("(%d) %s", t.getQtdExercito(),t.getNome());

			if (primeiro) {
				labelCentral = new JLabel("", SwingConstants.CENTER);
				labelCentral.setName(t.getNome());
				labelCentral.setOpaque(true);
				labelCentral.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent mouseEvent) {
						JLabel label = (JLabel) mouseEvent.getComponent();
						Territorio t = JogoWar.getInstancia().getMapa().getNomeTerritorio(label.getName());
						if (t == null) {
							JogoWar.getInstancia().focarPopUp();
							System.out.println(String.format("Não foi possível achar o território com nome %s",label.getName()));
						} else  JogoWar.getInstancia().selecionarTerritorio(t);
							
						
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
				});
				this.labelExercitos.add(labelCentral);
				this.add(labelCentral);
			} else {
				labelCentral = this.labelExercitos.get(i);
			}

			if (territorioSelecionado != null && territorioSelecionado.equals(t)) {
				zOrder = 0;
				corFronteira = Color.MAGENTA;
			} else if (dono.equals(JogoWar.getInstancia().getJogadorAtual())) {
				zOrder = 1;
				corFundo = corFundo.darker();
				corFronteira = Color.WHITE;
			} else {
				zOrder = 2;
				corFronteira = Color.BLACK;
			}

			switch (JogoWar.getInstancia().getEstadoAtual()) {
			case ATACAR:
				if (territorioSelecionado != null) {
					if (territorioSelecionado.podeAtacar(t)) {
						corFundo = corFundo.brighter();
						corFronteira = Color.YELLOW;
						zOrder = 1;
					}
				}
				break;
			case MOVER_EXERCITO:
				if (territorioSelecionado != null) {
					if (territorioSelecionado.podeMoverPara(t)) {
						corFundo = corFundo.brighter();
						corFronteira = Color.YELLOW;
						zOrder = 1;
					}
				}
				break;
			case POSICIONAR_NOVO_EXERCITO:
				break;
			default:
				break;
			}

			if (!primeiro) {
				this.setComponentZOrder(labelCentral, zOrder);
			}
			labelCentral.setBounds(
				(int) (t.getCentro().x * JogoWar.getInstancia().getWarFrame().getPainelMapa().coordMultiplicadorX),
				(int) (t.getCentro().y * JogoWar.getInstancia().getWarFrame().getPainelMapa().coordMultiplicadorY),
				width, 20);
			labelCentral.setForeground(Jogador.getCorPrimeiroPlano(dono.getCor()));
			labelCentral.setText(texto);
			labelCentral.setBorder(BorderFactory.createLineBorder(corFronteira, 3, true));
			labelCentral.setBackground(corFundo);
			this.repaint();
			labelCentral.repaint();
			i++;
		}
	}

	public void setImagemFundo(String arquivo) {
		try {
			this.imagemFundo = new ImageIcon(arquivo).getImage();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
	}


	private class MapPanelMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			for (Territorio t : JogoWar.getInstancia().getMapa().getTerritorio()) {
				if (t.getPoligono().contains(mouseEvent.getX() / JogoWar.getInstancia().getWarFrame().getPainelMapa().coordMultiplicadorX,
						mouseEvent.getY() / JogoWar.getInstancia().getWarFrame().getPainelMapa().coordMultiplicadorY)) {
					JogoWar.getInstancia().selecionarTerritorio(t);
					return;
				} else JogoWar.getInstancia().focarPopUp();
			}
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

	@Override
	public void update(Observable obs, Object obj) {
		this.atualizar(false);
	}
}
