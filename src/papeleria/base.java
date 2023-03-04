package papeleria;

import com.db4o.*;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import clases.Empleado;

public class base {

    public static ObjectContainer bd;

    public static void abrir() {
        try {
            bd = Db4o.openFile("C:\\Users\\FRANKLIN\\Downloads\\Papeleria\\BASE PAPELERIA\\Papeleria.yap");
        } catch (DatabaseFileLockedException | DatabaseReadOnlyException | Db4oIOException | IncompatibleFileFormatException | OldFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Error al abrir la base de datos!", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void cerrar() {
        try {
            bd.close();
        } catch (Db4oIOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Error al cerrar la base de datos!", null, JOptionPane.ERROR_MESSAGE);
        }

    }

    public static ObjectSet gettear(Object obj) {
        ObjectSet result = bd.get(obj);
        return result;
    }

    public static void settear(Object obj) {
        bd.set(obj);
    }

    public static void eliminar(Object obj) {
        bd.delete(obj);
    }

    private void open() {
        try {
            this.bd = Db4o.openFile("C:\\Users\\FRANKLIN\\Downloads\\Papeleria\\BASE PAPELERIA\\Papeleria.yap");
        } catch (DatabaseFileLockedException | DatabaseReadOnlyException | Db4oIOException | IncompatibleFileFormatException | OldFormatException e) {
            System.out.println(e);
        }
    }

    public boolean Buscar_Emple(String ced) {
        this.open();
        boolean valido = false;
        double Ced = Double.parseDouble(ced);
        Empleado encontrado = new Empleado(Ced);
        ObjectSet resultados = this.bd.get(encontrado);
        if (!resultados.isEmpty()) {
            encontrado = (Empleado) resultados.next();
            if (encontrado.getCedula().equalsIgnoreCase(ced)) {
                valido = true;
            }
        }
        this.bd.close();
        return valido;
    }

}
