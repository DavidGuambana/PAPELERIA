package clases;

import java.util.Date;

public class Producto {
    private int codigo;
    private String nombre;
    private double precio;
    private int existencias;
    private String nombre_cat;
    private Date fecha_reg;
    private String RUC_prov;
    private String url_imagen;

    public Producto() {
    }

    public Producto(int codigo, String nombre, double precio, int existencias, String nombre_cat, Date fecha_reg, String RUC_prov, String url_imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
        this.nombre_cat = nombre_cat;
        this.fecha_reg = fecha_reg;
        this.RUC_prov = RUC_prov;
        this.url_imagen = url_imagen;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public String getNombre_cat() {
        return nombre_cat;
    }

    public void setNombre_cat(String nombre_cat) {
        this.nombre_cat = nombre_cat;
    }

    public Date getFecha_reg() {
        return fecha_reg;
    }

    public void setFecha_reg(Date fecha_reg) {
        this.fecha_reg = fecha_reg;
    }

    public String getRUC_prov() {
        return RUC_prov;
    }

    public void setRUC_prov(String RUC_prov) {
        this.RUC_prov = RUC_prov;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    @Override
    public String toString() {
        return "Producto{" + "codigo=" + codigo + ", nombre=" + nombre + ", precio=" + precio + ", existencias=" + existencias + ", nombre_cat=" + nombre_cat + ", fecha_reg=" + fecha_reg + ", RUC_prov=" + RUC_prov + ", url_imagen=" + url_imagen + '}';
    }

    
}
