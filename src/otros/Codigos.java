package otros;

import com.db4o.ObjectSet;
import base_datos.base;


public class Codigos {
 private String clase;
 private int codigo;

    public Codigos() {
    }
    
    public Codigos(String clase, int codigo) {
        this.clase = clase;
        this.codigo = codigo;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    
    public static int obtener_codigo(String clase){
        Codigos codigo = new Codigos(clase,0);
        ObjectSet resultado = base.gettear(codigo);
        if (resultado.isEmpty()) {
            codigo.setCodigo(1);
        } else{
            codigo = (Codigos)resultado.next();
            codigo.setCodigo(codigo.getCodigo()+1);
        }
        base.settear(codigo);
        return codigo.getCodigo();
    }
    
    public static void reiniciar_codigo(String clase){
        Codigos cod = new Codigos(clase,0);
        ObjectSet resultado = base.gettear(cod);
        if (!resultado.isEmpty()) {
            cod = (Codigos)resultado.next();
            base.eliminar(cod);
        }
    }

    
    
    
 
 
}
