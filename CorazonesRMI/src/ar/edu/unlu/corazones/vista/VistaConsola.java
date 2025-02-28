package ar.edu.unlu.corazones.vista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.modelo.Carta;
import ar.edu.unlu.serializacion.Ganadores;
import ar.edu.unlu.serializacion.JugadorRanking;
import ar.edu.unlu.serializacion.Serializador;

public class VistaConsola implements IVista {

	// *************************************************************
	//                       CONSTANTES
	// *************************************************************
	
	private static Serializador serializador = new Serializador("src/datos.dat");
	
	private final int lineas = 50; //para el salto de linea

	// *************************************************************
	//                       ATRIBUTOS
	// *************************************************************
	
	private Scanner entrada;
	
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	private boolean menu;
	
	private Controlador controlador;
	
	private String nombreJugador;
	
	private String corazonesRotos;
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	//Creo la instancia para que el usuario pueda ingresar los datos
	public VistaConsola(Controlador controlador) {
		this.entrada = new Scanner(System.in);
		menu = true;
		
		this.controlador = controlador;
		this.controlador.setVista(this);
		
		//Control de desconexiones por parte del jugador
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		    controlador.desconectarJugador();
		    System.out.println("Jugador desconectado correctamente.");
		}));
	}
	
	// *************************************************************
	//                COMPORTAMIENTO PROPIO DE LA CONSOLA
	// *************************************************************
	
	private void continuar() {
	    System.out.println("Escriba cualquier tecla para continuar...");
	    try {
	        reader.readLine(); 
	    } catch (IOException e) {
	        e.printStackTrace(); 
	    }
		//limpiarPantalla();
	}
	
	//Limpieza de pantalla (en realidad agrega lineas)
	private void limpiarPantalla()
	{
	 /*for (int i=0; i < this.lineas; i++)
	 {
	  System.out.println();
	 }*/
	}
	
	// *************************************************************
	// 							MENSAJES
	// *************************************************************
	
	private void combinacionRondaJugada() throws RemoteException {
		puntaje();
		System.out.println("****************************");
		System.out.println("*         RONDA #" + this.controlador.numeroRonda() + "         *");
		System.out.println("*    	  JUGADA #" + this.controlador.numeroJugada() + "        *");
		System.out.println("****************************");
	}
	
	private void combinacionRondaPasaje() throws RemoteException {
		System.out.println("****************************");
	    System.out.println("*         RONDA #" + this.controlador.numeroRonda() + "         *");
		System.out.println("*     PASAJE DE CARTAS     *");
		System.out.println("****************************");
	}
	
	private void turnoJugador() throws RemoteException {
		System.out.println("****************************");
		System.out.println("*    JUGADOR #" + this.controlador.nombreJugadorActual() + "    *");
	}
	
	private void cartasEnMesa() throws RemoteException {
		System.out.println("*      CARTAS EN MESA      *");
		System.out.println("*      " + corazonesRotos + "      *");
		System.out.println();
		Carta[] cartasEnMesa = this.controlador.cartasEnMesa();
		for (int i = 0; i < cartasEnMesa.length ; i++) {
			
			Carta carta = cartasEnMesa[i];
			
			if (carta != null) {
				System.out.println((i+1) + ") " + this.controlador.getJugador(i) +
						": " + carta.mostrarCarta());
			} else {
				System.out.println((i+1) + ") " + this.controlador.getJugador(i) +
						": " + "sin jugar");
			}
			
		}
		System.out.println();
		System.out.println("****************************");
		System.out.println();
	}
	
	private void puntaje() throws RemoteException {
		System.out.println("*          PUNTAJE         *");
		System.out.println();
		int[] puntajes = this.controlador.puntajesJugadores();
		for (int i = 0; i < puntajes.length; i++) {
			System.out.println((i+1) + ") " + this.controlador.getJugador(i) + 
					" -> " + puntajes[i]);
		}
		System.out.println();
		System.out.println("****************************");
	}
		
	private void manoJugador() throws RemoteException {
		System.out.println();
		System.out.println("     	    MANO            ");
		System.out.println("----------------------------");
		String mano = "";
		ArrayList<Carta> manoJugador = this.controlador.manoJugador(this.controlador.posicionJugadorActual());
		for (int i = 0; i < manoJugador.size() ; i++) {
			Carta carta = manoJugador.get(i);
			mano = (i+1) + ") " + carta.mostrarCarta();
			System.out.println(mano);
		}
		System.out.println("----------------------------");
		System.out.println();
		System.out.println();
	}
	
	// *************************************************************
	//                         PRE-JUEGO
	// *************************************************************
	
	@Override
	public void iniciar() throws RemoteException {
		
		boolean salir = conectarJugador();
		String opcion = "";
		while(salir && menu) {
			//limpiarPantalla();
			mostrarMenu();
			
	        try {
	            opcion = reader.readLine();  // Lee la línea completa
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (NumberFormatException e) {
	            System.out.println("Opción no válida.");
	        }
	        
			limpiarPantalla();
			switch (opcion) {
				case "L": //Mostrar lista de jugadores 
					listaJugadores();
					break;
				case "R": //Rankings
					verRankingGandores();
					break;
				case "C": //Comenzar juego
					jugar();
					break;
				case "S": //Salir del juego
					salir = false;
					desconectarJugador();
					break;
				default: //Opcion por default
					System.out.println("Opcion no valida.");
			}
			continuar();
		}
	}
	
	//Menu principal del program
	private void mostrarMenu() {
		System.out.println("****************************");
		System.out.println("*    	 CORAZONES         *");
		System.out.println("****************************");
		System.out.println();
		System.out.println("Seleccione una opcion:");
		System.out.println("----------------------");
		System.out.println("L - Ver lista de jugadores");
		System.out.println("R - Ver Rankings de ganadores");
		System.out.println("C - Comenzar juego");
		System.out.println("----------------------");
		System.out.println();
		System.out.println("S - Salir");
		System.out.print("Opcion: ");
	}
	
	// ******************* LISTA DE JUGADORES  *********************
	
	private void listaJugadores() throws RemoteException {
		System.out.println("\n" + "Lista de jugadores:");
		String[] jugadores = controlador.listaJugadores();
		String s = "\n";
		for (int i = 0; i < controlador.cantidadJugadores(); i++) {
			s += (i+1) + ") Jugador: "; 
			if (jugadores[i] == null) {
				s += "(Sin agregar)";
			} else if (nombreJugador.equals(jugadores[i])){
				s += jugadores[i] + " <-"; 
			} else {
				s += jugadores[i];
			}
			s += "\n";
		}
		System.out.println(s);
	}
	
	// ****************** JUGADOR DESCONECTADO *********************
	
	@Override
	public void desconectarJugador() throws RemoteException{
		controlador.desconectarJugador();
		System.out.println("Jugador desconectado correctamente.");
	}
	
	// ******************** CONECTAR JUGADOR ***********************
	
	private boolean conectarJugador() throws RemoteException{
		boolean jugadorConectado = false;
		if (!this.controlador.isCantidadJugadoresValida()) {
			
			System.out.println("\n" + "---------- NUEVO JUGADOR! -------------" + "\n");
			System.out.print("Ingrese el nombre del nuevo jugador: ");
			nombreJugador = entrada.next();
			
			this.controlador.conectarJugador(nombreJugador);
			System.out.println("Jugador agregado con éxito.");
			jugadorConectado = true;
			
		} else {
			System.out.println("\n" + "Ya estan todos los jugadores inscriptos" + "\n");
		}
		return jugadorConectado;
	}
	
	// *************************************************************
	//                         JUGAR
	// *************************************************************
	
	public void jugar() throws RemoteException {
		if ( this.controlador.isCantidadJugadoresValida() ) {
			//System.out.println("Juego comenzado!");
			menu = false;
			/*continuar();
			controlador.iniciarJuego();*/
			//Lo ejecuto en un hilo separado para no bloquear la vista
			new Thread(() -> {
	            try {
	                controlador.iniciarJuego();
	            } catch (RemoteException e) {
	                e.printStackTrace();
	            }
	        }).start();
		} else {
			System.out.println("Faltan jugadores para comenzar el juego");
		}
	}
	
	// *************************************************************
	//                    PASAJE DE CARTAS
	// *************************************************************

	// ****************** CARTAS REPARTIDAS ************************
	
	@Override
	public void cartasRepartidas() throws RemoteException {
		//Caso donde otro jugador decidio comenzar, tnego que cortar el bluce de iniciar()
		
		corazonesRotos = "";
		menu = false;
		System.out.println("Repartiendo cartas!");
	}
	
	// ************ PANTALLA PARA PASAJE X JUGADOR *****************
	
	@Override
	public void pasajeDeCartas() throws RemoteException {
		int cantCartas = this.controlador.cantidadCartasPasaje();
		String direccion = direccionPasaje();
		combinacionRondaPasaje();
		System.out.println("Cada jugador debe pasar " + String.valueOf(cantCartas) + " de sus cartas. " + direccion);
	}
	
	public String direccionPasaje() throws RemoteException {
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
	public void pedirCartaPasaje() throws RemoteException {
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			
			continuar();
			combinacionRondaPasaje();
			turnoJugador();
			manoJugador();

	        int posCarta = -1;
	        System.out.print("Elija una carta: ");
	        
	        try {
	            String input = reader.readLine();  
	            posCarta = Integer.parseInt(input); 
	        } catch (IOException e) {
	            e.printStackTrace(); 
	        } catch (NumberFormatException e) {
	            posCarta = -1; 
	            System.out.println("Entrada no válida. Intenta de nuevo.");
	        }
	        
	        System.out.println("posCarta: " + posCarta);
	        if (posCarta >= 1) {
	            controlador.cartaJugadaPasaje(posCarta - 1);
	        } else {
	            System.out.println("Número de carta inválido.");
	        }
			
		} else {
			System.out.println("Esperando al jugador " + this.controlador.nombreJugadorActual() + " ...");
		}
	}
	
	// ************ CARTA TIRADA INVALIDA PASAJE *******************

	@Override
	public void cartaTiradaInvalidaPasaje() throws RemoteException {
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			System.out.println("La carta que seleccioanste es invalida."
					+ " Por favor, intentalo denuevo.");
			continuar();
			pedirCartaPasaje();
		}
		
	}
	
	// ************** CARTA TIRADA VALIDA PASAJE *******************

	@Override
	public void cartaTiradaValidaPasaje() throws RemoteException {
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			Carta cartaAJugar = this.controlador.getCartaAJugar();	
			System.out.println("Jugaste la carta " + cartaAJugar.getCarta());
		}
	}
	
	// ****************** FIN PASAJE DE CARTAS *********************

	@Override
	public void finPasajeDeCartas() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("****************************\r\n"
				+ "* FIN DEL PASAJE DE CARTAS *\r\n"
				+ "*    COMIENZA LA RONDA     *\r\n"
				+ "****************************");
	}
	
	// *************************************************************
	//                         JUEGO
	// *************************************************************
	
	@Override
	public void nuevaJugada() throws RemoteException {
		System.out.println("¡Comienza una nueva jugada!");
	}
	
	// ******************** JUGAR DOS DE TREBOL ********************

	@Override
	public void jugarDosDeTrebol() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Como es la primer jugada, el jugador " + this.controlador.nombreJugadorActual() +
				" debe iniciar el juego tirando el 2 de Trebol");
		continuar();
		pedirCarta();
	}
	
	// ************ PEDIR CARTAS (para tirar en mesa) **************
	
	@Override
	public void pedirCarta() throws RemoteException {
		
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			combinacionRondaJugada();
			turnoJugador();
			cartasEnMesa();
			
			manoJugador();

	        int posCarta = -1;
	        System.out.print("Elija una carta: ");
	        
	        try {
	            String input = reader.readLine(); 
	            posCarta = Integer.parseInt(input);
	        } catch (IOException e) {
	            e.printStackTrace();  
	        } catch (NumberFormatException e) {
	            posCarta = -1; 
	            System.out.println("Entrada no válida. Intenta de nuevo.");
	        }
	        
	        System.out.println("posCarta: " + posCarta);
	        if (posCarta >= 1) {
	        	System.out.println("Posicion: " + posCarta);
	            controlador.cartaJugada(posCarta - 1); //Paso la carta
	        } else {
	            System.out.println("Número de carta inválido.");
	        }
			
		} else {
			esperaJugadorActual();
		}
	}
	
	private void esperaJugadorActual() throws RemoteException{
		continuar();
		cartasEnMesa();
		System.out.println("Esperando al jugador " + this.controlador.nombreJugadorActual() + " ...");
	}
	
	// ****************** CARTA TIRADA VALIDA **********************
	
	@Override
	public void cartaTiradaValida() throws RemoteException {
		// TODO Auto-generated method stub
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			System.out.println("¡Carta tirada valida!");
		}
	}

	// ****************** CARTA TIRADA INVALIDA ********************
	
	@Override
	public void cartaTiradaInvalida() throws RemoteException {
		// TODO Auto-generated method stub
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			System.out.println("La carta que seleccionaste es invalida."
				+ "Tienes que tirar una carta del mismo palo que la que esta en la mesa."
				+ "Por favor, intentalo denuevo.");
			continuar();
			pedirCarta();
		}
	}

	@Override
	public void cartaTiradaInvalida2deTrebol() throws RemoteException {
		// TODO Auto-generated method stub
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			System.out.println("La carta que seleccionaste es invalida."
					+ "Tienes que tirar una carta del mismo palo que la que esta en la mesa."
					+ "Por favor, intentalo denuevo.");
			continuar();
			pedirCarta();
		}
	}
	
	// ******************** PERDEDOR JUGADA ************************

	@Override
	public void perdedorJugada() throws RemoteException {
		// TODO Auto-generated method stub
	
		combinacionRondaJugada();
		cartasEnMesa();
		
		System.out.println("El perdedor de esta jugada es " + this.controlador.jugadorPerdedorJugada() + "\n");
	
		continuar();
	}

	@Override
	public void corazonesRotos() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("\n" + "CORAZONES ROTOS" + "\n" + "A partir se pueden comenzar con corazones" + "\n");
		continuar();
	}



	@Override
	public void finDeRonda() throws RemoteException {
		System.out.println("****************************\r\n"
				   + "* 	 FIN DE LA RONDA     *\r\n"
				   + "****************************");
		System.out.println("Asi estan los puntajes hasta el momento" + "\n");
		puntaje();
		continuar();
	}

	@Override
	public void finDeJuego() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("****************************\r\n"
				   + "* 	  FIN DEL JUEGO      *\r\n"
				   + "****************************");
		puntaje();
		System.out.println("El ganador fue " + this.controlador.ganadorJuego());
		System.out.println("¡¡¡FELICIDADES!!!");
		continuar();
	}
	
	// *************************************************************
	//						   SERIALIZACION
	// *************************************************************
	
	@Override
	public void serializar(String ganador) {
		
		if (nombreJugador.equals(ganador)) {
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

	    // Solo los primeros 5 jugadores
	    int top = Math.min(5, rankingList.size());

	    System.out.println("*****************************************");
	    System.out.println("*           Ranking de ganadores        *");
	    System.out.println("*****************************************");
	    System.out.println("*      Nombre         | Partidas Ganadas*");
	    System.out.println("*****************************************");

	    for (int i = 0; i < top; i++) {
	        String nombre = String.format("%-20s", rankingList.get(i).getNombre());  // Alinear a la izquierda
	        String partidas = String.format("%-16d", rankingList.get(i).getCantidad()); // Alinear a la derecha
	        System.out.println("* " + nombre + "| " + partidas + "*");
	    }

	    System.out.println("*****************************************");
	}
	
}
