package ar.edu.unlu.corazones.modelo;

import java.io.Serializable;

public class Carta implements Serializable {
	
	// *************************************************************
	//                        ATRIBUTOS
	// *************************************************************
	
	private static final long serialVersionUID = 1L;

	private Palo palo;
	
	private int valor;
	
	private String valorTexto; // Numeros o J,Q,K y A
	
	// *************************************************************
	//                       CONSTRUCTOR
	// *************************************************************
	
	public Carta(Palo palo, int valor) {
		setPalo(palo);
		setValor(valor);
		valorCarta();
	}
	
	// *************************************************************
	//                       COMPORTAMIENTO
	// *************************************************************

	public String mostrarCarta() {
			return valorTexto + " - " + palo.toString();
	}
	
	private void valorCarta() {
		switch (this.valor) {
		case 11:
			setValorTexto("J");
			break;
		case 12:
			setValorTexto("Q");
			break;
		case 13:
			setValorTexto("K");
			break;
		case 1:
			setValorTexto("A");
			setValor(14); //La A es el valor mas alto en el juego
			break;
		default:
			setValorTexto(String.valueOf(this.valor));
			break;
		}
	}
	
	// *************************************************************
	//                      	GETTERS
	// *************************************************************
	
	public Palo getPalo() {
		return palo;
	}

	public int getValor() {
		return valor;
	}
	
	public String getValorTexto() {
		return valorTexto;
	}
	
	public String getCarta() {
		return String.valueOf(this.getValor()) + " - " + getPalo();
	}
	
	// *************************************************************
	//                      	SETTERS
	// *************************************************************
	
	public void setPalo(Palo palo){
		this.palo = palo;
	}
	
	public void setValor(int valor){
		this.valor = valor;
	}
	
	public void setValorTexto(String valorT) {
		this.valorTexto = valorT;
	}
	
}


