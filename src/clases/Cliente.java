package clases;

import java.util.Date;

public class Cliente extends PERSONA {
    private String descuento;

    public Cliente() {
    }

    public Cliente(String descuento, String cedula, String nombre, String apellido, String direccion, String telefono, Date fecha_nac, Date fecha_reg, String genero, String correo) {
        super(cedula, nombre, apellido, direccion, telefono, fecha_nac, fecha_reg, genero, correo);
        this.descuento = descuento;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return "Cliente{" + "descuento=" + descuento + '}';
    }

    
}
