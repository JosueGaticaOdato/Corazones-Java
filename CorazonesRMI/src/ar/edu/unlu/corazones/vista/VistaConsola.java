package ar.edu.unlu.corazones.vista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.modelo.Carta;

public class VistaConsola implements IVista {

	// *************************************************************
	//                       CONSTANTES
	// *************************************************************
	
	private final int lineas = 50; //para el salto de linea
	
	// *************************************************************
	//                       ATRIBUTOS
	// *************************************************************
	
	private Scanner entrada;
	
	private Controlador controlador;
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	//Creo la instancia para que el usuario pueda ingresar los datos
	public VistaConsola(Controlador controlador) {
		this.entrada = new Scanner(System.in);
		this.controlador = controlador;
		this.controlador.setVista(this);
		
		/* Control de desconexiones por parte del jugador */
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		    controlador.desconectarJugador();
		    System.out.println("Jugador desconectado correctamente.");
		}));
	}
	
	// *************************************************************
	//                COMPORTAMIENTO PROPIO DE LA CONSOLA
	// *************************************************************
	
	//Metodo para continuar y no sacar la pantalla de una
	private void continuar() {
		System.out.println("Escriba cualquier tecla para continuar...");
		this.entrada.next();
		limpiarPantalla();
	}
	
	//Limpieza de pantalla (en realidad agrega lineas)
	private void limpiarPantalla()
	{
	 for (int i=0; i < this.lineas; i++)
	 {
	  System.out.println();
	 }
	}
	
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
		boolean salir = false;
		while(!salir) {
			limpiarPantalla();
			mostrarMenu();
			int opcion = this.entrada.nextInt();
			limpiarPantalla();
			switch (opcion) {
				case 1: //Mostrar lista de jugadores 
					listaJugadores();
					break;
				case 2: //Comenzar juego
					jugar();
					break;
				case 0: //Salir del juego
					salir = true;
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
		System.out.println("1 - Ver lista de jugadores");
		System.out.println("2 - Comenzar juego");
		System.out.println("----------------------");
		System.out.println();
		System.out.println("0 - Salir");
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
			String nombre = entrada.next();
		
			if (nombre != null && !nombre.trim().isEmpty()) {
			
				this.controlador.conectarJugador(nombre);
				System.out.println("Jugador agregado con éxito.");
				jugadorConectado = true;
			
			} else {
			
				System.out.println("El nombre del jugador no puede estar vacio");
			}
		
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
			System.out.println("Juego comenzado!");
			continuar();
			controlador.iniciarJuego();
		} else {
			System.out.println("Faltan jugadores para comenzar el juego");
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
