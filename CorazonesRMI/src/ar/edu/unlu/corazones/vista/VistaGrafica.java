package ar.edu.unlu.corazones.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.modelo.Carta;
import ar.edu.unlu.corazones.vista.gui.FondoTapete;
import ar.edu.unlu.corazones.vista.gui.VistaCarta;
import ar.edu.unlu.corazones.vista.gui.VistaInicioSesion;
import ar.edu.unlu.serializacion.Ganadores;
import ar.edu.unlu.serializacion.JugadorRanking;
import ar.edu.unlu.serializacion.Serializador;

public class VistaGrafica extends JFrame implements IVista {

	private static final long serialVersionUID = 1L;
	
	private static Serializador serializador = new Serializador("src/datos.dat");

	// *************************************************************
	// 							CONSTANTES
	// *************************************************************

	private final String[] fuentes = {"Tahoma","Arial"};

	private final int[] tamañoFuentes = {14,12};
	
	private final int[] tiemposMensajes = {2000, 3000};

	private final ImageIcon menuCorazones = new ImageIcon(getClass().getResource("/ar/edu/unlu/corazones/img/menu.png"));
	
	private final ImageIcon iconCorazones = new ImageIcon(getClass().getResource("/ar/edu/unlu/corazones/img/corazon.png"));
	
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
	
	// *************************************************************
	//							CONSTRUCTOR
	// *************************************************************
	


