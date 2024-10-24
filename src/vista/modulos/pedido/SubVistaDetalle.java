package vista.modulos.pedido;

import controlador.ControladorPedido;
import datos.AlmacenDatos;
import datos.comun.Coleccion;
import datos.dao.PedidoDetalleDAO;
import java.awt.Image;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Libro;
import modelo.Pedido;
import modelo.PedidoDetalle;
import utilitarios.Constantes;
import vista.comun.BotonTablaEditor;
import vista.comun.BotonTablaRender;
import vista.comun.Vista;

public class SubVistaDetalle extends Vista {

    //atributos    
    
    private final java.util.List<JTextField> textFields;
    private final ControladorPedido controlador;//referencia del controlador padre
    private final DecimalFormat df;
    static PedidoDetalleDAO pedidoDetalleDAO;

    public SubVistaDetalle(ControladorPedido controlador, Constantes.Ordenamiento ordenamiento, Constantes.Busqueda busqueda) {
        super("Detalles");
        initComponents();
        configurarBotonEliminarEnTabla();
        textFields = java.util.Arrays.asList(txtCliente,
                txtEstado, txtFecha,
                txtId);
        this.controlador = controlador;        
        df = new DecimalFormat("#0.00");
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH)); //Bug fix
        cargarLibrosEnLista(); // Precargar lstLibros con todos los libros actuales
        pedidoDetalleDAO = new PedidoDetalleDAO();
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/recursos/iconos/add-list.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        btnAgregar.setIcon(new ImageIcon(imagenEscalada));
    }

    private void configurarBotonEliminarEnTabla() {
        tblLista.getColumnModel().getColumn(5).setCellRenderer(new BotonTablaRender()); // La columna 5 es para los botones
        tblLista.getColumnModel().getColumn(5).setCellEditor(new BotonTablaEditor(
                tblLista, e -> eliminarDetalle(Integer.parseInt(e.getActionCommand()))));
    }
    public Pedido mostrarDetallesPedido(int pedidoId) {
        Pedido pedido = controlador.buscar(pedidoId);        
        mostrarDetallesPedido(pedido);
        return pedido;
    }
    public void mostrarDetallesPedido(Pedido pedido) {
        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "Pedido no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        VistaPedido.punteroPedido = pedido;
        txtId.setText(String.valueOf(pedido.getId()));
        txtFecha.setText(pedido.getFechaDePedido());
        txtEstado.setText(pedido.getEstadoDelPedido());
        txtCliente.setText(String.valueOf(pedido.getCliente().getId()));
        lblSumaTotal.setText(df.format(pedido.getMontoTotal()));

        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        pedido.getPedidoDetalle().iniciarIteracion();
        while (pedido.getPedidoDetalle().actualElementoIteracion() != null) {
            PedidoDetalle detalle = pedido.getPedidoDetalle().actualElementoIteracion();
            Object[] nuevaFila = new Object[6];
            nuevaFila[0] = detalle.getLibro().getId();
            nuevaFila[1] = detalle.getLibro().getTitulo();
            nuevaFila[2] = detalle.getPrecio();
            nuevaFila[3] = detalle.getCantidad();
            nuevaFila[4] = detalle.getPrecio() * detalle.getCantidad();
            nuevaFila[5] = "Eliminar";
            modelo.addRow(nuevaFila);
            pedido.getPedidoDetalle().avanzarIteracion();
        }
    }
    private void cargarLibrosEnLista() {
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        AlmacenDatos.libros.iniciarIteracion();
        while (AlmacenDatos.libros.actualElementoIteracion() != null) {
            Libro libro = AlmacenDatos.libros.actualElementoIteracion();
            modeloLista.addElement(libro.getTitulo());
            AlmacenDatos.libros.avanzarIteracion();
        }
        lstLibros.setModel(modeloLista);
    }
    private void actualizarPrecio() {
        String libroSeleccionado = lstLibros.getSelectedValue();
        AlmacenDatos.libros.iniciarIteracion();
        if (libroSeleccionado != null) {
            while (AlmacenDatos.libros.actualElementoIteracion() != null) {
                Libro libro = AlmacenDatos.libros.actualElementoIteracion();
                if (libro.getTitulo().equals(libroSeleccionado)) {
                    lblPrecio.setText(df.format(libro.getPrecio()));
                    actualizarTotal();
                    break;
                }
                AlmacenDatos.libros.avanzarIteracion();
            }
        }
    }
    private void actualizarTotal() {
        double precio = Double.parseDouble(lblPrecio.getText());
        int cantidad = (Integer) spnCantidad.getValue();
        lblTotal.setText(df.format(precio * cantidad));
    }
    private void eliminarDetalle(int filaId) {
        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        Integer idTitulo = (Integer) modelo.getValueAt(filaId, 0);
        String libroTitulo = (String) modelo.getValueAt(filaId, 1);
        Coleccion<PedidoDetalle> detalles = VistaPedido.punteroPedido.getPedidoDetalle();
        detalles.iniciarIteracion();
        while (detalles.actualElementoIteracion() != null) {
            PedidoDetalle detalle = detalles.actualElementoIteracion();
            if (detalle.getLibro().getId() == idTitulo) {
                VistaPedido.punteroPedido.getPedidoDetalle().eliminar(detalle);
                break;
            }
            detalles.avanzarIteracion();
        }
        modelo.removeRow(filaId);
        actualizarSumaTotal();
    }
    private void actualizarSumaTotal() {
        double sumaTotal = 0.0;
        VistaPedido.punteroPedido.getPedidoDetalle().iniciarIteracion();
        while (VistaPedido.punteroPedido.getPedidoDetalle().actualElementoIteracion() != null) {
            PedidoDetalle detalle = VistaPedido.punteroPedido.getPedidoDetalle().actualElementoIteracion();
            sumaTotal += detalle.getPrecio() * detalle.getCantidad();
            VistaPedido.punteroPedido.getPedidoDetalle().avanzarIteracion();
        }
        lblSumaTotal.setText(df.format(sumaTotal));
    }
    private Libro buscarLibroPorTitulo(String titulo) {
        AlmacenDatos.libros.iniciarIteracion();
        while (AlmacenDatos.libros.actualElementoIteracion() != null) {
            Libro libro = AlmacenDatos.libros.actualElementoIteracion();
            if (libro.getTitulo().equals(titulo)) {
                return libro;
            }
            AlmacenDatos.libros.avanzarIteracion();
        }
        return null;
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
        txtFecha = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstLibros = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        spnCantidad = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        lblSumaTotal = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLista = new javax.swing.JTable();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jLabel3.setText("ID:");

        txtId.setText("jTextField1");

        jLabel4.setText("Fecha:");

        txtFecha.setText("jTextField2");

        jLabel5.setText("Estado:");

        txtEstado.setText("jTextField3");

        jLabel6.setText("Cliente:");

        txtCliente.setText("jTextField4");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Agregar pedido"));

        lstLibros.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstLibrosValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstLibros);

        jLabel1.setText("Precio:");

        lblPrecio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrecio.setText("lblPrecio");
        lblPrecio.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20)));

        jLabel2.setText("Cantidad:");

        spnCantidad.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnCantidadStateChanged(evt);
            }
        });

        jLabel7.setText("Total:");

        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("lblTotal");
        lblTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20)));

        btnAgregar.setText("Agregar");
        btnAgregar.setToolTipText("Agregar libro al pedido");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spnCantidad)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblPrecio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblTotal)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel8.setText("Total del Pedido:");

        lblSumaTotal.setText("jTextField1");
        lblSumaTotal.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addGap(118, 118, 118)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                            .addComponent(txtFecha)
                            .addComponent(txtEstado)
                            .addComponent(txtCliente)
                            .addComponent(lblSumaTotal))))
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
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblSumaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        add(jPanel1);

        tblLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Libro", "Libro", "Precio", "Cantidad", "Monto", "Accion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblLista);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if (!validarNuevoItem()) {
            return;
        }
        String libroSeleccionado = lstLibros.getSelectedValue();
        int cantidad = (Integer) spnCantidad.getValue();
        double precio = Double.parseDouble(lblPrecio.getText());
        double total = cantidad * precio;
        // Crear nuevo detalle de pedido
        Libro libro = buscarLibroPorTitulo(libroSeleccionado);
        PedidoDetalle nuevoDetalle = new PedidoDetalle();
        nuevoDetalle.setLibroId(libro.getId());
        nuevoDetalle.setCantidad(cantidad);
        nuevoDetalle.setPrecio(precio);
        nuevoDetalle.setLibro(libro); 
        nuevoDetalle.setTotal(cantidad * precio);
        
        // bug fix 2
        if(VistaPedido.punteroPedido == null){
            if(!validarNuevoPedido())
                return;
            VistaPedido.punteroPedido = new Pedido();
            VistaPedido.punteroPedido.setClienteId(Integer.valueOf(txtCliente.getText()));
            VistaPedido.punteroPedido.setEstadoDelPedido(txtEstado.getText());
            VistaPedido.punteroPedido.setFechaDePedido(txtFecha.getText());
        }
        
        VistaPedido.punteroPedido.agregarDetalle(nuevoDetalle); // Agregar a la lista de detalles del pedido
        
        // Agregar a la tabla
        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        modelo.addRow(new Object[]{libro.getId(),libroSeleccionado, precio, cantidad, total, "Eliminar"});

        actualizarSumaTotal();
        // Limpiar campos si es necesario
        //todo 
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void spnCantidadStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnCantidadStateChanged
        actualizarPrecio();
    }//GEN-LAST:event_spnCantidadStateChanged

    private void lstLibrosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstLibrosValueChanged
        actualizarPrecio();
    }//GEN-LAST:event_lstLibrosValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lblPrecio;
    private javax.swing.JTextField lblSumaTotal;
    public javax.swing.JLabel lblTotal;
    public javax.swing.JList<String> lstLibros;
    public javax.swing.JSpinner spnCantidad;
    public javax.swing.JTable tblLista;
    public javax.swing.JTextField txtCliente;
    public javax.swing.JTextField txtEstado;
    public javax.swing.JTextField txtFecha;
    public javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables

    private boolean validarNuevoItem() {
        // Obtener el libro seleccionado y la cantidad
        String libroSeleccionado = lstLibros.getSelectedValue();
        int cantidad = (Integer) spnCantidad.getValue();

        // Verificar si hay un libro seleccionado
        if (libroSeleccionado == null || libroSeleccionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un libro.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificar si la cantidad es mayor a cero
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificar si el libro ya está en la lista de detalles del pedido
        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Integer idLibroEnTabla = (Integer) modelo.getValueAt(i, 0);
            String libroEnTabla = (String) modelo.getValueAt(i, 1);
            if (libroSeleccionado.equals(libroEnTabla)) {
                JOptionPane.showMessageDialog(this, "El libro ya está en la lista de detalles del pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true; // Si todas las validaciones pasan, retornar true
    }


    @Override
    public void cambiarBusqueda(Constantes.Busqueda busqueda) {
        // implementar busqueda
    }

    @Override
    public void cambiarOrdenamiento(Constantes.Ordenamiento ordenamiento) {
        // implementar ordenamiento
    }

    @Override
    public void Limpiar() {
        lblSumaTotal.setText("0.00");
        for (JTextField textField : textFields) {
            textField.setText(Constantes.VACIO);
        }
        spnCantidad.setValue(0);
        lblPrecio.setText("0.00");
        lblTotal.setText("0.00");
        DefaultTableModel dtm = (DefaultTableModel) tblLista.getModel();
        dtm.setRowCount(0);
        lstLibros.clearSelection();
    }

    @Override
    public void Listar() {
        
    }

    @Override
    public boolean Guardar() {
        if (VistaPedido.punteroPedido == null || !validarCampos()) {
            return false;
        }
        Pedido pedidoActual = VistaPedido.punteroPedido;
        // Guardar el pedido y sus detalles
        if(pedidoActual.getId() == -1){
            pedidoActual.setId(Integer.parseInt(txtId.getText()));
            controlador.guardar(pedidoActual);
        }else{
            controlador.actualizar(pedidoActual);            
        }
        // Insertar nuevamente los detalles del pedido
        /*pedidoActual.getPedidoDetalle().iniciarIteracion();
        while (pedidoActual.getPedidoDetalle().actualElementoIteracion() != null) {
            PedidoDetalle detalle = pedidoActual.getPedidoDetalle().actualElementoIteracion();
            detalle.setPedidoId(pedidoActual.getId());
            pedidoDetalleDAO.guardar(detalle);
            pedidoActual.getPedidoDetalle().avanzarIteracion();
        }*/

        JOptionPane.showMessageDialog(this, "Pedido guardado correctamente.");
        //actualizar listado
        return true;
    }

    @Override
    public void Activar(Constantes.Activar tipo) {
        switch (tipo) {
            case TODOS_ELEMENTOS:
                for (JTextField textField : textFields) {
                    textField.setEnabled(true);
                }
                break;
            case SOLO_EDITAR:
                for (JTextField textField : textFields) {
                    textField.setEnabled(textField != txtId);
                }
                break;
            case BUSCAR:
                for (JTextField textField : textFields) {
                    textField.setEnabled(textField == txtId);
                }
                break;
            default:
                for (JTextField textField : textFields) {
                    textField.setEnabled(false);
                }
                break;
        }
    }

    @Override
    public boolean Eliminar() {
        return false;
    }

    @Override
    public Pedido Buscar() {
        if(txtId.getText().isBlank())
            return null;
        return mostrarDetallesPedido(Integer.parseInt(txtId.getText()));
    }

    @Override
    public boolean validarCampos() {
        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El pedido debe tener al menos un detalle.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //bug fix 2
        // Verificar que txtId y txtCliente contengan solo números
        if (!txtId.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El campo ID Pedido solo puede contener números.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!txtCliente.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El campo ID Cliente solo puede contener números.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return validarNuevoPedido(); 
    }
    //bug fix 2
    public boolean validarNuevoPedido(){
        if (txtCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Cliente no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo ID Pedido no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtEstado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Estado no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtFecha.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Fecha no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
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
}
