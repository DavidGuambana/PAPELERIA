package clases;

public class Detalle_fac {
    private int codigo;
    private int codigo_pro;
    private int cant;
    private double subtotal;

    public Detalle_fac(int codigo, int codigo_pro, int cant, double subtotal) {
        this.codigo = codigo;
        this.codigo_pro = codigo_pro;
        this.cant = cant;
        this.subtotal = subtotal;
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

    @Override
    public String toString() {
        return "Detalle_fac{" + "codigo=" + codigo + ", codigo_pro=" + codigo_pro + ", cant=" + cant + ", subtotal=" + subtotal + '}';
    }
}
