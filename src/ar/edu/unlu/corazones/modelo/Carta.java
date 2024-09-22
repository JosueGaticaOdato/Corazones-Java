package ar.edu.unlu.corazones.modelo;

public class Carta {
	
	// *************************************************************
	//                        ATRIBUTOS
	// *************************************************************
	
	//Valor del palo que tiene la carta
	private Palo palo;
	
	//Valor (1 a 13) que tiene la carta
	private int valor;
	
	//Valor textual de la carta (en el caso de que sea J,Q,K Y A)
	private String valorTexto;
	
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
	
	//Metodo que muestra la carta en forma de string
	public String mostrarCarta() {
			return valorTexto + " - " + palo.toString();
	}
	
	//Obtener el valor real de la carta (si son letras o no)
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
			setValor(14); //Paso a 14 porque es la mas grande
			break;
		default:
			setValorTexto(String.valueOf(this.valor));
			break;
		}
	}
	
	// *************************************************************
	//                      	GETTERS
	// *************************************************************
	
	//Getter para obtener el valor del palo de la carta
	public Palo getPalo() {
		return palo;
	}

	//Getter para obtener el valor de la carta
	public int getValor() {
		return valor;
	}
	
	//Getter para obtener el valor (en letra si es el caso) de la carta
	public String getValorTexto() {
		return valorTexto;
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
