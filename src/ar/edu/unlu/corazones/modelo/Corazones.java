package ar.edu.unlu.corazones.modelo;

import java.util.ArrayList;
import java.util.List;
import ar.edu.unlu.corazones.observer.Observable;
import ar.edu.unlu.corazones.observer.Observador;

/**
 * CLASE CORAZONES
 * Encarga de manejar todo el flujo del juego, que abarca el pasaje de cartas, control de rondas, puntajes, etc.
 * 
 */

public class Corazones implements Observable{

	// *************************************************************
	// 						CONSTANTES
	// *************************************************************

	private static final int cantCartasRepartidas = 13; // TESTING (REAL 13)
	private static final int cantCartasIntercambio = 0; // TESTING (REAL 3)
	private static final int puntajeMaximo = 12; // TESTING (REAL 100)
	private static final int cantJugadores = 4;
	
	// *************************************************************
	// 						ATRIBUTOS
	// *************************************************************
	
	// Mazo del juego
	private Mazo mazo;

	// Array donde estaran los jugadores
	private Jugador[] jugadores;

	// Numero de ronda
	private int ronda;

	// Jugadas (En una ronda hay 13 jugadas)
	private List<Jugada> jugadas;

	// Turno del jugador actual
	private int turno = 0;

	// Direccion del pasaje de cartas
	private Direccion direccion;

	// Atributo para guardar la carta a tirar por parte del jugador
	private Carta cartaAJugar;

	// Posicion del jugador ganador
	private int posJugadorGanador;

	// Funcionalidad corazones rotos
	private boolean corazonesRotos;
	
	// Lista de observadores
	private List<Observador> observadores;
	
	// *************************************************************
	// 						CONSTRUCTOR
	// *************************************************************
	
	// Constructor de la clase corazones
	public Corazones() {
		// Creo la instancia de los jugadores y inicializo la ronda
		jugadores = new Jugador[cantJugadores];
		ronda = 1;
		// Cargo default
		agregarJugadores("Jugador A");
		agregarJugadores("Jugador B");
		agregarJugadores("Jugador C");
		agregarJugadores("Jugador D");

		this.observadores = new ArrayList<>();
		this.jugadas = new ArrayList<>();
	}
	
	// *************************************************************
	// 						COMPORTAMIENTO
	// ************************************************************
	
	
	// *************************************************************
	// 						ALTA Y MODIFICACION
	// *************************************************************
	
	// Metodo que agrega jugadores al juego,segun lo que devuelva indica si se cargo
	// de forma correcta o no el jugador
	// true = se cargo - false = no se cargo porque ya estan todos los jugadores
	public boolean agregarJugadores(String nombre) {
		boolean hayEspacio = false;
		int pos = 0;
		while (!hayEspacio && pos < jugadores.length) {
			if (jugadores[pos] == null) {
				jugadores[pos] = new Jugador(nombre, pos);
				hayEspacio = true;
			} else {
				pos++;
			}
		}
		return hayEspacio;
	}
	
	//Metodo que modifica un jugador en el juego
	//Solamente modifica si existe un jugador en la posicion que quiere
	//modificar el usuario, sino no lo hace
	public boolean reemplazarJugadores(String nombre,int posicion) {
		boolean seReemplazo = false;
		//Chequeo si no hay ningun jugador, o el referencial apunta a nulo
		if (!(jugadores[posicion - 1] == null)){
			//Cambia la bandera y modifico al jugador
			seReemplazo = true;
			jugadores[posicion - 1].setNombre(nombre);
		}
		return seReemplazo;
	}
	
	// *************************************************************
	//                      GETTERS
	// *************************************************************

	public Mazo getMazo() {
		return mazo;
	}

	public Jugador[] getJugadores() {
		return jugadores;
	}

	public int getRonda() {
		return ronda;
	}

	public List<Jugada> getJugadas() {
		return jugadas;
	}

	public int getTurno() {
		return turno;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public Carta getCartaAJugar() {
		return cartaAJugar;
	}

	public int getPosJugadorGanador() {
		return posJugadorGanador;
	}

	public boolean isCorazonesRotos() {
		return corazonesRotos;
	}
	
	public int getCantidadJugadores() {
		return this.cantJugadores;
	}
	
	public int getCantCartasIntercambio() {
		return this.cantCartasIntercambio;
	}
	
	// *************************************************************
	//                  GETTERS ESPECIALES
	// *************************************************************
	
	//Me dice quien es el jugador actual
	public Jugador getJugadorActual() {
		return jugadores[turno];
	}
	
	//Me dice las cartas que hay en mesa
	public Carta[] getCartasEnMesa(){
		return this.jugadas.get(jugadas.size() - 1).getCartasJugadas();
	}
	
	//Muestra el jugador perdedor de la jugada
	public Jugador getJugadorPerdedorJugada() {
		return this.jugadas.get(jugadas.size() - 1).getJugadorPerdedor();
	}
	
	//
	public int getNumeroJugada() {
		return this.jugadas.get(jugadas.size()-1).getNumeroJugada();
	}
	
	//Me dice si la cantidad de jugadores es valida para poder jugar
	public boolean isCantidadJugadoresValida() {
		return getCantidadJugadores() == getJugadores().length;
	}

	//Me muestro una array con los nombre de todos los jugadores
	public String[] getListaJugadores(){
		String[] jugadores =  new String[this.jugadores.length];
		for (int i = 0; i < this.jugadores.length; i++)
		{
			jugadores[i] = this.jugadores[i].getNombre();
			//System.out.println(this.jugadores[i].getNombre());
		}
		return jugadores;
	}

	
	//getPUntajes
	//getGanadorJuego
	//getCartasMano
	
	
	// *************************************************************
	//                      SETTERS
	// *************************************************************

	public void setMazo(Mazo mazo) {
		this.mazo = mazo;
	}

	public void setJugadores(Jugador[] jugadores) {
		this.jugadores = jugadores;
	}

	public void setRonda(int ronda) {
		this.ronda = ronda;
	}

	public void setJugadas(List<Jugada> jugadas) {
		this.jugadas = jugadas;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public void setCartaAJugar(Carta cartaAJugar) {
		this.cartaAJugar = cartaAJugar;
	}

	public void setPosJugadorGanador(int posJugadorGanador) {
		this.posJugadorGanador = posJugadorGanador;
	}

	public void setCorazonesRotos(boolean corazonesRotos) {
		this.corazonesRotos = corazonesRotos;
	}
	
	// *************************************************************
	//					 MVC Y OBSERVER
	// *************************************************************

	@Override // Notificar los eventos
	public void notificar(Object evento) {
		for (Observador observador : this.observadores) {
			observador.actualizar(evento, this);
		}
	}

	@Override // Agregar observadores
	public void agregarObservador(Observador observador) {
		this.observadores.add(observador);
	}

	
	
	
}
