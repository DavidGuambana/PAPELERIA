package clases;

import java.util.Date;

public class Proveedor {
    private String RUC;
    private String nombre_empresa;
    private String codigo_ciu;
    private String telefono;
    private String correo;
    private Date fecha_reg;

    public Proveedor() {
    }

    public Proveedor(String RUC, String nombre_empresa, String codigo_ciu, String telefono, String correo, Date fecha_reg) {
        this.RUC = RUC;
        this.nombre_empresa = nombre_empresa;
        this.codigo_ciu = codigo_ciu;
        this.telefono = telefono;
        this.correo = correo;
        this.fecha_reg = fecha_reg;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public String getCodigo_ciu() {
        return codigo_ciu;
    }

    public void setCodigo_ciu(String codigo_ciu) {
        this.codigo_ciu = codigo_ciu;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFecha_reg() {
        return fecha_reg;
    }

    public void setFecha_reg(Date fecha_reg) {
        this.fecha_reg = fecha_reg;
    }

    @Override
    public String toString() {
        return "Proveedor{" + "RUC=" + RUC + ", nombre_empresa=" + nombre_empresa + ", codigo_ciu=" + codigo_ciu + ", telefono=" + telefono + ", correo=" + correo + ", fecha_reg=" + fecha_reg + '}';
    }
    
}
