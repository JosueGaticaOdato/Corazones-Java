package ar.edu.unlu.corazones.modelo;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unlu.corazones.observer.Observable;
import ar.edu.unlu.corazones.observer.Observador;

public class Corazones implements Observable{

	// *************************************************************
	// 						CONSTANTES
	// *************************************************************

	private static final int cantCartasRepartidas = 7; // TESTING (13)
	private static final int cantCartasIntercambio = 2; // TESTING (3)
	private static final int puntajeMaximo = 1; // TESTING (100)
	private static final int cantJugadores = 4;
	
	// *************************************************************
	// 						ATRIBUTOS
	// *************************************************************
	
	private Mazo mazo;
	
	private Jugador[] jugadores;
	
	private int ronda;
	
	private List<Jugada> jugadas;
	
	private int turno = 0;
	
	private Direccion direccion;
	
	private Carta cartaAJugar;
	
	private Jugador jugadorGanador;
	
	private boolean corazonesRotos;
	
	private List<Observador> observadores;
	
	// *************************************************************
	// 						CONSTRUCTOR
	// *************************************************************
	
	public Corazones() {
		jugadores = new Jugador[cantJugadores];
		ronda = 1;
		
		// Jugadores por defecto
		//agregarJugadores("Jugador A");
		//agregarJugadores("Jugador B");
		//agregarJugadores("Jugador C");
		//agregarJugadores("Jugador D");

		this.observadores = new ArrayList<>();
		this.jugadas = new ArrayList<>();
	}
	
	// *************************************************************
	// 						COMPORTAMIENTO
	// ************************************************************
	
	public void iniciarJuego() {
		boolean juegoTerminado = false;
		resetPuntajes();
		
		while (!juegoTerminado) {
			mazo = new Mazo();
			repartirCartas();
			notificar(EventosCorazones.CARTAS_REPARTIDAS);
			juegoTerminado = true;
			pasajeDeCartas();
			this.corazonesRotos = false;
			
			for (int j = 0; j < cantCartasRepartidas; j++) {
				
				int i = 0;
				Jugada jugada = new Jugada(this.jugadores);
				jugadas.add(jugada);
				
				/*CASO 2 DE TREBOL*/
				if (j == 0) {
					primerCarta2Trebol(jugada);
					i++;
				}
				
				/*1 JUGADA POR CADA JUGADOR*/
				while (i < cantJugadores) {
					notificar(EventosCorazones.PEDIR_CARTA);
					jugarCarta(jugada);
					i++;
				}
				
				turno = jugada.determinarPerdedor();
				notificar(EventosCorazones.PERDEDOR_JUGADA);
				
			}

			// Finalizada la ronda, se comprueba si se llego al puntaje maxixo para finalizar el juego
			if (puntajeMaximoActual() >= puntajeMaximo) {
				juegoTerminado = true;
			}
			notificar(EventosCorazones.FIN_DE_RONDA);
			Jugada.reiniciarContadorJugadas();
			ronda++;
		}
		
		determinarGanador();
		notificar(EventosCorazones.FIN_DE_JUEGO);
	}
	
	// *************************************************************
	// 					FUNCIONALIDAD RONDA
	// *************************************************************
	
	private void resetPuntajes() {
		for (Jugador jugadoresCorazones : jugadores ) {
			jugadoresCorazones.setPuntaje(0);
		}
	}
	
	private void repartirCartas() {
		for (int i = 0; i < cantCartasRepartidas; i++) {
			for (Jugador jugador : jugadores) {
				jugador.recibirCarta(mazo.sacarCarta());
			}
		}
	}
	
	private int puntajeMaximoActual() {
		int max = 0;
		for (Jugador jugadoresCorazones : jugadores) {
			if (max <= jugadoresCorazones.getPuntaje()) {
				max = jugadoresCorazones.getPuntaje();
			}
		}
		return max;
	}
	
	private void determinarGanador() {
		int minPuntaje = 100;
		for (int i = 0; i < cantJugadores; i++) {
			if (minPuntaje >= jugadores[i].getPuntaje()) {
				minPuntaje = jugadores[i].getPuntaje();
				this.jugadorGanador = jugadores[i];
			}
		}
	}
	
