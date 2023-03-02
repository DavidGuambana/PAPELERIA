package clases;

public class Detalle_fac {
    private int codigo;
    private int codigo_pro;
    private int cant;
    private double subtotal;
    private int codigo_fac;

    public Detalle_fac() {
    }

    public Detalle_fac(int codigo, int codigo_pro, int cant, double subtotal, int codigo_fac) {
        this.codigo = codigo;
        this.codigo_pro = codigo_pro;
        this.cant = cant;
        this.subtotal = subtotal;
        this.codigo_fac = codigo_fac;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo_pro() {
        return codigo_pro;
    }

    public void setCodigo_pro(int codigo_pro) {
        this.codigo_pro = codigo_pro;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getCodigo_fac() {
        return codigo_fac;
    }

    public void setCodigo_fac(int codigo_fac) {
        this.codigo_fac = codigo_fac;
    }

    
    
}
