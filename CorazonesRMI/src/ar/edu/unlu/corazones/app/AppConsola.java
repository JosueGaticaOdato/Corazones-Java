package ar.edu.unlu.corazones.app;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.vista.IVista;
import ar.edu.unlu.corazones.vista.VistaConsola;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class AppConsola {

	public static void main(String[] args) throws RemoteException {
		/*ArrayList<String> ips = Util.getIpDisponibles();
		String ip = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione la IP en la que escuchar� peticiones el cliente", "IP del cliente", 
				JOptionPane.QUESTION_MESSAGE, 
				null,
				ips.toArray(),
				null
		);*/
		String ip = "127.0.0.1";
		String port = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione el puerto en el que escuchar� peticiones el cliente", "Puerto del cliente", 
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				9999
		);
		String ipServidor = "127.0.0.1";
		String portServidor = "8888";
		/*String ipServidor = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione la IP en la corre el servidor", "IP del servidor", 
				JOptionPane.QUESTION_MESSAGE, 
				null,
				null,
				null
		);
		String portServidor = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione el puerto en el que corre el servidor", "Puerto del servidor", 
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				8888
		);*/
		//IVista vista = new VistaConsola();
		Controlador controlador = new Controlador();
		//IVista vista = new VistaGrafica(controlador);
		IVista vista = new VistaConsola(controlador);
		Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));	
		try {
			System.out.println("Iniciando controlador remoto...");
			c.iniciar(controlador);
			System.out.println("Controlador remoto inciado");
			vista.iniciar();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RMIMVCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
