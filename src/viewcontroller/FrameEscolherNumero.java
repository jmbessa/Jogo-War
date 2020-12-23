package viewcontroller;

import java.awt.BorderLayout; 
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import war.JogoWar;

@SuppressWarnings("serial")
public class FrameEscolherNumero extends JFrame implements ActionListener {
	JComboBox<String> numOpcoes;

	public FrameEscolherNumero(int numeroMaximo, String mensagem) {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(mensagem);
		this.setSize(new Dimension(350, 100));
		this.getContentPane().setLayout(new BorderLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		JLabel instructionLabel = new JLabel(mensagem);

		String[] opcoes = new String[numeroMaximo];
		for (int i = 0; i < numeroMaximo; i++) {
			opcoes[i] = ((Integer) (i + 1)).toString();
		}
		this.numOpcoes = new JComboBox<String>(opcoes);

		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		JButton confirmar = new JButton("Confirmar");
		confirmar.addActionListener(this);

		this.add(instructionLabel, BorderLayout.PAGE_START);
		this.add(this.numOpcoes, BorderLayout.CENTER);
		this.add(confirmar, BorderLayout.LINE_END);
		this.add(cancelar, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();

		String valor = (String) this.numOpcoes.getSelectedItem();
		JogoWar.getInstancia().selecionarNumero(Integer.parseInt(valor));
	}
}
