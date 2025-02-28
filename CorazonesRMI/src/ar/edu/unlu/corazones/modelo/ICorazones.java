package ar.edu.unlu.corazones.modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public interface ICorazones extends IObservableRemoto, Serializable{

	void iniciarJuego() throws RemoteException;

	void jugarCartaPasaje(int i) throws RemoteException;
	
	IJugador conectarJugador(String nombre) throws RemoteException;
	
	void desconectarJugador(int jugadorId) throws RemoteException;

	/*boolean agregarJugadores(String nombre) throws RemoteException;

	boolean reemplazarJugadores(String nombre, int posicion) throws RemoteException;*/

	Mazo getMazo() throws RemoteException;

	IJugador[] getJugadores() throws RemoteException;

	int getRonda() throws RemoteException;

	List<Jugada> getJugadas() throws RemoteException;

	String getDireccion() throws RemoteException;

	Carta getCartaAJugar() throws RemoteException;

	boolean isCorazonesRotos() throws RemoteException;

	int getCantidadJugadores() throws RemoteException;

	int getCantCartasIntercambio() throws RemoteException;

	String getJugador(int i) throws RemoteException;

	String getNombreJugadorActual() throws RemoteException;

	int getPosicionJugadorActual() throws RemoteException;

	Carta[] getCartasEnMesa() throws RemoteException;

	String getJugadorPerdedorJugada() throws RemoteException;

	int getNumeroJugada() throws RemoteException;

	boolean isCantidadJugadoresValida() throws RemoteException;

	String[] getListaJugadores() throws RemoteException;

	ArrayList<Carta> getManoJugador(int pos) throws RemoteException;
	
	ArrayList<Carta> getManoJugador(String nombreJugador) throws RemoteException;

	int[] puntajesJugadores() throws RemoteException;

	String getNombreGanadorJuego() throws RemoteException;

	void setCartaAJugar(int pos) throws RemoteException;

}