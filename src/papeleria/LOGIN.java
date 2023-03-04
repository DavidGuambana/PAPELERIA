/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package papeleria;

import javax.swing.JOptionPane;

/**
 *
 * @author FRANKLIN
 */
public class LOGIN extends javax.swing.JFrame {

    
    public LOGIN() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelbackround = new javax.swing.JPanel();
        panelesquina = new javax.swing.JPanel();
        labeltitulo = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        usuario = new javax.swing.JLabel();
        login = new javax.swing.JLabel();
        txtcedempleado = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        usuario1 = new javax.swing.JLabel();
        passwordfield = new javax.swing.JPasswordField();
        panelbtnsalir = new javax.swing.JPanel();
        labelbtnsalir = new javax.swing.JLabel();
        panelbtnlog = new javax.swing.JPanel();
        labelbtnlogin = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelbackround.setBackground(new java.awt.Color(255, 255, 255));
        panelbackround.setMinimumSize(new java.awt.Dimension(750, 600));
        panelbackround.setPreferredSize(new java.awt.Dimension(750, 600));
        panelbackround.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelesquina.setBackground(new java.awt.Color(0, 204, 204));

        labeltitulo.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        labeltitulo.setForeground(new java.awt.Color(255, 255, 255));
        labeltitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeltitulo.setText("PAPELERIA");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/papeleria.png"))); // NOI18N

        javax.swing.GroupLayout panelesquinaLayout = new javax.swing.GroupLayout(panelesquina);
        panelesquina.setLayout(panelesquinaLayout);
        panelesquinaLayout.setHorizontalGroup(
            panelesquinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelesquinaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelesquinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labeltitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logo))
                .addGap(41, 41, 41))
            .addGroup(panelesquinaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelesquinaLayout.setVerticalGroup(
            panelesquinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelesquinaLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(logo)
                .addGap(18, 18, 18)
                .addComponent(labeltitulo)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(133, Short.MAX_VALUE))
        );

        panelbackround.add(panelesquina, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 300, 500));

        usuario.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        usuario.setText("USUARIO:");
        panelbackround.add(usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 20));

        login.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        login.setText("INICIAR SESIÓN");
        panelbackround.add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        txtcedempleado.setForeground(new java.awt.Color(153, 153, 153));
        txtcedempleado.setBorder(null);
        txtcedempleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcedempleadoActionPerformed(evt);
            }
        });
        txtcedempleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcedempleadoKeyTyped(evt);
            }
        });
        panelbackround.add(txtcedempleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 400, 40));
        panelbackround.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 400, 30));

        usuario1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        usuario1.setText("CONTRASEÑA:");
        panelbackround.add(usuario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, 20));

        passwordfield.setBorder(null);
        passwordfield.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passwordfieldKeyTyped(evt);
            }
        });
        panelbackround.add(passwordfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 400, 40));

        panelbtnsalir.setBackground(new java.awt.Color(0, 204, 204));

        labelbtnsalir.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        labelbtnsalir.setForeground(new java.awt.Color(255, 255, 255));
        labelbtnsalir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelbtnsalir.setText("REGISTRARSE");
        labelbtnsalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelbtnsalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelbtnsalirMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelbtnsalirLayout = new javax.swing.GroupLayout(panelbtnsalir);
        panelbtnsalir.setLayout(panelbtnsalirLayout);
        panelbtnsalirLayout.setHorizontalGroup(
            panelbtnsalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbtnsalirLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelbtnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelbtnsalirLayout.setVerticalGroup(
            panelbtnsalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbtnsalirLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelbtnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelbackround.add(panelbtnsalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 320, 130, 40));

        panelbtnlog.setBackground(new java.awt.Color(0, 204, 204));

        labelbtnlogin.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        labelbtnlogin.setForeground(new java.awt.Color(255, 255, 255));
        labelbtnlogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelbtnlogin.setText("ENTRAR");
        labelbtnlogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelbtnlogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelbtnloginMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelbtnlogLayout = new javax.swing.GroupLayout(panelbtnlog);
        panelbtnlog.setLayout(panelbtnlogLayout);
        panelbtnlogLayout.setHorizontalGroup(
            panelbtnlogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbtnlogLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelbtnlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelbtnlogLayout.setVerticalGroup(
            panelbtnlogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbtnlogLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelbtnlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelbackround.add(panelbtnlog, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, -1, -1));
        panelbackround.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 400, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelbackround, javax.swing.GroupLayout.PREFERRED_SIZE, 772, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelbackround, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtcedempleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcedempleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcedempleadoActionPerformed

    private void labelbtnsalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelbtnsalirMouseClicked
        JFempleado.forma = "registrar";
        JFempleado emple = new JFempleado();
        emple.setVisible(true);
    }//GEN-LAST:event_labelbtnsalirMouseClicked

    private void labelbtnloginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelbtnloginMouseClicked
        String ced = txtcedempleado.getText().trim();
        String pwd;
        pwd = passwordfield.getText();
        if (Check_login(ced) == true) {
            if (pwd.equals("1234"))  {
                
                SISTEMA sistema = new SISTEMA();
                sistema.setVisible(true);
                this.setVisible(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "USUARIO O CONTRASEÑA NO VÁLIDOS");
        }
    }//GEN-LAST:event_labelbtnloginMouseClicked

    private void txtcedempleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcedempleadoKeyTyped
        // TODO add your handling code here:
        char validar = evt.getKeyChar();
        if (Character.isDigit(validar)) {
            if (txtcedempleado.getText().length() >= 10) {
                evt.consume();
            }
        } else {
            evt.consume();
        }
    }//GEN-LAST:event_txtcedempleadoKeyTyped

    private void passwordfieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordfieldKeyTyped
        // TODO add your handling code here:
        char validar = evt.getKeyChar();
        if (Character.isDigit(validar)) {
            if (passwordfield.getText().length() >= 6) {
                evt.consume();
            }
        } else {
            evt.consume();
        }
    }//GEN-LAST:event_passwordfieldKeyTyped

    public boolean Check_login(String ced) {
        base base = new base();
        boolean validar = false;
        try {
            if (base.Buscar_Emple(ced)) {
                validar = true;
            }
        } catch (Exception e) {
        }
        return validar;
    }

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LOGIN().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelbtnlogin;
    private javax.swing.JLabel labelbtnsalir;
    private javax.swing.JLabel labeltitulo;
    private javax.swing.JLabel login;
    private javax.swing.JLabel logo;
    private javax.swing.JPanel panelbackround;
    private javax.swing.JPanel panelbtnlog;
    private javax.swing.JPanel panelbtnsalir;
    private javax.swing.JPanel panelesquina;
    private javax.swing.JPasswordField passwordfield;
    private javax.swing.JTextField txtcedempleado;
    private javax.swing.JLabel usuario;
    private javax.swing.JLabel usuario1;
    // End of variables declaration//GEN-END:variables
}