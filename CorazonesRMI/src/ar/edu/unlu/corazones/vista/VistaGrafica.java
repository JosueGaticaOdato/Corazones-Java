package ar.edu.unlu.corazones.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.vista.gui.FondoTapete;
import ar.edu.unlu.corazones.vista.gui.VistaInicioSesion;

public class VistaGrafica extends JFrame implements IVista {

	// *************************************************************
	// 							CONSTANTES
	// *************************************************************

	private final String[] fuentes = {"Tahoma","Arial"};

	private final int[] tamañoFuentes = {14,12};

	private final ImageIcon iconoCorazon = new ImageIcon(
			new ImageIcon(getClass().getResource("/ar/edu/unlu/corazones/img/corazon.png")).getImage()
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH));

	private final ImageIcon iconoCorazonRoto = new ImageIcon(
			new ImageIcon(getClass().getResource("/ar/edu/unlu/corazones/img/corazonroto.png")).getImage()
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	
	// *************************************************************
	// 							ATRIBUTOS
	// *************************************************************

	private FondoTapete panelPrincipal;

	private Controlador controlador;

	private CardLayout cardLayout;
	
	// **************** PANEL INICIAR SESION  **********************
	
	private VistaInicioSesion vInicioSesion;
	private String nombreJugador;

	// ********************* PANEL MENU ****************************

	private JPanel panelMenu;
	
	// ******************** PANEL DE JUEGO ************************

	private JPanel panelJuego;
	private JPanel panelCentro;

	private JPanel barraSuperior;
	private JLabel labelBarraSuperior;

	private JPanel panelIzquierdo;
	private JPanel panelNumeroRonda;
	private JPanel panelNumeroJugada;
	private JPanel panelPuntaje;
	private JPanel panelCorazon;

	private JLabel labelRonda;
	private JLabel labelJugada;
	private JLabel labelCorazon;

	private JPanel panelInferior;
	private JPanel contenedorCartas;

	private Map<String, Point> posicionesJugadores = new HashMap<>();
	
	// *************************************************************
	//							CONSTRUCTOR
	// *************************************************************

	public VistaGrafica(Controlador controlador) {
		
		this.controlador = controlador;
		this.controlador.setVista(this);

		/* CONFIGURACIONES DE VENTANA */
		setTitle("Corazones");
		setSize(1100, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		/* INICIALIZAR LAYOUT Y PANEL PRINCIPAL */
		cardLayout = new CardLayout();
		panelPrincipal = new FondoTapete("/ar/edu/unlu/corazones/img/tapete.jpg");
		panelPrincipal.setLayout(cardLayout);
		setContentPane(panelPrincipal);
		
		/* VISTA INICIO DE SESION */
		this.vInicioSesion = new VistaInicioSesion();
		this.vInicioSesion.onClickIniciar(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nombreJugador = vInicioSesion.getGetNombreUsuario();
				controlador.conectarJugador(vInicioSesion.getGetNombreUsuario());
				iniciarMenu();
			}
		});
		
		/* CONTROL DE DESCONEXION DEL JUGADOR */
		
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.desconectarJugador();
                System.out.println("Jugador desconectado correctamente.");
                dispose(); 
            }
        });
	}
	
	// *************************************************************
	// 						CONTROL DE VISTAS
	// *************************************************************

	public void mostrarVista(String vista) {
		cardLayout.show(panelPrincipal, vista);
		System.out.println("CAMBIO DE VISTA A: " + vista);
	}
	
	// *************************************************************
	// 							MENSAJES
	// *************************************************************

	public void mostrarMensajeError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje);
	}
	
	// *************************************************************
	// 							PRE-JUEGO
	// *************************************************************
	
	@Override
	public void iniciar() {
		this.vInicioSesion.setVisible(true);
	}
	
	public void iniciarMenu() {
		crearMenu();
		this.vInicioSesion.setVisible(false);
		setVisible(true);
		mostrarVista("menu");
	}
	
	private void crearMenu() {
		panelMenu = new JPanel();
		panelMenu.setLayout(new BorderLayout());
		panelMenu.setOpaque(false);

		// Panel de botones
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
		panelBotones.setOpaque(false);

		int botonAncho = 200;
		int botonAlto = 40;

		JButton btnListaJugadores = crearBoton("Ver lista de jugadores", botonAncho, botonAlto);
		JButton btnComenzarJuego = crearBoton("Comenzar juego", botonAncho, botonAlto);
		JButton btnSalir = crearBoton("Salir", botonAncho, botonAlto);

		// Agregar los botones y darles un espaciado
		int espaciado = 20;
		
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnComenzarJuego);
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnListaJugadores);
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnSalir);

		// Centrar botones
		panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnListaJugadores.addActionListener(e -> {
			try {
				listarJugadores();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		btnComenzarJuego.addActionListener(e -> {
			try {
				iniciarJuego();
			} catch (HeadlessException | RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		btnSalir.addActionListener(e -> {
			try {
				desconectarJugador();
				System.exit(0);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} 
		});

		// Panel de botones en el centro de la pantalla
		JPanel contenedorBotones = new JPanel();
		contenedorBotones.setLayout(new GridBagLayout());
		contenedorBotones.setOpaque(false);
		contenedorBotones.add(panelBotones);
		panelMenu.add(contenedorBotones, BorderLayout.CENTER);

		panelPrincipal.add(panelMenu, "menu");
	}
	
	// Método para crear botones con tamaño fijo
	private JButton crearBoton(String texto, int ancho, int alto) {
		JButton boton = new JButton(texto);
		boton.setMaximumSize(new Dimension(ancho, alto));
		boton.setPreferredSize(new Dimension(ancho, alto));
		boton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar el botón en el panel
		return boton;
	}
	
	// ****************** JUGADOR DESCONECTADO ***********************
	
	public void desconectarJugador() throws RemoteException {
		controlador.desconectarJugador();
		mostrarMensaje("Jugador desconectado!");
	}
	
	// ******************* LISTA DE JUGADORES *********************

	private void listarJugadores() throws RemoteException {
		String[] jugadores = this.controlador.listaJugadores();

		if (jugadores == null || jugadores.length == 0) {

			JOptionPane.showMessageDialog(this, "No hay jugadores registrados.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		StringBuilder lista = new StringBuilder("Lista de jugadores:\n");

		for (int i = 0; i < jugadores.length; i++) {
			lista.append((i + 1)).append(") ").append(jugadores[i] != null ? jugadores[i] : "(Sin agregar)")
					.append("\n");
		}

		JOptionPane.showMessageDialog(this, lista.toString(), "Lista de Jugadores", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// ******************* COMENZAR JUEGO *************************

	private void iniciarJuego() throws HeadlessException, RemoteException {
		if (this.controlador.isCantidadJugadoresValida()) {

			JOptionPane.showMessageDialog(this, "Juego comenzado!", "Juego iniciado", JOptionPane.INFORMATION_MESSAGE);
			//controlador.iniciarJuego();
			//Lo ejecuto en un hilo separado para no bloquear la vista
			new Thread(() -> {
	            try {
	                controlador.iniciarJuego();
	            } catch (RemoteException e) {
	                e.printStackTrace();
	            }
	        }).start();
		} else {

			JOptionPane.showMessageDialog(this, "Faltan jugadores para comenzar el juego", "Jugadores insuficientes",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void pasajeDeCartas() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pedirCartaPasaje() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cartaTiradaInvalidaPasaje() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cartaTiradaValidaPasaje() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finPasajeDeCartas() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pasajeDeCartasJugador() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finPasajeDeCartasJugador() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nuevaJugada() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cartasRepartidas() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pedirCarta(String jugadorActual) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jugarDosDeTrebol(String jugadorActual) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cartaTiradaInvalida(String jugadorActual) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cartaTiradaInvalida2deTrebol(String jugadorActual) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void perdedorJugada() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void corazonesRotos() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cartaTiradaValida() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finDeRonda() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finDeJuego() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setControlador(Controlador controlador) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
