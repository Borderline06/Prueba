package vista.modulos;

import controlador.ControladorCliente;
import datos.AlmacenDatos;
import datos.comun.Coleccion;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import utilitarios.Constantes;
import vista.comun.Vista;

public class VistaCliente extends Vista {

    //atributos
    private java.util.List<JTextField> textFields;
    private Cliente punteroCliente;
    private ControladorCliente controlador;
    private JInternalFrame internalFrame;

    public VistaCliente(Constantes.Ordenamiento ordenamiento, Constantes.Busqueda busqueda) {
        //constructor de Vista
        super("Cliente");
        initComponents();
        textFields = java.util.Arrays.asList(txtNombreCompleto, txtDireccion, txtCorreo, txtId);
        punteroCliente = null;
        controlador = new ControladorCliente(ordenamiento, busqueda);
        controlador.setDatos(AlmacenDatos.clientes);//cola
        this.TAD = " - TAD Cola";
    }

    @Override
    public boolean Eliminar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduzca un ID para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (punteroCliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente no cargado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        controlador.eliminar(punteroCliente.getId());
        punteroCliente = null;
        Listar();
        return true;
    }

    @Override
    public void Limpiar() {
        txtCorreo.setText(Constantes.VACIO);
        txtDireccion.setText(Constantes.VACIO);
        txtId.setText(Constantes.VACIO);
        txtNombreCompleto.setText(Constantes.VACIO);
    }

    @Override
    public Cliente Buscar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduzca un ID para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        punteroCliente = controlador.buscar(Integer.parseInt(txtId.getText()));
        if (punteroCliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        txtNombreCompleto.setText(punteroCliente.getNombre());
        txtDireccion.setText(punteroCliente.getDireccion());
        txtCorreo.setText(punteroCliente.getCorreoElectronico());
        return punteroCliente;
    }

    @Override
    public void Listar() {
        Coleccion<Cliente> clientes = controlador.listarTodo();
        clientes.iniciarIteracion();
        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        modelo.setRowCount(0); // Limpiar tabla
        while (clientes.actualElementoIteracion() != null) {
            Cliente cliente = clientes.actualElementoIteracion();
            Object[] nuevaFila = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.getCorreoElectronico()
            };
            modelo.addRow(nuevaFila);
            clientes.avanzarIteracion();
        }
    }

    @Override
    public boolean Guardar() {
        if (validarCampos()) {
            if (punteroCliente == null) {
                punteroCliente = new Cliente(
                        Integer.parseInt(txtId.getText()),
                        txtNombreCompleto.getText(),
                        txtDireccion.getText(),
                        txtCorreo.getText());
            } else {
                punteroCliente.setNombre(txtNombreCompleto.getText());
                punteroCliente.setDireccion(txtDireccion.getText());
                punteroCliente.setCorreoElectronico(txtCorreo.getText());
            }
            controlador.guardar(punteroCliente);
            Listar();
            Limpiar();
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser llenados correctamente.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public void Activar(Constantes.Activar tipo) {
        switch (tipo) {
            case TODOS_ELEMENTOS:
                textFields.forEach(field -> field.setEnabled(true));
                break;
            case SOLO_EDITAR:
                textFields.forEach(field -> field.setEnabled(field != txtId)); // ID no es editable
                break;
            case BUSCAR:
                textFields.forEach(field -> field.setEnabled(field == txtId));
                break;
            default:
                textFields.forEach(field -> field.setEnabled(false));
                break;
        }
    }

    @Override
    public boolean validarCampos() {
        return textFields.stream().noneMatch(field -> field.getText().isBlank());
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        txtCorreo = new javax.swing.JTextField();
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

        jLabel6.setText("Correo electrónico:");

        txtCorreo.setText("jTextField4");

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
                    .addComponent(txtCorreo))
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
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        add(jPanel1);

        tblLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Dirección", "Correo Electrónico"
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
            .addGap(0, 11, Short.MAX_VALUE)
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
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombreCompleto;
    // End of variables declaration//GEN-END:variables

    public JInternalFrame getInternalFrame() {
        return internalFrame;
    }

    public void setInternalFrame(JInternalFrame internalFrame) {
        this.internalFrame = internalFrame;
    }

}
