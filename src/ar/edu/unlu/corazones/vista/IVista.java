package ar.edu.unlu.corazones.vista;

import ar.edu.unlu.corazones.controlador.Controlador;

public interface IVista {

	// *************************************************************
	//                         PRE-JUEGO
	// *************************************************************
	
	void iniciar();
	
	// *************************************************************
    //                      PASAJE DE CARTAS
	// *************************************************************
	
	
	void pasajeDeCartas();
	  
	void pedirCartaPasaje();
	 
	void cartaTiradaInvalidaPasaje();
	
	void cartaTiradaValidaPasaje();
	  
	void finPasajeDeCartas();
	 
	// *************************************************************
	// 							JUEGO
	//**************************************************************
	
	void nuevaJugada();
	  
	void cartasRepartidas();
	  
	void pedirCarta();
	  
	void jugarDosDeTrebol();
	  
	void cartaTiradaInvalida();
	
	void cartaTiradaInvalida2deTrebol();
	  
	void perdedorJugada();
	  
	void corazonesRotos();
	  
	void cartaTiradaValida();
	  
	// *************************************************************
	//						 FIN JUEGO - RONDA
	// *************************************************************
	  
	void finDeRonda();
	  
	void finDeJuego();
	 
	
	// *************************************************************
	//                		 OBSERVER
	// *************************************************************
	
	void setControlador(Controlador controlador);



}
