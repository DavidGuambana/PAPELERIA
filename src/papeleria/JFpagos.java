package papeleria;

import clases.Pago_prov;
import com.db4o.ObjectSet;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import javax.swing.JOptionPane;
//import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import otros.Codigos;
import otros.fechas;
import otros.validar;
import base_datos.base;


public class JFpagos extends javax.swing.JFrame {
    
    public static String modo_prov; 
    public static ObjectSet resultado;
    public static String url;
    public static double xprecio;
    public static String forma;
    public JFpagos() {
        initComponents();
        setLocationRelativeTo(null);

        
    }

    public static void cambiar_diseño() {
        String titulo = "";
        Color color = null;
        Font font = new Font("Yu Gothic UI Light", 0, 14);
        TitledBorder tb;
        if (forma.equals("registrar")) {
            color = new Color(0,204,102);
            codigo.setVisible(false);
            jlCodigo.setVisible(false);
            jl_titulo.setText("Registrar pagos a proveedores");
            jb_Ejecutar.setText("¡Registrar!");

        } else if(forma.equals("modificar")){
            color = new Color(0, 153, 255);
            codigo.setVisible(true);
            jlCodigo.setVisible(true);
            jl_titulo.setText("Modificar modificar pago a proveedor");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 6; i++) {
            switch (i) {
                case 1:
                    titulo = "Proveedor:";
                    break;
                case 2:
                    titulo = "Empleado:";
                    break;
                case 3:
                    titulo = "Precio:";
                    break;
                case 4:
                    titulo = "Descripcion:";
                    break;
                case 5:
                    titulo = "Código:";
                    break;
                
            }
            tb = new TitledBorder(titulo);
            tb.setTitleJustification(0);
            tb.setTitlePosition(1);
            tb.setTitleColor(color);
            tb.setTitleFont(font);
            switch (i) {
                case 1:
                    jt_proveedor.setBorder(tb);
                    break;
                case 2:
                    jt_empleado.setBorder(tb);
                    break;
                case 3:
                    jt_precio.setBorder(tb);
                    break;
                case 4:
                    jtDescripcion_pago.setBorder(tb);
                    break;
                case 5:
                    jlCodigo.setBorder(tb);
                    break;
                case 6:
                    
                    break;
            }
        }
    }

    public static void limpiar() {
        jt_precio.setText("");
        jt_proveedor.setText("¡Click para seleccionar!");
        jt_empleado.setText("¡Click para seleccionar!");
        jtDescripcion_pago.setText("");
    }

    public void llenar(int cod) {
        base.abrir();
        Pago_prov p = new Pago_prov(cod,0, null, null, null,null,null);
        resultado = base.gettear(p);
        if (resultado.isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Registro no existente!", null, JOptionPane.ERROR_MESSAGE);
        } else {
            codigo.setText(String.valueOf(p.getCodigo()));
            p = (Pago_prov) resultado.next();
            jt_proveedor.setText(p.getRUC_prov());
            jt_empleado.setText(p.getCedula_emp());
            jt_precio.setText(String.valueOf(p.getValor()));
            jtDescripcion_pago.setText(p.getDescripcion());
         
            this.setVisible(true);
        }
        base.cerrar();
    }

    public void registrar() {
        base.abrir();
        Pago_prov p = new Pago_prov(Codigos.obtener_codigo("Pago_prov"),Double.parseDouble(jt_precio.getText()),jt_empleado.getText(),jt_proveedor.getText()
        ,jtDescripcion_pago.getText(),fechas.obtener_fecha(),"ACTIVO");       
        base.settear(p);
        JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
        SISTEMA.actualizado = false;
        this.dispose();
        base.cerrar();
    }

