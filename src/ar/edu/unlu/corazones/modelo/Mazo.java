package ar.edu.unlu.corazones.modelo;

import java.io.Serializable;
import java.util.Random;

/* Mazo: Es una pila de cartas implementada como un array, donde se le reparten
a los jugadores las cartas que estan en el tope del mazo*/

public class Mazo implements Serializable{
	
	// *************************************************************
	//                        SERIALIZABLE
	// *************************************************************
	
	private static final long serialVersionUID = 1L;

	// *************************************************************
	//                        CONSTANTES
	// *************************************************************

	public final int Cant_Cartas = 52;
	
	//Cantidad de swap (mezcla)
	public final int Cant_Mezcla = 50;
	
	// *************************************************************
	//                        ATRIBUTOS
	// *************************************************************
	
	private Carta[] mazo;

	private int tope;
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	public Mazo() {
		this.mazo = new Carta[Cant_Cartas];
		this.tope = Cant_Cartas - 1; //[0,1,2,3,4,...51 (tope)]
		crearMazo();
		//barajarMazo();	
	}
	
	// *************************************************************
	
	private void crearMazo() {
		Palo[] misPalos = Palo.values();
		
		int posicion = 0;	
		
		for (int valorPalo = 0; valorPalo < 4; valorPalo++ ) {
			for (int numeroCarta = 1; numeroCarta < 14; numeroCarta++) {
				//Agrego al mazo las cartas que van del 1 al 13
				mazo[posicion] = new Carta(misPalos[valorPalo], numeroCarta);
				posicion++;
			}
		}
	}
	
	private void barajarMazo() {
		
		Random rand = new Random(); 
		
		for (int mezcla = 0; mezcla < Cant_Mezcla; mezcla++) {
			
			int posicion1 = rand.nextInt(Cant_Cartas);
			int posicion2 = rand.nextInt(Cant_Cartas);
			
			//Realizo el intercambio de lugares
			Carta swap = mazo[posicion1];
			mazo[posicion1] = mazo[posicion2];
			mazo[posicion2] = swap;
		}
	}
	
	// *************************************************************
	//                       COMPORTAMIENTO
	// *************************************************************

	public Carta sacarCarta() {
		Carta cartaSacada = mazo[this.tope];
		mazo[this.tope] = null;
		this.tope -= 1;
		return cartaSacada;
	}
	
	// *************************************************************
	//                       	GETTERS
	// *************************************************************
	
	public Carta[] getMazo() {
		return mazo;
	}
	
}

