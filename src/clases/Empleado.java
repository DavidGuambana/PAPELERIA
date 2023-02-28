package clases;

import java.util.Date;

public class Empleado extends PERSONA {
    private double sueldo;
    
    public Empleado() {
    }

    public Empleado(double sueldo, String cedula, String nombre, String apellido, String direccion, String telefono, Date fecha_nac, Date fecha_reg, String genero, String correo) {
        super(cedula, nombre, apellido, direccion, telefono, fecha_nac, fecha_reg, genero, correo);
        this.sueldo = sueldo;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public String toString() {
        return "Empleado{" + "sueldo=" + sueldo + '}';
    }
}
