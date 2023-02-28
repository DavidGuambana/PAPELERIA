package clases;

public class Ciudad {
    private int codigo;
    private String nombre;
    private String provincia;

    public Ciudad() {
    }

    public Ciudad(int codigo, String nombre, String provincia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.provincia = provincia;
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

   

    @Override
    public String toString() {
        return "Ciudad{" + "codigo=" + codigo + ", nombre=" + nombre + ", provincia=" + provincia + '}';
    }
    
    
}
