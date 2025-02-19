package ar.edu.unlu.corazones.modelo;

import java.util.ArrayList;

public class Jugador{
	
	// *************************************************************
	//                        ATRIBUTOS
	// *************************************************************
	
	private String nombre;
	
	private int puntaje;
	
	private ArrayList<Carta> mano;

	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	public Jugador(String nombre) {
		setNombre(nombre);
		setPuntaje(0);
		mano = new ArrayList<Carta>();
	}
	
	// *************************************************************
	//                       COMPORTAMIENTO
	// *************************************************************

	public void recibirCarta(Carta carta) {
		mano.add(carta);
	}
	
	public Carta tirarCarta(int posCarta) {
		Carta cartaTirada = obtenerCartaJugador(posCarta);
		mano.remove(posCarta);
		return cartaTirada;
	}
	
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
	//                 FUNCION EN CORAZONES
	// *************************************************************
	
	//Me dice si el jugador tiene en su mano cartas del mismo palo
	public boolean tieneCartasDelPalo(Palo palo) {
		for (Carta carta : getMano()) {
	        if (carta.getPalo() == palo) {
	            return true;
	        }
	    }
	    return false;
	}
	
	//Comprueba si el jugador tiene el 2 de trebol
    public boolean tengoDosDeTrebol() {
    	boolean loTiene = false;
    	int pos = 0;
    	while (!loTiene && pos < mano.size()) {
    		if (mano.get(pos).getPalo() == Palo.TREBOL && mano.get(pos).getValor() == 2)  {
            	loTiene = true;
    		} else {
    			pos++;
    		}
    	}
        return loTiene;
    }
    
    public void sumarPuntaje(int puntaje) {
		this.puntaje += puntaje;
    }
	
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
	
	public Carta obtenerCartaJugador(int posCarta) {
		try {
			return mano.get(posCarta);			
		} catch (Exception e) {
			return null;
		}
			
	}
	
	public int cantCartasMano() {
		return mano.size();
	}
	
	// *************************************************************
	//                      SETTERS
	// *************************************************************
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}





	
}