	// *************************************************************
	// 					FUNCIONALIDAD JUGADAS
	// *************************************************************
	
	//Para comenzar la ronda es necesario que el jugador que tiene el dos de trebol comience
	private void primerCarta2Trebol(Jugada jugada)  {
		boolean tengoDosDeTrebol = false;
		int pos = 0;
		while (!tengoDosDeTrebol && pos < cantJugadores) {
			
			//Obtengo al jugador que tiene el 2 de trebol
			tengoDosDeTrebol = jugadores[pos].tengoDosDeTrebol();
			if (tengoDosDeTrebol) {
				turno = pos;
				notificar(EventosCorazones.JUGAR_2_DE_TREBOL);
				boolean dosDeTrebolTirado = false;
				
				//Hasta que no tire el dos de trebol no arranca el juego!
				while ( !dosDeTrebolTirado ) {
					
					if (jugada.tirarDosDeTrebol(cartaAJugar, turno) && (cartaAJugar != null)) {
						
						jugadores[turno].tirarCarta(jugadores[turno].buscarCarta(cartaAJugar));
						notificar(EventosCorazones.CARTA_TIRADA_VALIDA);
						turno = (turno + 1) % cantJugadores;
						dosDeTrebolTirado = true;
						
					} else {
						
						notificar(EventosCorazones.CARTA_TIRADA_INVALIDA_2_DE_TREBOL);
					}
					
				}
				
			} else {
				pos++;
			}
		}
	}
	
	private void jugarCarta(Jugada jugada)  {
		
		boolean cartaTiradaValida = false;
		while ( !cartaTiradaValida ) {
			
			if (jugada.tirarCartaEnMesa(turno, cartaAJugar, this.corazonesRotos) && (cartaAJugar != null)) {
				jugadores[turno].tirarCarta(jugadores[turno].buscarCarta(cartaAJugar));
				tiroCorazones();
				notificar(EventosCorazones.CARTA_TIRADA_VALIDA);
				turno = (turno + 1) % cantJugadores;
				cartaTiradaValida = true;
				
			} else {
				
				notificar(EventosCorazones.CARTA_TIRADA_INVALIDA);
				
			}
		}
		
	}
	
	// Metodo para indicar que un jugador tiro la carta de corazones
	private void tiroCorazones()  {
		if (cartaAJugar.getPalo() == Palo.CORAZONES && !this.corazonesRotos) {
			notificar(EventosCorazones.CORAZONES_ROTOS);
			this.corazonesRotos = true;
		}
	}
	
	// *************************************************************
	// 					    PASAJE DE CARTAS
	// *************************************************************
	
	private void pasajeDeCartas()  {
		int variablePasaje = 0;
		
		// *************************************************************
		// CASOS
		// 1. Pasaje de 1: Se realiza el pasaje a la izquierda -> variablePasaje = 1
		// 2. Pasaje de 2: Se realiza el pasaje al frente -> variablePasaje = 0
		// 3. Pasaje de 3: Se realiza el pasaje a la derecha -> variablePasaje = -1
		// 4. Pasaje de 0: No hay pasaje
		// **************************************************************
		
		// Determino a donde se van a pasar las cartas
		switch (this.ronda) {
		case 1: // Izq
			variablePasaje = 1;
			direccion = Direccion.IZQUIERDA;
			break;
		case 2: // Frente
			variablePasaje = 2;
			direccion = Direccion.FRENTE;
			break;
		case 3: // Der
			variablePasaje = 3;
			direccion = Direccion.DERECHA;
			break;
		// Caso 4: No hay intercambio
		default:
			break;
		}
		
		notificar(EventosCorazones.PASAJE_DE_CARTAS);
		if (variablePasaje != 0) {
			intercambioDeCartas(variablePasaje);
			notificar(EventosCorazones.FIN_PASAJE_DE_CARTAS);
		}
	}
	
