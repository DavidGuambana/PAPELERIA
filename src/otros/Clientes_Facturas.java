package otros;

public class Clientes_Facturas {
    private String cedula;
    private String nombre;
    private String apellido;
    private int num_fac;

    public Clientes_Facturas() {
    }

    public Clientes_Facturas(String cedula, String nombre, String apellido, int num_fac) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.num_fac = num_fac;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getNum_fac() {
        return num_fac;
    }

    public void setNum_fac(int num_fac) {
        this.num_fac = num_fac;
    }

    @Override
    public String toString() {
        return "Clientes_Facturas{" + "cedula=" + cedula + ", nombre=" + nombre + ", apellido=" + apellido + ", num_fac=" + num_fac + '}';
    }
}
