package ar.edu.unlu.corazones.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements IJugador, Serializable {
	
	// *************************************************************
	//                        ATRIBUTOS
	// *************************************************************
	
	private static final long serialVersionUID = 1L;

	private String nombre;
	
	private int puntaje;
	
	private ArrayList<Carta> mano;
	
	// ************************ RMI ********************************
	
	private int id;
	
	private static int ID = 0;

	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	public Jugador(String nombre) {
		setNombre(nombre);
		setPuntaje(0);
		this.id = Jugador.ID++;
		mano = new ArrayList<Carta>();
	}
	
	// *************************************************************
	//                       COMPORTAMIENTO
	// *************************************************************

	@Override
	public void recibirCarta(Carta carta) {
		mano.add(carta);
	}
	
	@Override
	public Carta tirarCarta(int posCarta) {
		System.out.println("tirarCarta - posCarta:" + posCarta);
		Carta cartaTirada = obtenerCartaJugador(posCarta);
		mano.remove(posCarta);
		return cartaTirada;
	}
	
	@Override
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
	@Override
	public boolean tieneCartasDelPalo(Palo palo) {
		for (Carta carta : getMano()) {
	        if (carta.getPalo() == palo) {
	            return true;
	        }
	    }
	    return false;
	}
	
	//Comprueba si el jugador tiene el 2 de trebol
    @Override
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
    
    @Override
	public void sumarPuntaje(int puntaje) {
		this.puntaje += puntaje;
    }
	
	// *************************************************************
	//                      GETTERS
	// *************************************************************
	
	@Override
	public String getNombre() {
		return nombre;
	}
	
	@Override
	public ArrayList<Carta> getMano() {
		return mano;
	}

	@Override
	public int getPuntaje() {
		return puntaje;
	}
	
	@Override
	public Carta obtenerCartaJugador(int posCarta) {
		System.out.println("obtenerCartaJugador - posCarta:" + posCarta);
		try {
			return mano.get(posCarta);			
		} catch (Exception e) {
			return null;
		}
			
	}
	
	@Override
	public int cantCartasMano() {
		return mano.size();
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	// *************************************************************
	//                      SETTERS
	// *************************************************************
	
	@Override
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	@Override
	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}





	
}