	private void intercambioDeCartas(int valor)  {
		// Esto funciona para que el intercambio se haga sobre el final y los otros
		// jugadores no tengan acceso a las cartas nuevas recibidas
		ArrayList<Carta[]> arregloDeCartasAIntercambiar = new ArrayList<Carta[]>(cantJugadores);

		// Inicializar cada posición del ArrayList con un arreglo de Carta vacío
		for (int i = 0; i < cantJugadores; i++) {
			arregloDeCartasAIntercambiar.add(new Carta[0]);
		}

		for (Jugador jugadorPasaje : jugadores) {

			// Obtengo la posicion del jugaodr actual y a quien le va a pasar las cartas
			int posicionJugadorActual = buscarJugador(jugadorPasaje);
			
			// Para saber a quien le paso las cartas, tengo que sumar la variable de pasaje
			// a la posicion del jugador actual, y a eso dividirlo por la cantidad de
			// jugadores
			int posicionJugadorPasaje = (posicionJugadorActual + valor + jugadores.length) % jugadores.length;

			// Creo la lista de las cartas que se van a pasar al otro jugador
			Carta[] cartasIntercambio = new Carta[cantCartasIntercambio];

			for (int i = 0; i < cantCartasIntercambio; i++) {
				// Obntego la carta que jugo el jugador
				notificar(EventosCorazones.PEDIR_CARTA_PASAJE);
				cartaTiradaValidaPasaje(cartasIntercambio);
				cartasIntercambio[i] = this.cartaAJugar;
			}

			// Guardo en el arreglo (POR POSICION DE JUGADOR) las cartas nuevas obtenidas y saco las cartas de la mano
			arregloDeCartasAIntercambiar.set(posicionJugadorPasaje, cartasIntercambio);
			for (int i = 0; i < cantCartasIntercambio; i++) {
				// Obntego la carta que jugo el jugador
				jugadores[turno].tirarCarta(jugadores[turno].buscarCarta(cartasIntercambio[i]));
			}

			turno = (turno + 1) % jugadores.length; // Obtengo el siguiente jugador
		}

		otorgarCartasJugadores(arregloDeCartasAIntercambiar);
	}
	
	private void cartaTiradaValidaPasaje(Carta[] cartasIntercambio)  {
		
		boolean cartaTiradaValida = false;
		while ( !cartaTiradaValida ) {
			
			if ((cartaAJugar != null) && (!buscarCarta(cartasIntercambio, cartaAJugar))) {
				//jugadores[turno].tirarCarta(jugadores[turno].buscarCarta(cartaAJugar));
				cartaTiradaValida = true;
				notificar(EventosCorazones.CARTA_TIRADA_VALIDA_PASAJE);
			} else {
				notificar(EventosCorazones.CARTA_TIRADA_INVALIDA_PASAJE);
			}
		}
		
	}
	
	private boolean buscarCarta(Carta[] cartas, Carta cartaBuscada) {
		boolean encontrada = false;
		int i = 0;
		while ((!encontrada) && (i < cartas.length)) {
			
			if ( cartaBuscada == cartas[i]) {
				encontrada = true;
			} else {
				i++;
			}
		}
		return encontrada;
		
	}
	
	
	// Metodo que busca la posicion de un jugador determinado
	private int buscarJugador(Jugador jugador) {
		int posicionJugadorBuscar = 0;
		boolean encontrado = false;
		while (!encontrado && posicionJugadorBuscar < jugadores.length) {
			if (jugadores[posicionJugadorBuscar] == jugador) {
				encontrado = true;
			} else {
				posicionJugadorBuscar++;
			}
		}
		return posicionJugadorBuscar;
	}
	
	// Otorgo las cartas que se pasaron a cada jugador
	private void otorgarCartasJugadores(ArrayList<Carta[]> cartasPasaje) {
		for (int i = 0; i < cantJugadores; i++) {
			for (int j = 0; j < cantCartasIntercambio; j++) {
				jugadores[i].recibirCarta(cartasPasaje.get(i)[j]);
			}
		}
	}
	
	// Metodo para jugar la carta cuando se realize el pasaje

	public void jugarCartaPasaje(int i) {
		cartaAJugar = jugadores[turno].tirarCarta(i);
	}
	
	// *************************************************************
	// 						ALTA Y MODIFICACION
	// *************************************************************
	
