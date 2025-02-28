package ar.edu.unlu.serializacion;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Serializador {
	
	private String fileName; //Nombre del archivo donde voy a guardar datos

	public Serializador(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	//Escribe el primer objeto
	public boolean writeOneObject(Object obj) {
		boolean respuesta = false;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName)); //Necesito que genere un archivo en disco
			oos.writeObject(obj);
			oos.close();
			respuesta = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respuesta;
	}
	
	//Agregar un elemento
	public boolean addOneObject(Object obj) {
		boolean respuesta = false;
		try {
			AddableObjectOutputStream oos = new AddableObjectOutputStream (new FileOutputStream(fileName,true));
			oos.writeObject(obj);
			oos.close();
			respuesta = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respuesta;
	}
	
	
	public Object readFirstObject() {
		Object respuesta = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fileName));
			
			respuesta = ois.readObject();
			
			ois.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return respuesta;
	}
	
	//Los objetos se leen 1 a 1 como fueron guardados
	public Object[] readObjects() {
		Object[] respuesta;
		ArrayList<Object> listOfObject = new ArrayList<Object>();
		try {
			ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fileName));
			
			Object r = ois.readObject(); //Primer objeto
			while (r !=null)
            {
               listOfObject.add(r); //Agrego a la lista
               r = ois.readObject();
            }
            ois.close();
			
		}catch (EOFException e) {
			System.out.println("Lectura completada"); //Siempre tira esta Excepcion
            
        }catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!listOfObject.isEmpty() ) {
			respuesta = new Object[listOfObject.size()];
			int count = 0;
			for(Object o : listOfObject)
				respuesta[count ++] = o;
		} else {
			respuesta = null;
		}
		return respuesta;
	}
	
}
