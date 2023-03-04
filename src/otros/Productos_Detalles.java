package otros;

public class Productos_Detalles {
    private int codigo;
    private String nombre;
    private int ventas;

    public Productos_Detalles() {
    }

    public Productos_Detalles(int codigo, String nombre, int ventas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ventas = ventas;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    @Override
    public String toString() {
        return "Productos_Detalles{" + "codigo=" + codigo + ", nombre=" + nombre + ", ventas=" + ventas + '}';
    }
    
    
    
}
