package ar.edu.unlu.corazones.modelo;

import java.util.Random;

/* Mazo: Es una pila de cartas implementada como un array, donde se le reparten
a los jugadores las cartas que estan en el tope del mazo*/

public class Mazo {
	
	// *************************************************************
	//                        CONSTANTES
	// *************************************************************

	//Cantidad de cartas fija
	public final int Cant_Cartas = 52;
	
	//Cantidad de veces que se le hacen los swaps a las cartas
	//(Mayor sera el mayor, mejor sera la mezcla)
	public final int Cant_Mezcla = 50;
	
	// *************************************************************
	//                        ATRIBUTOS
	// *************************************************************
	
	//El mazo, que sera un array de cartas
	private Carta[] mazo;
	
	//Posicion del tope del mazo
	private int tope;
	

	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	public Mazo() {
		this.mazo = new Carta[Cant_Cartas];
		this.tope = Cant_Cartas - 1; //[0,1,2,3,4,...51 (tope)]
		//Creo y barajo el mazo
		crearMazo();
		barajarMazo();	
	}
	
	// *************************************************************
	//                    METODOS CONSTRUCTOR
	// *************************************************************
	
	private void crearMazo() {
		//Creo la variable con los palos que tengo disponible
		Palo[] misPalos = Palo.values();
		
		int posicion = 0;	
		
		//Primero recorro todos los palos que tengo: CORAZONES, DIAMANTES, PICAS Y TREBOLES
		for (int valorPalo = 0; valorPalo < 4; valorPalo++ ) {
			
			//Luego, recorro los 13 valores que tienen cada carta
			for (int numeroCarta = 1; numeroCarta < 14; numeroCarta++) {
				//Agrego al mazo las cartas que van del 1 al 13
				mazo[posicion] = new Carta(misPalos[valorPalo], numeroCarta);
				posicion++;
				}
			}
	}
	
	//Metodo que baraja el mazo de cartas
	private void barajarMazo() {
		
		//Creo el objeto random para obtener un numero random
		Random rand = new Random(); 
		
		for (int mezcla = 0; mezcla < Cant_Mezcla; mezcla++) {
			
			//Obtengo dos posiciones random dentro de las cartas
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
	
	//Metodo que saca la carta del tope del mazo
	public Carta sacarCarta() {
		//Obtengo la carta que saque
		Carta cartaSacada = mazo[this.tope];
		//Hago nulo la posicion de la carta que saque
		mazo[this.tope] = null;
		//Reduzco el valor del tope (ahora hay menos cartas)
		this.tope -= 1;		
		//Devuelvo la carta que saque
		return cartaSacada;
	}
	
	// *************************************************************
	//                       GETTERS
	// *************************************************************
	
	//Mostrar contenido del mazo (solo para Consola)
	public String mostrarMazo() {
		String texto = "";
		for (int i = 0; i < (this.tope + 1); i++) {
			texto += String.valueOf(i) + ") " + mazo[i].mostrarCarta() + "\n";
		}
		return texto;
	}
	
	//Get Mazo
	public Carta[] getMazo() {
		return this.mazo;
	}
	
	
}
