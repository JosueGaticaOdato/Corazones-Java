package ar.edu.unlu.corazones.vista;

import java.rmi.RemoteException;

import ar.edu.unlu.corazones.controlador.Controlador;

public interface IVista {

	// *************************************************************
	//                         PRE-JUEGO
	// *************************************************************
	
	void iniciar() throws RemoteException;
	
	void desconectarJugador() throws RemoteException;
	
	// *************************************************************
    //                      PASAJE DE CARTAS
	// *************************************************************
	
	void pasajeDeCartas() throws RemoteException;
	  
	void pedirCartaPasaje() throws RemoteException;
	 
	void cartaTiradaInvalidaPasaje() throws RemoteException;
	
	void cartaTiradaValidaPasaje() throws RemoteException;
	  
	void finPasajeDeCartas() throws RemoteException;
	 
	// *************************************************************
	// 							JUEGO
	//**************************************************************
	
	void nuevaJugada() throws RemoteException;
	  
	void cartasRepartidas() throws RemoteException;
	  
	void pedirCarta() throws RemoteException;
	  
	void jugarDosDeTrebol() throws RemoteException;
	  
	void cartaTiradaInvalida() throws RemoteException;
	
	void cartaTiradaInvalida2deTrebol() throws RemoteException;
	  
	void perdedorJugada() throws RemoteException;
	  
	void corazonesRotos() throws RemoteException;
	  
	void cartaTiradaValida() throws RemoteException;
	  
	// *************************************************************
	//						 FIN JUEGO - RONDA
	// *************************************************************
	  
	void finDeRonda() throws RemoteException;
	  
	void finDeJuego() throws RemoteException;
	
	// *************************************************************
	//						   SERIALIZACION
	// *************************************************************
	
	void serializarGanador() throws RemoteException;
	
	void verRankingGandores() throws RemoteException;
	 
	
	// *************************************************************
	//                		 OBSERVER
	// *************************************************************
	
	//void setControlador(Controlador controlador) throws RemoteException;

}
