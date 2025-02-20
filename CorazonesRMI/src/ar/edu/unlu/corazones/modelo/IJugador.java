package ar.edu.unlu.corazones.modelo;

import java.util.ArrayList;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public interface IJugador {

	void recibirCarta(Carta carta);

	Carta tirarCarta(int posCarta);

	int buscarCarta(Carta carta);

	//Me dice si el jugador tiene en su mano cartas del mismo palo
	boolean tieneCartasDelPalo(Palo palo);

	//Comprueba si el jugador tiene el 2 de trebol
	boolean tengoDosDeTrebol();

	void sumarPuntaje(int puntaje);

	String getNombre();

	ArrayList<Carta> getMano();

	int getPuntaje();

	Carta obtenerCartaJugador(int posCarta);

	int cantCartasMano();

	int getId();

	void setNombre(String nombre);

	void setPuntaje(int puntaje);

}