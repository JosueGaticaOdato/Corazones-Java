package ar.edu.unlu.corazones.app;

import java.rmi.RemoteException;

import ar.edu.unlu.corazones.controlador.Controlador;
import ar.edu.unlu.corazones.vista.IVista;
import ar.edu.unlu.corazones.vista.VistaConsola;
import ar.edu.unlu.corazones.vista.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class AppClientes {

    public static void main(String[] args) throws RemoteException {
        String ipCliente = "127.0.0.1";
        String ipServidor = "127.0.0.1";
        int portServidor = 8888;

        for (int i = 0; i < 3; i++) {
            int portCliente = 9991 + i; // Puertos 9991, 9992, 9993, 9994
            new Thread(() -> iniciarCliente(ipCliente, portCliente, ipServidor, portServidor)).start();
        }
    }

    private static void iniciarCliente(String ipCliente, int portCliente, String ipServidor, int portServidor) {
        try {
            Controlador controlador = new Controlador();
            /*IVista vista;
            if (portCliente == 9993) {
            	vista = new VistaConsola(controlador);
            } else {
            	vista = new VistaGrafica(controlador);
            }*/
            IVista vista = new VistaGrafica(controlador);
            Cliente cliente = new Cliente(ipCliente, portCliente, ipServidor, portServidor);
            
            vista.iniciar();

            System.out.println("Iniciando controlador remoto en puerto " + portCliente + "...");
            cliente.iniciar(controlador);
            System.out.println("Controlador remoto iniciado en puerto " + portCliente);
        } catch (RemoteException | RMIMVCException e) {
            e.printStackTrace();
        }
    }
}
