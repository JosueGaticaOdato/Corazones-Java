package ar.edu.unlu.corazones.modelo;

import java.util.ArrayList;

/**
 * CLASE JUGADOR
 * Se encarga de realizar todos los movimientos dentro del juego
 * Es el que impulsa al juego y sus decisiones afectan a su funcionamiento
 */

public class Jugador {
	
	// *************************************************************
	//                        ATRIBUTOS
	// *************************************************************
	
	//Nombre del jugador
	private String nombre;
	
	//Puntaje del jugador
	private int puntaje;
	
	//Mano del jugador
	private ArrayList<Carta> mano;
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	public Jugador(String nombre, int posicion) {
		this.nombre = nombre; //Determino el nombre del jugador
		this.puntaje = 0; //Seteo los puntos en 0
		mano = new ArrayList<Carta>();
	}

	// *************************************************************
	//                       COMPORTAMIENTO
	// *************************************************************
	
	//Metodo que le da las cartas al jugador y las agrega a su mano
	public void recibirCarta(Carta carta) {
		mano.add(carta);
	}
	
	//Tirar carta a traves de la posicion
	public Carta tirarCarta(int posCarta) {
		Carta cartaTirada = obtenerCarta(posCarta);
		mano.remove(posCarta);
		return cartaTirada;
	}
	
	//Buscar carta por posicion
	public int buscarCarta(Carta carta) {
    	boolean cartaEncontrada = false;
    	int pos = 0;
    	while (!cartaEncontrada && pos < mano.size()) {
    		if (mano.get(pos).getPalo() == carta.getPalo() && mano.get(pos).getValor() == carta.getValor())  {
    			cartaEncontrada = true;
    		} else {
    			pos++;
    		}
    	}
    	if (!cartaEncontrada) {
    		pos = -1;
    	}
        return pos;
	}

	
	// *************************************************************
	//                 FUNCION EN CORAZONES (sin implementar aun)
	// *************************************************************
	
	
	// *************************************************************
	//                      GETTERS
	// *************************************************************

	public String getNombre() {
		return nombre;
	}

	public ArrayList<Carta> getMano() {
		return mano;
	}

	public int getPuntaje() {
		return puntaje;
	}
	
	//Obtener carta a traves de la posicion
	public Carta obtenerCarta(int posCarta) {
		return mano.get(posCarta);
	}
	
	//Metodo que muestra la cantidad de cartas en mano del jugador
	public int cartasEnMano() {
		return mano.size();
	}
	
	// *************************************************************
	//                      SETTERS
	// *************************************************************

	//El puntaje es la suma del puntaje actual mas el de la carta que se paso
	public void setPuntaje(int puntajeCarta) {
		this.puntaje += puntajeCarta;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	

}
