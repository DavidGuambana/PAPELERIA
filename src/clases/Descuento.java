package clases;

public class Descuento {
    private String nombre;
    private int porcentaje;

    public Descuento() {
    }
    
    public Descuento(String nombre, int porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public String toString() {
        return "Descuento{" + "nombre=" + nombre + ", porcentaje=" + porcentaje + '}';
    }
}
