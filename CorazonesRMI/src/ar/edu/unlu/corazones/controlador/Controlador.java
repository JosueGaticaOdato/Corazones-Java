package ar.edu.unlu.corazones.controlador;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.corazones.modelo.Carta;
import ar.edu.unlu.corazones.modelo.EventosCorazones;
import ar.edu.unlu.corazones.modelo.ICorazones;
import ar.edu.unlu.corazones.modelo.IJugador;
import ar.edu.unlu.corazones.vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class Controlador implements IControladorRemoto {
	
	private ICorazones modelo;
	
	private IVista vista;
	
	private IJugador jugador;
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************

	public <T extends IObservableRemoto> Controlador(T modelo) {
		try {
			this.setModeloRemoto(modelo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Controlador() {
		
	}
	
	public void setVista(IVista vista) {
		this.vista = vista;
	}

	// *************************************************************
	//                       COMPORTAMIENTO
	// *************************************************************
	
	// ****************** MANEJO DE JUGADORES  *********************
	
	public void conectarJugador(String nombre) {
		try {
			this.jugador = (IJugador) this.modelo.conectarJugador(nombre);
		} catch (RemoteException e) {
			e.printStackTrace();
		}	
	}
	
	public void desconectarJugador() {
		try {
			this.modelo.desconectarJugador(this.jugador.getId());
		} catch (RemoteException e) {
			e.printStackTrace();
		}	
	}	

	// ********************* PRE-JUEGO *****************************
	
	public boolean isCantidadJugadoresValida() throws RemoteException  {
		return modelo.isCantidadJugadoresValida();
	}
	
	public String[] listaJugadores() throws RemoteException  {
		return modelo.getListaJugadores();
	}
	
	public int cantidadJugadores() throws RemoteException  {
		return modelo.getCantidadJugadores();
	}
	
	public void iniciarJuego() throws RemoteException  {
		try {
			this.modelo.iniciarJuego();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// ************************ PASAJE ******************************
	
	public String direccionPasaje() throws RemoteException  {
		return this.modelo.getDireccion();
	}
	
	public void cartaJugadaPasaje(int i) throws RemoteException  {
		this.modelo.setCartaAJugar(i);
	}
	
	public int cantidadCartasPasaje() throws RemoteException  {
		return this.modelo.getCantCartasIntercambio();
	}
	
	// ************************ JUEGO ******************************
	
	public int numeroRonda() throws RemoteException  {
		return this.modelo.getRonda();
	}

	public int numeroJugada() throws RemoteException  {
		return this.modelo.getNumeroJugada();
	}
	
	public ArrayList<Carta> manoJugador(int pos) throws RemoteException {
		return this.modelo.getManoJugador(pos);
	} 
	
	public ArrayList <Carta> manoJugador(String nombreJugador)  throws RemoteException {
		return this.modelo.getManoJugador(nombreJugador);
	}
	
	public String nombreJugadorActual() throws RemoteException  {
		return this.modelo.getNombreJugadorActual();
	}
	
	public int posicionJugadorActual() throws RemoteException  {
		return this.modelo.getPosicionJugadorActual();
	}
	
	public String getJugador(int i) throws RemoteException  {
		return this.modelo.getJugador(i);
	}

	public void cartaJugada(int i) throws RemoteException  {
		this.modelo.setCartaAJugar(i);
	}
	
	public Carta[] cartasEnMesa() throws RemoteException  {
		return this.modelo.getCartasEnMesa();
	}
	
	public String jugadorPerdedorJugada() throws RemoteException  {
		return this.modelo.getJugadorPerdedorJugada();
	}
	
	public int[] puntajesJugadores() throws RemoteException  {
		return this.modelo.puntajesJugadores();
	}

	public boolean isCorazonesRotos() throws RemoteException  {
		return this.modelo.isCorazonesRotos();
	}
	
	public Carta getCartaAJugar() throws RemoteException  {
		return this.modelo.getCartaAJugar();
	}
	
	public String ganadorJuego() throws RemoteException  {
		return this.modelo.getNombreGanadorJuego();
	}
	
	// *************************************************************
	//                       OBSERVER
	// *************************************************************

	@Override
	public void actualizar(IObservableRemoto observable, Object evento) throws RemoteException {
		// TODO Auto-generated method stub
		if (evento instanceof EventosCorazones) {
			switch ((EventosCorazones) evento) {
			
			case CARTAS_REPARTIDAS:
				this.vista.cartasRepartidas();
				break;
			case PASAJE_DE_CARTAS:
				this.vista.pasajeDeCartas();
				break;
			case PEDIR_CARTA_PASAJE:
				this.vista.pedirCartaPasaje();
				break;
			case PASAJE_DE_CARTAS_POR_JUGADOR:
				this.vista.pasajeDeCartasJugador();
				break;
			case FIN_PASAJE_DE_CARTAS_POR_JUGADOR:
				this.vista.finPasajeDeCartasJugador();
				break;
			case CARTA_TIRADA_VALIDA_PASAJE:
				this.vista.cartaTiradaValidaPasaje();
				break;
			case CARTA_TIRADA_INVALIDA_PASAJE:
				this.vista.cartaTiradaInvalidaPasaje();
				break;
			case FIN_PASAJE_DE_CARTAS:
				this.vista.finPasajeDeCartas();
				break;	
			case NUEVA_JUGADA:
				this.vista.nuevaJugada();
				break;	
			case PEDIR_CARTA:
				this.vista.pedirCarta();
				break;
			case JUGAR_2_DE_TREBOL:
				this.vista.jugarDosDeTrebol();
				break;
			case CARTA_TIRADA_INVALIDA:
				this.vista.cartaTiradaInvalida();
				break;
			case CARTA_TIRADA_INVALIDA_2_DE_TREBOL:
				this.vista.cartaTiradaInvalida2deTrebol();
				break;
			case PERDEDOR_JUGADA:
				this.vista.perdedorJugada();
				break;
			case CORAZONES_ROTOS:
				this.vista.corazonesRotos();
				break;
			case CARTA_TIRADA_VALIDA:
				this.vista.cartaTiradaValida();
				break;
			case FIN_DE_RONDA:
				this.vista.finDeRonda();
				break;
			case FIN_DE_JUEGO:
				this.vista.finDeJuego();
				break;
			}
		}
	}
	
	@Override
	public <T extends IObservableRemoto> void setModeloRemoto(T modelo) throws RemoteException {
		// TODO Auto-generated method stub
		this.modelo = (ICorazones) modelo;
	}
}
