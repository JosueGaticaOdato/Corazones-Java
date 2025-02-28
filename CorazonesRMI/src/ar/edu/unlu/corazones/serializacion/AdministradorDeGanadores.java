package ar.edu.unlu.corazones.serializacion;

import java.io.Serializable;
import java.util.ArrayList;
public class AdministradorDeGanadores implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> nombres;
	private ArrayList<Integer> cantidad;
	
	public AdministradorDeGanadores() {
		nombres=new ArrayList<String>();
		cantidad=new ArrayList<Integer>();
	}
	
	public void addGanador(String nombre) {
		int partidasGanadas = 0;
		for(int i = 0 ; i < nombres.size() ; i++) {
			if(nombre.equals(nombres.get(i))) {
				partidasGanadas=cantidad.get(i) + 1;
				cantidad.remove(i);
				cantidad.add(i, partidasGanadas);
			}
		}
		if (partidasGanadas==0) {
			nombres.add(nombre);
			cantidad.add(1);
		}
	}
	
	public ArrayList<String> getNombresGanadores() {
		return nombres;
	}
	public ArrayList<Integer> getCantGanadas() {
		return cantidad;
	}
}