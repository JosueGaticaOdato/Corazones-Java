package ar.edu.unlu.corazones.vista;

import ar.edu.unlu.corazones.controlador.Controlador;

public interface IVista {

	//Menu principal del programa
	void mostrarMenu();
	
	//Iniciar juego
	void iniciar();
	
	//Metodo que setea el controlador
	void setControlador(Controlador controlador);
	
}
