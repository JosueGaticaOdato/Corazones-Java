package ar.edu.unlu.corazones.vista.gui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class VistaInicioSesion extends JFrame {

	private final ImageIcon iconCorazones = new ImageIcon(getClass().getResource("/ar/edu/unlu/corazones/img/corazon.png"));
	
	private JPanel contentPane;
	private JTextField textUsuario;
	private JButton btnIniciar;

	/**
	 * Create the frame.
	 */
	public VistaInicioSesion() {
		setTitle("Iniciar sesion - Corazones");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 247, 109);
		setLocationRelativeTo(null);
		setIconImage(iconCorazones.getImage());
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow]", "[][]"));
		
		JLabel lblUsuario = new JLabel("Usuario");
		contentPane.add(lblUsuario, "cell 0 0,alignx trailing");
		
		textUsuario = new JTextField();
		contentPane.add(textUsuario, "cell 1 0,growx");
		textUsuario.setColumns(10);
		
		btnIniciar = new JButton("Iniciar");
		contentPane.add(btnIniciar, "cell 1 1,alignx right");
		
		SwingUtilities.getRootPane(btnIniciar).setDefaultButton(btnIniciar);
	}
	
	public void onClickIniciar(ActionListener listener) {
		this.btnIniciar.addActionListener(listener);
	}
	
	public String getGetNombreUsuario() {
		return this.textUsuario.getText();
	}
}
