package ar.edu.unlu.corazones.app;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.modelo.Corazones;
import ar.edu.unlu.corazones.modelo.Mazo;
import ar.edu.unlu.corazones.vista.IVista;
import ar.edu.unlu.corazones.vista.VistaConsola;
import ar.edu.unlu.corazones.vista.VistaGrafica;

public class app {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Corazones modelo = new Corazones();
		//IVista vista = new VistaGrafica();
		IVista vista = new VistaConsola();
		Controlador controlador = new Controlador(modelo,vista);
		vista.iniciar();
	}

}
