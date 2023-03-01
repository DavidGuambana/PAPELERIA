package papeleria;

import clases.Categoria;
import clases.Ciudad;
import clases.Cliente;
import clases.Descuento;
import clases.Detalle_fac;
import clases.Empleado;
import clases.Encabezado_fac;
import clases.Producto;
import clases.Proveedor;
import com.db4o.ObjectSet;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import otros.BotonTabla;
import otros.Codigos;
import otros.ImagenTabla;
import otros.fechas;

public class SISTEMA extends javax.swing.JFrame implements Runnable {

    //variables que guardan el número de registros:
    public static int cat, ciu, cli, des, det, emp, enc, gas, pap, pro, prov;

    //otras variables útiles:
    public static boolean actualizado = false;
    ObjectSet resultado;
    ObjectSet resultado2;
    DefaultTableModel tabla = null, tabla_detalle = null;
    TableRowSorter sorter;
    
    ArrayList<String> detalles = new ArrayList<>();
    JButton boton1 = new JButton();

    String hora, minutos, segundos;
    Thread hilo;
    
    //variables para factura:
    public static int descuento = 0 ,total = 0, num_det = 0;
    
    //instancias de los frames:
    public static JFcategoria JFcat = new JFcategoria();//fr1
    public static JFciudad JFciu = new JFciudad();//fr2
    public static JFcliente JFcli = new JFcliente(); //fr3
    public static JFdescuento JFdes = new JFdescuento();//fr4
    public static JFempleado JFemp = new JFempleado();//fr6

    public static JFproducto JFpro = new JFproducto();//fr9
    public static JFproveedor JFprov = new JFproveedor(); //fr10

    public SISTEMA() {
        initComponents();
        setLocationRelativeTo(null);
        iniciar();
        //setExtendedState(MAXIMIZED_BOTH);
    }

    public final void iniciar() {
        
        //para la fecha y hora
        hilo = new Thread(this);
        hilo.start();
        //para seleccionar filas de las tablas
        seleccionar();
        //para icono en el boton y modelo en tabla JTdetalle
        InsertarIcono(boton1, "/imagenes/elim.png");
        tabla_detalle = new DefaultTableModel(null, new Object[]{"Codigo de producto", "Descipción", "Cantidad", "Precio unitario",
            "Descuento", "Precio con descuento", "valor total", "Aciones"});
        JTdetalle.setModel(tabla_detalle);
        //ocultar o bloquear componentes
        lim_cat.setVisible(false);
        lim_ciu.setVisible(false);
        lim_cli.setVisible(false);
        lim_des.setVisible(false);
        //lim_det.setVisible(false);
        lim_emp.setVisible(false);
        //lim_enc.setVisible(false);
        //lim_gas.setVisible(false);
        lim_pro.setVisible(false);
        lim_prov.setVisible(false);
        
    }

    //método para cargar los datos en las tablas:
    public void visualizar() {
        base.abrir();
        for (int i = 1; i <= 10; i++) {
            switch (i) {
                case 1:
                    String[] colum_cat = {"Nombre", "Descipción"};
                    tabla = new DefaultTableModel(null, colum_cat);
                    Categoria cate = new Categoria(null, null);
                    resultado = base.gettear(cate);
                    for (int j = 0; j < resultado.size(); j++) {
                        cate = (Categoria) resultado.next();
                        tabla.addRow(new Object[]{cate.getNombre(), cate.getDescripcion()});
                    }
                    JTcategorias.setModel(tabla);
                    cat = resultado.size();
                    v1.setText(String.valueOf(cat));
                    res_num_cat.setText("Resultados: " + cat + " de " + cat);
                    break;
                case 2://ciudades
                    String[] colum_ciud = {"Código", "Nombre", "Provincia"};
                    tabla = new DefaultTableModel(null, colum_ciud);
                    Ciudad c = new Ciudad(0, null, null);
                    resultado = base.gettear(c);
                    if (!resultado.isEmpty()) {
                        for (int j = 0; j < resultado.size(); j++) {
                            c = (Ciudad) resultado.next();
                            tabla.addRow(new Object[]{c.getCodigo(), c.getNombre(), c.getProvincia()});
                        }
                    } else {
                        Codigos.reiniciar_codigo("Ciudad");
                    }
                    JTciudades.setModel(tabla);
                    ciu = resultado.size();
                    v2.setText(String.valueOf(ciu));
                    res_num_ciu.setText("Resultados: " + ciu + " de " + ciu);
                    break;
                case 3://clientes
                    String[] colum_cli = {"Cédula", "Nombre", "Apellido", "F. Nacimiento", "Género", "Teléfono", "Correo", "Dirección", "T. Descuento", "F. Registro"};
                    tabla = new DefaultTableModel(null, colum_cli);
                    Cliente cl = new Cliente(null, null, null, null, null, null, null, null, null, null);
                    resultado = base.gettear(cl);
                    for (int j = 0; j < resultado.size(); j++) {
                        cl = (Cliente) resultado.next();
                        tabla.addRow(new Object[]{cl.getCedula(), cl.getNombre(), cl.getApellido(), fechas.transformar_fecha(cl.getFecha_nac()), cl.getGenero(),
                            cl.getTelefono(), cl.getCorreo(), cl.getDireccion(), cl.getDescuento(), fechas.transformar_fecha(cl.getFecha_reg())});
                    }
                    JTclientes.setModel(tabla);
                    cli = resultado.size();
                    v3.setText(String.valueOf(cli));
                    res_num_cli.setText("Resultados: " + cli + " de " + cli);

                    break;
                case 4://descuentos
                    String[] colum_des = {"Nombre", "Porcentaje (%)"};
                    tabla = new DefaultTableModel(null, colum_des);
                    Descuento d = new Descuento(null, 0);
                    resultado = base.gettear(d);
                    for (int j = 0; j < resultado.size(); j++) {
                        d = (Descuento) resultado.next();
                        tabla.addRow(new Object[]{d.getNombre(), d.getPorcentaje()});
                    }
                    JTdescuentos.setModel(tabla);
                    des = resultado.size();
                    v4.setText(String.valueOf(des));
                    res_num_des.setText("Resultados: " + des + " de " + des);
                    break;
                case 5:
                    break;
                case 6: //empleados
                    String[] colum_emp = {"Cédula", "Nombre", "Apellido", "F. Nacimiento", "Género", "Teléfono", "Correo", "Dirección", "Sueldo", "F. Registro"};
                    tabla = new DefaultTableModel(null, colum_emp);
                    Empleado em = new Empleado(0, null, null, null, null, null, null, null, null, null);
                    resultado = base.gettear(em);
                    for (int j = 0; j < resultado.size(); j++) {
                        em = (Empleado) resultado.next();
                        tabla.addRow(new Object[]{em.getCedula(), em.getNombre(), em.getApellido(), fechas.transformar_fecha(em.getFecha_nac()), em.getGenero(),
                            em.getTelefono(), em.getCorreo(), em.getDireccion(), em.getSueldo(), fechas.transformar_fecha(em.getFecha_reg())});
                    }
                    JTempleados.setModel(tabla);
                    emp = resultado.size();
                    v6.setText(String.valueOf(emp));
                    res_num_emp.setText("Resultados: " + emp + " de " + emp);
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9://productos
                    JTproductos.setDefaultRenderer(Object.class, new ImagenTabla());
                    String[] colum_pro = {"Código", "Nombre", "Precio", "Existencias", "Categoría", "Proveedor", "F. Registro", "Imagen"};
                    tabla = new DefaultTableModel(null, colum_pro);
                    Producto pr = new Producto(0, null, 0, 0, null, null, null, null);
                    resultado = base.gettear(pr);
                    if (!resultado.isEmpty()) {
                        for (int j = 0; j < resultado.size(); j++) {
                            pr = (Producto) resultado.next();
                            JLabel lb = new JLabel();
                            lb.setSize(125, 60);
                            rsscalelabel.RSScaleLabel.setScaleLabel(lb, pr.getUrl_imagen());
                            tabla.addRow(new Object[]{pr.getCodigo(), pr.getNombre(), pr.getPrecio(), pr.getExistencias(), pr.getNombre_cat(), pr.getRUC_prov(), fechas.transformar_fecha(pr.getFecha_reg()), lb});
                        }
                    } else {
                        Codigos.reiniciar_codigo("Producto");
                    }
                    JTproductos.setModel(tabla);
                    pro = resultado.size();
                    v9.setText(String.valueOf(pro));
                    res_num_pro.setText("Resultados: " + pro + " de " + pro);
                    break;
                case 10: //proveedores
                    String[] colum_prov = {"RUC", "Nombre", "Ciudad", "Teléfono", "Correo", "F. Registro"};
                    tabla = new DefaultTableModel(null, colum_prov);
                    Proveedor p = new Proveedor(null, null, null, null, null, null);
                    resultado = base.gettear(p);
                    for (int j = 0; j < resultado.size(); j++) {
                        p = (Proveedor) resultado.next();
                        tabla.addRow(new Object[]{p.getRUC(), p.getNombre_empresa(), p.getCodigo_ciu(), p.getTelefono(), p.getCorreo(), fechas.transformar_fecha(p.getFecha_reg())});
                    }
                    JTproveedores.setModel(tabla);
                    prov = resultado.size();
                    v10.setText(String.valueOf(prov));
                    res_num_prov.setText("Resultados: " + prov + " de " + prov);
                    break;

            }
        }
        base.cerrar();
    }

