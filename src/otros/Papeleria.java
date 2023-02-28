package otros;

import java.util.Date;

public class Papeleria {
    private String RUC;
    private String nombre;
    private String direccion;
    private Date fecha;

    public Papeleria() {
    }

    public Papeleria(String RUC, String nombre, String direccion, Date fecha) {
        this.RUC = RUC;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fecha = fecha;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Papeleria{" + "RUC=" + RUC + ", nombre=" + nombre + ", direccion=" + direccion + ", fecha=" + fecha + '}';
    }
    
    
}
