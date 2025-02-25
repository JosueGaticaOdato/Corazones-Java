package ar.edu.unlu.corazones.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.modelo.Carta;
import ar.edu.unlu.corazones.vista.gui.FondoTapete;
import ar.edu.unlu.corazones.vista.gui.VistaCarta;
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
	
	private void mostrarAvisoNoEsTuTurno() {
	    JDialog dialogo = new JDialog(this, "Aviso", false);
	    dialogo.setLayout(new BorderLayout());
	    dialogo.add(new JLabel("No es tu turno. Espera tu turno.", SwingConstants.CENTER), BorderLayout.CENTER);
	    dialogo.setSize(250, 100);
	    dialogo.setLocationRelativeTo(this);
	    
	    // 🔹 Cierra el cartel después de 1.5 segundos
	    new Timer(1500, e -> dialogo.dispose()).start();
	    
	    dialogo.setVisible(true);
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
			//Lo ejecuto en un hilo separado para no bloquear la vista
			new Thread(() -> {
	            try {
	                controlador.iniciarJuego();
	            } catch (RemoteException e) {
	                e.printStackTrace();
	            }
	        }).start();
			//controlador.iniciarJuego();
		} else {

			JOptionPane.showMessageDialog(this, "Faltan jugadores para comenzar el juego", "Jugadores insuficientes",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// *************************************************************
	//                      PANTALLA JUEGO
	// *************************************************************
	
	private void crearVistaJuego() throws RemoteException {
		panelJuego = new JPanel(new BorderLayout());
		panelJuego.setOpaque(false);

		// Barra superior
		barraSuperior = new JPanel(new BorderLayout());
		barraSuperior.setOpaque(false);
		barraSuperior.setPreferredSize(new Dimension(1100, 50));
		barraSuperior.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2), " Estado del juego ",
						TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuentes[0], Font.BOLD, tamañoFuentes[0]), Color.WHITE));
		barraSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Ocupa todo el ancho
		labelBarraSuperior = new JLabel("", SwingConstants.CENTER);
		labelBarraSuperior.setFont(new Font(fuentes[0], Font.BOLD, tamañoFuentes[0]));
		labelBarraSuperior.setForeground(Color.WHITE);
		barraSuperior.add(labelBarraSuperior, BorderLayout.CENTER);
		panelJuego.add(barraSuperior, BorderLayout.NORTH);

		// Panel izquierdo (Numero de jugada, numero de ronda, puntajes)
		panelIzquierdo = crearPanelIzquierdo();

		panelJuego.add(panelIzquierdo, BorderLayout.WEST);

		// Panel central para cartas jugadas
		panelCentro = crearPanelCentro();
		panelJuego.add(panelCentro, BorderLayout.CENTER);

		// Panel inferior (cartas en mano)
		panelInferior = crearPanelJugador();
		panelJuego.add(panelInferior, BorderLayout.SOUTH);

		// Inicializar posiciones dinámicamente
		String[] nombresJugadores = this.controlador.listaJugadores();
		inicializarPosicionesJugadores(nombresJugadores);

		panelPrincipal.add(panelJuego, "juego");
	}
	
	private JPanel crearPanelCentro() throws RemoteException {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setOpaque(false);

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.weightx = 1.0;
	    gbc.weighty = 1.0;
	    gbc.anchor = GridBagConstraints.CENTER; 

	    Font fuenteNombres = new Font(fuentes[0], Font.BOLD, tamañoFuentes[0]);
	    Color colorTexto = Color.WHITE;
	    
	    String[] jugadores = this.controlador.listaJugadores();

	    // Paneles para cada jugador
	    JPanel panelNorte = crearPanelJugadorCentro(jugadores[2], new VistaCarta(), fuenteNombres, colorTexto);
	    JPanel panelSur = crearPanelJugadorCentro(jugadores[0], new VistaCarta(), fuenteNombres, colorTexto);
	    JPanel panelEste = crearPanelJugadorCentro(jugadores[3], new VistaCarta(), fuenteNombres, colorTexto);
	    JPanel panelOeste = crearPanelJugadorCentro(jugadores[1], new VistaCarta(), fuenteNombres, colorTexto);

	    // Ubico los paneles en el GridBagLayout
	    gbc.gridx = 1; gbc.gridy = 0;
	    panel.add(panelNorte, gbc);  // (Norte)

	    gbc.gridx = 1; gbc.gridy = 2;
	    panel.add(panelSur, gbc);  // (Sur)

	    gbc.gridx = 2; gbc.gridy = 1;
	    panel.add(panelEste, gbc);  // (Este)

	    gbc.gridx = 0; gbc.gridy = 1;
	    panel.add(panelOeste, gbc);  // (Oeste)

	    return panel;
	}
	
	private JPanel crearPanelJugadorCentro(String nombre, VistaCarta carta, Font fuente, Color color) {
	    JPanel panelJugador = new JPanel();
	    panelJugador.setLayout(new BorderLayout());
	    panelJugador.setOpaque(false);

	    JLabel labelNombre = new JLabel(nombre, SwingConstants.CENTER);
	    labelNombre.setFont(fuente);
	    labelNombre.setForeground(color);

	    panelJugador.add(labelNombre, BorderLayout.NORTH);
	    panelJugador.add(carta, BorderLayout.CENTER);

	    return panelJugador;
	}
	
	private JPanel crearPanelIzquierdo() throws RemoteException {
		panelIzquierdo = new JPanel();
		panelIzquierdo.setOpaque(false);
		panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
		panelIzquierdo.setPreferredSize(new Dimension(300, 150));

		// ******************* Sección: Número de Ronda *********************

		panelNumeroRonda = new JPanel(new BorderLayout());
		panelNumeroRonda.setOpaque(false);
		panelNumeroRonda.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				" Ronda N° ", TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuentes[0], Font.BOLD, tamañoFuentes[0]), Color.WHITE));
		panelNumeroRonda.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Ocupa todo el ancho
		labelRonda = new JLabel("1", SwingConstants.CENTER);
		labelRonda.setFont(new Font(fuentes[0], Font.BOLD, tamañoFuentes[0]));
		labelRonda.setForeground(Color.WHITE);
		panelNumeroRonda.add(labelRonda, BorderLayout.CENTER);

		// ******************* Sección: Número de Jugada *********************

		panelNumeroJugada = new JPanel(new BorderLayout());
		panelNumeroJugada.setOpaque(false);
		panelNumeroJugada.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				" Jugada N°", TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuentes[0], Font.BOLD, tamañoFuentes[0]), Color.WHITE));
		panelNumeroJugada.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Ocupa todo el ancho
		labelJugada = new JLabel("1", SwingConstants.CENTER);
		labelJugada.setFont(new Font(fuentes[0], Font.BOLD, tamañoFuentes[0]));
		labelJugada.setForeground(Color.WHITE);
		panelNumeroJugada.add(labelJugada, BorderLayout.CENTER);

		// **** Sección: Puntajes (GridLayout de 4 filas, 2 columnas) ********

		panelPuntaje = new JPanel(new GridLayout(4, 2, 5, 5));
		panelPuntaje.setOpaque(false);
		panelPuntaje.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				" Puntaje ", TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuentes[0], Font.BOLD, tamañoFuentes[0]), Color.WHITE));
		panelPuntaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); // Ocupa todo el ancho

		String[] jugadores = this.controlador.listaJugadores();
		
		for (int i = 0; i < 4; i++) {
			
			JLabel nombreJugador = new JLabel(jugadores[i], SwingConstants.CENTER);
			nombreJugador.setForeground(Color.WHITE);
			nombreJugador.setFont(new Font(this.fuentes[0], Font.BOLD, tamañoFuentes[0]));

			JLabel puntajeJugador = new JLabel("0", SwingConstants.CENTER);
			puntajeJugador.setFont(new Font(this.fuentes[0], Font.BOLD, tamañoFuentes[0]));
			puntajeJugador.setForeground(Color.WHITE);
			panelPuntaje.add(nombreJugador);
			panelPuntaje.add(puntajeJugador);
		}

		// ******************* Sección: Corazones rotos *********************

		panelCorazon = new JPanel(new BorderLayout());
		panelCorazon.setOpaque(false);
		panelCorazon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Ocupa todo el ancho

		labelCorazon = new JLabel(iconoCorazon);
		labelCorazon.setHorizontalAlignment(SwingConstants.CENTER);
		labelCorazon.setVerticalAlignment(SwingConstants.CENTER);

		// Agrego la imagen al panel
		panelCorazon.add(labelCorazon, BorderLayout.CENTER);

		// *******************************************************************

		// Agrego las secciones al panel izquierdo
		panelIzquierdo.add(panelNumeroRonda);
		panelIzquierdo.add(panelNumeroJugada);
		panelIzquierdo.add(panelPuntaje);
		panelIzquierdo.add(panelCorazon);

		return panelIzquierdo;
	}
	
	private JPanel crearPanelJugador() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(150, 150));
		panel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2), " " + vInicioSesion.getGetNombreUsuario() + " ",
						TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuentes[0], Font.BOLD, this.tamañoFuentes[0]), Color.WHITE));
		panel.setName(vInicioSesion.getGetNombreUsuario());

		// Contenedor para las cartas
		contenedorCartas = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contenedorCartas.setOpaque(false);
		panel.add(contenedorCartas, BorderLayout.CENTER);

		return panel;
	}

	private void inicializarPosicionesJugadores(String[] nombresJugadores) {
		posicionesJugadores = new HashMap<>();

		// Asociar jugadores a posiciones en base al índice
		posicionesJugadores.put(nombresJugadores[0], new Point(1, 2)); // Sur
		posicionesJugadores.put(nombresJugadores[1], new Point(0, 1)); // Oeste
		posicionesJugadores.put(nombresJugadores[2], new Point(1, 0)); // Norte
		posicionesJugadores.put(nombresJugadores[3], new Point(2, 1)); // Este
	}
	
	// *************************************************************
	//                    PASAJE DE CARTAS
	// *************************************************************

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
	
	// *************************************************************
	// 							JUEGO
	// *************************************************************

	// ****************** ACTUALIZAR ESTADO ************************
	
	private void actualizarJugadaPuntaje() throws RemoteException {
		labelJugada.setText(String.valueOf(this.controlador.numeroJugada()));

		int[] puntajes = this.controlador.puntajesJugadores();
		for (int i = 0; i < puntajes.length; i++) {

			((JLabel) panelPuntaje.getComponent(i * 2 + 1)).setText(String.valueOf(puntajes[i]));
		}

		panelIzquierdo.revalidate();
		panelIzquierdo.repaint();
	}

	private void actualizarRonda() throws RemoteException {
		labelRonda.setText("Ronda N° " + this.controlador.numeroRonda());

		panelIzquierdo.revalidate();
		panelIzquierdo.repaint();
	}

	private void actualizarEstadoJuego(String text) {
		labelBarraSuperior.setText(text);
		barraSuperior.revalidate();
		barraSuperior.repaint();
	}

	private void actualizarCorazon(Boolean corazonRoto) {
		if (corazonRoto) {
			labelCorazon.setIcon(iconoCorazonRoto);
		} else {
			labelCorazon.setIcon(iconoCorazon);
		}

		panelIzquierdo.revalidate();
		panelIzquierdo.repaint();
	}
		
	// ******** NUEVA JUGADA (ACTUALIZA JUGADA-PUNTOS) ************

	@Override
	public void nuevaJugada() throws RemoteException {
		actualizarJugadaPuntaje();
	}
	
	// ****************** CARTAS REPARTIDAS ************************

	@Override
	public void cartasRepartidas() throws RemoteException {
		crearVistaJuego();
		mostrarVista("juego");
		System.out.println("cartas repartidas");
		actualizarEstadoJuego("Cartas repartidas!");
		
		//Muestro la mano correspondiente del jugador
		mostrarCartasJugador(this.controlador.manoJugador(vInicioSesion.getGetNombreUsuario()));
	}

	private void mostrarCartasJugador(ArrayList<Carta> cartas) {

		contenedorCartas.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		contenedorCartas.removeAll();

		for (int i = 0; i < cartas.size(); i++) {
			Carta carta = cartas.get(i);
			VistaCarta vistaCarta = new VistaCarta(carta);

			// Panel vertical para la carta y su posición
			JPanel panelCarta = new JPanel();
			panelCarta.setLayout(new BorderLayout());
			panelCarta.setOpaque(false);
			
			final int indice = i; // Guardamos el índice actual

	        // Agregar evento de clic para jugar la carta
	        vistaCarta.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                jugarCarta(indice);
	            }
	            
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                // 🔹 Efecto cuando el mouse pasa sobre la carta
	                vistaCarta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	                // 🔹 Quitar el efecto cuando el mouse sale de la carta
	                vistaCarta.setBorder(null);
	            }
	            
	        });

			// Añadir la carta
			panelCarta.add(vistaCarta, BorderLayout.CENTER);

			contenedorCartas.add(panelCarta);
		}

		contenedorCartas.revalidate();
		contenedorCartas.repaint();
	}
	
	// ******************** JUGAR DOS DE TREBOL ********************
	
	@Override
	public void jugarDosDeTrebol() throws RemoteException{
		String jugadorActual = this.controlador.nombreJugadorActual();
		actualizarEstadoJuego("Jugador dos de trebol - " + jugadorActual);

		pedirCarta();
	}
	
	// ********************** PEDIR CARTA ***************************

	@Override
	public void pedirCarta() throws RemoteException {
		
		String jugadorActual = this.controlador.nombreJugadorActual();
		actualizarEstadoJuego("Turno de " + jugadorActual);
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
			System.out.println("Pedir cartas");
			
			String mensaje = "Es el turno del jugador " + jugadorActual;
	
			mostrarMensaje(mensaje);
			
		} else {
			esperaJugadorActual();
		}
	}
	
	private void jugarCarta(int indice) {
		try {
	        // 🔹 Mostrar mensaje con la carta seleccionada
			String jugadorActual = this.controlador.nombreJugadorActual();
			if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
				JOptionPane.showMessageDialog(this, "Has seleccionado: " + indice, "Carta Seleccionada", JOptionPane.INFORMATION_MESSAGE);
				
				controlador.cartaJugada(indice);
			} else {
				mostrarAvisoNoEsTuTurno();
			}
	        
	    } catch (RemoteException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void esperaJugadorActual() throws RemoteException {
		System.out.println("Esperando que el jugador " + this.controlador.nombreJugadorActual() + " termine su jugada...");
	}
	

	// ****************** CARTA TIRADA VALIDA **********************

	@Override
	public void cartaTiradaValida() throws RemoteException {

		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {

			// Crear la vista de la carta jugada
			Carta cartaAJugar = this.controlador.getCartaAJugar();
			VistaCarta vistaCarta = new VistaCarta(cartaAJugar);

			removerCartaDeLaMano(cartaAJugar);

			enviarCartaJugadaAlCentro(nombreJugador, vistaCarta);
			
			//1actualizarEstadoJuego("Cambio de turno");
			
		}
	}
	
	private void removerCartaDeLaMano(Carta carta) {
		// Iterar sobre los componentes del contenedor de cartas
		for (Component comp : contenedorCartas.getComponents()) {
			if (comp instanceof JPanel panelCarta) {
				// Buscar la VistaCarta dentro de este panel
				for (Component subComp : panelCarta.getComponents()) {
					if (subComp instanceof VistaCarta vistaSubCarta) {
						if (vistaSubCarta.getCarta().getPalo() == carta.getPalo() && 
								vistaSubCarta.getCarta().getValor() == carta.getValor()) {

							System.out.println("Chau carta");
							contenedorCartas.remove(panelCarta);
							return;

						}
					}
				}
			}
		}

		// Refrescar la vista del contenedor
		contenedorCartas.revalidate();
		contenedorCartas.repaint();

	}
	
	private void enviarCartaJugadaAlCentro(String nombreJugador, VistaCarta vistaCarta) {
	    // Obtener la posición del jugador en el panel central
	    Point posicion = posicionesJugadores.get(nombreJugador);

	    // Buscar el panel del jugador en la grilla
	    for (Component comp : panelCentro.getComponents()) {
	        if (comp instanceof JPanel) {
	            GridBagConstraints constraints = ((GridBagLayout) panelCentro.getLayout()).getConstraints(comp);
	            if (constraints.gridx == (int) posicion.getX() && constraints.gridy == (int) posicion.getY()) {
	                JPanel panelJugador = (JPanel) comp;

	                // Reemplazar solo la carta
	                Component[] componentes = panelJugador.getComponents();
	                for (Component c : componentes) {
	                    if (c instanceof VistaCarta) {
	                        panelJugador.remove(c);
	                        break;
	                    }
	                }

	                // Agregar la nueva carta jugada
	                panelJugador.add(vistaCarta, BorderLayout.CENTER);

	                // Refrescar la vista
	                panelJugador.revalidate();
	                panelJugador.repaint();
	                break;
	            }
	        }
	    }
	}

	// ****************** CARTA TIRADA INVALIDA ********************

	@Override
	public void cartaTiradaInvalida() throws RemoteException {
		
		/*String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {

			mostrarMensajeError("La carta que seleccioanste es invalida."
					+ "Tienes que tirar una carta del mismo palo que la que esta en la mesa."
					+ "Por favor, intentalo denuevo.");
			pedirCarta();
			
		}*/
		
	}

	@Override
	public void cartaTiradaInvalida2deTrebol() throws RemoteException {
		
		/*String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {

			mostrarMensajeError("La carta que seleccioanste es invalida. "
					+ "Para comenzar el juego si o si tienes que tirar " + "el 2 de Trebol. Por favor, intentalo denuevo.");
			pedirCarta();
		
		}*/
	}
	
	// ******************** PERDEDOR JUGADA ************************

	@Override
	public void perdedorJugada() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void corazonesRotos() throws RemoteException {
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
