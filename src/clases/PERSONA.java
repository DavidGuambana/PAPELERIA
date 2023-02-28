package clases;

import java.util.Date;


public abstract class PERSONA {
    private String cedula;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private Date fecha_nac;
    private Date fecha_reg;
    private String genero;
    private String correo;

    public PERSONA() {
    }

    public PERSONA(String cedula, String nombre, String apellido, String direccion, String telefono, Date fecha_nac, Date fecha_reg, String genero, String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fecha_nac = fecha_nac;
        this.fecha_reg = fecha_reg;
        this.genero = genero;
        this.correo = correo;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public Date getFecha_reg() {
        return fecha_reg;
    }

    public void setFecha_reg(Date fecha_reg) {
        this.fecha_reg = fecha_reg;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "PERSONA{" + "cedula=" + cedula + ", nombre=" + nombre + ", apellido=" + apellido + ", direccion=" + direccion + ", telefono=" + telefono + ", fecha_nac=" + fecha_nac + ", fecha_reg=" + fecha_reg + ", genero=" + genero + ", correo=" + correo + '}';
    }
    
    
}