    //método para buscar registros de cualquier tabla:
    public void buscar(JTable tab, JTextField tex, JLabel a, int b, JComboBox c) {
        DefaultTableModel modelo = (DefaultTableModel) tab.getModel();
        sorter = new TableRowSorter<>(modelo);
        tab.setAutoCreateRowSorter(true);
        tab.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(tex.getText(), c.getSelectedIndex()));
        a.setText("Resultados: " + String.valueOf(tab.getRowCount()) + " de " + b);
    }

    //método para eliminar registros de la base de datos:
    public void eliminar(int clase) {
        if (JOptionPane.showConfirmDialog(this, "¡Está seguro de que desea eliminar este registro de forma permanente?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            boolean eliminado = true;
            try {
                base.abrir();
                switch (clase) {
                    case 1: //eliminar categoria
                        Categoria cate = new Categoria(jlNombre_cat.getText(), null);
                        resultado = base.gettear(cate);
                        Producto prod = new Producto(0, null, 0, 0, jlNombre_cat.getText(), null, null, null);
                        resultado2 = base.gettear(prod);
                        if (resultado2.size() == 0) {
                            cate = (Categoria) resultado.next();
                            base.eliminar(cate); 
                            limpiar(1);
                        } else {
                            JOptionPane.showMessageDialog(null, "!No es posible eliminar el registro ya que cuenta con un producto asignado!");
                            eliminado = false;
                        }

                        break;
                    case 2: //eliminar ciudad
                        Ciudad c = new Ciudad(Integer.parseInt(jlCodigo_ciu.getText()), null, null);
                        resultado = base.gettear(c);
                        Proveedor prov=new Proveedor(null,null,jlCodigo_ciu.getText(),null,null,null);
                        resultado2 = base.gettear(prov);
                        if (resultado2.size() == 0) {
                            
                        c = (Ciudad) resultado.next();
                        base.eliminar(c);   
                        }else{
                        JOptionPane.showMessageDialog(null, "!No es posible eliminar el registro ya que cuenta con un Proveedor asignado!");
                        eliminado = false;   
                        }
                       
                        break;
                    case 3: //eliminar cliente
                        Cliente cl = new Cliente(null, jlCedula_cli.getText(), null, null, null, null, null, null, null, null);
                        resultado = base.gettear(cl);         
                        Encabezado_fac fact =new Encabezado_fac(0,jlCedula_cli.getText(),null,0,null);
                        resultado2 = base.gettear(fact);
                        if (resultado2.size()==0) {
                        cl = (Cliente) resultado.next();
                        base.eliminar(cl);   
                        }else{
                        JOptionPane.showMessageDialog(null, "!No es posible eliminar el registro ya que cuenta con un Factura asignado!");
                        eliminado = false;         
                        }                       
                        break;
                    case 4: //eliminar descuento
                        Descuento d = new Descuento(jlNombre_des.getText(), 0);
                        resultado = base.gettear(d);
                        Cliente cli2 = new Cliente(jlNombre_des.getText(), null, null, null, null, null, null, null, null, null);
                        resultado2 = base.gettear(cli2);
                        if (resultado2.size() == 0) {
                         d = (Descuento) resultado.next();
                        base.eliminar(d);                     
                        }else{
                        JOptionPane.showMessageDialog(null, "!No es posible eliminar el registro ya que cuenta con un Cliente asignado!");
                        eliminado = false;      
                        }
                      
                        
                        
                        break;
                    case 6: //eliminar empleado
                        Empleado e = new Empleado(0, jlCedula_emp.getText(), null, null, null, null, null, null, null, null);
                        resultado = base.gettear(e);
                        e = (Empleado) resultado.next();
                        base.eliminar(e);
                        break;
                    case 8: //eliminar gasto
                        break;
                    case 9: //eliminar producto
                        Producto pr = new Producto(Integer.parseInt(jlCodigo_pro.getText()), null, 0, 0, null, null, null, null);
                        resultado = base.gettear(pr);
                        Detalle_fac enca=new Detalle_fac(0,Integer.parseInt(jlCodigo_pro.getText()),0,0); 
                        resultado2 = base.gettear(enca);
                        if (resultado2.size()==0) {
                        pr = (Producto) resultado.next();
                        base.eliminar(pr);     
                        }else{
                        JOptionPane.showMessageDialog(null, "!No es posible eliminar el registro ya que se encuantra en una factura!");
                        eliminado = false;     
                        }
                       
                        
                        break;
                    case 10: //eliminar proveedor
                        Proveedor p = new Proveedor(jlRUC.getText(), null, null, null, null, null);
                        resultado = base.gettear(p); 
                        Producto pr2 = new Producto(0, null, 0, 0, null, null, jlRUC.getText(), null);
                        resultado2= base.gettear(pr2);
                        if (resultado2.size()==0) {
                        p = (Proveedor) resultado.next();
                        base.eliminar(p);   
                        }else{
                        JOptionPane.showMessageDialog(null, "!No es posible eliminar el registro ya que cuenta con un Producto asignado!");
                        eliminado = false;        
                        }
                       
                        break;

                }
            } catch (Exception e) {
                eliminado = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡Registro no existente!", null, JOptionPane.ERROR_MESSAGE);
            } finally {
                base.cerrar();
                if (eliminado) {
                    JOptionPane.showMessageDialog(null, "¡Eliminado correctamente!");
                }
                visualizar();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jl_dinero2 = new javax.swing.JLabel();
        v_dinero2 = new javax.swing.JLabel();
        v_dinero4 = new javax.swing.JLabel();
        jl_dinero4 = new javax.swing.JLabel();
        jlE3 = new javax.swing.JLabel();
        jlEmail3 = new javax.swing.JLabel();
        JPencabezado2 = new javax.swing.JPanel();
        v10 = new javax.swing.JLabel();
        v3 = new javax.swing.JLabel();
        v6 = new javax.swing.JLabel();
        v9 = new javax.swing.JLabel();
        v1 = new javax.swing.JLabel();
        v4 = new javax.swing.JLabel();
        v2 = new javax.swing.JLabel();
        V0 = new javax.swing.JLabel();
        MENU = new javax.swing.JTabbedPane();
        INICIO = new javax.swing.JTabbedPane();
        jScrollPane = new javax.swing.JScrollPane();
        JPfactura = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        enc_fecha = new javax.swing.JLabel();
        enc_codigo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        enc_cedula = new javax.swing.JTextField();
        enc_nombre_apellido = new javax.swing.JTextField();
        enc_direccion = new javax.swing.JTextField();
        enc_telefono = new javax.swing.JTextField();
        enc_correo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jsTabla_ciu1 = new javax.swing.JScrollPane();
        JTdetalle = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jl_num_det = new javax.swing.JLabel();
        jl_total = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jl_num_det1 = new javax.swing.JLabel();
        jl_total1 = new javax.swing.JLabel();
        jl_num_det2 = new javax.swing.JLabel();
        jl_total2 = new javax.swing.JLabel();
        jl_num_det3 = new javax.swing.JLabel();
        jl_num_det4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPanew = new javax.swing.JScrollPane();
        JPventas = new javax.swing.JPanel();
        jl_titulo6 = new javax.swing.JLabel();
        JPgastos = new javax.swing.JPanel();
        JPcategorias = new javax.swing.JPanel();
        jl_titulo5 = new javax.swing.JLabel();
        jpDatos_cat = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jlR4 = new javax.swing.JLabel();
        jlN4 = new javax.swing.JLabel();
        jlNombre_cat = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaDescripcion_cat = new javax.swing.JTextArea();
        jbEnviar_cat = new javax.swing.JButton();
        jbEliminar_cat = new javax.swing.JButton();
        jbModificar_cat = new javax.swing.JButton();
        jbRegistrar_cat = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JSeparator();
        jcBuscar_cat = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        jtBuscar_cat = new javax.swing.JTextField();
        lim_cat = new javax.swing.JLabel();
        jsTabla_cat = new javax.swing.JScrollPane();
        JTcategorias = new javax.swing.JTable();
        res_num_cat = new javax.swing.JLabel();
        JPciudades = new javax.swing.JPanel();
        res_num_ciu = new javax.swing.JLabel();
        jcBuscar_ciu = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jtBuscar_ciu = new javax.swing.JTextField();
        lim_ciu = new javax.swing.JLabel();
        jsTabla_ciu = new javax.swing.JScrollPane();
        JTciudades = new javax.swing.JTable();
        jl_titulo7 = new javax.swing.JLabel();
        jpDatos_cat1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jlR1 = new javax.swing.JLabel();
        jlN1 = new javax.swing.JLabel();
        jlC1 = new javax.swing.JLabel();
        jlCodigo_ciu = new javax.swing.JLabel();
        jlNombre_ciu = new javax.swing.JLabel();
        jlProvincia_ciu = new javax.swing.JLabel();
        jbRegistrar_ciu = new javax.swing.JButton();
        jbEnviar_ciu = new javax.swing.JButton();
        jbEliminar_ciu = new javax.swing.JButton();
        jbModificar_ciu = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JSeparator();
        JPclientes = new javax.swing.JPanel();
        res_num_cli = new javax.swing.JLabel();
        jpDatos_cli = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jlR2 = new javax.swing.JLabel();
        jbEnviar_cli = new javax.swing.JButton();
        jb_Eliminar_cli = new javax.swing.JButton();
        jbModificar_cli = new javax.swing.JButton();
        jlN2 = new javax.swing.JLabel();
        jlC2 = new javax.swing.JLabel();
        jlT1 = new javax.swing.JLabel();
        jlF1 = new javax.swing.JLabel();
        jlCedula_cli = new javax.swing.JLabel();
        jlNombre_cli = new javax.swing.JLabel();
        jlApellido_cli = new javax.swing.JLabel();
        jlNac_cli = new javax.swing.JLabel();
        jlReg_cli = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jbRegistrar_cli = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        jlE2 = new javax.swing.JLabel();
        jlGenero_cli = new javax.swing.JLabel();
        jlE4 = new javax.swing.JLabel();
        jlDescuento_cli = new javax.swing.JLabel();
        jlE5 = new javax.swing.JLabel();
        jlTelefono_cli = new javax.swing.JLabel();
        jlCorreo_cli = new javax.swing.JLabel();
        jlE6 = new javax.swing.JLabel();
        jlDireccion_cli = new javax.swing.JLabel();
        jlE7 = new javax.swing.JLabel();
        jcBuscar_cli = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jtBuscar_cli = new javax.swing.JTextField();
        lim_cli = new javax.swing.JLabel();
        jsTabla_cat7 = new javax.swing.JScrollPane();
        JTclientes = new javax.swing.JTable();
        jl_titulo10 = new javax.swing.JLabel();
        JPdescuentos = new javax.swing.JPanel();
        res_num_des = new javax.swing.JLabel();
        jpDatos_des = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jlR3 = new javax.swing.JLabel();
        jbEnviar_des = new javax.swing.JButton();
        jbEliminar_des = new javax.swing.JButton();
        jbModificar_des = new javax.swing.JButton();
        jlN3 = new javax.swing.JLabel();
        jlNombre_des = new javax.swing.JLabel();
        jlPorcentaje_des = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jbRegistrar_des = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jcBuscar_des = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jtBuscar_des = new javax.swing.JTextField();
        lim_des = new javax.swing.JLabel();
        jsTabla_des = new javax.swing.JScrollPane();
        JTdescuentos = new javax.swing.JTable();
        jl_titulo14 = new javax.swing.JLabel();
        JPempleados = new javax.swing.JPanel();
        res_num_emp = new javax.swing.JLabel();
        jpDatos_emp = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jlR5 = new javax.swing.JLabel();
        jbEnviar_cli1 = new javax.swing.JButton();
        jb_Eliminar_cli1 = new javax.swing.JButton();
        jbModificar_cli1 = new javax.swing.JButton();
        jlN5 = new javax.swing.JLabel();
        jlC3 = new javax.swing.JLabel();
        jlT2 = new javax.swing.JLabel();
        jlF2 = new javax.swing.JLabel();
        jlCedula_emp = new javax.swing.JLabel();
        jlNombre_emp = new javax.swing.JLabel();
        jlApellido_emp = new javax.swing.JLabel();
        jlNac_emp = new javax.swing.JLabel();
        jlReg_emp = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        jbRegistrar_cli1 = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JSeparator();
        jlGen_emp = new javax.swing.JLabel();
        jlGenero_emp = new javax.swing.JLabel();
        jlE10 = new javax.swing.JLabel();
        jlSueldo_emp = new javax.swing.JLabel();
        jlE11 = new javax.swing.JLabel();
        jlTelefono_emp = new javax.swing.JLabel();
        jlCorreo_emp = new javax.swing.JLabel();
        jlE12 = new javax.swing.JLabel();
        jlDireccion_emp = new javax.swing.JLabel();
        jlE13 = new javax.swing.JLabel();
        jcBuscar_emp = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        jtBuscar_emp = new javax.swing.JTextField();
        lim_emp = new javax.swing.JLabel();
        jsTabla_cat8 = new javax.swing.JScrollPane();
        JTempleados = new javax.swing.JTable();
        jl_titulo11 = new javax.swing.JLabel();
        JPproductos = new javax.swing.JPanel();
        res_num_pro = new javax.swing.JLabel();
        jpDatos_pro = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jlR6 = new javax.swing.JLabel();
        jbEnviar_pro = new javax.swing.JButton();
        jb_Eliminar_pro = new javax.swing.JButton();
        jbModificar_pro = new javax.swing.JButton();
        jlN6 = new javax.swing.JLabel();
        jlC4 = new javax.swing.JLabel();
        jlF3 = new javax.swing.JLabel();
        jlCodigo_pro = new javax.swing.JLabel();
        jlNombre_pro = new javax.swing.JLabel();
        jlPrecio_pro = new javax.swing.JLabel();
        jlReg_pro = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jbRegistrar_pro = new javax.swing.JButton();
        jlGen_emp1 = new javax.swing.JLabel();
        jlExistencias_pro = new javax.swing.JLabel();
        jlE15 = new javax.swing.JLabel();
        jlCategoria_pro = new javax.swing.JLabel();
        jlProveedor_pro = new javax.swing.JLabel();
        jlE16 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        jcBuscar_pro = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        jtBuscar_pro = new javax.swing.JTextField();
        lim_pro = new javax.swing.JLabel();
        jsTabla_pro = new javax.swing.JScrollPane();
        JTproductos = new javax.swing.JTable();
        jl_titulo12 = new javax.swing.JLabel();
        JPproveedores = new javax.swing.JPanel();
        res_num_prov = new javax.swing.JLabel();
        jpDatos_prov = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jlR = new javax.swing.JLabel();
        jlEmail = new javax.swing.JLabel();
        jbEnviar_prov = new javax.swing.JButton();
        jb_Eliminar_prov = new javax.swing.JButton();
        jbModificar_prov = new javax.swing.JButton();
        jlN = new javax.swing.JLabel();
        jlC = new javax.swing.JLabel();
        jlT = new javax.swing.JLabel();
        jlE = new javax.swing.JLabel();
        jlF = new javax.swing.JLabel();
        jlRUC = new javax.swing.JLabel();
        jlNombre = new javax.swing.JLabel();
        jlCiudad = new javax.swing.JLabel();
        jlTelefono = new javax.swing.JLabel();
        jlFecha_reg = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jbRegistrar_prov = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jcBuscar_prov = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jtBuscar_prov = new javax.swing.JTextField();
        lim_prov = new javax.swing.JLabel();
        jsTabla_cat6 = new javax.swing.JScrollPane();
        JTproveedores = new javax.swing.JTable();
        jl_titulo13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        sistema_titulo = new javax.swing.JLabel();
        FECHA_HORA = new javax.swing.JLabel();
        USUARIO = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        SALIR = new javax.swing.JLabel();

        jl_dinero2.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        jl_dinero2.setForeground(new java.awt.Color(255, 255, 255));
        jl_dinero2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_dinero2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/dinero (1).png"))); // NOI18N
        jl_dinero2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        v_dinero2.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        v_dinero2.setForeground(new java.awt.Color(51, 51, 51));
        v_dinero2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        v_dinero2.setText("000.00");

        v_dinero4.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        v_dinero4.setForeground(new java.awt.Color(51, 51, 51));
        v_dinero4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        v_dinero4.setText("000.00");

        jl_dinero4.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        jl_dinero4.setForeground(new java.awt.Color(255, 255, 255));
        jl_dinero4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_dinero4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/dinero (1).png"))); // NOI18N
        jl_dinero4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jlE3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlE3.setForeground(new java.awt.Color(51, 51, 51));
        jlE3.setText("Género");

        jlEmail3.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jlEmail3.setText("(null)");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        v10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        v10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/-local-shipping_90041.png"))); // NOI18N
        v10.setText("0000");

        v3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        v3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/-person_90382.png"))); // NOI18N
        v3.setText("0000");

        v6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        v6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/employee_solid_icon_235377.png"))); // NOI18N
        v6.setText("0000");

        v9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        v9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/products_new_cube_product_design_icon_153845.png"))); // NOI18N
        v9.setText("0000");

        v1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        v1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/category_icon_214824.png"))); // NOI18N
        v1.setText("0000");

        v4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        v4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/badge_discount_icon_178995.png"))); // NOI18N
        v4.setText("0000");

        v2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        v2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/flag_icon-icons.com_50393.png"))); // NOI18N
        v2.setText("0000");

        V0.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        V0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        V0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/home-button_icon-icons.com_72700.png"))); // NOI18N

        javax.swing.GroupLayout JPencabezado2Layout = new javax.swing.GroupLayout(JPencabezado2);
        JPencabezado2.setLayout(JPencabezado2Layout);
        JPencabezado2Layout.setHorizontalGroup(
            JPencabezado2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPencabezado2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(V0)
                .addGap(36, 36, 36)
                .addComponent(v1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(v2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(v3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(v6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(v9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(v10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPencabezado2Layout.setVerticalGroup(
            JPencabezado2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(V0, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(v1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(v2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(v3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(v4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(v6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(v9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(v10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(JPencabezado2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1264, -1));

        MENU.setBackground(new java.awt.Color(255, 255, 255));
        MENU.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        MENU.setMinimumSize(new java.awt.Dimension(1110, 504));
        MENU.setPreferredSize(new java.awt.Dimension(1099, 449));

        INICIO.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        INICIO.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N

        JPfactura.setBackground(new java.awt.Color(204, 204, 204));
        JPfactura.setPreferredSize(new java.awt.Dimension(1135, 1300));
        JPfactura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(0, 51, 153));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 0, 204));
        jLabel3.setText("Factura");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Fecha de emisión:");

        enc_fecha.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_fecha.setText("yyyy-MM-dd");

        enc_codigo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        enc_codigo.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel12.setText("Código de factura:");

        jPanel4.setBackground(new java.awt.Color(0, 51, 153));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Datos del cliente ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel13.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel13.setText("Identificación:");

        enc_cedula.setEditable(false);
        enc_cedula.setBackground(new java.awt.Color(255, 255, 255));
        enc_cedula.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_cedula.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Click para seleccionar o cambiar cliente...", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI", 0, 12), new java.awt.Color(0, 51, 153))); // NOI18N
        enc_cedula.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        enc_cedula.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enc_cedulaMouseClicked(evt);
            }
        });
        enc_cedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enc_cedulaActionPerformed(evt);
            }
        });

        enc_nombre_apellido.setEditable(false);
        enc_nombre_apellido.setBackground(new java.awt.Color(255, 255, 255));
        enc_nombre_apellido.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_nombre_apellido.setText(" ");

        enc_direccion.setEditable(false);
        enc_direccion.setBackground(new java.awt.Color(255, 255, 255));
        enc_direccion.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_direccion.setText(" ");

        enc_telefono.setEditable(false);
        enc_telefono.setBackground(new java.awt.Color(255, 255, 255));
        enc_telefono.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_telefono.setText(" ");

        enc_correo.setEditable(false);
        enc_correo.setBackground(new java.awt.Color(255, 255, 255));
        enc_correo.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_correo.setText(" ");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel16.setText("Teléfono:");

        jLabel15.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel15.setText("Dirección:");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel14.setText("Razón social:");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel18.setText("Correo electrónico:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(enc_correo, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(40, 40, 40)
                                .addComponent(enc_telefono))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(40, 40, 40)
                                .addComponent(enc_direccion))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(40, 40, 40)
                                .addComponent(enc_nombre_apellido))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(40, 40, 40)
                                .addComponent(enc_cedula))
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(40, 40, 40)
                                .addComponent(enc_codigo)))
                        .addGap(34, 34, 34))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(40, 40, 40)
                                .addComponent(enc_fecha)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel3)
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(enc_fecha))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(enc_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(enc_cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(enc_nombre_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(enc_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(enc_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enc_correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        JPfactura.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 0, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(0, 51, 153));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Detalle");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        JTdetalle = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdetalle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdetalle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTdetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdetalle.setFocusable(false);
        JTdetalle.setGridColor(new java.awt.Color(255, 255, 255));
        JTdetalle.setOpaque(false);
        JTdetalle.setRowHeight(30);
        JTdetalle.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdetalle.getTableHeader().setResizingAllowed(false);
        JTdetalle.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu1.setViewportView(JTdetalle);

        jButton1.setBackground(new java.awt.Color(0, 51, 153));
        jButton1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("+    Seleccionar producto");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jl_num_det.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det.setText("Detalles: 0");

        jl_total.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_total.setText("Total: $0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jl_num_det, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jsTabla_ciu1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(34, 34, 34))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jl_num_det, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jl_total))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jsTabla_ciu1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        JPfactura.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 521, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(0, 51, 153));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Totales");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 306, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jl_num_det1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det1.setText("Total descuento:");

        jl_total1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_total1.setText("Subtotal:");

        jl_num_det2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det2.setText("Valor a pagar:");

        jl_total2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_total2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jl_total2.setText("000");

        jl_num_det3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jl_num_det3.setText("000");

        jl_num_det4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jl_num_det4.setText("000");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jl_num_det2)
                                .addGap(22, 22, 22))
                            .addComponent(jl_total1)
                            .addComponent(jl_num_det1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jl_num_det4, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(jl_num_det3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jl_total2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(22, 22, 22)))
                .addGap(0, 37, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jl_total1)
                        .addGap(18, 18, 18)
                        .addComponent(jl_num_det1)
                        .addGap(18, 18, 18)
                        .addComponent(jl_num_det2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jl_total2)
                        .addGap(18, 18, 18)
                        .addComponent(jl_num_det3)
                        .addGap(18, 18, 18)
                        .addComponent(jl_num_det4)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        JPfactura.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(339, 920, -1, -1));

        jButton2.setBackground(new java.awt.Color(0, 51, 153));
        jButton2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("CREAR FACTURA");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        JPfactura.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 1150, -1, 38));

        jScrollPane.setViewportView(JPfactura);

        INICIO.addTab("FACTURAR", jScrollPane);

        jScrollPanew.setPreferredSize(new java.awt.Dimension(1149, 702));

        JPventas.setBackground(new java.awt.Color(204, 255, 255));
        JPventas.setPreferredSize(new java.awt.Dimension(1135, 1000));

        jl_titulo6.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        jl_titulo6.setText("FACTURA");
        jl_titulo6.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPventasLayout = new javax.swing.GroupLayout(JPventas);
        JPventas.setLayout(JPventasLayout);
        JPventasLayout.setHorizontalGroup(
            JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1137, Short.MAX_VALUE)
            .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JPventasLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jl_titulo6)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        JPventasLayout.setVerticalGroup(
            JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
            .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JPventasLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jl_titulo6)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jScrollPanew.setViewportView(JPventas);

        INICIO.addTab("VENTAS", jScrollPanew);

        JPgastos.setBackground(new java.awt.Color(204, 255, 255));

        javax.swing.GroupLayout JPgastosLayout = new javax.swing.GroupLayout(JPgastos);
        JPgastos.setLayout(JPgastosLayout);
        JPgastosLayout.setHorizontalGroup(
            JPgastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1099, Short.MAX_VALUE)
        );
        JPgastosLayout.setVerticalGroup(
            JPgastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );

        INICIO.addTab("GASTOS", JPgastos);

        MENU.addTab("INICIO", INICIO);

        JPcategorias.setBackground(new java.awt.Color(204, 255, 255));

        jl_titulo5.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo5.setText("Lista de categorías");
        jl_titulo5.setIconTextGap(10);
        jl_titulo5.setVerifyInputWhenFocusTarget(false);

        jpDatos_cat.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_cat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_cat.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Categoría seleccionada:");

        jlR4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR4.setText("Nombre:");

        jlN4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN4.setText("Descripción:");

        jlNombre_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_cat.setText(" ");

        jSeparator12.setForeground(new java.awt.Color(0, 0, 0));

        jScrollPane2.setBorder(null);

        jtaDescripcion_cat.setEditable(false);
        jtaDescripcion_cat.setColumns(20);
        jtaDescripcion_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jtaDescripcion_cat.setLineWrap(true);
        jtaDescripcion_cat.setRows(5);
        jtaDescripcion_cat.setWrapStyleWord(true);
        jtaDescripcion_cat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 0, 14))); // NOI18N
        jtaDescripcion_cat.setFocusable(false);
        jScrollPane2.setViewportView(jtaDescripcion_cat);

        jbEnviar_cat.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_cat.setText(" Enviar");
        jbEnviar_cat.setBorder(null);
        jbEnviar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_catMouseExited(evt);
            }
        });
        jbEnviar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_catActionPerformed(evt);
            }
        });

        jbEliminar_cat.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEliminar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar_cat.setText(" Eliminar");
        jbEliminar_cat.setBorder(null);
        jbEliminar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_catMouseExited(evt);
            }
        });
        jbEliminar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_catActionPerformed(evt);
            }
        });

        jbModificar_cat.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_cat.setText(" Modificar");
        jbModificar_cat.setBorder(null);
        jbModificar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_catMouseExited(evt);
            }
        });
        jbModificar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_catActionPerformed(evt);
            }
        });

        jbRegistrar_cat.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_cat.setText("Agregar");
        jbRegistrar_cat.setBorder(null);
        jbRegistrar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_catMouseExited(evt);
            }
        });
        jbRegistrar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_catActionPerformed(evt);
            }
        });

        jSeparator19.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jpDatos_catLayout = new javax.swing.GroupLayout(jpDatos_cat);
        jpDatos_cat.setLayout(jpDatos_catLayout);
        jpDatos_catLayout.setHorizontalGroup(
            jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator12, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jpDatos_catLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDatos_catLayout.createSequentialGroup()
                        .addGroup(jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbRegistrar_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbModificar_cat, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbEliminar_cat, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                            .addComponent(jbEnviar_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jlN4)
                        .addGroup(jpDatos_catLayout.createSequentialGroup()
                            .addComponent(jlR4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlNombre_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))
                .addContainerGap(12, Short.MAX_VALUE))
            .addComponent(jSeparator19)
        );
        jpDatos_catLayout.setVerticalGroup(
            jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_catLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR4)
                    .addComponent(jlNombre_cat))
                .addGap(25, 25, 25)
                .addComponent(jlN4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbEnviar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbRegistrar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpDatos_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbEliminar_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbModificar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jcBuscar_cat.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_cat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Descripción" }));
        jcBuscar_cat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_catItemStateChanged(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText("Buscar categoría por");

        jtBuscar_cat.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_cat.setText("Buscar");
        jtBuscar_cat.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_cat.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_cat.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_cat.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_cat.setSelectionEnd(0);
        jtBuscar_cat.setSelectionStart(0);
        jtBuscar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_catMouseClicked(evt);
            }
        });
        jtBuscar_cat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_catKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_catKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_catKeyTyped(evt);
            }
        });

        lim_cat.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_cat.setForeground(new java.awt.Color(0, 102, 102));
        lim_cat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_catMouseClicked(evt);
            }
        });

        JTcategorias = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTcategorias.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTcategorias.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTcategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTcategorias.setFocusable(false);
        JTcategorias.setGridColor(new java.awt.Color(255, 255, 255));
        JTcategorias.setOpaque(false);
        JTcategorias.setRowHeight(30);
        JTcategorias.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTcategorias.getTableHeader().setResizingAllowed(false);
        JTcategorias.getTableHeader().setReorderingAllowed(false);
        jsTabla_cat.setViewportView(JTcategorias);

        res_num_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_cat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_cat.setText("Resultados: 0 de 0");

        javax.swing.GroupLayout JPcategoriasLayout = new javax.swing.GroupLayout(JPcategorias);
        JPcategorias.setLayout(JPcategoriasLayout);
        JPcategoriasLayout.setHorizontalGroup(
            JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcategoriasLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(res_num_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsTabla_cat)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPcategoriasLayout.createSequentialGroup()
                        .addComponent(jl_titulo5, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(179, 179, 179)
                        .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(JPcategoriasLayout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcBuscar_cat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jtBuscar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2)
                .addComponent(lim_cat)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jpDatos_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        JPcategoriasLayout.setVerticalGroup(
            JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcategoriasLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jpDatos_cat, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                    .addGroup(JPcategoriasLayout.createSequentialGroup()
                        .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPcategoriasLayout.createSequentialGroup()
                                .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jcBuscar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lim_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jtBuscar_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jl_titulo5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jsTabla_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(res_num_cat)
                .addGap(124, 124, 124))
        );

        MENU.addTab("CATEGORÍAS", JPcategorias);

        JPciudades.setBackground(new java.awt.Color(204, 255, 255));

        res_num_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_ciu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_ciu.setText("Resultados: 0 de 0");

        jcBuscar_ciu.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_ciu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Nombre", "Provincia" }));
        jcBuscar_ciu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_ciuItemStateChanged(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("Buscar ciudad por");

        jtBuscar_ciu.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jtBuscar_ciu.setText("Buscar");
        jtBuscar_ciu.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_ciu.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_ciu.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_ciu.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_ciu.setSelectionEnd(0);
        jtBuscar_ciu.setSelectionStart(0);
        jtBuscar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_ciuMouseClicked(evt);
            }
        });
        jtBuscar_ciu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_ciuKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_ciuKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_ciuKeyTyped(evt);
            }
        });

        lim_ciu.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_ciu.setForeground(new java.awt.Color(0, 102, 102));
        lim_ciu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_ciuMouseClicked(evt);
            }
        });

        JTciudades = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTciudades.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTciudades.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTciudades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTciudades.setFocusable(false);
        JTciudades.setGridColor(new java.awt.Color(255, 255, 255));
        JTciudades.setOpaque(false);
        JTciudades.setRowHeight(30);
        JTciudades.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTciudades.getTableHeader().setResizingAllowed(false);
        JTciudades.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu.setViewportView(JTciudades);

        jl_titulo7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo7.setText("Lista de ciudades");
        jl_titulo7.setIconTextGap(10);
        jl_titulo7.setVerifyInputWhenFocusTarget(false);

        jpDatos_cat1.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_cat1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_cat1.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel20.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Categoría seleccionada:");

        jSeparator13.setForeground(new java.awt.Color(0, 0, 0));

        jlR1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR1.setForeground(new java.awt.Color(51, 51, 51));
        jlR1.setText("Código:");

        jlN1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN1.setForeground(new java.awt.Color(51, 51, 51));
        jlN1.setText("Nombre:");

        jlC1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC1.setForeground(new java.awt.Color(51, 51, 51));
        jlC1.setText("Provincia:");

        jlCodigo_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCodigo_ciu.setText(" ");

        jlNombre_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_ciu.setText(" ");

        jlProvincia_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlProvincia_ciu.setText(" ");

        jbRegistrar_ciu.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_ciu.setText("Agregar");
        jbRegistrar_ciu.setBorder(null);
        jbRegistrar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_ciuMouseExited(evt);
            }
        });
        jbRegistrar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_ciuActionPerformed(evt);
            }
        });

        jbEnviar_ciu.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_ciu.setText(" Enviar");
        jbEnviar_ciu.setBorder(null);
        jbEnviar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_ciuMouseExited(evt);
            }
        });
        jbEnviar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_ciuActionPerformed(evt);
            }
        });

        jbEliminar_ciu.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEliminar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar_ciu.setText(" Eliminar");
        jbEliminar_ciu.setBorder(null);
        jbEliminar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_ciuMouseExited(evt);
            }
        });
        jbEliminar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_ciuActionPerformed(evt);
            }
        });

        jbModificar_ciu.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_ciu.setText(" Modificar");
        jbModificar_ciu.setBorder(null);
        jbModificar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_ciuMouseExited(evt);
            }
        });
        jbModificar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_ciuActionPerformed(evt);
            }
        });

        jSeparator18.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jpDatos_cat1Layout = new javax.swing.GroupLayout(jpDatos_cat1);
        jpDatos_cat1.setLayout(jpDatos_cat1Layout);
        jpDatos_cat1Layout.setHorizontalGroup(
            jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator13)
            .addGroup(jpDatos_cat1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDatos_cat1Layout.createSequentialGroup()
                        .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbRegistrar_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbModificar_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbEliminar_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                            .addComponent(jbEnviar_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jpDatos_cat1Layout.createSequentialGroup()
                            .addComponent(jlC1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlProvincia_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jpDatos_cat1Layout.createSequentialGroup()
                            .addComponent(jlN1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlNombre_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jpDatos_cat1Layout.createSequentialGroup()
                            .addComponent(jlR1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlCodigo_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
            .addComponent(jSeparator18, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jpDatos_cat1Layout.setVerticalGroup(
            jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_cat1Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR1)
                    .addComponent(jlCodigo_ciu))
                .addGap(25, 25, 25)
                .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlN1)
                    .addComponent(jlNombre_ciu))
                .addGap(25, 25, 25)
                .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlC1)
                    .addComponent(jlProvincia_ciu))
                .addGap(18, 18, 18)
                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbEnviar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbRegistrar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpDatos_cat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbEliminar_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbModificar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPciudadesLayout = new javax.swing.GroupLayout(JPciudades);
        JPciudades.setLayout(JPciudadesLayout);
        JPciudadesLayout.setHorizontalGroup(
            JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciudadesLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(res_num_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsTabla_ciu)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPciudadesLayout.createSequentialGroup()
                        .addComponent(jl_titulo7, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(192, 192, 192)
                        .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPciudadesLayout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jtBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2)
                .addComponent(lim_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpDatos_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        JPciudadesLayout.setVerticalGroup(
            JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciudadesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPciudadesLayout.createSequentialGroup()
                        .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPciudadesLayout.createSequentialGroup()
                                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lim_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jl_titulo7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jsTabla_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_cat1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addComponent(res_num_ciu)
                .addGap(85, 85, 85))
        );

        MENU.addTab("CIUDADES", JPciudades);

        JPclientes.setBackground(new java.awt.Color(204, 255, 255));

        res_num_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_cli.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_cli.setText("Resultados: 0 de 0");

        jpDatos_cli.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_cli.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_cli.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Cliente seleccionado:");

        jlR2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR2.setForeground(new java.awt.Color(51, 51, 51));
        jlR2.setText("Cédula");

        jbEnviar_cli.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_cli.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_cli.setText(" Enviar");
        jbEnviar_cli.setBorder(null);
        jbEnviar_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_cliMouseExited(evt);
            }
        });
        jbEnviar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_cliActionPerformed(evt);
            }
        });

        jb_Eliminar_cli.setBackground(new java.awt.Color(255, 0, 51));
        jb_Eliminar_cli.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jb_Eliminar_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jb_Eliminar_cli.setText(" Eliminar");
        jb_Eliminar_cli.setBorder(null);
        jb_Eliminar_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Eliminar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jb_Eliminar_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jb_Eliminar_cliMouseExited(evt);
            }
        });
        jb_Eliminar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_Eliminar_cliActionPerformed(evt);
            }
        });

        jbModificar_cli.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_cli.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_cli.setText(" Modificar");
        jbModificar_cli.setBorder(null);
        jbModificar_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_cliMouseExited(evt);
            }
        });
        jbModificar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_cliActionPerformed(evt);
            }
        });

        jlN2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN2.setForeground(new java.awt.Color(51, 51, 51));
        jlN2.setText("Nombre:");

        jlC2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC2.setForeground(new java.awt.Color(51, 51, 51));
        jlC2.setText("Apellido:");

        jlT1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlT1.setForeground(new java.awt.Color(51, 51, 51));
        jlT1.setText("Fecha de nacimiento:");

        jlF1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlF1.setForeground(new java.awt.Color(51, 51, 51));
        jlF1.setText("Fecha de registro:");

        jlCedula_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCedula_cli.setText(" ");

        jlNombre_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_cli.setText(" ");

        jlApellido_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlApellido_cli.setText(" ");

        jlNac_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNac_cli.setText(" ");

        jlReg_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlReg_cli.setText(" ");

        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));

        jbRegistrar_cli.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_cli.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_cli.setText("Agregar");
        jbRegistrar_cli.setBorder(null);
        jbRegistrar_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_cliMouseExited(evt);
            }
        });
        jbRegistrar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_cliActionPerformed(evt);
            }
        });

        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));

        jlE2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE2.setForeground(new java.awt.Color(51, 51, 51));
        jlE2.setText("Género:");

        jlGenero_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlGenero_cli.setText(" ");

        jlE4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE4.setForeground(new java.awt.Color(51, 51, 51));
        jlE4.setText("Tipo de descuento:");

        jlDescuento_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlDescuento_cli.setText(" ");

        jlE5.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE5.setForeground(new java.awt.Color(51, 51, 51));
        jlE5.setText("Teléfono:");

        jlTelefono_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlTelefono_cli.setText(" ");

        jlCorreo_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCorreo_cli.setText(" ");

        jlE6.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE6.setForeground(new java.awt.Color(51, 51, 51));
        jlE6.setText("Correo:");

        jlDireccion_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlDireccion_cli.setText(" ");

        jlE7.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE7.setForeground(new java.awt.Color(51, 51, 51));
        jlE7.setText("Dirección:");

        javax.swing.GroupLayout jpDatos_cliLayout = new javax.swing.GroupLayout(jpDatos_cli);
        jpDatos_cli.setLayout(jpDatos_cliLayout);
        jpDatos_cliLayout.setHorizontalGroup(
            jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jpDatos_cliLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jbRegistrar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jbEnviar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jbModificar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jb_Eliminar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jpDatos_cliLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jpDatos_cliLayout.createSequentialGroup()
                        .addComponent(jlR2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlCedula_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_cliLayout.createSequentialGroup()
                        .addComponent(jlN2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlNombre_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_cliLayout.createSequentialGroup()
                        .addComponent(jlC2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlApellido_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_cliLayout.createSequentialGroup()
                        .addComponent(jlT1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlNac_cli, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)))
                .addGap(150, 150, 150)
                .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDatos_cliLayout.createSequentialGroup()
                        .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jpDatos_cliLayout.createSequentialGroup()
                                .addComponent(jlE5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlTelefono_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpDatos_cliLayout.createSequentialGroup()
                                .addComponent(jlE2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlGenero_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(136, 136, 136)
                        .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_cliLayout.createSequentialGroup()
                                .addComponent(jlF1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlReg_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_cliLayout.createSequentialGroup()
                                .addComponent(jlE4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlDescuento_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpDatos_cliLayout.createSequentialGroup()
                            .addComponent(jlE6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlCorreo_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpDatos_cliLayout.createSequentialGroup()
                            .addComponent(jlE7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlDireccion_cli, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)))))
        );
        jpDatos_cliLayout.setVerticalGroup(
            jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatos_cliLayout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR2)
                    .addComponent(jlCedula_cli)
                    .addComponent(jlE2)
                    .addComponent(jlGenero_cli)
                    .addComponent(jlDescuento_cli)
                    .addComponent(jlE4))
                .addGap(4, 4, 4)
                .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlN2)
                    .addComponent(jlNombre_cli)
                    .addComponent(jlE5)
                    .addComponent(jlTelefono_cli)
                    .addComponent(jlReg_cli)
                    .addComponent(jlF1))
                .addGap(4, 4, 4)
                .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlC2)
                    .addComponent(jlApellido_cli)
                    .addComponent(jlE6)
                    .addComponent(jlCorreo_cli))
                .addGap(4, 4, 4)
                .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlT1)
                    .addComponent(jlNac_cli)
                    .addComponent(jlE7)
                    .addComponent(jlDireccion_cli))
                .addGap(10, 10, 10)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jpDatos_cliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbRegistrar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEnviar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbModificar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_Eliminar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jcBuscar_cli.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_cli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "Nombre", "Apellido", "F. Nacimiento", "Género", "Teléfono", "Correo", "Dirección", "T. Descuento", "F. Registro" }));
        jcBuscar_cli.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_cliItemStateChanged(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel36.setText("Buscar cliente por");

        jtBuscar_cli.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_cli.setText("Buscar");
        jtBuscar_cli.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_cli.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_cli.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_cli.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_cli.setSelectionEnd(0);
        jtBuscar_cli.setSelectionStart(0);
        jtBuscar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_cliMouseClicked(evt);
            }
        });
        jtBuscar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBuscar_cliActionPerformed(evt);
            }
        });
        jtBuscar_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_cliKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_cliKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_cliKeyTyped(evt);
            }
        });

        lim_cli.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_cli.setForeground(new java.awt.Color(0, 102, 102));
        lim_cli.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_cliMouseClicked(evt);
            }
        });

        JTclientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTclientes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTclientes.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTclientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTclientes.setFocusable(false);
        JTclientes.setGridColor(new java.awt.Color(255, 255, 255));
        JTclientes.setOpaque(false);
        JTclientes.setRowHeight(30);
        JTclientes.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTclientes.getTableHeader().setResizingAllowed(false);
        JTclientes.getTableHeader().setReorderingAllowed(false);
        jsTabla_cat7.setViewportView(JTclientes);

        jl_titulo10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo10.setText("Lista de clientes");
        jl_titulo10.setIconTextGap(10);
        jl_titulo10.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPclientesLayout = new javax.swing.GroupLayout(JPclientes);
        JPclientes.setLayout(JPclientesLayout);
        JPclientesLayout.setHorizontalGroup(
            JPclientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPclientesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPclientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPclientesLayout.createSequentialGroup()
                        .addComponent(jl_titulo10, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcBuscar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jtBuscar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lim_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(res_num_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_cli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1248, Short.MAX_VALUE)
                    .addComponent(jsTabla_cat7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPclientesLayout.setVerticalGroup(
            JPclientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPclientesLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(JPclientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lim_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(res_num_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtBuscar_cli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcBuscar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jl_titulo10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jsTabla_cat7, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jpDatos_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        MENU.addTab("CLIENTES", JPclientes);

        JPdescuentos.setBackground(new java.awt.Color(204, 255, 255));

        res_num_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_des.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_des.setText("Resultados: 0 de 0");

        jpDatos_des.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_des.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_des.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Descuento seleccionado:");

        jlR3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR3.setForeground(new java.awt.Color(51, 51, 51));
        jlR3.setText("Nombre:");

        jbEnviar_des.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_des.setText(" Enviar");
        jbEnviar_des.setBorder(null);
        jbEnviar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_desMouseExited(evt);
            }
        });
        jbEnviar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_desActionPerformed(evt);
            }
        });

        jbEliminar_des.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEliminar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar_des.setText(" Eliminar");
        jbEliminar_des.setBorder(null);
        jbEliminar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_desMouseExited(evt);
            }
        });
        jbEliminar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_desActionPerformed(evt);
            }
        });

        jbModificar_des.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_des.setText(" Modificar");
        jbModificar_des.setBorder(null);
        jbModificar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_desMouseExited(evt);
            }
        });
        jbModificar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_desActionPerformed(evt);
            }
        });

        jlN3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN3.setForeground(new java.awt.Color(51, 51, 51));
        jlN3.setText("Porcentaje: %");

        jlNombre_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_des.setText(" ");

        jlPorcentaje_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlPorcentaje_des.setText(" ");

        jSeparator10.setForeground(new java.awt.Color(0, 0, 0));

        jbRegistrar_des.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_des.setText("Agregar");
        jbRegistrar_des.setBorder(null);
        jbRegistrar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_desMouseExited(evt);
            }
        });
        jbRegistrar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_desActionPerformed(evt);
            }
        });

        jSeparator11.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jpDatos_desLayout = new javax.swing.GroupLayout(jpDatos_des);
        jpDatos_des.setLayout(jpDatos_desLayout);
        jpDatos_desLayout.setHorizontalGroup(
            jpDatos_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
            .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jpDatos_desLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpDatos_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbRegistrar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpDatos_desLayout.createSequentialGroup()
                        .addComponent(jbModificar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbEliminar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jbEnviar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpDatos_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpDatos_desLayout.createSequentialGroup()
                            .addComponent(jlR3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlNombre_des, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpDatos_desLayout.createSequentialGroup()
                            .addComponent(jlN3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlPorcentaje_des, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
            .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jpDatos_desLayout.setVerticalGroup(
            jpDatos_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_desLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDatos_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR3)
                    .addComponent(jlNombre_des))
                .addGap(25, 25, 25)
                .addGroup(jpDatos_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlN3)
                    .addComponent(jlPorcentaje_des))
                .addGap(36, 36, 36)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jbRegistrar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jbEnviar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jpDatos_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbEliminar_des, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbModificar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jcBuscar_des.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_des.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Porcentaje" }));
        jcBuscar_des.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_desItemStateChanged(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel37.setText("Buscar descuento por");

        jtBuscar_des.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_des.setText("Buscar");
        jtBuscar_des.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_des.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_des.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_des.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_des.setSelectionEnd(0);
        jtBuscar_des.setSelectionStart(0);
        jtBuscar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_desMouseClicked(evt);
            }
        });
        jtBuscar_des.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_desKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_desKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_desKeyTyped(evt);
            }
        });

        lim_des.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_des.setForeground(new java.awt.Color(0, 102, 102));
        lim_des.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_desMouseClicked(evt);
            }
        });

        JTdescuentos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdescuentos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdescuentos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTdescuentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdescuentos.setFocusable(false);
        JTdescuentos.setGridColor(new java.awt.Color(255, 255, 255));
        JTdescuentos.setOpaque(false);
        JTdescuentos.setRowHeight(30);
        JTdescuentos.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdescuentos.getTableHeader().setResizingAllowed(false);
        JTdescuentos.getTableHeader().setReorderingAllowed(false);
        jsTabla_des.setViewportView(JTdescuentos);

        jl_titulo14.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo14.setText("Lista de descuetos");
        jl_titulo14.setIconTextGap(10);
        jl_titulo14.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPdescuentosLayout = new javax.swing.GroupLayout(JPdescuentos);
        JPdescuentos.setLayout(JPdescuentosLayout);
        JPdescuentosLayout.setHorizontalGroup(
            JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdescuentosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                        .addComponent(res_num_des, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                        .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPdescuentosLayout.createSequentialGroup()
                                .addComponent(jl_titulo14, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(157, 157, 157)
                                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                                        .addComponent(jtBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(lim_des))
                                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                                        .addComponent(jLabel37)
                                        .addGap(3, 3, 3)
                                        .addComponent(jcBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jsTabla_des, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpDatos_des, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        JPdescuentosLayout.setVerticalGroup(
            JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdescuentosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                        .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPdescuentosLayout.createSequentialGroup()
                                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jcBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lim_des, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jl_titulo14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jsTabla_des, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_des, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(res_num_des))
        );

        MENU.addTab("DESCUENTOS", JPdescuentos);

        JPempleados.setBackground(new java.awt.Color(204, 255, 255));

        res_num_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_emp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_emp.setText("Resultados: 0 de 0");

        jpDatos_emp.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_emp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_emp.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel10.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Empleado seleccionado:");

        jlR5.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR5.setForeground(new java.awt.Color(51, 51, 51));
        jlR5.setText("Cédula");

        jbEnviar_cli1.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_cli1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_cli1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_cli1.setText(" Enviar");
        jbEnviar_cli1.setBorder(null);
        jbEnviar_cli1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_cli1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_cli1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_cli1MouseExited(evt);
            }
        });
        jbEnviar_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_cli1ActionPerformed(evt);
            }
        });

        jb_Eliminar_cli1.setBackground(new java.awt.Color(255, 0, 51));
        jb_Eliminar_cli1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jb_Eliminar_cli1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jb_Eliminar_cli1.setText(" Eliminar");
        jb_Eliminar_cli1.setBorder(null);
        jb_Eliminar_cli1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Eliminar_cli1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jb_Eliminar_cli1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jb_Eliminar_cli1MouseExited(evt);
            }
        });
        jb_Eliminar_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_Eliminar_cli1ActionPerformed(evt);
            }
        });

        jbModificar_cli1.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_cli1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_cli1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_cli1.setText(" Modificar");
        jbModificar_cli1.setBorder(null);
        jbModificar_cli1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_cli1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_cli1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_cli1MouseExited(evt);
            }
        });
        jbModificar_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_cli1ActionPerformed(evt);
            }
        });

        jlN5.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN5.setForeground(new java.awt.Color(51, 51, 51));
        jlN5.setText("Nombre:");

        jlC3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC3.setForeground(new java.awt.Color(51, 51, 51));
        jlC3.setText("Apellido:");

        jlT2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlT2.setForeground(new java.awt.Color(51, 51, 51));
        jlT2.setText("Fecha de nacimiento:");

        jlF2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlF2.setForeground(new java.awt.Color(51, 51, 51));
        jlF2.setText("Fecha de registro:");

        jlCedula_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCedula_emp.setText(" ");

        jlNombre_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_emp.setText(" ");

        jlApellido_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlApellido_emp.setText(" ");

        jlNac_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNac_emp.setText(" ");

        jlReg_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlReg_emp.setText(" ");

        jSeparator14.setForeground(new java.awt.Color(0, 0, 0));

        jbRegistrar_cli1.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_cli1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_cli1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_cli1.setText("Agregar");
        jbRegistrar_cli1.setBorder(null);
        jbRegistrar_cli1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_cli1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_cli1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_cli1MouseExited(evt);
            }
        });
        jbRegistrar_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_cli1ActionPerformed(evt);
            }
        });

        jSeparator15.setForeground(new java.awt.Color(0, 0, 0));

        jlGen_emp.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlGen_emp.setForeground(new java.awt.Color(51, 51, 51));
        jlGen_emp.setText("Género:");

        jlGenero_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlGenero_emp.setText(" ");

        jlE10.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE10.setForeground(new java.awt.Color(51, 51, 51));
        jlE10.setText("Sueldo:");

        jlSueldo_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlSueldo_emp.setText(" ");

        jlE11.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE11.setForeground(new java.awt.Color(51, 51, 51));
        jlE11.setText("Teléfono:");

        jlTelefono_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlTelefono_emp.setText(" ");

        jlCorreo_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCorreo_emp.setText(" ");

        jlE12.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE12.setForeground(new java.awt.Color(51, 51, 51));
        jlE12.setText("Correo:");

        jlDireccion_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlDireccion_emp.setText(" ");

        jlE13.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE13.setForeground(new java.awt.Color(51, 51, 51));
        jlE13.setText("Dirección:");

        javax.swing.GroupLayout jpDatos_empLayout = new javax.swing.GroupLayout(jpDatos_emp);
        jpDatos_emp.setLayout(jpDatos_empLayout);
        jpDatos_empLayout.setHorizontalGroup(
            jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jpDatos_empLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jbRegistrar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jbEnviar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jbModificar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jb_Eliminar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jpDatos_empLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlR5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlCedula_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlN5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlNombre_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlC3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlApellido_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlT2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlNac_emp, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)))
                .addGap(150, 150, 150)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlGen_emp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlGenero_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlE13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlDireccion_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlE12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlCorreo_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlE11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlTelefono_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(131, 131, 131)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlF2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlReg_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_empLayout.createSequentialGroup()
                        .addComponent(jlE10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlSueldo_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jpDatos_empLayout.setVerticalGroup(
            jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatos_empLayout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR5)
                    .addComponent(jlCedula_emp)
                    .addComponent(jlGen_emp)
                    .addComponent(jlGenero_emp)
                    .addComponent(jlE10)
                    .addComponent(jlSueldo_emp))
                .addGap(4, 4, 4)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlN5)
                    .addComponent(jlNombre_emp)
                    .addComponent(jlE11)
                    .addComponent(jlTelefono_emp)
                    .addComponent(jlReg_emp)
                    .addComponent(jlF2))
                .addGap(4, 4, 4)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlC3)
                    .addComponent(jlApellido_emp)
                    .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCorreo_emp)
                        .addComponent(jlE12)))
                .addGap(4, 4, 4)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlT2)
                    .addComponent(jlNac_emp)
                    .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlE13)
                        .addComponent(jlDireccion_emp)))
                .addGap(10, 10, 10)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jpDatos_empLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbRegistrar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEnviar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbModificar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_Eliminar_cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jcBuscar_emp.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_emp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "Nombre", "Apellido", "F. Nacimiento", "Género", "Teléfono", "Correo", "Dirección", "Sueldo", "F. Registro" }));
        jcBuscar_emp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_empItemStateChanged(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel39.setText("Buscar empleado por");

        jtBuscar_emp.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_emp.setText("Buscar");
        jtBuscar_emp.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_emp.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_emp.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_emp.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_emp.setSelectionEnd(0);
        jtBuscar_emp.setSelectionStart(0);
        jtBuscar_emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_empMouseClicked(evt);
            }
        });
        jtBuscar_emp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBuscar_empActionPerformed(evt);
            }
        });
        jtBuscar_emp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_empKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_empKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_empKeyTyped(evt);
            }
        });

        lim_emp.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_emp.setForeground(new java.awt.Color(0, 102, 102));
        lim_emp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_emp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_emp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_empMouseClicked(evt);
            }
        });

        JTempleados = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTempleados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTempleados.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTempleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTempleados.setFocusable(false);
        JTempleados.setGridColor(new java.awt.Color(255, 255, 255));
        JTempleados.setOpaque(false);
        JTempleados.setRowHeight(30);
        JTempleados.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTempleados.getTableHeader().setResizingAllowed(false);
        JTempleados.getTableHeader().setReorderingAllowed(false);
        jsTabla_cat8.setViewportView(JTempleados);

        jl_titulo11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo11.setText("Lista de empleados");
        jl_titulo11.setIconTextGap(10);
        jl_titulo11.setPreferredSize(new java.awt.Dimension(226, 32));
        jl_titulo11.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPempleadosLayout = new javax.swing.GroupLayout(JPempleados);
        JPempleados.setLayout(JPempleadosLayout);
        JPempleadosLayout.setHorizontalGroup(
            JPempleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPempleadosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPempleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(JPempleadosLayout.createSequentialGroup()
                        .addComponent(jl_titulo11, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel39)
                        .addGap(6, 6, 6)
                        .addComponent(jcBuscar_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jtBuscar_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lim_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(res_num_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_emp, javax.swing.GroupLayout.DEFAULT_SIZE, 1248, Short.MAX_VALUE)
                    .addComponent(jsTabla_cat8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPempleadosLayout.setVerticalGroup(
            JPempleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPempleadosLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(JPempleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(res_num_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lim_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtBuscar_emp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jl_titulo11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcBuscar_emp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jsTabla_cat8, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jpDatos_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        MENU.addTab("EMPLEADOS", JPempleados);

        JPproductos.setBackground(new java.awt.Color(204, 255, 255));

        res_num_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_pro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_pro.setText("Resultados: 0 de 0");

        jpDatos_pro.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_pro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_pro.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Producto seleccionado:");

        jlR6.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR6.setForeground(new java.awt.Color(51, 51, 51));
        jlR6.setText("Código:");

        jbEnviar_pro.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_pro.setText(" Enviar");
        jbEnviar_pro.setBorder(null);
        jbEnviar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_proMouseExited(evt);
            }
        });
        jbEnviar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_proActionPerformed(evt);
            }
        });

        jb_Eliminar_pro.setBackground(new java.awt.Color(255, 0, 51));
        jb_Eliminar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jb_Eliminar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jb_Eliminar_pro.setText(" Eliminar");
        jb_Eliminar_pro.setBorder(null);
        jb_Eliminar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Eliminar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jb_Eliminar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jb_Eliminar_proMouseExited(evt);
            }
        });
        jb_Eliminar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_Eliminar_proActionPerformed(evt);
            }
        });

        jbModificar_pro.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_pro.setText(" Modificar");
        jbModificar_pro.setBorder(null);
        jbModificar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_proMouseExited(evt);
            }
        });
        jbModificar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_proActionPerformed(evt);
            }
        });

        jlN6.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN6.setForeground(new java.awt.Color(51, 51, 51));
        jlN6.setText("Nombre:");

        jlC4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC4.setForeground(new java.awt.Color(51, 51, 51));
        jlC4.setText("Precio: $");

        jlF3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlF3.setForeground(new java.awt.Color(51, 51, 51));
        jlF3.setText("Fecha de registro:");

        jlCodigo_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCodigo_pro.setText(" ");

        jlNombre_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_pro.setText(" ");

        jlPrecio_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlPrecio_pro.setText(" ");

        jlReg_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlReg_pro.setText(" ");

        jSeparator16.setForeground(new java.awt.Color(0, 0, 0));

        jbRegistrar_pro.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_pro.setText("Agregar");
        jbRegistrar_pro.setBorder(null);
        jbRegistrar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_proMouseExited(evt);
            }
        });
        jbRegistrar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_proActionPerformed(evt);
            }
        });

        jlGen_emp1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlGen_emp1.setForeground(new java.awt.Color(51, 51, 51));
        jlGen_emp1.setText("Existencias:");

        jlExistencias_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlExistencias_pro.setText(" ");

        jlE15.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE15.setForeground(new java.awt.Color(51, 51, 51));
        jlE15.setText("Categoría:");

        jlCategoria_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCategoria_pro.setText(" ");

        jlProveedor_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlProveedor_pro.setText(" ");

        jlE16.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE16.setForeground(new java.awt.Color(51, 51, 51));
        jlE16.setText("Proveedor:");

        jSeparator17.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jpDatos_proLayout = new javax.swing.GroupLayout(jpDatos_pro);
        jpDatos_pro.setLayout(jpDatos_proLayout);
        jpDatos_proLayout.setHorizontalGroup(
            jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator16)
            .addComponent(jSeparator17)
            .addGroup(jpDatos_proLayout.createSequentialGroup()
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDatos_proLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jpDatos_proLayout.createSequentialGroup()
                                .addComponent(jlF3)
                                .addGap(5, 5, 5)
                                .addComponent(jlReg_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpDatos_proLayout.createSequentialGroup()
                                .addComponent(jlE16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlProveedor_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpDatos_proLayout.createSequentialGroup()
                                .addComponent(jlE15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlCategoria_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpDatos_proLayout.createSequentialGroup()
                                .addComponent(jlR6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlCodigo_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpDatos_proLayout.createSequentialGroup()
                                .addComponent(jlN6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlNombre_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpDatos_proLayout.createSequentialGroup()
                                .addComponent(jlC4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlPrecio_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpDatos_proLayout.createSequentialGroup()
                                .addComponent(jlGen_emp1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlExistencias_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jpDatos_proLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbRegistrar_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbModificar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jb_Eliminar_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbEnviar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jpDatos_proLayout.setVerticalGroup(
            jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatos_proLayout.createSequentialGroup()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR6)
                    .addComponent(jlCodigo_pro))
                .addGap(6, 6, 6)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlN6)
                    .addComponent(jlNombre_pro))
                .addGap(6, 6, 6)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlC4)
                    .addComponent(jlPrecio_pro))
                .addGap(6, 6, 6)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlGen_emp1)
                    .addComponent(jlExistencias_pro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlE15)
                    .addComponent(jlCategoria_pro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlE16)
                    .addComponent(jlProveedor_pro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlF3)
                    .addComponent(jlReg_pro))
                .addGap(18, 18, 18)
                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbRegistrar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEnviar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpDatos_proLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jb_Eliminar_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbModificar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(104, 104, 104))
        );

        jcBuscar_pro.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_pro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Nombre", "Precio", "Existencias", "Categoría", "Proveedor", "F. Registro" }));
        jcBuscar_pro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_proItemStateChanged(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel40.setText("Buscar producto por");

        jtBuscar_pro.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_pro.setText("Buscar");
        jtBuscar_pro.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_pro.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_pro.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_pro.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_pro.setSelectionEnd(0);
        jtBuscar_pro.setSelectionStart(0);
        jtBuscar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_proMouseClicked(evt);
            }
        });
        jtBuscar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBuscar_proActionPerformed(evt);
            }
        });
        jtBuscar_pro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_proKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_proKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_proKeyTyped(evt);
            }
        });

        lim_pro.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_pro.setForeground(new java.awt.Color(0, 102, 102));
        lim_pro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_proMouseClicked(evt);
            }
        });

        JTproductos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTproductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTproductos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTproductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTproductos.setFocusable(false);
        JTproductos.setGridColor(new java.awt.Color(255, 255, 255));
        JTproductos.setOpaque(false);
        JTproductos.setRowHeight(60);
        JTproductos.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTproductos.getTableHeader().setResizingAllowed(false);
        JTproductos.getTableHeader().setReorderingAllowed(false);
        jsTabla_pro.setViewportView(JTproductos);

        jl_titulo12.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo12.setText("Lista de productos");
        jl_titulo12.setIconTextGap(10);
        jl_titulo12.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPproductosLayout = new javax.swing.GroupLayout(JPproductos);
        JPproductos.setLayout(JPproductosLayout);
        JPproductosLayout.setHorizontalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproductosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addComponent(res_num_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPproductosLayout.createSequentialGroup()
                                .addComponent(jl_titulo12, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(188, 188, 188)
                                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(JPproductosLayout.createSequentialGroup()
                                        .addComponent(jLabel40)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcBuscar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jtBuscar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jsTabla_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(lim_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpDatos_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        JPproductosLayout.setVerticalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproductosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(JPproductosLayout.createSequentialGroup()
                                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jcBuscar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lim_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jtBuscar_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jl_titulo12, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jsTabla_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 423, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(res_num_pro)
                .addGap(127, 127, 127))
        );

        MENU.addTab("PRODUCTOS", JPproductos);

        JPproveedores.setBackground(new java.awt.Color(204, 255, 255));

        res_num_prov.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_prov.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_prov.setText("Resultados: 0 de 0");

        jpDatos_prov.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_prov.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_prov.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Proveedor seleccionado:");

        jlR.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR.setForeground(new java.awt.Color(51, 51, 51));
        jlR.setText("RUC:");

        jlEmail.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlEmail.setText(" ");

        jbEnviar_prov.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_prov.setText(" Enviar");
        jbEnviar_prov.setBorder(null);
        jbEnviar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_provMouseExited(evt);
            }
        });
        jbEnviar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_provActionPerformed(evt);
            }
        });

        jb_Eliminar_prov.setBackground(new java.awt.Color(255, 0, 51));
        jb_Eliminar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jb_Eliminar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jb_Eliminar_prov.setText(" Eliminar");
        jb_Eliminar_prov.setBorder(null);
        jb_Eliminar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Eliminar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jb_Eliminar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jb_Eliminar_provMouseExited(evt);
            }
        });
        jb_Eliminar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_Eliminar_provActionPerformed(evt);
            }
        });

        jbModificar_prov.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_prov.setText(" Modificar");
        jbModificar_prov.setBorder(null);
        jbModificar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_provMouseExited(evt);
            }
        });
        jbModificar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_provActionPerformed(evt);
            }
        });

        jlN.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN.setForeground(new java.awt.Color(51, 51, 51));
        jlN.setText("Nombre:");

        jlC.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC.setForeground(new java.awt.Color(51, 51, 51));
        jlC.setText("Ciudad:");

        jlT.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlT.setForeground(new java.awt.Color(51, 51, 51));
        jlT.setText("Teléfono:");

        jlE.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE.setForeground(new java.awt.Color(51, 51, 51));
        jlE.setText("Correo:");

        jlF.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlF.setForeground(new java.awt.Color(51, 51, 51));
        jlF.setText("Fecha de registro:");

        jlRUC.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlRUC.setText(" ");

        jlNombre.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre.setText(" ");

        jlCiudad.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCiudad.setText(" ");

        jlTelefono.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlTelefono.setText(" ");

        jlFecha_reg.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlFecha_reg.setText(" ");

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        jbRegistrar_prov.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_prov.setText("Agregar");
        jbRegistrar_prov.setBorder(null);
        jbRegistrar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_provMouseExited(evt);
            }
        });
        jbRegistrar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_provActionPerformed(evt);
            }
        });

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jpDatos_provLayout = new javax.swing.GroupLayout(jpDatos_prov);
        jpDatos_prov.setLayout(jpDatos_provLayout);
        jpDatos_provLayout.setHorizontalGroup(
            jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jpDatos_provLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbRegistrar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addComponent(jbModificar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jb_Eliminar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jbEnviar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addComponent(jlF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlFecha_reg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addComponent(jlE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addComponent(jlR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlRUC, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addComponent(jlN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addComponent(jlC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlCiudad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addComponent(jlT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(22, Short.MAX_VALUE))
            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jpDatos_provLayout.setVerticalGroup(
            jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_provLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR)
                    .addComponent(jlRUC))
                .addGap(6, 6, 6)
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlN)
                    .addComponent(jlNombre))
                .addGap(6, 6, 6)
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlC)
                    .addComponent(jlCiudad))
                .addGap(6, 6, 6)
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlT)
                    .addComponent(jlTelefono))
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDatos_provLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jlE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatos_provLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlEmail)))
                .addGap(6, 6, 6)
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlF)
                    .addComponent(jlFecha_reg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jbRegistrar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbEnviar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jpDatos_provLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jb_Eliminar_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbModificar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jcBuscar_prov.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_prov.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RUC", "Nombre", "Ciudad", "Teléfono", "Correo", "F. Registro" }));
        jcBuscar_prov.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_provItemStateChanged(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("Buscar proveedor por");

        jtBuscar_prov.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_prov.setText("Buscar");
        jtBuscar_prov.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_prov.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_prov.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_prov.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_prov.setSelectionEnd(0);
        jtBuscar_prov.setSelectionStart(0);
        jtBuscar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_provMouseClicked(evt);
            }
        });
        jtBuscar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBuscar_provActionPerformed(evt);
            }
        });
        jtBuscar_prov.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_provKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_provKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_provKeyTyped(evt);
            }
        });

        lim_prov.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_prov.setForeground(new java.awt.Color(0, 102, 102));
        lim_prov.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_provMouseClicked(evt);
            }
        });

        JTproveedores = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTproveedores.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTproveedores.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTproveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTproveedores.setFocusable(false);
        JTproveedores.setGridColor(new java.awt.Color(255, 255, 255));
        JTproveedores.setOpaque(false);
        JTproveedores.setRowHeight(30);
        JTproveedores.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTproveedores.getTableHeader().setResizingAllowed(false);
        JTproveedores.getTableHeader().setReorderingAllowed(false);
        jsTabla_cat6.setViewportView(JTproveedores);

        jl_titulo13.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo13.setText("Lista de productos");
        jl_titulo13.setIconTextGap(10);
        jl_titulo13.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPproveedoresLayout = new javax.swing.GroupLayout(JPproveedores);
        JPproveedores.setLayout(JPproveedoresLayout);
        JPproveedoresLayout.setHorizontalGroup(
            JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproveedoresLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                        .addComponent(res_num_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                        .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPproveedoresLayout.createSequentialGroup()
                                .addComponent(jl_titulo13, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(157, 157, 157)
                                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                                        .addComponent(jtBuscar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(lim_prov))
                                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                                        .addComponent(jLabel34)
                                        .addGap(3, 3, 3)
                                        .addComponent(jcBuscar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jsTabla_cat6, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpDatos_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        JPproveedoresLayout.setVerticalGroup(
            JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproveedoresLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                        .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(JPproveedoresLayout.createSequentialGroup()
                                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jcBuscar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lim_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jtBuscar_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jl_titulo13, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jsTabla_cat6, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(res_num_prov))
        );

        MENU.addTab("PROVEEDORES", JPproveedores);

        getContentPane().add(MENU, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1264, 560));

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));

        sistema_titulo.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        sistema_titulo.setForeground(new java.awt.Color(255, 255, 255));
        sistema_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sistema_titulo.setText("Sistema Papelería");

        FECHA_HORA.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        FECHA_HORA.setForeground(new java.awt.Color(255, 255, 255));
        FECHA_HORA.setText("FECHA_HORA");

        USUARIO.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        USUARIO.setForeground(new java.awt.Color(255, 255, 255));
        USUARIO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario-de-perfil.png"))); // NOI18N
        USUARIO.setText("USUARIO");
        USUARIO.setIconTextGap(10);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        SALIR.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        SALIR.setForeground(new java.awt.Color(255, 255, 255));
        SALIR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar-sesion (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sistema_titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FECHA_HORA, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(USUARIO, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(SALIR)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sistema_titulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(USUARIO)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(FECHA_HORA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SALIR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1264, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbEnviar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_provActionPerformed
        if (!jlRUC.getText().equals(" ")) {
            JFproducto.jt_proveedor.setText(jlRUC.getText());
            SISTEMA.MENU.setSelectedIndex(6);
            JFpro.setVisible(true);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_provActionPerformed

    private void jb_Eliminar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_Eliminar_provActionPerformed
        if (!jlRUC.getText().equals(" ")) {
            eliminar(10);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jb_Eliminar_provActionPerformed

    private void jbModificar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_provActionPerformed
        if (!jlRUC.getText().equals(" ")) {
            JFproveedor.forma = "modificar";
            JFproveedor.cambiar_diseño();
            JFprov.llenar(jlRUC.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }

    }//GEN-LAST:event_jbModificar_provActionPerformed

    private void jcBuscar_provItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_provItemStateChanged

    }//GEN-LAST:event_jcBuscar_provItemStateChanged

    private void jtBuscar_provMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_provMouseClicked
        if (jtBuscar_prov.getText().equals("Buscar")) {
            jtBuscar_prov.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_provMouseClicked

    private void jtBuscar_provKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_provKeyPressed
        if (jtBuscar_prov.getText().equals("Buscar")) {
            jtBuscar_prov.setText("");
            lim_prov.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_provKeyPressed

    private void jtBuscar_provKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_provKeyReleased
        if (!jtBuscar_prov.getText().equals("")) {
            buscar(JTproveedores, jtBuscar_prov, res_num_prov, prov, jcBuscar_prov);
        } else {
            lim_prov.setVisible(false);
            jtBuscar_prov.setText("Buscar");
            jtBuscar_prov.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_provKeyReleased

    private void jtBuscar_provKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_provKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_provKeyTyped

    private void lim_provMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_provMouseClicked
        lim_prov.setVisible(false);
        jtBuscar_prov.setText("Buscar");
        jtBuscar_prov.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_provMouseClicked

    private void jbEnviar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_provMouseEntered
        jbEnviar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jbEnviar_provMouseEntered

    private void jbEnviar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_provMouseExited
        jbEnviar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jbEnviar_provMouseExited

    private void jbModificar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_provMouseEntered
        jbModificar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jbModificar_provMouseEntered

    private void jbModificar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_provMouseExited
        jbModificar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jbModificar_provMouseExited

    private void jb_Eliminar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_provMouseEntered
        jb_Eliminar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jb_Eliminar_provMouseEntered

    private void jb_Eliminar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_provMouseExited
        jb_Eliminar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jb_Eliminar_provMouseExited

    private void jbRegistrar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_provMouseEntered
        jbRegistrar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jbRegistrar_provMouseEntered

    private void jbRegistrar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_provMouseExited
        jbRegistrar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jbRegistrar_provMouseExited

    private void jbRegistrar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_provActionPerformed
        JFproveedor.forma = "registrar";
        JFproveedor.cambiar_diseño();
        JFproveedor.limpiar();
        JFprov.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_provActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if (actualizado == false) {
            visualizar();
            actualizado = true;
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void jcBuscar_ciuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_ciuItemStateChanged

    }//GEN-LAST:event_jcBuscar_ciuItemStateChanged

    private void jtBuscar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBuscar_provActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_provActionPerformed

    private void lim_ciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_ciuMouseClicked
        lim_ciu.setVisible(false);
        jtBuscar_ciu.setText("Buscar");
        jtBuscar_ciu.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_ciuMouseClicked

    private void jtBuscar_ciuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_ciuKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_ciuKeyTyped

    private void jtBuscar_ciuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_ciuKeyReleased
        if (!jtBuscar_ciu.getText().equals("")) {
            buscar(JTciudades, jtBuscar_ciu, res_num_ciu, ciu, jcBuscar_ciu);
        } else {
            lim_ciu.setVisible(false);
            jtBuscar_ciu.setText("Buscar");
            jtBuscar_ciu.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_ciuKeyReleased

    private void jtBuscar_ciuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_ciuKeyPressed
        if (jtBuscar_ciu.getText().equals("Buscar")) {
            jtBuscar_ciu.setText("");
            lim_ciu.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_ciuKeyPressed

    private void jtBuscar_ciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_ciuMouseClicked
        if (jtBuscar_ciu.getText().equals("Buscar")) {
            jtBuscar_ciu.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_ciuMouseClicked

    private void jbEnviar_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_cliMouseEntered
        jbEnviar_cli.setForeground(Color.white);
    }//GEN-LAST:event_jbEnviar_cliMouseEntered

    private void jbEnviar_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_cliMouseExited
        jbEnviar_cli.setForeground(Color.black);
    }//GEN-LAST:event_jbEnviar_cliMouseExited

    private void jbEnviar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_cliActionPerformed
        if (!jlCedula_cli.getText().equals(" ")) {
            base.abrir();
            Cliente c = new Cliente(null,jlCedula_cli.getText(),null,null,null,null,null,null,null,null);
            resultado = base.gettear(c);
            if (!resultado.isEmpty()) {
                c = (Cliente)resultado.next();
                enc_cedula.setText(c.getCedula());
                enc_nombre_apellido.setText(c.getNombre()+" "+c.getApellido());
                enc_direccion.setText(c.getDireccion());
                enc_telefono.setText(c.getTelefono());
                enc_correo.setText(c.getCorreo());
                Descuento d = new Descuento(c.getDescuento(),0);
                resultado = base.gettear(d);
                d = (Descuento)resultado.next();
                descuento = d.getPorcentaje();
                MENU.setSelectedIndex(0);
                INICIO.setSelectedIndex(0);
            }
            base.cerrar();
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_cliActionPerformed

    private void jb_Eliminar_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_cliMouseEntered
        jb_Eliminar_cli.setForeground(Color.white);
    }//GEN-LAST:event_jb_Eliminar_cliMouseEntered

    private void jb_Eliminar_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_cliMouseExited
        jb_Eliminar_cli.setForeground(Color.black);
    }//GEN-LAST:event_jb_Eliminar_cliMouseExited

    private void jb_Eliminar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_Eliminar_cliActionPerformed
        if (!jlCedula_cli.getText().equals(" ")) {
            eliminar(3);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jb_Eliminar_cliActionPerformed

    private void jbModificar_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_cliMouseEntered
        jbModificar_cli.setForeground(Color.white);
    }//GEN-LAST:event_jbModificar_cliMouseEntered

    private void jbModificar_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_cliMouseExited
        jbModificar_cli.setForeground(Color.black);
    }//GEN-LAST:event_jbModificar_cliMouseExited

    private void jbModificar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_cliActionPerformed
        if (!jlCedula_cli.getText().equals(" ")) {
            JFcliente.forma = "modificar";
            JFcliente.cambiar_diseño();
            JFcli.llenar(jlCedula_cli.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }

    }//GEN-LAST:event_jbModificar_cliActionPerformed

    private void jbRegistrar_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_cliMouseEntered
        jbRegistrar_cli.setForeground(Color.white);
    }//GEN-LAST:event_jbRegistrar_cliMouseEntered

    private void jbRegistrar_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_cliMouseExited
        jbRegistrar_cli.setForeground(Color.black);
    }//GEN-LAST:event_jbRegistrar_cliMouseExited

    private void jbRegistrar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_cliActionPerformed
        JFcliente.forma = "registrar";
        JFcliente.cambiar_diseño();
        JFcliente.limpiar();
        JFcli.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_cliActionPerformed

    private void jcBuscar_cliItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_cliItemStateChanged

    }//GEN-LAST:event_jcBuscar_cliItemStateChanged

    private void jtBuscar_cliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_cliMouseClicked
        if (jtBuscar_cli.getText().equals("Buscar")) {
            jtBuscar_cli.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_cliMouseClicked

    private void jtBuscar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBuscar_cliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_cliActionPerformed

    private void jtBuscar_cliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_cliKeyPressed
        if (jtBuscar_cli.getText().equals("Buscar")) {
            jtBuscar_cli.setText("");
            lim_cli.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_cliKeyPressed

    private void jtBuscar_cliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_cliKeyReleased
        if (!jtBuscar_cli.getText().equals("")) {
            buscar(JTclientes, jtBuscar_cli, res_num_cli, cli, jcBuscar_cli);
        } else {
            lim_cli.setVisible(false);
            jtBuscar_cli.setText("Buscar");
            jtBuscar_cli.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_cliKeyReleased

    private void jtBuscar_cliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_cliKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_cliKeyTyped

    private void lim_cliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_cliMouseClicked
        lim_cli.setVisible(false);
        jtBuscar_cli.setText("Buscar");
        jtBuscar_cli.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_cliMouseClicked

    private void jbEnviar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_desMouseEntered

    private void jbEnviar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_desMouseExited

    private void jbEnviar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_desActionPerformed
        if (!jlNombre_des.getText().equals(" ")) {
            JFcliente.descuento.setText(jlNombre_des.getText());
            SISTEMA.MENU.setSelectedIndex(3);
            JFcli.setVisible(true);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_desActionPerformed

    private void jbEliminar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_desMouseEntered

    private void jbEliminar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_desMouseExited

    private void jbEliminar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_desActionPerformed
        if (!jlNombre_des.getText().equals(" ")) {
            eliminar(4);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEliminar_desActionPerformed

    private void jbModificar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_desMouseEntered

    private void jbModificar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_desMouseExited

    private void jbModificar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_desActionPerformed
        if (!jlNombre_des.getText().equals(" ")) {
            JFdescuento.forma = "modificar";
            JFdescuento.cambiar_diseño();
            JFdes.llenar(jlNombre_des.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbModificar_desActionPerformed

    private void jbRegistrar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_desMouseEntered

    private void jbRegistrar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_desMouseExited

    private void jbRegistrar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_desActionPerformed
        JFdescuento.forma = "registrar";
        JFdescuento.cambiar_diseño();
        JFdescuento.limpiar();
        JFdes.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_desActionPerformed

    private void jcBuscar_desItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_desItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_desItemStateChanged

    private void jtBuscar_desMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_desMouseClicked
        if (jtBuscar_des.getText().equals("Buscar")) {
            jtBuscar_des.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_desMouseClicked

    private void jtBuscar_desKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_desKeyPressed
        if (jtBuscar_des.getText().equals("Buscar")) {
            jtBuscar_des.setText("");
            lim_des.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_desKeyPressed

    private void jtBuscar_desKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_desKeyReleased
        if (!jtBuscar_des.getText().equals("")) {
            buscar(JTdescuentos, jtBuscar_des, res_num_des, des, jcBuscar_des);
        } else {
            lim_des.setVisible(false);
            jtBuscar_des.setText("Buscar");
            jtBuscar_des.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_desKeyReleased

    private void jtBuscar_desKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_desKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_desKeyTyped

    private void lim_desMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_desMouseClicked
        lim_des.setVisible(false);
        jtBuscar_des.setText("Buscar");
        jtBuscar_des.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_desMouseClicked

    private void jbEliminar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_catMouseEntered
        jbEliminar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbEliminar_catMouseEntered

    private void jbEliminar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_catMouseExited
        jbEliminar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbEliminar_catMouseExited

    private void jbEliminar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_catActionPerformed
        if (!jlNombre_cat.getText().equals(" ")) {
            eliminar(1);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEliminar_catActionPerformed

    private void jbModificar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_catMouseEntered
        jbModificar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbModificar_catMouseEntered

    private void jbModificar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_catMouseExited
        jbModificar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbModificar_catMouseExited

    private void jbModificar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_catActionPerformed
        if (!jlNombre_cat.getText().equals(" ")) {
            JFcategoria.forma = "modificar";
            JFcategoria.cambiar_diseño();
            JFcat.llenar(jlNombre_cat.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbModificar_catActionPerformed

    private void jbRegistrar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_catMouseEntered
        jbRegistrar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbRegistrar_catMouseEntered

    private void jbRegistrar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_catMouseExited
        jbRegistrar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbRegistrar_catMouseExited

    private void jbRegistrar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_catActionPerformed
        JFcategoria.forma = "registrar";
        JFcategoria.cambiar_diseño();
        JFcategoria.limpiar();
        JFcat.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_catActionPerformed

    private void jcBuscar_catItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_catItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_catItemStateChanged

    private void jtBuscar_catMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_catMouseClicked
        if (jtBuscar_cat.getText().equals("Buscar")) {
            jtBuscar_cat.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_catMouseClicked

    private void jtBuscar_catKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_catKeyPressed
        if (jtBuscar_cat.getText().equals("Buscar")) {
            jtBuscar_cat.setText("");
            lim_cat.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_catKeyPressed

    private void jtBuscar_catKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_catKeyReleased
        if (!jtBuscar_cat.getText().equals("")) {
            buscar(JTcategorias, jtBuscar_cat, res_num_cat, cat, jcBuscar_cat);
        } else {
            lim_cat.setVisible(false);
            jtBuscar_cat.setText("Buscar");
            jtBuscar_cat.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_catKeyReleased

    private void jtBuscar_catKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_catKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_catKeyTyped

    private void lim_catMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_catMouseClicked
        lim_cat.setVisible(false);
        jtBuscar_cat.setText("Buscar");
        jtBuscar_cat.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_catMouseClicked

    private void jbEnviar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_catActionPerformed
        if (!jlNombre_cat.getText().equals(" ")) {
            JFproducto.jt_categoria.setText(jlNombre_cat.getText());
            SISTEMA.MENU.setSelectedIndex(6);
            JFpro.setVisible(true);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_catActionPerformed

    private void jbEnviar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_catMouseExited
        jbEnviar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbEnviar_catMouseExited

    private void jbEnviar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_catMouseEntered
        jbEnviar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbEnviar_catMouseEntered

    private void enc_cedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enc_cedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enc_cedulaActionPerformed

    private void jbEnviar_cli1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_cli1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_cli1MouseEntered

    private void jbEnviar_cli1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_cli1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_cli1MouseExited

    private void jbEnviar_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_cli1ActionPerformed
        if (!jlCedula_emp.getText().equals(" ")) {
            /*
            
             */
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_cli1ActionPerformed

    private void jb_Eliminar_cli1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_cli1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_cli1MouseEntered

    private void jb_Eliminar_cli1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_cli1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_cli1MouseExited

    private void jb_Eliminar_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_Eliminar_cli1ActionPerformed
        if (!jlCedula_emp.getText().equals(" ")) {
            eliminar(6);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jb_Eliminar_cli1ActionPerformed

    private void jbModificar_cli1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_cli1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_cli1MouseEntered

    private void jbModificar_cli1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_cli1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_cli1MouseExited

    private void jbModificar_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_cli1ActionPerformed

        if (!jlCedula_emp.getText().equals(" ")) {
            JFempleado.forma = "modificar";
            JFempleado.cambiar_diseño();
            JFemp.llenar(jlCedula_emp.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbModificar_cli1ActionPerformed

    private void jbRegistrar_cli1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_cli1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_cli1MouseEntered

    private void jbRegistrar_cli1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_cli1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_cli1MouseExited

    private void jbRegistrar_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_cli1ActionPerformed

        JFempleado.forma = "registrar";
        JFempleado.cambiar_diseño();
        JFempleado.limpiar();
        JFemp.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_cli1ActionPerformed

    private void jcBuscar_empItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_empItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_empItemStateChanged

    private void jtBuscar_empMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_empMouseClicked
        if (jtBuscar_emp.getText().equals("Buscar")) {
            jtBuscar_emp.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_empMouseClicked

    private void jtBuscar_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBuscar_empActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_empActionPerformed

    private void jtBuscar_empKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_empKeyPressed
        if (jtBuscar_emp.getText().equals("Buscar")) {
            jtBuscar_emp.setText("");
            lim_emp.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_empKeyPressed

    private void jtBuscar_empKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_empKeyReleased
        if (!jtBuscar_emp.getText().equals("")) {
            buscar(JTempleados, jtBuscar_emp, res_num_emp, emp, jcBuscar_emp);
        } else {
            lim_emp.setVisible(false);
            jtBuscar_emp.setText("Buscar");
            jtBuscar_emp.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_empKeyReleased

    private void jtBuscar_empKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_empKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_empKeyTyped

    private void lim_empMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_empMouseClicked
        lim_emp.setVisible(false);
        jtBuscar_emp.setText("Buscar");
        jtBuscar_emp.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_empMouseClicked

    private void jbEnviar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_proMouseEntered

    private void jbEnviar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_proMouseExited

    private void jbEnviar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_proActionPerformed
        if (!jlCodigo_pro.getText().equals(" ")) {
            int existencias = Integer.parseInt(jlExistencias_pro.getText());
            if (existencias > 0) {
                boolean repetido = false;
                for (int i = 0; i < detalles.size(); i++) {
                    if (detalles.get(i).equals(jlCodigo_pro.getText())) {
                        repetido = true;
                        break;
                    }
                }
                if (repetido) {
                    JOptionPane.showMessageDialog(null, "¡Este producto ya fué seleccionado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad:", 1));
                        if (cantidad > 0 && cantidad <= Integer.parseInt(jlExistencias_pro.getText())) {
                            Double xprecio = Double.valueOf(jlPrecio_pro.getText());
                            JTdetalle.setDefaultRenderer(Object.class, new BotonTabla());
                            Object detalle[] = {jlCodigo_pro.getText(), jlNombre_pro.getText(), cantidad, xprecio,descuento+"%", xprecio - ((descuento*xprecio)/100), Math.round((cantidad * (xprecio - ((descuento*xprecio)/100))) * 100.0) / 100.0, boton1};

                            detalles.add(jlCodigo_pro.getText());
                            //-------------- Agrega un detalle a la tabla
                            tabla_detalle.addRow(detalle);
                            JTdetalle.setModel(tabla_detalle);
                            JTdetalle.setRowHeight(35);
                            //Confirmar_venta.setEnabled(true);
                            //--------------- Actualiza variables
                            
                            
                            num_det++;
                            jl_num_det.setText("Detalles: "+String.valueOf(num_det));
                            total += (Math.round((cantidad * (xprecio - ((descuento*xprecio)/100))) * 100.0) / 100.0);
                            jl_total.setText(String.valueOf("Total: $"+total));
                            
                            //valor_total.setText("VALOR TOTAL: $" + String.valueOf(xvalor_total));

                            //---------- Redireccionar
                            MENU.setSelectedIndex(0);
                            INICIO.setSelectedIndex(0);
                        } else {
                            if (cantidad > Integer.parseInt(jlExistencias_pro.getText())) {
                                JOptionPane.showMessageDialog(null, "¡Solo existen '" + jlExistencias_pro.getText() + "' de este producto!", null, JOptionPane.WARNING_MESSAGE);
                            }
                            if (cantidad <= 0) {
                                JOptionPane.showMessageDialog(null, "¡El mínimo de venta es de 1!", null, JOptionPane.WARNING_MESSAGE);
                            }

                        }

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "¡Cantiadad inválida!", null, JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "¡Producto agotado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
            }
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_proActionPerformed

    private void jb_Eliminar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_proMouseEntered

    private void jb_Eliminar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_proMouseExited

    private void jb_Eliminar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_Eliminar_proActionPerformed
        if (!jlCodigo_pro.getText().equals(" ")) {
            eliminar(9);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jb_Eliminar_proActionPerformed

    private void jbModificar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_proMouseEntered

    private void jbModificar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_proMouseExited

    private void jbModificar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_proActionPerformed
        if (!jlCodigo_pro.getText().equals(" ")) {
            JFproducto.forma = "modificar";
            JFproducto.cambiar_diseño();
            JFpro.llenar(Integer.parseInt(jlCodigo_pro.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbModificar_proActionPerformed

    private void jbRegistrar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_proMouseEntered

    private void jbRegistrar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_proMouseExited

    private void jbRegistrar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_proActionPerformed
        JFproducto.forma = "registrar";
        JFproducto.cambiar_diseño();
        JFproducto.limpiar();
        JFpro.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_proActionPerformed

    private void jcBuscar_proItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_proItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_proItemStateChanged

    private void jtBuscar_proMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_proMouseClicked
        if (jtBuscar_pro.getText().equals("Buscar")) {
            jtBuscar_pro.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_proMouseClicked

    private void jtBuscar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBuscar_proActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_proActionPerformed

    private void jtBuscar_proKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_proKeyPressed
        if (jtBuscar_pro.getText().equals("Buscar")) {
            jtBuscar_pro.setText("");
            lim_pro.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_proKeyPressed

    private void jtBuscar_proKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_proKeyReleased
        if (!jtBuscar_pro.getText().equals("")) {
            buscar(JTproductos, jtBuscar_pro, res_num_pro, pro, jcBuscar_pro);
        } else {
            lim_pro.setVisible(false);
            jtBuscar_pro.setText("Buscar");
            jtBuscar_pro.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_proKeyReleased

    private void jtBuscar_proKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_proKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_proKeyTyped

    private void lim_proMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_proMouseClicked
        lim_pro.setVisible(false);
        jtBuscar_pro.setText("Buscar");
        jtBuscar_pro.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_proMouseClicked

    private void jbRegistrar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_ciuMouseEntered
        jbRegistrar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbRegistrar_ciuMouseEntered

    private void jbRegistrar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_ciuMouseExited
        jbRegistrar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbRegistrar_ciuMouseExited

    private void jbRegistrar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_ciuActionPerformed
        JFciudad.forma = "registrar";
        JFciudad.cambiar_diseño();
        JFciudad.limpiar();
        JFciudad.cargar_ciudades();
        JFciu.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_ciuActionPerformed

    private void jbEnviar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_ciuMouseEntered
        jbEnviar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbEnviar_ciuMouseEntered

    private void jbEnviar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_ciuMouseExited
        jbEnviar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbEnviar_ciuMouseExited

    private void jbEnviar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_ciuActionPerformed
        if (!jlCodigo_ciu.getText().equals(" ")) {
            JFproveedor.jt_ciudad.setText(jlCodigo_ciu.getText());
            SISTEMA.MENU.setSelectedIndex(5);
            JFprov.setVisible(true);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_ciuActionPerformed

    private void jbModificar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_ciuMouseEntered
        jbModificar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbModificar_ciuMouseEntered

    private void jbModificar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_ciuMouseExited
        jbModificar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbModificar_ciuMouseExited

    private void jbModificar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_ciuActionPerformed
        if (!jlCodigo_ciu.getText().equals(" ")) {
            JFciudad.forma = "modificar";
            JFciudad.cambiar_diseño();
            JFciudad.cargar_ciudades_mod(jlNombre_ciu.getText());
            JFciu.llenar(Integer.parseInt(jlCodigo_ciu.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbModificar_ciuActionPerformed

    private void jbEliminar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_ciuMouseEntered
        jbEliminar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbEliminar_ciuMouseEntered

    private void jbEliminar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_ciuMouseExited
        jbEliminar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbEliminar_ciuMouseExited

    private void jbEliminar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_ciuActionPerformed
        if (!jlCodigo_ciu.getText().equals(" ")) {
            eliminar(2);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEliminar_ciuActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        MENU.setSelectedIndex(6);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void enc_cedulaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enc_cedulaMouseClicked
        MENU.setSelectedIndex(3);
    }//GEN-LAST:event_enc_cedulaMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    
    public void InsertarIcono(JButton bot, String ruta){ //insertar icono en boton:
        bot.setIcon(new javax.swing.ImageIcon(getClass().getResource(ruta)));
    }
    
    
    //método para dar click o doble click en las tablas:
    public void seleccionar() {
        JTcategorias.addMouseListener(new MouseAdapter() { //categorias(1)
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlNombre_cat.setText(JTcategorias.getValueAt(JTcategorias.getSelectedRow(), 0).toString());
                    jtaDescripcion_cat.setText(JTcategorias.getValueAt(JTcategorias.getSelectedRow(), 1).toString());

                }
                if (Mouse_evt.getClickCount() == 2) {
                    /*
                    JFproveedor.jt_ciudad.setText(jlCodigo_ciu.getText());
                    SISTEMA.MENU.setSelectedIndex(1);
                    JFprov.setVisible(true);
                     */
                }
            }
        });
        JTciudades.addMouseListener(new MouseAdapter() { //ciudades(2)
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlCodigo_ciu.setText(JTciudades.getValueAt(JTciudades.getSelectedRow(), 0).toString());
                    jlNombre_ciu.setText(JTciudades.getValueAt(JTciudades.getSelectedRow(), 1).toString());
                    jlProvincia_ciu.setText(JTciudades.getValueAt(JTciudades.getSelectedRow(), 2).toString());
                }
                if (Mouse_evt.getClickCount() == 2) {
                    JFproveedor.jt_ciudad.setText(jlCodigo_ciu.getText());
                    SISTEMA.MENU.setSelectedIndex(1);
                    JFprov.setVisible(true);
                }
            }
        });
        JTclientes.addMouseListener(new MouseAdapter() { //clientes(3)
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlCedula_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 0).toString());
                    jlNombre_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 1).toString());
                    jlApellido_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 2).toString());
                    jlNac_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 3).toString());
                    jlGenero_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 4).toString());
                    jlTelefono_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 5).toString());
                    jlCorreo_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 6).toString());
                    jlDireccion_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 7).toString());
                    jlDescuento_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 8).toString());
                    jlReg_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 9).toString());
                }
                if (Mouse_evt.getClickCount() == 2) {
                    base.abrir();
                    Cliente c = new Cliente(null, jlCedula_cli.getText(), null, null, null, null, null, null, null, null);
                    resultado = base.gettear(c);
                    if (!resultado.isEmpty()) {
                        c = (Cliente) resultado.next();
                        enc_cedula.setText(c.getCedula());
                        enc_nombre_apellido.setText(c.getNombre() + " " + c.getApellido());
                        enc_direccion.setText(c.getDireccion());
                        enc_telefono.setText(c.getTelefono());
                        enc_correo.setText(c.getCorreo());
                        Descuento d = new Descuento(c.getDescuento(), 0);
                        resultado = base.gettear(d);
                        d = (Descuento) resultado.next();
                        descuento = d.getPorcentaje();
                        MENU.setSelectedIndex(0);
                        INICIO.setSelectedIndex(0);
                    }
                    base.cerrar();

                }
            }
        });
        JTdescuentos.addMouseListener(new MouseAdapter() { //descuentos(4)
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlNombre_des.setText(JTdescuentos.getValueAt(JTdescuentos.getSelectedRow(), 0).toString());
                    jlPorcentaje_des.setText(JTdescuentos.getValueAt(JTdescuentos.getSelectedRow(), 1).toString());
                }
                if (Mouse_evt.getClickCount() == 2) {
                    JFcliente.descuento.setText(jlNombre_des.getText());
                    SISTEMA.MENU.setSelectedIndex(3);
                    JFcli.setVisible(true);
                }
            }
        });
        JTempleados.addMouseListener(new MouseAdapter() { //clientes(6)
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlCedula_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 0).toString());
                    jlNombre_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 1).toString());
                    jlApellido_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 2).toString());
                    jlNac_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 3).toString());
                    jlGenero_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 4).toString());
                    jlTelefono_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 5).toString());
                    jlCorreo_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 6).toString());
                    jlDireccion_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 7).toString());
                    jlSueldo_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 8).toString());
                    jlReg_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 9).toString());
                }
                if (Mouse_evt.getClickCount() == 2) {
                    /*
                    JFfactura.jt_ciudad.setText(jlCodigo_ciu.getText());
                    SISTEMA.MENU.setSelectedIndex(1);
                    JFfac.setVisible(true);
                     */

                }
            }
        });
        JTproductos.addMouseListener(new MouseAdapter() { //productos(9)
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlCodigo_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 0).toString());
                    jlNombre_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 1).toString());
                    jlPrecio_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 2).toString());
                    jlExistencias_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 3).toString());
                    jlCategoria_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 4).toString());
                    jlProveedor_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 5).toString());
                    jlReg_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 6).toString());
                }
                if (Mouse_evt.getClickCount() == 2) {
                    /*
                    JFproveedor.jt_ciudad.setText(jlCodigo_ciu.getText());
                    SISTEMA.MENU.setSelectedIndex(1);
                    JFprov.setVisible(true);
                     */
                }
            }
        });
        JTproveedores.addMouseListener(new MouseAdapter() { //10
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlRUC.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 0).toString());
                    jlNombre.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 1).toString());
                    jlCiudad.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 2).toString());
                    jlTelefono.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 3).toString());
                    jlEmail.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 4).toString());
                    jlFecha_reg.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 5).toString());
                }
            }
        });

    }

    
    
    public void hora(){
        Calendar calendario = new GregorianCalendar();
        Date hora_actual = new Date();
        calendario.setTime(hora_actual);
        hora = calendario.get(Calendar.HOUR_OF_DAY)>9?""+calendario.get(Calendar.HOUR_OF_DAY):"0"+calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE)>9?""+calendario.get(Calendar.MINUTE):"0"+calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND)>9?""+calendario.get(Calendar.SECOND):"0"+calendario.get(Calendar.SECOND);
    }
    public void run(){
        Thread current = Thread.currentThread();
        while (current == hilo){
            hora();
            FECHA_HORA.setText("Ecuador, "+fechas.transformar_fecha(fechas.obtener_fecha())+" - "+hora+":"+minutos+":"+segundos);
        }
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
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    //1 new AluminiumLookAndFeel());
                    //1 new GraphiteLookAndFeel()
                    //2 new AcrylLookAndFeel()
                    //3 new BernsteinLookAndFeel()
                    //4 new LunaLookAndFeel() //
                    //5 new McWinLookAndFeel()//
                    
                    //NimbusLookAndFeel
                    UIManager.setLookAndFeel(new NimbusLookAndFeel() );
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
                }
                new SISTEMA().setVisible(true);
            }
        });
      
    }
     public  void limpiar(int num){
      switch(num){
          case 1 ://categoria
           jlNombre_cat.setText(" ");
           jtaDescripcion_cat.setText(" ");
           
          break;
           case 2 :
          break;
           case 3 :
          break;
           case 4 :
          break;
           case 5 :
          break;
           case 6 :
          break;
           case 7 :
          break;
           case 8 :
          break;
           case 9 :
          break;
           case 10 :
          break;
          
              
      }   
     } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FECHA_HORA;
    private javax.swing.JTabbedPane INICIO;
    private javax.swing.JPanel JPcategorias;
    private javax.swing.JPanel JPciudades;
    private javax.swing.JPanel JPclientes;
    private javax.swing.JPanel JPdescuentos;
    private javax.swing.JPanel JPempleados;
    private javax.swing.JPanel JPencabezado2;
    private javax.swing.JPanel JPfactura;
    private javax.swing.JPanel JPgastos;
    private javax.swing.JPanel JPproductos;
    private javax.swing.JPanel JPproveedores;
    private javax.swing.JPanel JPventas;
    private javax.swing.JTable JTcategorias;
    private javax.swing.JTable JTciudades;
    public static javax.swing.JTable JTclientes;
    private javax.swing.JTable JTdescuentos;
    public transient javax.swing.JTable JTdetalle;
    public static javax.swing.JTable JTempleados;
    public static javax.swing.JTable JTproductos;
    public static javax.swing.JTable JTproveedores;
    public static javax.swing.JTabbedPane MENU;
    private javax.swing.JLabel SALIR;
    private javax.swing.JLabel USUARIO;
    public static javax.swing.JLabel V0;
    private javax.swing.JTextField enc_cedula;
    private javax.swing.JTextField enc_codigo;
    private javax.swing.JTextField enc_correo;
    private javax.swing.JTextField enc_direccion;
    private javax.swing.JLabel enc_fecha;
    private javax.swing.JTextField enc_nombre_apellido;
    private javax.swing.JTextField enc_telefono;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPanew;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JButton jbEliminar_cat;
    private javax.swing.JButton jbEliminar_ciu;
    private javax.swing.JButton jbEliminar_des;
    private javax.swing.JButton jbEnviar_cat;
    private javax.swing.JButton jbEnviar_ciu;
    private javax.swing.JButton jbEnviar_cli;
    private javax.swing.JButton jbEnviar_cli1;
    private javax.swing.JButton jbEnviar_des;
    private javax.swing.JButton jbEnviar_pro;
    private javax.swing.JButton jbEnviar_prov;
    private javax.swing.JButton jbModificar_cat;
    private javax.swing.JButton jbModificar_ciu;
    private javax.swing.JButton jbModificar_cli;
    private javax.swing.JButton jbModificar_cli1;
    private javax.swing.JButton jbModificar_des;
    private javax.swing.JButton jbModificar_pro;
    private javax.swing.JButton jbModificar_prov;
    private javax.swing.JButton jbRegistrar_cat;
    private javax.swing.JButton jbRegistrar_ciu;
    private javax.swing.JButton jbRegistrar_cli;
    private javax.swing.JButton jbRegistrar_cli1;
    private javax.swing.JButton jbRegistrar_des;
    private javax.swing.JButton jbRegistrar_pro;
    private javax.swing.JButton jbRegistrar_prov;
    private javax.swing.JButton jb_Eliminar_cli;
    private javax.swing.JButton jb_Eliminar_cli1;
    private javax.swing.JButton jb_Eliminar_pro;
    private javax.swing.JButton jb_Eliminar_prov;
    private javax.swing.JComboBox<String> jcBuscar_cat;
    private javax.swing.JComboBox<String> jcBuscar_ciu;
    private javax.swing.JComboBox<String> jcBuscar_cli;
    private javax.swing.JComboBox<String> jcBuscar_des;
    private javax.swing.JComboBox<String> jcBuscar_emp;
    private javax.swing.JComboBox<String> jcBuscar_pro;
    private javax.swing.JComboBox<String> jcBuscar_prov;
    private javax.swing.JLabel jlApellido_cli;
    private javax.swing.JLabel jlApellido_emp;
    private javax.swing.JLabel jlC;
    private javax.swing.JLabel jlC1;
    private javax.swing.JLabel jlC2;
    private javax.swing.JLabel jlC3;
    private javax.swing.JLabel jlC4;
    private javax.swing.JLabel jlCategoria_pro;
    private javax.swing.JLabel jlCedula_cli;
    private javax.swing.JLabel jlCedula_emp;
    private javax.swing.JLabel jlCiudad;
    private javax.swing.JLabel jlCodigo_ciu;
    private javax.swing.JLabel jlCodigo_pro;
    private javax.swing.JLabel jlCorreo_cli;
    private javax.swing.JLabel jlCorreo_emp;
    private javax.swing.JLabel jlDescuento_cli;
    private javax.swing.JLabel jlDireccion_cli;
    private javax.swing.JLabel jlDireccion_emp;
    private javax.swing.JLabel jlE;
    private javax.swing.JLabel jlE10;
    private javax.swing.JLabel jlE11;
    private javax.swing.JLabel jlE12;
    private javax.swing.JLabel jlE13;
    private javax.swing.JLabel jlE15;
    private javax.swing.JLabel jlE16;
    private javax.swing.JLabel jlE2;
    private javax.swing.JLabel jlE3;
    private javax.swing.JLabel jlE4;
    private javax.swing.JLabel jlE5;
    private javax.swing.JLabel jlE6;
    private javax.swing.JLabel jlE7;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlEmail3;
    private javax.swing.JLabel jlExistencias_pro;
    private javax.swing.JLabel jlF;
    private javax.swing.JLabel jlF1;
    private javax.swing.JLabel jlF2;
    private javax.swing.JLabel jlF3;
    private javax.swing.JLabel jlFecha_reg;
    private javax.swing.JLabel jlGen_emp;
    private javax.swing.JLabel jlGen_emp1;
    private javax.swing.JLabel jlGenero_cli;
    private javax.swing.JLabel jlGenero_emp;
    private javax.swing.JLabel jlN;
    private javax.swing.JLabel jlN1;
    private javax.swing.JLabel jlN2;
    private javax.swing.JLabel jlN3;
    private javax.swing.JLabel jlN4;
    private javax.swing.JLabel jlN5;
    private javax.swing.JLabel jlN6;
    private javax.swing.JLabel jlNac_cli;
    private javax.swing.JLabel jlNac_emp;
    private javax.swing.JLabel jlNombre;
    private javax.swing.JLabel jlNombre_cat;
    private javax.swing.JLabel jlNombre_ciu;
    private javax.swing.JLabel jlNombre_cli;
    private javax.swing.JLabel jlNombre_des;
    private javax.swing.JLabel jlNombre_emp;
    private javax.swing.JLabel jlNombre_pro;
    private javax.swing.JLabel jlPorcentaje_des;
    private javax.swing.JLabel jlPrecio_pro;
    private javax.swing.JLabel jlProveedor_pro;
    private javax.swing.JLabel jlProvincia_ciu;
    private javax.swing.JLabel jlR;
    private javax.swing.JLabel jlR1;
    private javax.swing.JLabel jlR2;
    private javax.swing.JLabel jlR3;
    private javax.swing.JLabel jlR4;
    private javax.swing.JLabel jlR5;
    private javax.swing.JLabel jlR6;
    private javax.swing.JLabel jlRUC;
    private javax.swing.JLabel jlReg_cli;
    private javax.swing.JLabel jlReg_emp;
    private javax.swing.JLabel jlReg_pro;
    private javax.swing.JLabel jlSueldo_emp;
    private javax.swing.JLabel jlT;
    private javax.swing.JLabel jlT1;
    private javax.swing.JLabel jlT2;
    private javax.swing.JLabel jlTelefono;
    private javax.swing.JLabel jlTelefono_cli;
    private javax.swing.JLabel jlTelefono_emp;
    public static javax.swing.JLabel jl_dinero2;
    public static javax.swing.JLabel jl_dinero4;
    private javax.swing.JLabel jl_num_det;
    private javax.swing.JLabel jl_num_det1;
    private javax.swing.JLabel jl_num_det2;
    private javax.swing.JLabel jl_num_det3;
    private javax.swing.JLabel jl_num_det4;
    private javax.swing.JLabel jl_titulo10;
    private javax.swing.JLabel jl_titulo11;
    private javax.swing.JLabel jl_titulo12;
    private javax.swing.JLabel jl_titulo13;
    private javax.swing.JLabel jl_titulo14;
    private javax.swing.JLabel jl_titulo5;
    private javax.swing.JLabel jl_titulo6;
    private javax.swing.JLabel jl_titulo7;
    private javax.swing.JLabel jl_total;
    private javax.swing.JLabel jl_total1;
    private javax.swing.JLabel jl_total2;
    public static javax.swing.JPanel jpDatos_cat;
    public static javax.swing.JPanel jpDatos_cat1;
    public static javax.swing.JPanel jpDatos_cli;
    public static javax.swing.JPanel jpDatos_des;
    public static javax.swing.JPanel jpDatos_emp;
    public static javax.swing.JPanel jpDatos_pro;
    public static javax.swing.JPanel jpDatos_prov;
    private javax.swing.JScrollPane jsTabla_cat;
    private javax.swing.JScrollPane jsTabla_cat6;
    private javax.swing.JScrollPane jsTabla_cat7;
    private javax.swing.JScrollPane jsTabla_cat8;
    private javax.swing.JScrollPane jsTabla_ciu;
    private javax.swing.JScrollPane jsTabla_ciu1;
    private javax.swing.JScrollPane jsTabla_des;
    private javax.swing.JScrollPane jsTabla_pro;
    public static javax.swing.JTextField jtBuscar_cat;
    public static javax.swing.JTextField jtBuscar_ciu;
    public static javax.swing.JTextField jtBuscar_cli;
    public static javax.swing.JTextField jtBuscar_des;
    public static javax.swing.JTextField jtBuscar_emp;
    public static javax.swing.JTextField jtBuscar_pro;
    public static javax.swing.JTextField jtBuscar_prov;
    public static javax.swing.JTextArea jtaDescripcion_cat;
    private javax.swing.JLabel lim_cat;
    private javax.swing.JLabel lim_ciu;
    private javax.swing.JLabel lim_cli;
    private javax.swing.JLabel lim_des;
    private javax.swing.JLabel lim_emp;
    private javax.swing.JLabel lim_pro;
    private javax.swing.JLabel lim_prov;
    private javax.swing.JLabel res_num_cat;
    private javax.swing.JLabel res_num_ciu;
    private javax.swing.JLabel res_num_cli;
    private javax.swing.JLabel res_num_des;
    private javax.swing.JLabel res_num_emp;
    private javax.swing.JLabel res_num_pro;
    private javax.swing.JLabel res_num_prov;
    public static javax.swing.JLabel sistema_titulo;
    public static javax.swing.JLabel v1;
    public static javax.swing.JLabel v10;
    public static javax.swing.JLabel v2;
    public static javax.swing.JLabel v3;
    public static javax.swing.JLabel v4;
    public static javax.swing.JLabel v6;
    public static javax.swing.JLabel v9;
    private javax.swing.JLabel v_dinero2;
    private javax.swing.JLabel v_dinero4;
    // End of variables declaration//GEN-END:variables
}