	public VistaGrafica(Controlador controlador) {
			
		this.controlador = controlador;
		this.controlador.setVista(this);
		//System.out.println(serializador);

		/* CONFIGURACIONES DE VENTANA */
		setTitle("Corazones");
		setSize(1100, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(iconCorazones.getImage());


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
				vInicioSesion.getGetNombreUsuario();
				controlador.conectarJugador(vInicioSesion.getGetNombreUsuario());
				iniciarMenu();
			}
		});
		
		/* CONTROL DE DESCONEXION DEL JUGADOR */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.desconectarJugador();
                ////System.out.println("Jugador desconectado correctamente.");
                dispose(); 
            }
        });
	}
	

	
	// *************************************************************
	// 						CONTROL DE VISTAS
	// *************************************************************

	public void mostrarVista(String vista) {
		cardLayout.show(panelPrincipal, vista);
		//System.out.println("CAMBIO DE VISTA A: " + vista);
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
	
	private void mostrarMensajeTemporal(String mensaje, int tiempoMilisegundos) {
	  
	    JOptionPane pane = new JOptionPane(mensaje, JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = pane.createDialog(this, "Mensaje");
	    
	    // Iniciar un Timer para cerrar el diálogo después de X milisegundos
	    Timer timer = new Timer(tiempoMilisegundos, e -> dialog.dispose());
	    timer.setRepeats(false); // Solo se ejecuta una vez
	    timer.start();
	    
	    // Mostrar el cuadro de diálogo
	    dialog.setVisible(true);
	
	    // Si el usuario lo cierra antes, detener el Timer
	    timer.stop();
	}

	private void mostrarAviso(String mensaje, int tiempo) {
	    JDialog dialogo = new JDialog(this, "Aviso", false);
	    dialogo.setLayout(new BorderLayout());

	    JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
	    dialogo.add(label, BorderLayout.CENTER);

	    // Obtengo el ancho y alto del texto
	    FontMetrics metrics = label.getFontMetrics(label.getFont());
	    int textWidth = metrics.stringWidth(mensaje);
	    int textHeight = metrics.getHeight();

	    // Definir un padding para que no quede justo
	    int paddingX = 40; 
	    int paddingY = 40; 
	    int width = Math.max(250, textWidth + paddingX);
	    int height = Math.max(100, textHeight + paddingY);

	    // Ajustar tamaño dinámico
	    dialogo.setSize(width, height);
	    dialogo.setLocationRelativeTo(this);

	    // Cierra el cartel después de X segundos
	    new Timer(tiempo, e -> dialogo.dispose()).start();

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

	    // Panel de logo + botones
	    JPanel panelContenido = new JPanel();
	    panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
	    panelContenido.setOpaque(false);

	    int botonAncho = 200;
	    int botonAlto = 40;

	    JButton btnListaJugadores = crearBoton("Ver lista de jugadores", botonAncho, botonAlto);
	    JButton btnComenzarJuego = crearBoton("Comenzar juego", botonAncho, botonAlto);
	    JButton btnRankingJugadores = crearBoton("Ranking de ganadores", botonAncho, botonAlto);
	    JButton btnSalir = crearBoton("Salir", botonAncho, botonAlto);

	    // Imagen/Logo del juego
	    JLabel imagenLabel = new JLabel(menuCorazones);
	    imagenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panelContenido.add(imagenLabel);

	    // Agregar los botones y darles un espaciado
	    int espaciado = 20;

	    panelContenido.add(Box.createVerticalStrut(espaciado));
	    panelContenido.add(btnComenzarJuego);
	    panelContenido.add(Box.createVerticalStrut(espaciado));
	    panelContenido.add(btnListaJugadores);
	    panelContenido.add(Box.createVerticalStrut(espaciado));
	    panelContenido.add(btnRankingJugadores);
	    panelContenido.add(Box.createVerticalStrut(espaciado));
	    panelContenido.add(btnSalir);

	    panelContenido.setAlignmentX(Component.CENTER_ALIGNMENT);  // Botones centrados

	    btnListaJugadores.addActionListener(e -> {
	        try {
	            listarJugadores();
	        } catch (RemoteException e1) {
	            e1.printStackTrace();
	        }
	    });

	    btnComenzarJuego.addActionListener(e -> {
	        try {
	            iniciarJuego();
	        } catch (HeadlessException | RemoteException e1) {
	            e1.printStackTrace();
	        }
	    });
	    
	    btnRankingJugadores.addActionListener(e -> {
	        try {
	            verRankingGandores();
	        } catch (HeadlessException | RemoteException e1) {
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
	    contenedorBotones.add(panelContenido);

	    // Asegurarse de que el panelContenido esté centrado dentro del panel principal
	    panelMenu.add(contenedorBotones, BorderLayout.CENTER);

	    panelPrincipal.add(panelMenu, "menu");
	}
	
	// Método para crear botones con tamaño fijo
	private JButton crearBoton(String texto, int ancho, int alto) {
		JButton boton = new JButton(texto);
		
		boton.setMaximumSize(new Dimension(ancho, alto));
		boton.setPreferredSize(new Dimension(ancho, alto));
		boton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    boton.setFocusPainted(false);
	    boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	    boton.setFont(new Font(fuentes[1], Font.BOLD, 16));
	    boton.setForeground(Color.BLACK);
	    boton.setBackground(new Color(200, 200, 200));

	    // Efecto hover
	    boton.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            boton.setBackground(new Color(50, 205, 50));
	            boton.setForeground(Color.WHITE);
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            boton.setBackground(new Color(200, 200, 200)); 
	            boton.setForeground(Color.BLACK);
	        }
	    });
	    
	    
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
			
		    lista.append((i + 1)).append(") ");

		    if (jugadores[i] == null) {
		        lista.append("(Sin agregar)");
		    } else if (vInicioSesion.getGetNombreUsuario().equals(jugadores[i])) {
		        lista.append(jugadores[i]).append(" (*)");
		    } else {
		        lista.append(jugadores[i]);
		    }
		    
		    lista.append("\n");	
		
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
	    
	    // Asocio cada indice con el panel del jugador
	    panelNorte.putClientProperty("jugadorIndex", 2);
	    panelSur.putClientProperty("jugadorIndex", 0);
	    panelEste.putClientProperty("jugadorIndex", 3);
	    panelOeste.putClientProperty("jugadorIndex", 1);
	    
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

	    // Tamaño fijo de panel
	    int anchoPanel = carta.getAncho(); 
	    int altoPanel = carta.getAlto();
	    panelJugador.setPreferredSize(new Dimension(anchoPanel, altoPanel));

	    JLabel labelNombre = new JLabel(nombre, SwingConstants.CENTER);
	    labelNombre.setFont(fuente);
	    labelNombre.setForeground(color);

	    //panelJugador.add(labelNombre);
	    panelJugador.add(labelNombre, BorderLayout.NORTH);
	    panelJugador.add(carta, BorderLayout.CENTER);
	    
	    panelJugador.setName(nombre);

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
	
	// *************************************************************
	//                    PASAJE DE CARTAS
	// *************************************************************
	
	// ****************** CARTAS REPARTIDAS ************************

	@Override
	public void cartasRepartidas() throws RemoteException {
		crearVistaJuego();
		mostrarVista("juego");
		
		mostrarAviso("Repartiendo cartas!",tiemposMensajes[0]);
		
		//Muestro la mano correspondiente del jugador
		mostrarCartasJugadorPasaje(this.controlador.manoJugador(vInicioSesion.getGetNombreUsuario()));
	}
	
	private void mostrarCartasJugadorPasaje(ArrayList<Carta> cartas) {

		contenedorCartas.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		contenedorCartas.removeAll();

		for (int i = 0; i < cartas.size(); i++) {
			Carta carta = cartas.get(i);
			VistaCarta vistaCarta = new VistaCarta(carta);

			// Panel vertical para la carta y su posición
			JPanel panelCarta = new JPanel();
			panelCarta.setLayout(new BorderLayout());
			panelCarta.setOpaque(false);
			
			final int indice = i; // Indice actual

	        // Eventos de click para jugar la carta
	        vistaCarta.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                jugarCartaPasaje(indice, carta.getCarta());
	            }
	            
	            @Override
	            public void mouseEntered(MouseEvent e) {
	            	// Si la carta NO está marcada en azul, se le pone borde amarillo
	                if (!vistaCarta.isMarcada()) {
	                    vistaCarta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
	                }
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	            	// Si la carta NO está marcada en azul, se le quita el borde
	                if (!vistaCarta.isMarcada()) {
	                    vistaCarta.setBorder(null);
	                }
	            }
	            
	        });

			// Añadir la carta
			panelCarta.add(vistaCarta, BorderLayout.CENTER);

			contenedorCartas.add(panelCarta);
		}

		contenedorCartas.revalidate();
		contenedorCartas.repaint();
	}

	// ************ PANTALLA PARA PASAJE X JUGADOR *****************
	
	@Override
	public void pasajeDeCartas() throws RemoteException {
		int cantCartas = this.controlador.cantidadCartasPasaje();
		String direccion = this.controlador.direccionPasaje(); 
		mostrarAviso("Cada jugador debe pasar " + String.valueOf(cantCartas) + " a su " + direccion,tiemposMensajes[0]);
	}

	// ****************** PEDIR CARTA (pasaje) *********************
	
	@Override
	public void pedirCartaPasaje() throws RemoteException {
		// TODO Auto-generated method stub
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
			
			//System.out.println("Pedir cartas pasaje");
			actualizarEstadoJuego("ES TU TURNO PARA PASAR CARTAS");
			
		} else {
			actualizarEstadoJuego("Esperando al jugador " + jugadorActual + "...");
		}
	}
	
	private void jugarCartaPasaje(int indice, String carta) {
		try {
			String jugadorActual = this.controlador.nombreJugadorActual();
			if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
				JOptionPane.showMessageDialog(this, "Has seleccionado la carta: " + carta, "Carta Seleccionada", JOptionPane.INFORMATION_MESSAGE);
				controlador.cartaJugada(indice);
				
			} else {
				mostrarAviso("Aun no es tu turno. Espera por favor.",tiemposMensajes[0]);
			}
	        
	    } catch (RemoteException e) {
	        e.printStackTrace();
	    }
	}
	
	// ************ CARTA TIRADA INVALIDA PASAJE *******************
	
	@Override
	public void cartaTiradaInvalidaPasaje() throws RemoteException {
		// TODO Auto-generated method stub
		
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {

			mostrarMensajeError("La carta que seleccioanste es invalida. Por favor, intentalo denuevo.");
			pedirCartaPasaje();
			
		}
		
	}
	
	// ************** CARTA TIRADA VALIDA PASAJE *******************

	@Override
	public void cartaTiradaValidaPasaje() throws RemoteException {
		// TODO Auto-generated method stub
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
			Carta cartaAJugar = this.controlador.getCartaAJugar();	
			marcarCartaPasaje(cartaAJugar);
		}
		
	}
	
	private void marcarCartaPasaje(Carta carta) {

	    for (Component comp : contenedorCartas.getComponents()) {
	        if (comp instanceof JPanel panelCarta) {
	            // Buscar la VistaCarta dentro del panel
	            for (Component subComp : panelCarta.getComponents()) {
	                if (subComp instanceof VistaCarta vistaSubCarta) {
	                    if (vistaSubCarta.getCarta().getPalo() == carta.getPalo() && 
								vistaSubCarta.getCarta().getValor() == carta.getValor()) {
	                        //System.out.println("Carta marcada con borde azul");

	                        //Borde azul para carta tirada
	                        vistaSubCarta.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
	                        vistaSubCarta.setMarcada(true); // Marcar como azul

	                        //Actualiza la vista
	                        vistaSubCarta.revalidate();
	                        vistaSubCarta.repaint();
	                        panelCarta.revalidate();
	                        panelCarta.repaint();
	                        contenedorCartas.revalidate();
	                        contenedorCartas.repaint();

	                        return;
	                    }
	                }
	            }
	        }
	    }
	}

	// ****************** FIN PASAJE DE CARTAS *********************
	
	@Override
	public void finPasajeDeCartas() throws RemoteException {
		// TODO Auto-generated method stub
		mostrarAviso("Fin del pasaje de cartas",tiemposMensajes[0]);
		mostrarCartasJugador(this.controlador.manoJugador(vInicioSesion.getGetNombreUsuario()));
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
			
			final int indice = i; // Indice actual

	        // Eventos de click para jugar la carta
	        vistaCarta.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                jugarCarta(indice, carta.getCarta());
	            }
	            
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                //Efecto cuando el mouse pasa sobre la carta
	                vistaCarta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	                //Quitar el efecto cuando el mouse sale de la carta
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
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
			mostrarMensajeTemporal("Como es la primer jugada, usted debe tirar el 2 de trebol",tiemposMensajes[1]);
		}
		pedirCarta();
	}
	
	// ********************** PEDIR CARTA ***************************

	@Override
	public void pedirCarta() throws RemoteException {
		
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
			
			mostrarCartasJugador(this.controlador.manoJugador(vInicioSesion.getGetNombreUsuario()));
			//System.out.println("Pedir cartas");
			actualizarEstadoJuego("ES TU TURNO - SELECCIONA UNA CARTA");
			
		} else {
			actualizarEstadoJuego("Esperando al jugador " + jugadorActual + "...");
		}
	}
	
	private void jugarCarta(int indice, String carta) {
		try {
			String jugadorActual = this.controlador.nombreJugadorActual();
			if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {
				JOptionPane.showMessageDialog(this, "Has seleccionado la carta: " + carta, "Carta Seleccionada", JOptionPane.INFORMATION_MESSAGE);
				controlador.cartaJugada(indice);
				
				// Volver a mostrar las cartas con los índices actualizados
				//mostrarCartasJugador(this.controlador.manoJugador(vInicioSesion.getGetNombreUsuario()));
			} else {
				mostrarAviso("Aun no es tu turno. Espera por favor.",tiemposMensajes[0]);
			}
	        
	    } catch (RemoteException e) {
	        e.printStackTrace();
	    }
	}
	

	// ****************** CARTA TIRADA VALIDA **********************

	@Override
	public void cartaTiradaValida() throws RemoteException {

		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {

			removerCartaDeLaMano(this.controlador.getCartaAJugar());
			
		}
		
		//Cada vez que se tira una carta, se tiene que actualizar la mesa de cada jugador
		actualizarMesa();
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

	                        //System.out.println("Chau carta");
	                        contenedorCartas.remove(panelCarta);
	                        
	                        // Refrescar la vista del contenedor
	                        contenedorCartas.revalidate();
	                        contenedorCartas.repaint();   
	                        return;

						}
					}
				}
			}
		}

		
		contenedorCartas.revalidate();
		contenedorCartas.repaint();

	}
	
	private void actualizarMesa() throws RemoteException {
	    Carta[] cartasEnMesa = this.controlador.cartasEnMesa();

	    for (int i = 0; i < cartasEnMesa.length; i++) {
	        if (cartasEnMesa[i] != null) { // Si el jugador ya jugo una carta
	            VistaCarta vistaCarta = new VistaCarta(cartasEnMesa[i]); 
	            
	            // Busco el panel del jugador por su indice en el componente
	            for (Component comp : panelCentro.getComponents()) {
	                if (comp instanceof JPanel) {
	                    JPanel panelJugador = (JPanel) comp;
	                    Integer jugadorIndex = (Integer) panelJugador.getClientProperty("jugadorIndex");

	                    if (jugadorIndex != null && jugadorIndex == i) { 
	                    	
	                        // Reemplazar la carta en el panel
	                        Component[] componentes = panelJugador.getComponents();
	                        for (Component c : componentes) {
	                            if (c instanceof VistaCarta) {
	                                panelJugador.remove(c);
	                                break;
	                            }
	                        }

	                        panelJugador.add(vistaCarta, BorderLayout.CENTER);

	                        // Refrescar la vista
	                        panelJugador.revalidate();
	                        panelJugador.repaint();
	                        break;
	                    }
	                }
	            }
	        }
	    }
	}


	// ****************** CARTA TIRADA INVALIDA ********************

	@Override
	public void cartaTiradaInvalida() throws RemoteException {
		
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {

			mostrarMensajeError("La carta que seleccioanste es invalida."
					+ "Tienes que tirar una carta del mismo palo que la que esta en la mesa."
					+ "Por favor, intentalo denuevo.");
			pedirCarta();
			
		}
		
	}

	@Override
	public void cartaTiradaInvalida2deTrebol() throws RemoteException {
		
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorActual)) {

			mostrarMensajeError("La carta que seleccioanste es invalida. "
					+ "Para comenzar el juego si o si tienes que tirar " + "el 2 de Trebol. Por favor, intentalo denuevo.");
			pedirCarta();
		
		}
	}
	
	// ******************** PERDEDOR JUGADA ************************

	@Override
	public void perdedorJugada() throws RemoteException {
		// TODO Auto-generated method stub
		mostrarAviso("El perdedor de esta jugada es "  + this.controlador.jugadorPerdedorJugada(),tiemposMensajes[1]);
		
		actualizarJugadaPuntaje();
		
		limpiarCartasJugadas();
	}	
	
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

	// ******************* CORAZONES ROTOS ***********************
	
	@Override
	public void corazonesRotos() throws RemoteException {
		// TODO Auto-generated method stub
		actualizarCorazon(true);
		//actualizarEstadoJuego("CORAZONES ROTOS");
		//mostrarAviso("CORAZONES ROTOS",tiemposMensajes[0]);
		mostrarAviso("A partir de ahora se puede comenzar con corazones",tiemposMensajes[1]);
	}
	
	// *************************************************************
	// 						   PUNTAJE
	// *************************************************************

	private String puntaje() throws RemoteException {
	    StringBuilder sb = new StringBuilder();
	    sb.append("*          PUNTAJE         *\n");
	    sb.append("\n");
	    
	    // Espaciado para alinear columnas
	    String formato = "%-20s %-5s\n";  // Alineacion a la izquierda para numeros y jugadores

	    sb.append(String.format(formato, "Jugador", "Puntaje"));
	    sb.append("----------------------------\n");

	    int[] puntajes = this.controlador.puntajesJugadores();
	    for (int i = 0; i < puntajes.length; i++) {
	        sb.append(String.format(formato, this.controlador.getJugador(i), puntajes[i]));
	    }
	    
	    return sb.toString();
	}
	
	// *************************************************************
	//                       FIN DE RONDA
	// *************************************************************

	@Override
	public void finDeRonda() throws RemoteException {
		// TODO Auto-generated method stub
		actualizarEstadoJuego("FIN DE RONDA");
		
		//mostrarAviso("FIN DE LA RONDA", tiemposMensajes[0]);
		mostrarMensajeTemporal(puntaje(), tiemposMensajes[1]);
		actualizarRonda();
		actualizarCorazon(false);
	}
	
	// *************************************************************
	//                       FIN DE JUEGO
	// *************************************************************

	@Override
	public void finDeJuego() throws RemoteException {
		// TODO Auto-generated method stub
		actualizarEstadoJuego("FIN DEL JUEGO");
		mostrarMensajeTemporal("FIN DEL JUEGO",tiemposMensajes[0]);
		String jugadorGanador = this.controlador.ganadorJuego();
		actualizarEstadoJuego("GANADOR DEL JUEGO: " + jugadorGanador + " ¡¡¡FELICIDADES!!!");
		
		if (vInicioSesion.getGetNombreUsuario().equals(jugadorGanador)) {
			mostrarMensajeTemporal("¡FELICIDADES, SOS EL GANADOR!",tiemposMensajes[1]);
		} else {			
			mostrarMensajeTemporal("El ganador fue " + this.controlador.ganadorJuego(),tiemposMensajes[1]);
		}
		mostrarVista("menu");
	}
	
	// *************************************************************
	//						   SERIALIZACION
	// *************************************************************

	@Override
	public void serializar(String ganador) {
		
		if (vInicioSesion.getGetNombreUsuario().equals(ganador)) {
			if (serializador!=null) {
				
				Ganadores lista=(Ganadores) serializador.readFirstObject();
				lista.agregarGanador(ganador);
				serializador.writeOneObject(lista);
				//serializador=null;
				
			}
		}
		
	}

	@Override
	public void verRankingGandores() throws RemoteException {
	    
	    // Obtengo la lista de ganadores
	    Ganadores lista = (Ganadores) serializador.readFirstObject();
	    ArrayList<String> nombres = lista.getNombresGanadores();
	    ArrayList<Integer> cantidad = lista.getCantGanadas();

	    // Lista de objetos para ordenar
	    ArrayList<JugadorRanking> rankingList = new ArrayList<>();

	    for (int i = 0; i < nombres.size(); i++) {
	        rankingList.add(new JugadorRanking(nombres.get(i), cantidad.get(i)));
	    }

	    // Ordenar por partidas ganadas
	    rankingList.sort((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()));

	    // Solo los primeros 5
	    int top = Math.min(5, rankingList.size());
	    String[] columnNames = {"Nombre", "Partidas Ganadas"};
	    Object[][] data = new Object[top][2];

	    for (int i = 0; i < top; i++) {
	        data[i][0] = rankingList.get(i).getNombre();
	        data[i][1] = rankingList.get(i).getCantidad();
	    }

	    // Crear la tabla con modelo no editable
	    DefaultTableModel model = new DefaultTableModel(data, columnNames);

	    JTable rankingTable = new JTable(model);
	    rankingTable.setFillsViewportHeight(true);
	    rankingTable.setRowHeight(25); // Altura de filas

	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
	    rankingTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

	    // Tamaño de columnas
	    rankingTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Nombre
	    rankingTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Partidas ganadas

	    // Encabezado
	    JTableHeader header = rankingTable.getTableHeader();
	    header.setFont(new Font("Arial", Font.BOLD, 14));
	    header.setBackground(new Color(200, 200, 200));

	    JScrollPane scrollPane = new JScrollPane(rankingTable);
	    scrollPane.setPreferredSize(new Dimension(300, 150));

	    // Mostrar en JOptionPane
	    JOptionPane.showMessageDialog(null, scrollPane, "Ranking de Ganadores", JOptionPane.PLAIN_MESSAGE);
	}



}
