package ar.edu.unlu.serializacion;

public class JugadorRanking {
    private String nombre;
    private int cantidad;

    public JugadorRanking(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }
}