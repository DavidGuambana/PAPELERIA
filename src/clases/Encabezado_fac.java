package clases;

import java.util.Date;

public class Encabezado_fac {
    private int codigo;
    private String cedula_cli;
    private Date fecha;
    private double total;
    private String estado;
    

    public Encabezado_fac() {
    }

    public Encabezado_fac(int codigo, String cedula_cli, Date fecha, double total, String estado) {
        this.codigo = codigo;
        this.cedula_cli = cedula_cli;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCedula_cli() {
        return cedula_cli;
    }

    public void setCedula_cli(String cedula_cli) {
        this.cedula_cli = cedula_cli;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Encabezado_fac{" + "codigo=" + codigo + ", cedula_cli=" + cedula_cli + ", fecha=" + fecha + ", total=" + total + ", estado=" + estado + '}';
    }

    
}
