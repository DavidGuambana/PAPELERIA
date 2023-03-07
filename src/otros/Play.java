package otros;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import papeleria.JFlogin;
import papeleria.SISTEMA;

public class Play {

    public static void main(String[] args) throws ClassNotFoundException {
        try {
                        UIManager.setLookAndFeel(new NimbusLookAndFeel());
                    } catch (UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
                    }

        Splashz loading = new Splashz();
        loading.setVisible(true);

        try {
            for (int i = 0; i <= 100; i++) {

                Thread.sleep(10);
                Splashz.jLabel1.setText(i + "%");
                Splashz.jProgressBar.setValue(i);
                if (i == 100) {
                    
                    JFlogin sesión = new JFlogin();
                    sesión.setVisible(true);
                    loading.dispose();
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        } catch (Exception e) {
        }

    }

}