    public void modificar() {
        base.abrir();
        Pago_prov p = new Pago_prov(0,0,null,null,null,null,null);     
        resultado = base.gettear(p);
        if (!resultado.isEmpty()) {
            p = (Pago_prov) resultado.next();
            p.setCodigo(Integer.parseInt(codigo.getText()));
            p.setRUC_prov(jt_proveedor.getText());
            p.setCedula_emp(jt_proveedor.getText());
            p.setValor(Double.parseDouble(jt_precio.getText()));
            p.setRUC_prov(jt_proveedor.getText());
            p.setDescripcion(jtDescripcion_pago.getText());
            base.settear(p);
            JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
        }
        base.cerrar();
        SISTEMA.actualizado = false;
        this.dispose();
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jp_1 = new javax.swing.JPanel();
        jl_cerrar = new javax.swing.JLabel();
        jl_titulo = new javax.swing.JLabel();
        jp_2 = new javax.swing.JPanel();
        jt_proveedor = new javax.swing.JTextField();
        jb_Ejecutar = new javax.swing.JButton();
        jlCodigo = new javax.swing.JLabel();
        codigo = new javax.swing.JLabel();
        jt_precio = new javax.swing.JTextField();
        jt_empleado = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDescripcion_pago = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jp_1.setBackground(new java.awt.Color(0, 204, 102));
        jp_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jp_1.setPreferredSize(new java.awt.Dimension(560, 40));

        jl_cerrar.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        jl_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        jl_cerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_cerrar.setText("×");
        jl_cerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jl_cerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jl_cerrarMouseClicked(evt);
            }
        });

        jl_titulo.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jl_titulo.setForeground(new java.awt.Color(255, 255, 255));
        jl_titulo.setText("Registrar pago a proveedor");

        javax.swing.GroupLayout jp_1Layout = new javax.swing.GroupLayout(jp_1);
        jp_1.setLayout(jp_1Layout);
        jp_1Layout.setHorizontalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jl_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jp_1Layout.setVerticalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jl_cerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)))
        );

        jp_2.setBackground(new java.awt.Color(255, 255, 255));
        jp_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jt_proveedor.setEditable(false);
        jt_proveedor.setBackground(new java.awt.Color(255, 255, 255));
        jt_proveedor.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jt_proveedor.setForeground(new java.awt.Color(0, 153, 153));
        jt_proveedor.setText("¡Click para seleccionar!");
        jt_proveedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Proveedor:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_proveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_proveedorMouseClicked(evt);
            }
        });
        jt_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_proveedorActionPerformed(evt);
            }
        });

        jb_Ejecutar.setBackground(new java.awt.Color(0, 204, 102));
        jb_Ejecutar.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        jb_Ejecutar.setForeground(new java.awt.Color(255, 255, 255));
        jb_Ejecutar.setText("¡Registrar!");
        jb_Ejecutar.setBorder(null);
        jb_Ejecutar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_EjecutarActionPerformed(evt);
            }
        });

        jlCodigo.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlCodigo.setForeground(new java.awt.Color(0, 204, 102));
        jlCodigo.setText("Código:");

        codigo.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        codigo.setText("000");

        jt_precio.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_precio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Precio:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_precioActionPerformed(evt);
            }
        });
        jt_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_precioKeyPressed(evt);
            }
        });

        jt_empleado.setEditable(false);
        jt_empleado.setBackground(new java.awt.Color(255, 255, 255));
        jt_empleado.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jt_empleado.setForeground(new java.awt.Color(0, 153, 153));
        jt_empleado.setText("¡Click para seleccionar!");
        jt_empleado.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Empleado:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_empleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_empleadoMouseClicked(evt);
            }
        });
        jt_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_empleadoActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        jtDescripcion_pago.setColumns(20);
        jtDescripcion_pago.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtDescripcion_pago.setLineWrap(true);
        jtDescripcion_pago.setRows(5);
        jtDescripcion_pago.setWrapStyleWord(true);
        jtDescripcion_pago.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descripción:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jtDescripcion_pago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtDescripcion_pagoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtDescripcion_pago);

        javax.swing.GroupLayout jp_2Layout = new javax.swing.GroupLayout(jp_2);
        jp_2.setLayout(jp_2Layout);
        jp_2Layout.setHorizontalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jt_precio)
                            .addComponent(jt_proveedor)
                            .addComponent(jt_empleado)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jp_2Layout.createSequentialGroup()
                                .addComponent(jlCodigo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(22, 22, 22))
        );
        jp_2Layout.setVerticalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jt_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jt_empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jt_precio)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codigo))
                .addGap(27, 27, 27)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
            .addComponent(jp_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jp_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
        try {
            xprecio = Double.parseDouble(jt_precio.getText());
        } catch (NumberFormatException e) {
            xprecio = 0;
        }
        if ( jt_precio.getText().equals("") || jtDescripcion_pago.getText().equals("")
                ||jt_empleado.getText().equals("¡Click para seleccionar!")||jt_proveedor.getText().equals("¡Click para seleccionar!")) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
        } else {
            if (xprecio > 0) {
                if (forma.equals("registrar")) {
                    registrar();
                } else if(forma.equals("modificar")){
                    modificar();
                }
                this.dispose();
            } else {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡Precio inválido!");
                jt_precio.setText("");
            }
        }
        
    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void jt_proveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_proveedorMouseClicked
        SISTEMA.MENU.setSelectedIndex(7);
        this.setVisible(false);
        
        
    }//GEN-LAST:event_jt_proveedorMouseClicked

    private void jt_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_proveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_proveedorActionPerformed

    private void jt_precioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_precioKeyPressed
        validar.dinero(jt_precio,6);
    }//GEN-LAST:event_jt_precioKeyPressed

    private void jt_empleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_empleadoMouseClicked
        // TODO add your handling code here:
         SISTEMA.MENU.setSelectedIndex(5);
        this.setVisible(false);
    }//GEN-LAST:event_jt_empleadoMouseClicked

    private void jt_empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_empleadoActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jt_empleadoActionPerformed

    private void jtDescripcion_pagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtDescripcion_pagoKeyPressed
        validar.V_desc(jtDescripcion_pago, 50);
    }//GEN-LAST:event_jtDescripcion_pagoKeyPressed

    private void jt_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_precioActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFpagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFpagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFpagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFpagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFpagos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel codigo;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JButton jb_Ejecutar;
    private static javax.swing.JLabel jlCodigo;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    public static javax.swing.JTextArea jtDescripcion_pago;
    public static javax.swing.JTextField jt_empleado;
    public static javax.swing.JTextField jt_precio;
    public static javax.swing.JTextField jt_proveedor;
    // End of variables declaration//GEN-END:variables
}
