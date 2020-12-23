package viewcontroller;

import java.awt.BorderLayout;  
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import viewcontroller.PainelMapa;
import war.Utilidades;
import war.JogoWar;
import war.LogicaWar;
import model.Carta;
import model.Jogador;

@SuppressWarnings("serial")
public class PainelUI extends JPanel implements MouseListener, Observer {

	private CardLayout layout;

	private JPanel painelInicio;
	private JPanel painelCarregarJogo;
	private JPanel painelColocarNome;
	private JPanel painelJogo;
	private JPanel painelOpcoes;
	private JPanel painelNomes;
	private JPanel painelFimDeJogo;

	private List<JLabel> labelJogadores = new LinkedList<JLabel>();
	private JLabel statusLabel;

	private JButton botaoAcao;
	private JButton botaoMostraObjetivos;
	private JButton botaoMostraCartas;
	private JButton botaoFinalizarVez;


	private Dimension tamanho;

	private final double MULTIPLIER_X = 0.45;
	private final double MULTIPLIER_Y = 0.6;

	public PainelUI() {
		this.layout = new CardLayout();
		this.setLayout(layout);

		this.tamanho = Utilidades.getTamanhoJogo();
		this.tamanho.height = (int) (this.tamanho.height * MULTIPLIER_Y);
		this.tamanho.width = (int) (this.tamanho.width * MULTIPLIER_X);
		this.setMaximumSize(this.tamanho);
		this.addIniciarPainelUI();

		this.layout.show(this, "Starting UI");
	}

