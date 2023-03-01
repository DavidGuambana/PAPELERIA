package papeleria;

import com.db4o.*;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import javax.swing.JOptionPane;
import java.awt.Toolkit;

public class base {

    public static ObjectContainer bd;

    public static void abrir() {
        try {
            bd = Db4o.openFile("C:\\Users\\ariel\\OneDrive\\Desktop\\PAPELERIA AVANZADA\\Papeleria.yap");
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
    
    public static void eliminar(Object obj){
        bd.delete(obj);
    }

}
