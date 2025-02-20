package ar.edu.unlu.corazones.app;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ar.edu.unlu.corazones.modelo.Corazones;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

public class AppServidor {

	public static void main(String[] args) {
		//ArrayList<String> ips = Util.getIpDisponibles();
		String ip = "127.0.0.1";
		String port = "8888";		
		Corazones modelo = new Corazones();
		Servidor servidor = new Servidor(ip, Integer.parseInt(port));
		try {
			System.out.println("Servidor iniciado");
			servidor.iniciar(modelo);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RMIMVCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}