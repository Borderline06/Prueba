
package vista.modulos;


import controlador.ControladorLibro;
import datos.AlmacenDatos;
import datos.comun.Coleccion;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Libro;
import utilitarios.Constantes;
import vista.comun.Vista;

public class VistaLibro extends Vista {

    //atributos
    private java.util.List<JTextField> textFields;
    private Libro punteroLibro;
    private ControladorLibro controlador;
    
    
    public VistaLibro(Constantes.Ordenamiento ordenamiento, Constantes.Busqueda busqueda) {
        //constructor de Vista
        super("Libro");
        //inicia componentes
        initComponents();
        //tener los elementos en una lista para mantenimiento rapido
        textFields = java.util.Arrays.asList(txtAutor, txtGenero, txtId, txtInventario, txtPrecio, txtTitulo);
        punteroLibro = null;
        controlador = new ControladorLibro(ordenamiento, busqueda);
        //cargar datos estaticos desde el almacen
        controlador.setDatos(AlmacenDatos.libros);
        this.TAD = " - TAD Lista Simplemente Enlazada";
    }
    @Override
    public boolean Eliminar(){
        if(txtId.getText().isBlank()){
            punteroLibro = null;
            return false;
        }
        if(punteroLibro == null)
            return false;
        controlador.eliminar(punteroLibro.getId());
        punteroLibro = null;
        //Actualizar lista
        Listar();
        return true;
    }
    @Override
    public void Limpiar(){
        for (JTextField textField : textFields) {
            textField.setText(Constantes.VACIO);
        }        
    }
    @Override
    public Libro Buscar() {
        if(txtId.getText().isBlank())
            return null;
        punteroLibro = controlador.buscar(Integer.parseInt(txtId.getText()));
        if(punteroLibro == null){
            JOptionPane.showMessageDialog(this, "No encontrado");
            return null;
        }
            
        txtAutor.setText(punteroLibro.getAutor());
        txtGenero.setText(punteroLibro.getGenero());
        txtInventario.setText(punteroLibro.getCantidadEnInventario()+ "");
        txtPrecio.setText(punteroLibro.getPrecio()+"");
        txtTitulo.setText(punteroLibro.getTitulo());
        return punteroLibro;
    }
    @Override
    public void Listar(){
        Coleccion<Libro> libros = controlador.listarTodo();
        libros.iniciarIteracion();
        DefaultTableModel modelo = (DefaultTableModel) tblLista.getModel();
        modelo.setRowCount(0); //limpiar tabla
        //iterar la coleccion e ir agregando las filas a la tabla
        while(libros.actualElementoIteracion() != null){
            Libro libro = libros.actualElementoIteracion();
            Object[] nuevaFila = new Object[6];
            nuevaFila[0] = libro.getId();
            nuevaFila[1] = libro.getTitulo();
            nuevaFila[2] = libro.getAutor();
            nuevaFila[3] = libro.getGenero();
            nuevaFila[4] = libro.getPrecio();
            nuevaFila[5] = libro.getCantidadEnInventario();
            modelo.addRow(nuevaFila);
            libros.avanzarIteracion();
        }        
    }
    @Override
    public boolean Guardar() {
        if(validarCampos()){
            Libro nuevoLibro = new Libro(Integer.parseInt(txtId.getText()));
            nuevoLibro.setAutor(txtAutor.getText());
            nuevoLibro.setCantidadEnInventario(Integer.parseInt(txtInventario.getText()));
            nuevoLibro.setGenero(txtGenero.getText());
            nuevoLibro.setPrecio(Double.parseDouble(txtPrecio.getText()));
            nuevoLibro.setTitulo(txtTitulo.getText());
            if(punteroLibro == null)
                controlador.guardar(nuevoLibro);
            else
                controlador.actualizar(nuevoLibro);
            punteroLibro = null;
            Listar();
            return true;
        }else{
            //error
            JOptionPane.showMessageDialog(this,"Llene todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
    }
    @Override
    public boolean validarCampos() {
        for (JTextField textField : textFields) {
            if(textField.getText().isBlank())
                return false;
        }
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
        txtTitulo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtAutor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtGenero = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtInventario = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLista = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jLabel3.setText("ID:");

        txtId.setText("jTextField1");

        jLabel4.setText("Título:");

        txtTitulo.setText("jTextField2");

        jLabel5.setText("Autor:");

        txtAutor.setText("jTextField3");

        jLabel6.setText("Género:");

        txtGenero.setText("jTextField4");

        jLabel7.setText("Precio:");

        txtPrecio.setText("jTextField5");

        jLabel8.setText("Cantidad en Inventario:");

        txtInventario.setText("jTextField6");

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
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(100, 100, 100)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtId, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                    .addComponent(txtTitulo)
                    .addComponent(txtAutor)
                    .addComponent(txtGenero)
                    .addComponent(txtPrecio)
                    .addComponent(txtInventario))
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
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(jPanel1);

        tblLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Título", "Autor", "Género", "Precio", "Inventario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
            .addGap(0, 53, Short.MAX_VALUE)
        );

        add(jPanel2);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblLista;
    private javax.swing.JTextField txtAutor;
    private javax.swing.JTextField txtGenero;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtInventario;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables

   
}
