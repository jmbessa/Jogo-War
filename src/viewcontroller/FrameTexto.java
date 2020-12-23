package viewcontroller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class FrameTexto extends JFrame {
	JComboBox<String> numOpcoes;

	public FrameTexto(String mensagem) {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(mensagem);
		this.setSize(new Dimension(300, 100));
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		JTextArea texto = new JTextArea(mensagem);
		texto.setEditable(false);
		texto.setLineWrap(true);
		JButton fechar = new JButton("Fechar");
		fechar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		this.add(texto, BorderLayout.CENTER);
		this.add(fechar, BorderLayout.PAGE_END);
	}
}