	private void addIniciarPainelUI() {
		this.painelInicio = new JPanel();
		this.painelInicio.setLayout(new BoxLayout(painelInicio, BoxLayout.X_AXIS));

		this.painelColocarNome = new JPanel();
		this.painelColocarNome.setLayout(new BoxLayout(painelColocarNome, BoxLayout.Y_AXIS));
		final JLabel l1 = new JLabel("Digite o nome dos jogadores: ");
		l1.setMaximumSize(new Dimension(300, 50));
		l1.setAlignmentY(Component.TOP_ALIGNMENT);
		this.painelColocarNome.add(l1);

		final List<JTextField> campoNomeJogadores = new LinkedList<JTextField>();

		for (int i = 0; i < LogicaWar.MAX_JOGADORES; i++) {
			JTextField nomeJogador = new JTextField();
			nomeJogador.setMaximumSize(new Dimension(400, 50));
			nomeJogador.setBackground(Jogador.corJogadores[i]);
			nomeJogador.setFont(new Font("Times New Roman", Font.PLAIN, 34));
			campoNomeJogadores.add(nomeJogador);
			nomeJogador.setCaretColor(Jogador
					.getCorPrimeiroPlano(Jogador.corJogadores[i]));
			nomeJogador.setForeground(Jogador
					.getCorPrimeiroPlano(Jogador.corJogadores[i]));
			nomeJogador.setAlignmentY(Component.TOP_ALIGNMENT);
			this.painelColocarNome.add(nomeJogador);
		}

		JButton botaoComecar = new JButton("Começar");
		botaoComecar.setMaximumSize(new Dimension(200, 50));
		botaoComecar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				List<Jogador> jogadores = new ArrayList<Jogador>();
				for (int i = 0; i < LogicaWar.MAX_JOGADORES; i++) {
					JTextField nomeJogadores = campoNomeJogadores
							.get(i);
					if (nomeJogadores.getText().length() > 0) {
						jogadores.add(new Jogador(nomeJogadores.getText(),
								Jogador.corJogadores[i]));
					}
				}
				if (jogadores.size() >= LogicaWar.MIN_JOGADORES) {
					
					JogoWar.getInstancia().comecarJogo(jogadores);
				} else {
					jogadores.clear();
					l1.setText("<html>Introduza o nome de, pelo menos, 3 jogadores</html>");
					l1.setOpaque(true);
					l1.setBackground(Color.RED);
				}

			}
		});
		botaoComecar.setAlignmentY(Component.TOP_ALIGNMENT);
		this.painelColocarNome.add(botaoComecar);
		
		this.painelCarregarJogo = new JPanel();
		this.painelCarregarJogo.setLayout(new BoxLayout(painelCarregarJogo, BoxLayout.X_AXIS));
		this.painelCarregarJogo.setMaximumSize(new Dimension(500, 50));
		JTextField nomeSalvarArquivo = new JTextField("Digite o nome do arquivo para carregá-lo");
		nomeSalvarArquivo.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				nomeSalvarArquivo.setText("");
				
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
		
		JButton botaoCarregaoJogo = new JButton("Carregar Jogo");
		botaoCarregaoJogo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JogoWar.getInstancia().carregarJogo(nomeSalvarArquivo.getText());
			}});

		this.painelCarregarJogo.add(nomeSalvarArquivo);
		this.painelCarregarJogo.add(botaoCarregaoJogo);
		
		this.painelInicio.add(painelColocarNome);
		this.painelInicio.add(painelCarregarJogo);
		this.add(painelInicio, "UI sendo iniciada");
	}

	public void atualizar(boolean primeiro) {
		this.updateGameUI(primeiro);
		if (primeiro) {
			layout.show(this, "Game UI");
		}
	}

	public void updateGameUI(boolean primeiro) {
		if (primeiro) {
			this.painelJogo = new JPanel();
			this.painelJogo
					.setLayout(new BoxLayout(painelJogo, BoxLayout.X_AXIS));
			this.add(painelJogo, "Game UI");
			this.painelJogo.addMouseListener(this);
		}
		this.painelAtualizaNomes(primeiro);
		this.painelAtualizaOpcoes(primeiro);
	}

	public void mostrarPainelFimDeJogo(Jogador vencedor) {
		this.painelFimDeJogo = new JPanel();
		this.painelFimDeJogo.setLayout(new BorderLayout());
		JTextArea mensagemFimJogo = new JTextArea(String.format("%s é o vencedor da partida por completar seu objetivo (%s)", vencedor.getNome(), vencedor.getObjetivo().getDescricao()));
		this.painelFimDeJogo.add(mensagemFimJogo, BorderLayout.CENTER);
		this.add(painelFimDeJogo, "Game Ended");
		layout.show(this, "Game Ended");
	}

	private void painelAtualizaNomes(boolean first) {
		if (first) {
			this.painelNomes = new JPanel();
			this.painelNomes.setLayout(new BoxLayout(painelNomes,
					BoxLayout.Y_AXIS));
			this.painelNomes.setMaximumSize(new Dimension(this.tamanho.width / 8,
					this.tamanho.height));
			JLabel labelJogadores = new JLabel("Jogadores:");
			this.painelNomes.add(labelJogadores);
			this.painelJogo.add(this.painelNomes);
		}
		int i = 0;
		for (Jogador p : JogoWar.getInstancia().getJogadores()) {
			JLabel labelJogador;
			if (first) {
				labelJogador = new JLabel();
				labelJogador.setOpaque(true);
				labelJogador.setBackground(p.getCor());
				labelJogador.setForeground(Jogador.getCorPrimeiroPlano(p.getCor()));
				labelJogador.setAlignmentX(Component.LEFT_ALIGNMENT);
				this.labelJogadores.add(labelJogador);
				this.painelNomes.add(labelJogador);
			} else {
				labelJogador = this.labelJogadores.get(i);
			}
			StringBuilder sb = new StringBuilder();
			for (Carta c : p.getCartas()) {
				sb.append(c.getTipo().toString());
				sb.append(" ");
			}
			labelJogador.setText(String.format("%s (%d territorios)",
					p.getNome(), JogoWar.getInstancia().getMapa().getTerritoriosPorDono(p).size()));
			if (JogoWar.getInstancia().getJogadorAtual().equals(p)) {
				labelJogador.setFont(labelJogador.getFont().deriveFont(
						labelJogador.getFont().getStyle() | Font.BOLD));
			} else {
				labelJogador.setFont(labelJogador.getFont().deriveFont(
						labelJogador.getFont().getStyle() & ~Font.BOLD));
			}
			i++;
		}
	}

	public void painelAtualizaOpcoes(boolean primeiro) {
		if (primeiro) {
			this.painelOpcoes = new JPanel();
			this.painelOpcoes.setLayout(new BoxLayout(painelOpcoes,
					BoxLayout.Y_AXIS));
			this.painelOpcoes.setMaximumSize(new Dimension(
					this.tamanho.width / 8 * 6, this.tamanho.height));
			this.statusLabel = new JLabel();
			this.statusLabel.setOpaque(true);
			this.statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.botaoAcao = new JButton();
			this.botaoAcao.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.botaoAcao.setMaximumSize(new Dimension(250, 25));
			this.botaoAcao.setEnabled(false);
			ActionListener actionButtonListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JogoWar.getInstancia().actionPerformed();
				}
			};
			botaoAcao.addActionListener(actionButtonListener);

			this.botaoMostraCartas = new JButton("Mostrar Cartas");
			this.botaoMostraCartas.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.botaoMostraCartas.setMaximumSize(new Dimension(250, 25));
			this.botaoMostraCartas.setEnabled(false);
			ActionListener listenerMostrarCartas = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JogoWar.getInstancia().mostrarCartas(false);
				}
			};
			this.botaoMostraCartas.addActionListener(listenerMostrarCartas);

			this.botaoMostraObjetivos = new JButton("Mostrar Objetivo");
			this.botaoMostraObjetivos.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.botaoMostraObjetivos.setMaximumSize(new Dimension(250, 25));
			this.botaoMostraObjetivos.setEnabled(true);
			ActionListener listenerMostrarObjetivo = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JogoWar.getInstancia().mostrarObjetivo();
				}
			};
			this.botaoMostraObjetivos.addActionListener(listenerMostrarObjetivo);

			this.botaoFinalizarVez = new JButton("Finalizar Jogada");
			this.botaoFinalizarVez.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.botaoFinalizarVez.setMaximumSize(new Dimension(250, 25));
			this.botaoFinalizarVez.setEnabled(false);
			ActionListener listenerBotaoFinalizarVez = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JogoWar.getInstancia().proximaVez();
				}
			};
			this.botaoFinalizarVez.addActionListener(listenerBotaoFinalizarVez);

			this.painelOpcoes.add(this.statusLabel);
			this.painelOpcoes.add(this.botaoAcao);
			this.painelOpcoes.add(this.botaoMostraCartas);
			this.painelOpcoes.add(this.botaoMostraObjetivos);
			this.painelOpcoes.add(this.botaoFinalizarVez);


			this.painelJogo.add(this.painelOpcoes);
		}

		Jogador jogadorAtual = JogoWar.getInstancia().getJogadorAtual();
		String stringAcao = "Sem ação";
		String stringStatus = "Sem status";
		this.botaoAcao.setEnabled(false);
		this.botaoFinalizarVez.setEnabled(true);
		if (jogadorAtual.getCartas().isEmpty()) {
			this.botaoMostraCartas.setEnabled(false);
			this.botaoMostraCartas.setText("Mostrar Cartas");
		} else {	
			this.botaoMostraCartas.setEnabled(true);
			this.botaoMostraCartas.setText(String.format("Mostrar Cartas (%d)", jogadorAtual.getCartas().size()));
		}
		switch (JogoWar.getInstancia().getEstadoAtual()) {
		case ATACAR:
			if (JogoWar.getInstancia().getTerritorioSelecionado() == null) {
				stringStatus = "Selecione o país de onde quer atacar";
			} else {
				stringStatus = "Selecione o país que deseja atacar";
			}
			stringAcao = "Parar de atacar";
			this.botaoAcao.setEnabled(true);
			break;
		case MOVER_EXERCITO:
			stringAcao = "Limpar selecionados";
			this.botaoAcao.setEnabled(true);
			if (JogoWar.getInstancia().getTerritorioSelecionado() == null) {
				stringStatus = "Selecione de país que deseja mover os exércitos";
			} else if (JogoWar.getInstancia().getTerritorioDesejado() == null) {
				stringStatus = "Selecione para qual país deseja mover os exércitos";
			}
			break;
		case POSICIONAR_NOVO_EXERCITO:
			stringStatus = String
					.format("Escolha um país para colocar os exércitos (você possui %d exércitos restantes)",
							jogadorAtual.getExercitosNaoColocados());
			if (JogoWar.getInstancia().getTerritorioSelecionado() != null
					&& jogadorAtual.getExercitosNaoColocados() > 0) {
				stringAcao = String.format("Colocar exércitos em %s", JogoWar
						.getInstancia().getTerritorioSelecionado().getNome());
				this.botaoAcao.setEnabled(true);
			}
			this.botaoFinalizarVez.setEnabled(false);
			break;
		default:
			break;
		}
		this.statusLabel.setText(String.format("(Vez de %s) %s",
				jogadorAtual.getNome(), stringStatus));
		this.statusLabel.setBackground(jogadorAtual.getCor());
		this.statusLabel.setForeground(Jogador.getCorPrimeiroPlano(jogadorAtual
				.getCor()));
		this.botaoAcao.setText(stringAcao);
	}
	

	@Override
	public void mouseClicked(MouseEvent me) {
		JogoWar.getInstancia().focarPopUp();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable obs, Object obj) {
		this.atualizar(false);		
	}
}
