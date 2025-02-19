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
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.modelo.Carta;
import ar.edu.unlu.corazones.vista.gui.FondoTapete;
import ar.edu.unlu.corazones.vista.gui.VistaCarta;

public class VistaGrafica extends JFrame implements IVista {
	
	private static final long serialVersionUID = 1L;

	// *************************************************************
	// 							CONSTANTES
	// *************************************************************

	private final String fuente = "Tahoma";

	private final int tamañoFuente = 14;

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

	private Map<String, Point> posicionesJugadores = new HashMap<>();;

	// *************************************************************
	//							CONSTRUCTOR
	// *************************************************************

	public VistaGrafica() {

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
	}

	// *************************************************************
// 							CONTROL DE VISTAS
	// *************************************************************

	public void mostrarVista(String vista) {
		cardLayout.show(panelPrincipal, vista);
		System.out.println("CAMBIO DE VISTA A: " + vista);
	}

	// *************************************************************
// 								MENSAJES
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
		crearMenu();
		setVisible(true);
		mostrarVista("menu");
	}

	private void crearMenu() {
		panelMenu = new JPanel();
		panelMenu.setLayout(new BorderLayout());
		panelMenu.setOpaque(false);

		/*
		 * Falta agregar logo JLabel lblImagen = new JLabel(new
		 * ImageIcon("/ar/edu/unlu/corazones/img/logo.png"));
		 * lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
		 * panelMenu.add(lblImagen, BorderLayout.NORTH);
		 */

		// Panel de botones
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
		panelBotones.setOpaque(false);

		int botonAncho = 200;
		int botonAlto = 40;

		JButton btnCrearJugador = crearBoton("Crear jugador", botonAncho, botonAlto);
		JButton btnModificarJugador = crearBoton("Modificar jugador", botonAncho, botonAlto);
		JButton btnListaJugadores = crearBoton("Ver lista de jugadores", botonAncho, botonAlto);
		JButton btnComenzarJuego = crearBoton("Comenzar juego", botonAncho, botonAlto);
		JButton btnSalir = crearBoton("Salir", botonAncho, botonAlto);

		// Agregar los botones y darles un espaciado
		int espaciado = 20;
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnCrearJugador);
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnModificarJugador);
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnListaJugadores);
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnComenzarJuego);
		panelBotones.add(Box.createVerticalStrut(espaciado));
		panelBotones.add(btnSalir);

		// Centrar botones
		panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

		/* LISTENERS DE BOTONES */
		btnCrearJugador.addActionListener(e -> nuevoJugador());

		btnModificarJugador.addActionListener(e -> modificarJugador());

		btnListaJugadores.addActionListener(e -> listarJugadores());

		btnComenzarJuego.addActionListener(e -> iniciarJuego());

		btnSalir.addActionListener(e -> System.exit(0));

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

	// ************************* ALTA ******************************

	private void nuevoJugador() {
		if (!this.controlador.isCantidadJugadoresValida()) {

			String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del nuevo jugador:", "Nuevo Jugador",
					JOptionPane.PLAIN_MESSAGE);

			if (nombre != null && !nombre.trim().isEmpty()) {

				this.controlador.agregarJugador(nombre);
				JOptionPane.showMessageDialog(this, "Jugador agregado con éxito.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);

			} else {

				JOptionPane.showMessageDialog(this, "El nombre del jugador no puede estar vacio.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {

			JOptionPane.showMessageDialog(this, "Ya están todos los jugadores inscritos.", "Límite Alcanzado",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	// ********************* MODIFICACION **************************

	private void modificarJugador() {
		String[] jugadores = this.controlador.listaJugadores();

		if (jugadores == null || jugadores.length == 0) {
			JOptionPane.showMessageDialog(this, "No hay jugadores registrados para modificar.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String seleccion = (String) JOptionPane.showInputDialog(this, "Seleccione el jugador a modificar:",
				"Modificar Jugador", JOptionPane.QUESTION_MESSAGE, null, jugadores, jugadores[0]);

		if (seleccion != null) {

			int pos = Arrays.asList(jugadores).indexOf(seleccion) + 1; // Obtengo la posicion en el arreglo

			String nuevoNombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre para el jugador:",
					"Modificar Jugador", JOptionPane.PLAIN_MESSAGE);
			if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {

				boolean modificado = controlador.modificarJugador(nuevoNombre, pos);

				if (modificado) {
					JOptionPane.showMessageDialog(this, "Jugador modificado con éxito.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);

				} else {

					JOptionPane.showMessageDialog(this, "No se pudo modificar el jugador.", "Error",
							JOptionPane.ERROR_MESSAGE);

				}
			} else {

				JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ******************* LISTA DE JUGADORES *********************

	private void listarJugadores() {
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

	private void iniciarJuego() {
		if (this.controlador.isCantidadJugadoresValida()) {

			JOptionPane.showMessageDialog(this, "Juego comenzado!", "Juego iniciado", JOptionPane.INFORMATION_MESSAGE);
			controlador.iniciarJuego();
		} else {

			JOptionPane.showMessageDialog(this, "Faltan jugadores para comenzar el juego", "Jugadores insuficientes",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// *************************************************************
	//                      PANTALLA JUEGO
	// *************************************************************
	
	private void crearVistaJuego() {
		panelJuego = new JPanel(new BorderLayout());
		panelJuego.setOpaque(false);

		// Barra superior
		barraSuperior = new JPanel(new BorderLayout());
		barraSuperior.setOpaque(false);
		barraSuperior.setPreferredSize(new Dimension(1100, 50));
		barraSuperior.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2), " Estado del juego ",
						TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuente, Font.BOLD, tamañoFuente), Color.WHITE));
		barraSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Ocupa todo el ancho
		labelBarraSuperior = new JLabel("", SwingConstants.CENTER);
		labelBarraSuperior.setFont(new Font("Arial", Font.BOLD, tamañoFuente));
		labelBarraSuperior.setForeground(Color.WHITE);
		barraSuperior.add(labelBarraSuperior, BorderLayout.CENTER);
		panelJuego.add(barraSuperior, BorderLayout.NORTH);

		// Panel izquierdo (información, estadísticas, etc.)
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

	private JPanel crearPanelCentro() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setOpaque(false);

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.weightx = 1.0;
	    gbc.weighty = 1.0;
	    gbc.anchor = GridBagConstraints.CENTER; 

	    // Fuente y color para los nombres
	    Font fuenteNombres = new Font("Arial", Font.BOLD, 14);
	    Color colorTexto = Color.WHITE;
	    
	    String[] jugadores = this.controlador.listaJugadores();

	    // Crear los paneles de cada jugador
	    JPanel panelNorte = crearPanelJugadorCentro(jugadores[2], new VistaCarta(), fuenteNombres, colorTexto);
	    JPanel panelSur = crearPanelJugadorCentro(jugadores[0], new VistaCarta(), fuenteNombres, colorTexto);
	    JPanel panelEste = crearPanelJugadorCentro(jugadores[3], new VistaCarta(), fuenteNombres, colorTexto);
	    JPanel panelOeste = crearPanelJugadorCentro(jugadores[1], new VistaCarta(), fuenteNombres, colorTexto);

	    // Ubicar los paneles en el GridBagLayout
	    gbc.gridx = 1; gbc.gridy = 0;
	    panel.add(panelNorte, gbc);  // Jugador 1 (Norte)

	    gbc.gridx = 1; gbc.gridy = 2;
	    panel.add(panelSur, gbc);  // Jugador 2 (Sur)

	    gbc.gridx = 2; gbc.gridy = 1;
	    panel.add(panelEste, gbc);  // Jugador 3 (Este)

	    gbc.gridx = 0; gbc.gridy = 1;
	    panel.add(panelOeste, gbc);  // Jugador 4 (Oeste)

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
	
	private JPanel crearPanelIzquierdo() {
		panelIzquierdo = new JPanel();
		panelIzquierdo.setOpaque(false);
		panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
		panelIzquierdo.setPreferredSize(new Dimension(300, 150));

		// ******************* Sección: Número de Ronda *********************

		panelNumeroRonda = new JPanel(new BorderLayout());
		panelNumeroRonda.setOpaque(false);
		panelNumeroRonda.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				" Ronda N° ", TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuente, Font.BOLD, tamañoFuente), Color.WHITE));
		panelNumeroRonda.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Ocupa todo el ancho
		labelRonda = new JLabel("1", SwingConstants.CENTER);
		labelRonda.setFont(new Font("Arial", Font.BOLD, tamañoFuente));
		labelRonda.setForeground(Color.WHITE);
		panelNumeroRonda.add(labelRonda, BorderLayout.CENTER);

		// ******************* Sección: Número de Jugada *********************

		panelNumeroJugada = new JPanel(new BorderLayout());
		panelNumeroJugada.setOpaque(false);
		panelNumeroJugada.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				" Jugada N°", TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuente, Font.BOLD, tamañoFuente), Color.WHITE));
		panelNumeroJugada.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Ocupa todo el ancho
		labelJugada = new JLabel("1", SwingConstants.CENTER);
		labelJugada.setFont(new Font("Arial", Font.BOLD, tamañoFuente));
		labelJugada.setForeground(Color.WHITE);
		panelNumeroJugada.add(labelJugada, BorderLayout.CENTER);

		// **** Sección: Puntajes (GridLayout de 4 filas, 2 columnas) ********

		panelPuntaje = new JPanel(new GridLayout(4, 2, 5, 5));
		panelPuntaje.setOpaque(false);
		panelPuntaje.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				" Puntaje ", TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuente, Font.BOLD, tamañoFuente), Color.WHITE));
		panelPuntaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); // Ocupa todo el ancho

		String[] jugadores = this.controlador.listaJugadores();
		
		for (int i = 0; i < 4; i++) {
			
			JLabel nombreJugador = new JLabel(jugadores[i], SwingConstants.CENTER);
			nombreJugador.setForeground(Color.WHITE);
			nombreJugador.setFont(new Font(this.fuente, Font.BOLD, tamañoFuente));

			JLabel puntajeJugador = new JLabel("0", SwingConstants.CENTER);
			puntajeJugador.setFont(new Font(this.fuente, Font.BOLD, tamañoFuente));
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

		// Agregar el label con la imagen al panel
		panelCorazon.add(labelCorazon, BorderLayout.CENTER);

		// *******************************************************************

		// Agregar las secciones al panel izquierdo
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
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2), " Nombre del jugador ",
						TitledBorder.CENTER, TitledBorder.TOP, new Font(this.fuente, Font.BOLD, tamañoFuente), Color.WHITE));
		panel.setName("Nombre del jugador");

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
	public void pasajeDeCartas() {
		System.out.println("Pasaje");
		actualizarEstadoJuego("PASAJE DE CARTAS");
		mostrarMensaje("PASAJE DE CARTAS" + "\n" + direccionPasaje());
	}
	
	public String direccionPasaje() {
		String s = "No hay pasaje de cartas";
		String direccion = this.controlador.direccionPasaje();
		if (direccion != null) {
			s = "Las cartas se pasan en la siguiente direccion: " + direccion + "\n";
			s += "Cantidad de cartas a pasar: " + String.valueOf(this.controlador.cantidadCartasPasaje());
		}
		return s;
	}
	
	// ****************** PEDIR CARTA (pasaje) *********************

	@Override
	public void pedirCartaPasaje() {
		System.out.println("Pedir cartas Pasaje");
		
		mostrarCartasJugador(this.controlador.manoJugador(this.controlador.posicionJugadorActual()));
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		String mensaje = "Es el turno del jugador " +  jugadorActual;
		
		actualizarEstadoJuego("Turno de " + jugadorActual);
		mostrarMensaje(mensaje);
		
		int indiceCarta = mostrarSeleccionCarta(mensaje);
		
		if (indiceCarta >= 0) {
			System.out.println(indiceCarta);
	        controlador.cartaJugadaPasaje(indiceCarta);
	    } else {
	    	mostrarMensajeError("Selección inválida. Intente nuevamente.");
	        pedirCartaPasaje(); // Volver a pedir si el índice no es válido
	    }
	}

	// ************** CARTA TIRADA VALIDA PASAJE *******************
	
	@Override
	public void cartaTiradaValidaPasaje() {
		
		Carta cartaAJugar = this.controlador.getCartaAJugar();
		String nombreJugador = this.controlador.nombreJugadorActual();
		
		marcarCartaPasaje(nombreJugador, cartaAJugar);
	}
	
	private void marcarCartaPasaje(String nombreJugador, Carta carta) {
	    // Obtener la posición del jugador en el panel central
	    Point posicion = posicionesJugadores.get(nombreJugador);

	    // Buscar el panel del jugador en la grilla
	    for (Component comp : panelCentro.getComponents()) {
	        if (comp instanceof JPanel panelJugador) {
	            GridBagConstraints constraints = ((GridBagLayout) panelCentro.getLayout()).getConstraints(comp);

	            // Verificar que sea el panel del jugador correspondiente
	            if (constraints.gridx == (int) posicion.getX() && constraints.gridy == (int) posicion.getY()) {
	                
	                // Buscar la VistaCarta dentro del panel del jugador
	                for (Component subComp : panelJugador.getComponents()) {
	                    if (subComp instanceof VistaCarta vistaSubCarta) {
	                        
	                        // Verificar si es la carta que debe marcarse
	                        if (vistaSubCarta.getCarta().equals(carta)) {
	                            
	                            // Marcar con borde azul
	                            vistaSubCarta.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
	                            System.out.println("Carta marcada con borde azul");
	                            
	                            // Refrescar el panel sin afectar el layout
	                            vistaSubCarta.revalidate();
	                            vistaSubCarta.repaint();
	                            
	                            return;
	                        }
	                    }
	                }
	            }
	        }
	    }
	}

	// ************ CARTA TIRADA INVALIDA PASAJE *******************
	
	@Override
	public void cartaTiradaInvalidaPasaje() {
		mostrarMensajeError("La carta que seleccioanste es invalida."
				+ " Por favor, intentalo denuevo.");
		pedirCartaPasaje();
	}

	// *************************************************************
	// 							JUEGO
	// *************************************************************

	// ****************** ACTUALIZAR DATOS ************************

	private void actualizarJugadaPuntaje() {
		labelJugada.setText(String.valueOf(this.controlador.numeroJugada()));

		int[] puntajes = this.controlador.puntajesJugadores();
		for (int i = 0; i < puntajes.length; i++) {

			((JLabel) panelPuntaje.getComponent(i * 2 + 1)).setText(String.valueOf(puntajes[i]));
		}

		System.out.println("Componentes en panelPuntaje: " + panelPuntaje.getComponentCount());

		panelIzquierdo.revalidate();
		panelIzquierdo.repaint();
	}

	private void actualizarRonda() {
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
	public void nuevaJugada() {
		actualizarJugadaPuntaje();
	}

	// ****************** CARTAS REPARTIDAS ************************

	@Override
	public void cartasRepartidas() {
		crearVistaJuego();
		mostrarVista("juego");
		System.out.println("cartas repartidas");
		actualizarEstadoJuego("Cartas repartidas!");
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

			// Añadir la carta
			panelCarta.add(vistaCarta, BorderLayout.CENTER);

			// Crear y añadir el JLabel con la posición
			JLabel labelPosicion = new JLabel(String.valueOf(i + 1));
			labelPosicion.setHorizontalAlignment(SwingConstants.CENTER); // Texto centrado
			labelPosicion.setFont(new Font("Arial", Font.PLAIN, 12));
			labelPosicion.setForeground(Color.WHITE);
			panelCarta.add(labelPosicion, BorderLayout.SOUTH);

			contenedorCartas.add(panelCarta);
		}

		/*
		 * for (Carta carta: cartas) { VistaCarta vistaCarta = new VistaCarta(carta);
		 * contenedorCartas.add(vistaCarta); }
		 */

		contenedorCartas.revalidate();
		contenedorCartas.repaint();
	}

	// ******************** JUGAR DOS DE TREBOL ********************

	@Override
	public void jugarDosDeTrebol() {
		actualizarEstadoJuego("Jugador dos de trebol");

		String jugadorActual = this.controlador.nombreJugadorActual();

		JOptionPane.showMessageDialog(this,
				"Comienza la ronda el jugador " + jugadorActual + " ya que tiene el 2 de trebol", "Comienzo de ronda",
				JOptionPane.INFORMATION_MESSAGE);

		pedirCarta();
	}

	// ********************** PEDIR CARTA ***************************

	@Override
	public void pedirCarta() {
		System.out.println("Pedir cartas");

		mostrarCartasJugador(this.controlador.manoJugador(this.controlador.posicionJugadorActual()));
		String jugadorActual = this.controlador.nombreJugadorActual();

		String mensaje = "Es el turno del jugador " + jugadorActual;

		actualizarEstadoJuego("Turno de " + jugadorActual);

		mostrarMensaje(mensaje);

		int indiceCarta = mostrarSeleccionCarta(mensaje);

		if (indiceCarta >= 0) {
			System.out.println(indiceCarta);
			actualizarEstadoJuego("Cambio de turno");
			controlador.cartaJugada(indiceCarta);
		} else {
			mostrarMensajeError("Selección inválida. Intente nuevamente.");
			pedirCarta(); // Volver a pedir si el índice no es válido
		}
	}

	private int mostrarSeleccionCarta(String text) {
		String entrada = JOptionPane.showInputDialog(this, "Ingrese el número de la carta que desea jugar (1 a X):",
				text, JOptionPane.QUESTION_MESSAGE);

		System.out.println(entrada);
		try {
			int seleccion = Integer.parseInt(entrada);
			return seleccion - 1;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	// ****************** CARTA TIRADA VALIDA **********************

	@Override
	public void cartaTiradaValida() {

		// Crear la vista de la carta jugada
		Carta cartaAJugar = this.controlador.getCartaAJugar();
		String nombreJugador = this.controlador.nombreJugadorActual();
		VistaCarta vistaCarta = new VistaCarta(cartaAJugar);

		removerCartaDeLaMano(cartaAJugar);

		enviarCartaJugadaAlCentro(nombreJugador, vistaCarta);
	}

	private void removerCartaDeLaMano(Carta carta) {
		// Iterar sobre los componentes del contenedor de cartas
		for (Component comp : contenedorCartas.getComponents()) {
			if (comp instanceof JPanel panelCarta) {
				// Buscar la VistaCarta dentro de este panel
				for (Component subComp : panelCarta.getComponents()) {
					if (subComp instanceof VistaCarta vistaSubCarta) {
						if (vistaSubCarta.getCarta().equals(carta)) {

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

	/*private void enviarCartaJugadaAlCentro(String nombreJugador, VistaCarta vistaCarta) {
		// Obtener la posición del jugador en el panel central
		Point posicion = posicionesJugadores.get(nombreJugador);

		// Coordenadas para la carta
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = (int) posicion.getX();
		gbc.gridy = (int) posicion.getY();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;

		// Eliminar cualquier componente existente en esa posición (si es necesario)
		for (Component comp : panelCentro.getComponents()) {
			GridBagConstraints constraints = ((GridBagLayout) panelCentro.getLayout()).getConstraints(comp);
			if (constraints.gridx == gbc.gridx && constraints.gridy == gbc.gridy) {
				panelCentro.remove(comp);
				break;
			}
		}

		// Agregar la carta jugada en la posición correspondiente
		panelCentro.add(vistaCarta, gbc);

		// Refrescar vista
		panelCentro.revalidate();
		panelCentro.repaint();
	}*/
	
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
	public void cartaTiradaInvalida() {
		mostrarMensajeError("La carta que seleccioanste es invalida."
				+ "Tienes que tirar una carta del mismo palo que la que esta en la mesa."
				+ "Por favor, intentalo denuevo.");
		pedirCarta();
	}

	@Override
	public void cartaTiradaInvalida2deTrebol() {
		mostrarMensajeError("La carta que seleccioanste es invalida. "
				+ "Para comenzar el juego si o si tienes que tirar " + "el 2 de Trebol. Por favor, intentalo denuevo.");
		pedirCarta();
	}

	// ******************** PERDEDOR JUGADA ************************

	@Override
	public void perdedorJugada() {
		mostrarMensajeError("El perdedor de esta jugada es " + this.controlador.jugadorPerdedorJugada() + "\n");

		limpiarCartasJugadas();
	}

	/*private void limpiarCartasJugadas() {
		// Itero sobre todos los componentes del panel central
		Component[] componentes = panelCentro.getComponents();

		// Elimino todos los componentes que sean instancias de VistaCarta
		for (Component componente : componentes) {
			if (componente instanceof VistaCarta) {

				GridBagConstraints gbc = ((GridBagLayout) panelCentro.getLayout()).getConstraints(componente);

				panelCentro.remove(componente);

				VistaCarta nuevaCarta = new VistaCarta();

				panelCentro.add(nuevaCarta, gbc);
			}
		}

		// Refresco el panel central para que los cambios sean visibles
		panelCentro.revalidate();
		panelCentro.repaint();
	}*/
	
	private void limpiarCartasJugadas() {
	    for (Component comp : panelCentro.getComponents()) {
	        if (comp instanceof JPanel) {
	            JPanel panelJugador = (JPanel) comp;

	            // Buscar y remover la carta jugada dentro del panel
	            Component[] componentes = panelJugador.getComponents();
	            for (Component c : componentes) {
	                if (c instanceof VistaCarta) {
	                    panelJugador.remove(c);
	                    break;
	                }
	            }

	            // Agregar una nueva carta vacía
	            VistaCarta nuevaCarta = new VistaCarta();
	            panelJugador.add(nuevaCarta, BorderLayout.CENTER);

	            // Refrescar el panel
	            panelJugador.revalidate();
	            panelJugador.repaint();
	        }
	    }
	}
	
	// ****************** FIN PASAJE DE CARTAS *********************
	
	@Override
	public void finPasajeDeCartas() {
		mostrarMensaje("FIN DEL PASAJE DE CARTAS");
		mostrarMensaje("COMIENZA LA RONDA");
	}
	
	// ******************* CORAZONES ROTOS ***********************

	@Override
	public void corazonesRotos() {
		// TODO Auto-generated method stub
		actualizarCorazon(true);
		actualizarEstadoJuego("CORAZONES ROTOS");
		mostrarMensaje("A partir de ahora se pueden tirar corazones");
	}

	// *************************************************************
	// 						   PUNTAJE
	// *************************************************************

	private String puntaje() {
		String s = "*          PUNTAJE         *" + "\n";
		s += "\n";
		int[] puntajes = this.controlador.puntajesJugadores();
		for (int i = 0; i < puntajes.length; i++) {
			s += (i + 1) + ") " + this.controlador.getJugador(i) + " -> " + puntajes[i];
		}
		s += "\n";
		s += "****************************" + "\n";
		return s;
	}

	// *************************************************************
	//                       FIN DE RONDA
	// *************************************************************

	@Override
	public void finDeRonda() {
		actualizarEstadoJuego("FIN DE RONDA");
		mostrarMensaje("FIN DE LA RONDA");
		mostrarMensaje("Asi estan los puntajes hasta el momento" + "\n" + puntaje());
		actualizarRonda();
		actualizarCorazon(false);
	}

	// *************************************************************
	//                       FIN DE JUEGO
	// *************************************************************

	@Override
	public void finDeJuego() {
		actualizarEstadoJuego("FIN DEL JUEGO");
		mostrarMensaje("FIN DEL JUEGO");
		String jugadorGanador = this.controlador.ganadorJuego();
		actualizarEstadoJuego("GANADOR DEL JUEGO: " + jugadorGanador + " ¡¡¡FELICIDADES!!!");
		mostrarMensaje(
				puntaje() + "\n" + "El ganador fue " + this.controlador.ganadorJuego() + "\n" + "¡¡¡FELICIDADES!!!");
	}

	// ************************************************************
	//                        OBSERVER
	// ************************************************************

	@Override
	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
	}

}
