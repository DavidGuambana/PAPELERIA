package clases;

import java.util.Date;

public class Gasto {
    private int codigo;
    private double valor;
    private String cedula_emp;
    private String RUC_prov;
    private String descripcion;
    private Date fecha_reg;

    public Gasto() {
    }

    public Gasto(int codigo, double valor, String cedula_emp, String RUC_prov, String descripcion, Date fecha_reg) {
        this.codigo = codigo;
        this.valor = valor;
        this.cedula_emp = cedula_emp;
        this.RUC_prov = RUC_prov;
        this.descripcion = descripcion;
        this.fecha_reg = fecha_reg;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCedula_emp() {
        return cedula_emp;
    }

    public void setCedula_emp(String cedula_emp) {
        this.cedula_emp = cedula_emp;
    }

    public String getRUC_prov() {
        return RUC_prov;
    }

    public void setRUC_prov(String RUC_prov) {
        this.RUC_prov = RUC_prov;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_reg() {
        return fecha_reg;
    }

    public void setFecha_reg(Date fecha_reg) {
        this.fecha_reg = fecha_reg;
    }

    @Override
    public String toString() {
        return "Gasto{" + "codigo=" + codigo + ", valor=" + valor + ", cedula_emp=" + cedula_emp + ", RUC_prov=" + RUC_prov + ", descripcion=" + descripcion + ", fecha_reg=" + fecha_reg + '}';
    }

    
}
