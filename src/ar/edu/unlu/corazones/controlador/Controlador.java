package ar.edu.unlu.corazones.controlador;

import ar.edu.unlu.corazones.modelo.Corazones;
import ar.edu.unlu.corazones.observer.Observable;
import ar.edu.unlu.corazones.observer.Observador;
import ar.edu.unlu.corazones.vista.IVista;

/**
 *	CONTROLADOR
 *	.Encargado de la comunicacion entre la vista y el modelo (juego)
 */

public class Controlador implements Observador {

	private Corazones modelo;
	
	private IVista vista;
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	public Controlador(Corazones modelo, IVista vista) {
		this.modelo = modelo;
		this.vista = vista;
		this.vista.setControlador(this);
		this.modelo.agregarObservador(this);
	}
	
	// *************************************************************
	//                       COMPORTAMIENTO
	// *************************************************************

	// ********************* PRE-JUEGO *****************************
	
	//Consulto al juego si la cantidad de jugadores es valida para poder comenzar a jugar
	public boolean isCantidadJugadoresValida() {
		// TODO Auto-generated method stub
		return modelo.isCantidadJugadoresValida();
	}
	
	//Agregar jugador al juego
	public void agregarJugador(String nombre) {
		// TODO Auto-generated method stub
		modelo.agregarJugadores(nombre);
	}
	
	public String[] listaJugadores() {
		// TODO Auto-generated method stub
		return modelo.getListaJugadores();
	}
	
	public int cantidadJugadores() {
		return modelo.getCantidadJugadores();
	}
	
	public boolean modificarJugador(String nombre, int pos) {
		return this.modelo.reemplazarJugadores(nombre, pos);
	}

	
	// *************************************************************
	//                       OBSERVER
	// *************************************************************
	
	@Override
	public void actualizar(Object evento, Observable observado) {
		// TODO Auto-generated method stub
		
	}





}
