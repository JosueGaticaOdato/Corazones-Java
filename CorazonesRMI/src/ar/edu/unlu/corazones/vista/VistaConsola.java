package ar.edu.unlu.corazones.vista;

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
	
	// *************************************************************
	//                       ATRIBUTOS
	// *************************************************************
	
	private Scanner entrada;
	
	private Controlador controlador;
	
	private String nombreJugador;
	
	private String corazonesRotos;
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	//Creo la instancia para que el usuario pueda ingresar los datos
	public VistaConsola(Controlador controlador) {
		this.entrada = new Scanner(System.in);
		
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
	
	// *************************************************************
	// 							MENSAJES
	// *************************************************************
	
	private void combinacionRondaJugada() throws RemoteException {
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
		conectarJugador();
		System.out.println("Esperando a que uno de los jugadores comienze....");
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
	//                    PASAJE DE CARTAS
	// *************************************************************
	
	// ****************** CARTAS REPARTIDAS ************************
	
	@Override
	public void cartasRepartidas() throws RemoteException {
		corazonesRotos = "";
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
			
			combinacionRondaPasaje();
			turnoJugador();
			manoJugador();

	        int posCarta = -1;
	        System.out.print("Elija una carta: ");
	        posCarta = this.entrada.nextInt();
	        
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
			//edirCartaPasaje();
		}
		
	}
	
	// ************** CARTA TIRADA VALIDA PASAJE *******************

	@Override
	public void cartaTiradaValidaPasaje() throws RemoteException {
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			Carta cartaAJugar = this.controlador.getCartaAJugar();	
			System.out.println("Pasaste la carta " + cartaAJugar.getCarta());
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
		System.out.println("Como es la primer jugada, el jugador " + this.controlador.nombreJugadorActual() +
				" debe iniciar el juego tirando el 2 de Trebol");

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
	        posCarta = this.entrada.nextInt();
	        
	        if (posCarta >= 1) {
	            controlador.cartaJugadaPasaje(posCarta - 1);
	        } else {
	            System.out.println("Número de carta inválido.");
	        }
	    } else {
	    	cartasEnMesa();
			System.out.println("Esperando al jugador " + this.controlador.nombreJugadorActual() + " ...");
	    }
	}
	
	// ****************** CARTA TIRADA VALIDA **********************
	
	@Override
	public void cartaTiradaValida() throws RemoteException {
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			System.out.println("¡Carta tirada valida!");
		}
	}

	// ****************** CARTA TIRADA INVALIDA ********************
	
	@Override
	public void cartaTiradaInvalida() throws RemoteException {
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			System.out.println("La carta que seleccionaste es invalida."
				+ "Tienes que tirar una carta del mismo palo que la que esta en la mesa."
				+ "Por favor, intentalo denuevo.");
			//pedirCarta();
		}
	}

	@Override
	public void cartaTiradaInvalida2deTrebol() throws RemoteException {
		String jugadorActual = this.controlador.nombreJugadorActual();
		
		if (nombreJugador.equals(jugadorActual)) {
			System.out.println("La carta que seleccionaste es invalida."
					+ "Tienes que tirar una carta del mismo palo que la que esta en la mesa."
					+ "Por favor, intentalo denuevo.");
			//pedirCarta();
		}
	}

	// ******************** PERDEDOR JUGADA ************************

	@Override
	public void perdedorJugada() throws RemoteException {
	
		combinacionRondaJugada();
		cartasEnMesa();
		
		System.out.println("El perdedor de esta jugada es " + this.controlador.jugadorPerdedorJugada() + "\n");
	}
	
	// ******************* CORAZONES ROTOS ***********************

	@Override
	public void corazonesRotos() throws RemoteException {
		corazonesRotos = "CORAZONES ROTOS";
		System.out.println("\n" + "CORAZONES ROTOS" + "\n" + "A partir se pueden comenzar con corazones" + "\n");
	}
	
	// *************************************************************
	//                       FIN DE RONDA
	// *************************************************************

	@Override
	public void finDeRonda() throws RemoteException {
		System.out.println("****************************\r\n"
				   + "* 	 FIN DE LA RONDA     *\r\n"
				   + "****************************");
		System.out.println("Asi estan los puntajes hasta el momento" + "\n");
		puntaje();
	}
	
	// *************************************************************
	//                       FIN DE JUEGO
	// *************************************************************

	@Override
	public void finDeJuego() throws RemoteException {
		System.out.println("****************************\r\n"
				   + "* 	  FIN DEL JUEGO      *\r\n"
				   + "****************************");
		puntaje();
		String jugadorGanador = this.controlador.ganadorJuego();
		if (nombreJugador.equals(jugadorGanador)) {
			System.out.println("¡FELICIDADES, SOS EL GANADOR!");
		} else {			
			System.out.println("El ganador fue " + this.controlador.ganadorJuego());
			System.out.println("¡¡¡FELICIDADES!!!");
		}
		
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