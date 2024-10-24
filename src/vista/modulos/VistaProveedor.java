package vista.modulos;

import controlador.ControladorProveedor;
import controlador.comun.Controlador;
import datos.AlmacenDatos;
import datos.comun.Coleccion;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Proveedor;
import utilitarios.Constantes;
import vista.comun.Vista;

public class VistaProveedor extends Vista {

    //atributos
    private java.util.List<JTextField> textFields;
    private Proveedor punteroProveedor;
    private ControladorProveedor controlador;
    

    public VistaProveedor(Constantes.Ordenamiento ordenamiento, Constantes.Busqueda busqueda) {
        super("Proveedor");
        initComponents();
        textFields = java.util.Arrays.asList(txtNombreCompleto, txtDireccion, txtTelefono, txtId);
        punteroProveedor = null;
        controlador = new ControladorProveedor(ordenamiento, busqueda);
        controlador.setDatos(AlmacenDatos.proveedores);
        this.TAD = " - TAD Tabla Hash con Lista Enlazada";
    }

    @Override
    public boolean Eliminar() {
        if (txtId.getText().isBlank()) {
            punteroProveedor = null;
            return false;
        }
        if (punteroProveedor == null) {
            return false;
        }
        controlador.eliminar(punteroProveedor.getId());
        punteroProveedor = null;
        Listar();
        return true;
    }

    @Override
    public void Limpiar() {
        for (JTextField textField : textFields) {
            textField.setText(Constantes.VACIO);
        }
    }

    @Override
    public Proveedor Buscar() {
        if (txtId.getText().isBlank()) {
            return null;
        }
        punteroProveedor = controlador.buscar(Integer.parseInt(txtId.getText()));
        if (punteroProveedor == null) {
            JOptionPane.showMessageDialog(this, "No encontrado");
            return null;
        }
        txtNombreCompleto.setText(punteroProveedor.getNombre());
        txtDireccion.setText(punteroProveedor.getDireccion());
        txtTelefono.setText(punteroProveedor.getTelefono());
        return punteroProveedor;
    }

    @Override
    public void Listar() {
        Coleccion<Proveedor> proveedores = controlador.listarTodo();
        proveedores.iniciarIteracion();
        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        modelo.setRowCount(0); //limpiar tabla
        //iterar la coleccion e ir agregando las filas a la tabla
        while (proveedores.actualElementoIteracion() != null) {
            Proveedor proveedor = proveedores.actualElementoIteracion();
            Object[] nuevaFila = new Object[6];
            nuevaFila[0] = proveedor.getId();
            nuevaFila[1] = proveedor.getNombre();
            nuevaFila[2] = proveedor.getDireccion();
            nuevaFila[3] = proveedor.getTelefono();
            modelo.addRow(nuevaFila);
            proveedores.avanzarIteracion();
        }
    }

    @Override
    public boolean Guardar() {
        if (validarCampos()) {
            Proveedor nuevoProveedor = new Proveedor(
                    Integer.parseInt(txtId.getText()),
                    txtNombreCompleto.getText(),
                    txtDireccion.getText(),
                    txtTelefono.getText()
            );
            if (punteroProveedor == null) {
                controlador.guardar(nuevoProveedor);
            } else {
                controlador.actualizar(nuevoProveedor);
                punteroProveedor = null;
            }
            Listar();
            Limpiar();
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Llene todos los campos correctamente", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public void Activar(Constantes.Activar tipo) {
        for (JTextField textField : textFields) {
            switch (tipo) {
                case TODOS_ELEMENTOS:
                    textField.setEnabled(true);
                    break;
                case SOLO_EDITAR:
                    textField.setEnabled(textField != txtId); // ID no editable
                    break;
                case BUSCAR:
                    textField.setEnabled(textField == txtId);
                    break;
                default:
                    textField.setEnabled(false);
                    break;
            }
        }
    }

    @Override
    public boolean validarCampos() {
        for (JTextField textField : textFields) {
            if (textField.getText().isBlank()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void cambiarBusqueda(Constantes.Busqueda busqueda) {
        controlador.cambiarBusqueda(busqueda);
    }

    @Override
    public void cambiarOrdenamiento(Constantes.Ordenamiento ordenamiento) {
        controlador.cambiarOrdenamiento(ordenamiento);
    }

    @Override
    public void Ordenar(boolean asc) {
        if (asc) {
            controlador.ordenar();
        } else {
            controlador.ordenar();
            controlador.invertir(); // Método adicional para invertir el orden de la colección
        }
        Listar();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNombreCompleto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLista = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jLabel3.setText("ID:");

        txtId.setText("jTextField1");

        jLabel4.setText("Nombre Completo:");

        txtNombreCompleto.setText("jTextField2");

        jLabel5.setText("Dirección:");

        txtDireccion.setText("jTextField3");

        jLabel6.setText("Teléfono:");

        txtTelefono.setText("jTextField4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(118, 118, 118)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtId, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                    .addComponent(txtNombreCompleto)
                    .addComponent(txtDireccion)
                    .addComponent(txtTelefono))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNombreCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        add(jPanel1);

        tblLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Dirección", "Teléfono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblLista);

        add(jScrollPane1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        add(jPanel2);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblLista;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombreCompleto;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
