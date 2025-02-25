package ar.edu.unlu.corazones.modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ar.edu.unlu.corazones.observer.Observador;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class Corazones extends ObservableRemoto implements ICorazones {

	// *************************************************************
	// 						CONSTANTES
	// *************************************************************
	
	private static final int cantCartasRepartidas = 13; // TESTING (13)
	private static final int cantCartasIntercambio = 2; // TESTING (3)
	private static final int puntajeMaximo = 1; // TESTING (100)
	private static final int cantJugadores = 4;
	
	// *************************************************************
	// 						ATRIBUTOS
	// *************************************************************
	
	private Mazo mazo;
	
	private HashMap<Integer, Jugador> jugadores;
	
	private int ronda;
	
	private List<Jugada> jugadas;
	
	private int turno = 0;
	
	private Direccion direccion;
	
	private Carta cartaAJugar;
	
	private IJugador jugadorGanador;
	
	private boolean corazonesRotos;
	
	// *************************************************************
	// 						CONSTRUCTOR
	// *************************************************************
	
	public Corazones() {
		this.jugadores = new HashMap<>();
		this.jugadas = new ArrayList<>();
		ronda = 1;
	}
	
	// *************************************************************
	// 				     AGREGAR/ELIMINAR JUGADORES
	// ************************************************************
	
	public IJugador conectarJugador(String nombre) throws RemoteException {
		Jugador j = new Jugador(nombre);
		this.jugadores.put(j.getId(), j);
		return j;
	}
	
	public void desconectarJugador(int jugadorId) throws RemoteException {
		this.jugadores.remove(jugadorId);
	}
	
	// *************************************************************
	// 						COMPORTAMIENTO
	// ************************************************************
	
	@Override
	public void iniciarJuego() throws RemoteException {
		boolean juegoTerminado = false;
		resetPuntajes();
		
		while (!juegoTerminado) {
			mazo = new Mazo();
			repartirCartas();
			notificarObservadores(EventosCorazones.CARTAS_REPARTIDAS);
			juegoTerminado = true;
			//pasajeDeCartas();
			this.corazonesRotos = false;
			
			for (int j = 0; j < cantCartasRepartidas; j++) {
				
				int i = 0;
				Jugada jugada = new Jugada(getJugadores());
				jugadas.add(jugada);
				notificarObservadores(EventosCorazones.NUEVA_JUGADA);
				
				/*CASO 2 DE TREBOL*/
				if (j == 0) {
					primerCarta2Trebol(jugada);
					i++;
				}
				
				/*1 JUGADA POR CADA JUGADOR*/
				while (i < cantJugadores) {
					notificarObservadores(EventosCorazones.PEDIR_CARTA);
					jugarCarta(jugada);
					i++;
				}
				
				turno = jugada.determinarPerdedor();
				notificarObservadores(EventosCorazones.PERDEDOR_JUGADA);
				
			}

			// Finalizada la ronda, se comprueba si se llego al puntaje maxixo para finalizar el juego
			if (puntajeMaximoActual() >= puntajeMaximo) {
				juegoTerminado = true;
			}
			notificarObservadores(EventosCorazones.FIN_DE_RONDA);
			Jugada.reiniciarContadorJugadas();
			ronda++;
		}
		
		determinarGanador();
		notificarObservadores(EventosCorazones.FIN_DE_JUEGO);
	}
	
	// *************************************************************
	// 					FUNCIONALIDAD RONDA
	// *************************************************************
	
	private void resetPuntajes() throws RemoteException {
		for (IJugador jugadoresCorazones : getJugadores() ) {
			jugadoresCorazones.setPuntaje(0);
		}
	}
	
	private void repartirCartas() throws RemoteException {
		for (int i = 0; i < cantCartasRepartidas; i++) {
			for (IJugador jugador : getJugadores()) {
				System.out.println(jugador);
				jugador.recibirCarta(mazo.sacarCarta());
			}
		}
	}
	
	private int puntajeMaximoActual() throws RemoteException {
		int max = 0;
		for (IJugador jugadoresCorazones : getJugadores()) {
			if (max <= jugadoresCorazones.getPuntaje()) {
				max = jugadoresCorazones.getPuntaje();
			}
		}
		return max;
	}
	
	private void determinarGanador() throws RemoteException {
		int minPuntaje = 100;
		for (int i = 0; i < cantJugadores; i++) {
			if (minPuntaje >= getJugadores()[i].getPuntaje()) {
				minPuntaje = getJugadores()[i].getPuntaje();
				this.jugadorGanador = getJugadores()[i];
			}
		}
	}
	
	// *************************************************************
	// 					FUNCIONALIDAD JUGADAS
	// *************************************************************
	
	//Para comenzar la ronda es necesario que el jugador que tiene el dos de trebol comience
	private void primerCarta2Trebol(Jugada jugada) throws RemoteException  {
		boolean tengoDosDeTrebol = false;
		int pos = 0;
		while (!tengoDosDeTrebol && pos < cantJugadores) {
			
			//Obtengo al jugador que tiene el 2 de trebol
			tengoDosDeTrebol = getJugadores()[pos].tengoDosDeTrebol();
			if (tengoDosDeTrebol) {
				turno = pos;
				notificarObservadores(EventosCorazones.JUGAR_2_DE_TREBOL);
				boolean dosDeTrebolTirado = false;
				
				//Hasta que no tire el dos de trebol no arranca el juego!
				while ( !dosDeTrebolTirado ) {
					
					if (jugada.tirarDosDeTrebol(cartaAJugar, turno) && (cartaAJugar != null)) {
						
						getJugadores()[turno].tirarCarta(getJugadores()[turno].buscarCarta(cartaAJugar));
						notificarObservadores(EventosCorazones.CARTA_TIRADA_VALIDA);
						turno = (turno + 1) % cantJugadores;
						dosDeTrebolTirado = true;
						
					} else {
						
						notificarObservadores(EventosCorazones.CARTA_TIRADA_INVALIDA_2_DE_TREBOL);
					}
					
				}
				
			} else {
				pos++;
			}
		}
	}
	
	private void jugarCarta(Jugada jugada) throws RemoteException  {
		
		boolean cartaTiradaValida = false;
		while ( !cartaTiradaValida ) {
			
			if (jugada.tirarCartaEnMesa(turno, cartaAJugar, this.corazonesRotos) && (cartaAJugar != null)) {
				getJugadores()[turno].tirarCarta(getJugadores()[turno].buscarCarta(cartaAJugar));
				tiroCorazones();
				notificarObservadores(EventosCorazones.CARTA_TIRADA_VALIDA);
				turno = (turno + 1) % cantJugadores;
				cartaTiradaValida = true;
				
			} else {
				
				notificarObservadores(EventosCorazones.CARTA_TIRADA_INVALIDA);
				
			}
		}
		
	}
	
	// Metodo para indicar que un jugador tiro la carta de corazones
	private void tiroCorazones() throws RemoteException  {
		if (cartaAJugar.getPalo() == Palo.CORAZONES && !this.corazonesRotos) {
			notificarObservadores(EventosCorazones.CORAZONES_ROTOS);
			this.corazonesRotos = true;
		}
	}
	
	// *************************************************************
	// 					    PASAJE DE CARTAS
	// *************************************************************
	
	private void pasajeDeCartas() throws RemoteException  {
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
		
		notificarObservadores(EventosCorazones.PASAJE_DE_CARTAS);
		if (variablePasaje != 0) {
			intercambioDeCartas(variablePasaje);
			notificarObservadores(EventosCorazones.FIN_PASAJE_DE_CARTAS);
		}
	}
	
	private void intercambioDeCartas(int valor) throws RemoteException  {
		// Esto funciona para que el intercambio se haga sobre el final y los otros
		// jugadores no tengan acceso a las cartas nuevas recibidas
		ArrayList<Carta[]> arregloDeCartasAIntercambiar = new ArrayList<Carta[]>(cantJugadores);

		// Inicializar cada posición del ArrayList con un arreglo de Carta vacío
		for (int i = 0; i < cantJugadores; i++) {
			arregloDeCartasAIntercambiar.add(new Carta[0]);
		}
		
		for (IJugador jugadorPasaje : getJugadores()) {
			
			notificarObservadores(EventosCorazones.PASAJE_DE_CARTAS_POR_JUGADOR);

			// Obtengo la posicion del jugaodr actual y a quien le va a pasar las cartas
			int posicionJugadorActual = buscarJugador(jugadorPasaje);
			
			// Para saber a quien le paso las cartas, tengo que sumar la variable de pasaje
			// a la posicion del jugador actual, y a eso dividirlo por la cantidad de
			// jugadores
			int posicionJugadorPasaje = (posicionJugadorActual + valor + getJugadores().length) % getJugadores().length;

			// Creo la lista de las cartas que se van a pasar al otro jugador
			Carta[] cartasIntercambio = new Carta[cantCartasIntercambio];

			for (int i = 0; i < cantCartasIntercambio; i++) {
				// Obntego la carta que jugo el jugador
				notificarObservadores(EventosCorazones.PEDIR_CARTA_PASAJE);
				cartaTiradaValidaPasaje(cartasIntercambio);
				cartasIntercambio[i] = this.cartaAJugar;
			}

			// Guardo en el arreglo (POR POSICION DE JUGADOR) las cartas nuevas obtenidas y saco las cartas de la mano
			arregloDeCartasAIntercambiar.set(posicionJugadorPasaje, cartasIntercambio);
			for (int i = 0; i < cantCartasIntercambio; i++) {
				// Obntego la carta que jugo el jugador
				getJugadores()[turno].tirarCarta(getJugadores()[turno].buscarCarta(cartasIntercambio[i]));
			}
			
			notificarObservadores(EventosCorazones.FIN_PASAJE_DE_CARTAS_POR_JUGADOR);

			turno = (turno + 1) % getJugadores().length; // Obtengo el siguiente jugador
		}

		otorgarCartasJugadores(arregloDeCartasAIntercambiar);
	}
	
	private void cartaTiradaValidaPasaje(Carta[] cartasIntercambio) throws RemoteException  {
		
		boolean cartaTiradaValida = false;
		while ( !cartaTiradaValida ) {
			
			if ((cartaAJugar != null) && (!buscarCarta(cartasIntercambio, cartaAJugar))) {
				//jugadores[turno].tirarCarta(jugadores[turno].buscarCarta(cartaAJugar));
				cartaTiradaValida = true;
				notificarObservadores(EventosCorazones.CARTA_TIRADA_VALIDA_PASAJE);
			} else {
				notificarObservadores(EventosCorazones.CARTA_TIRADA_INVALIDA_PASAJE);
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
	private int buscarJugador(IJugador jugador) throws RemoteException {
		int posicionJugadorBuscar = 0;
		boolean encontrado = false;
		while (!encontrado && posicionJugadorBuscar < getJugadores().length) {
			if (getJugadores()[posicionJugadorBuscar] == jugador) {
				encontrado = true;
			} else {
				posicionJugadorBuscar++;
			}
		}
		return posicionJugadorBuscar;
	}
	
	// Otorgo las cartas que se pasaron a cada jugador
	private void otorgarCartasJugadores(ArrayList<Carta[]> cartasPasaje) throws RemoteException {
		for (int i = 0; i < cantJugadores; i++) {
			for (int j = 0; j < cantCartasIntercambio; j++) {
				getJugadores()[i].recibirCarta(cartasPasaje.get(i)[j]);
			}
		}
	}
	
	// Metodo para jugar la carta cuando se realize el pasaje
	@Override
	public void jugarCartaPasaje(int i) throws RemoteException {
		cartaAJugar = getJugadores()[turno].tirarCarta(i);
	}
	
	// *************************************************************
	//                      GETTERS
	// *************************************************************
	
	@Override
	public Mazo getMazo()  {
		return mazo;
	}
	
	@Override
	public Jugador[] getJugadores() throws RemoteException {
		Jugador[] jugadores = new Jugador[this.jugadores.size()];
		return this.jugadores.values().toArray(jugadores);
	}

	@Override
	public int getRonda()  {
		return ronda;
	}
	
	@Override
	public List<Jugada> getJugadas()  {
		return jugadas;
	}
	
	@Override
	public String getDireccion()  {
		return String.valueOf(direccion);
	}
	
	@Override
	public Carta getCartaAJugar()  {
		return cartaAJugar;
	}
	
	@Override
	public boolean isCorazonesRotos()  {
		return corazonesRotos;
	}
		
	@Override
	public int getCantidadJugadores()  {
		return cantJugadores;
	}
		
	@Override
	public int getCantCartasIntercambio()  {
		return cantCartasIntercambio;
	}
	
	// *************************************************************
	//                  GETTERS ESPECIALES
	// *************************************************************
	
	
	@Override
	public String getJugador(int i) throws RemoteException  {
		return getJugadores()[i].getNombre();
	}
	
	@Override
	public String getNombreJugadorActual() throws RemoteException  {
		return getJugadores()[turno].getNombre();
	}
	
	@Override
	public int getPosicionJugadorActual()  {
		return turno;
	}
	
	@Override
	public Carta[] getCartasEnMesa() {
		return this.jugadas.get(jugadas.size() - 1).getCartasJugadas();
	}
	
	@Override
	public String getJugadorPerdedorJugada()  {
		return this.jugadas.get(jugadas.size() - 1).getJugadorPerdedor().getNombre();
	}	
	
	@Override
	public int getNumeroJugada()  {
		return this.jugadas.get(jugadas.size()-1).getNumeroJugada();
	}
	
	@Override
	public boolean isCantidadJugadoresValida() throws RemoteException  {
	    int jugadoresRegistrados = 0;
	    for (Object jugador : getJugadores()) {
	        if (jugador != null) {
	            jugadoresRegistrados++;
	        }
	    }
	    return jugadoresRegistrados == getCantidadJugadores();
	}
	
	//Me muestro una array con los nombre de todos los jugadores
	@Override
	public String[] getListaJugadores() throws RemoteException {
	    String[] nombresJugadores = new String[cantJugadores]; 

	    // Recorrer los jugadores según sus claves
	    List<Integer> clavesOrdenadas = new ArrayList<>(jugadores.keySet());

	    for (int i = 0; i < cantJugadores; i++) {
	        if (i < clavesOrdenadas.size()) {
	            int clave = clavesOrdenadas.get(i);
	            nombresJugadores[i] = jugadores.get(clave).getNombre();
	        } else {
	            nombresJugadores[i] = null; // Espaciio vacio si no hay jugadores
	        }
	    }
	    
	    return nombresJugadores;
	}
	
	@Override
	public ArrayList<Carta> getManoJugador(int pos) throws RemoteException  {
		return this.getJugadores()[pos].getMano();
	}
	
	@Override
	public ArrayList<Carta> getManoJugador(String nombreJugador) throws RemoteException  {
		for (Jugador jugador : jugadores.values()) {
	        if (jugador.getNombre().equals(nombreJugador)) {
	            return jugador.getMano();
	        }
	    }
		return new ArrayList<>();
	}
	
	@Override
	public int[] puntajesJugadores() throws RemoteException  {
		int[] puntajes = new int[cantJugadores];
		for (int i = 0; i < cantJugadores; i++) {
			puntajes[i] = this.getJugadores()[i].getPuntaje();
		}
		return puntajes;
	}
	
	@Override
	public String getNombreGanadorJuego() {
		return this.jugadorGanador.getNombre();
	}
	
	// *************************************************************
	//                      SETTERS
	// *************************************************************
	
	@Override
	public void setCartaAJugar(int pos) throws RemoteException  {
		cartaAJugar = getJugadores()[turno].obtenerCartaJugador(pos);
	}
	
	// *************************************************************
	//					 MVC Y OBSERVER
	// *************************************************************

	/*@Override
	public void notificar(Object evento) {
		for (Observador observador : this.observadores) {
			observador.actualizar(evento, this);
		}
	}

	@Override
	public void agregarObservador(Observador observador) {
		this.observadores.add(observador);
	}*/
}