	public boolean agregarJugadores(String nombre)  {
		boolean hayEspacio = false;
		int pos = 0;
		while (!hayEspacio && pos < jugadores.length) {
			if (jugadores[pos] == null) {
				jugadores[pos] = new Jugador(nombre);
				hayEspacio = true; //Solamente se dan altas si hay lugares disponibles
			} else {
				pos++;
			}
		}
		return hayEspacio;
	}
	
	public boolean reemplazarJugadores(String nombre,int posicion)  {
		boolean seReemplazo = false;
		if (posicion >= 0 && posicion <= cantJugadores) {
			if (!(jugadores[posicion - 1] == null)){
				seReemplazo = true;
				jugadores[posicion - 1].setNombre(nombre);
			}
		}
		return seReemplazo;
	}
	
	// *************************************************************
	//                      GETTERS
	// *************************************************************
	
	public Mazo getMazo()  {
		return mazo;
	}

	
	public Jugador[] getJugadores() {
		return jugadores;
	}

	
	public int getRonda()  {
		return ronda;
	}

	
	public List<Jugada> getJugadas()  {
		return jugadas;
	}

	
	public String getDireccion()  {
		return String.valueOf(direccion);
	}

	
	public Carta getCartaAJugar()  {
		return cartaAJugar;
	}

	
	public boolean isCorazonesRotos()  {
		return corazonesRotos;
	}
	
	
	public int getCantidadJugadores()  {
		return cantJugadores;
	}
	
	
	public int getCantCartasIntercambio()  {
		return cantCartasIntercambio;
	}
	
	// *************************************************************
	//                  GETTERS ESPECIALES
	// *************************************************************
	
	
	public String getJugador(int i)  {
		return jugadores[i].getNombre();
	}
	
	public String getNombreJugadorActual()  {
		return jugadores[turno].getNombre();
	}
	
	
	public int getPosicionJugadorActual()  {
		return turno;
	}
	
	
	public Carta[] getCartasEnMesa() {
		return this.jugadas.get(jugadas.size() - 1).getCartasJugadas();
	}
	
	
	public String getJugadorPerdedorJugada()  {
		return this.jugadas.get(jugadas.size() - 1).getJugadorPerdedor().getNombre();
	}
	
	
	public int getNumeroJugada()  {
		return this.jugadas.get(jugadas.size()-1).getNumeroJugada();
	}
	
	
	public boolean isCantidadJugadoresValida()  {
	    int jugadoresRegistrados = 0;
	    for (Object jugador : getJugadores()) {
	        if (jugador != null) {
	            jugadoresRegistrados++;
	        }
	    }
	    return jugadoresRegistrados == getCantidadJugadores();
	}
	
	
	//Me muestro una array con los nombre de todos los jugadores
	
	public String[] getListaJugadores() {
		
		String[] jugadores = new String[cantJugadores];
		for (int i = 0; i < cantJugadores; i++) {
			if (this.jugadores[i] != null) {				
				jugadores[i] = this.jugadores[i].getNombre();
			} else {
				jugadores[i] = null;
			}
		}
		return jugadores;
	}
	
	
	public ArrayList<Carta> getManoJugador(int pos)  {
		return this.jugadores[pos].getMano();
	}
	
	
	public int[] puntajesJugadores()  {
		int[] puntajes = new int[cantJugadores];
		for (int i = 0; i < cantJugadores; i++) {
			puntajes[i] = this.jugadores[i].getPuntaje();
		}
		return puntajes;
	}
	
	
	public String getNombreGanadorJuego() {
		return this.jugadorGanador.getNombre();
	}
	
	// *************************************************************
	//                      SETTERS
	// *************************************************************
	
	
	public void setCartaAJugar(int pos)  {
		cartaAJugar = jugadores[turno].obtenerCartaJugador(pos);
	}
	
	// *************************************************************
	//					 MVC Y OBSERVER
	// *************************************************************

	@Override
	public void notificar(Object evento) {
		for (Observador observador : this.observadores) {
			observador.actualizar(evento, this);
		}
	}

	@Override
	public void agregarObservador(Observador observador) {
		this.observadores.add(observador);
	}
}
