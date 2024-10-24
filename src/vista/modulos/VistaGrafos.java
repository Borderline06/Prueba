package vista.modulos;

import datos.tad.grafo.Grafo;
import datos.tad.grafo.GrafoDirigido;
import datos.tad.grafo.GrafoNoDirigido;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import utilitarios.Constantes;
import vista.comun.Vista;

public class VistaGrafos extends Vista {

    private GrafoDirigido<String> grafo;
    private GrafoNoDirigido<String> grafoSecciones;
    private DefaultTableModel modeloTabla;

    /**
     * Creates new form VistaGrafos
     */
    public VistaGrafos(Constantes.Ordenamiento ordenamiento, Constantes.Busqueda busqueda) {
        super("Recomendación de Libros y Navegación de Biblioteca");
        initComponents();
        this.TAD = "TAD Grafo";

        // Inicializar grafos
        grafo = new GrafoDirigido<>();  // Grafo dirigido
        grafoSecciones = new GrafoNoDirigido<>();  // Grafo no dirigido
        modeloTabla = new DefaultTableModel();
        tblMatrizAdyacencia.setModel(modeloTabla);
        /*llenarGrafoLibros();
        llenarGrafoSecciones();*/
        inicializarDatos();
        actualizarVista();
    }

    private void inicializarDatos() {

        grafo.agregarVertice("1984");
        grafo.agregarVertice("Brave New World");
        grafo.agregarVertice("Fahrenheit 451");
        grafo.agregarVertice("The Handmaid's Tale");

        grafo.agregarArista("1984", "Brave New World");
        grafo.agregarArista("Brave New World", "Fahrenheit 451");
        grafo.agregarArista("Fahrenheit 451", "The Handmaid's Tale");
    }


    private void actualizarVista() {
        actualizarTablaAdyacencia();
        actualizarCombos();
    }

    private void actualizarTablaAdyacencia() {
        List<String> vertices = grafo.obtenerVertices();
        int[][] matriz = grafo.obtenerMatrizAdyacencia();
        String[] columnas = new String[vertices.size() + 1];
        columnas[0] = "";
        for (int i = 0; i < vertices.size(); i++) {
            columnas[i + 1] = vertices.get(i);
        }
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(columnas);

        for (int i = 0; i < matriz.length; i++) {
            Object[] fila = new Object[matriz[i].length + 1];
            fila[0] = vertices.get(i); // Cabecera del eje Y
            for (int j = 0; j < matriz[i].length; j++) {
                fila[j + 1] = matriz[i][j];
            }
            modeloTabla.addRow(fila);
        }

    }

    private void actualizarCombos() {
        List<String> vertices = grafo.obtenerVertices();
        cmbInicio.removeAllItems();
        cmbDestino.removeAllItems();
        cmbOrigenArista.removeAllItems();
        cmbDestinoArista.removeAllItems();

        for (String vertice : vertices) {
            cmbInicio.addItem(vertice);
            cmbDestino.addItem(vertice);
            cmbOrigenArista.addItem(vertice);
            cmbDestinoArista.addItem(vertice);
        }
    }


    private void buscarRecomendacionLibro() {
        String inicio = (String) cmbInicio.getSelectedItem();
        String destino = (String) cmbDestino.getSelectedItem();
        if (inicio != null && destino != null) {
            java.util.List<String> camino = grafo.bfs(inicio, destino);
            txtResultado.setText("Recomendación de libro:\n" + camino);
            //mostrarMatrizAdyacencia(grafo);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un libro de inicio y uno de destino.");
        }
    }

    private void buscarRutaSeccion() {
        String inicio = (String) cmbInicio.getSelectedItem();
        String destino = (String) cmbDestino.getSelectedItem();
        if (inicio != null && destino != null) {
            java.util.List<String> camino = grafoSecciones.bfs(inicio, destino);
            txtResultado.setText("Ruta entre secciones:\n" + camino);
            mostrarMatrizAdyacencia(grafoSecciones);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona una sección de inicio y una de destino.");
        }
    }

    private void mostrarMatrizAdyacencia(Grafo<String> grafo) {
        java.util.List<String> vertices = new java.util.ArrayList<>(grafo.obtenerVertices());
        int n = vertices.size();
        String[] columnas = vertices.toArray(new String[n]);
        String[][] datos = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                datos[i][j] = grafo.existeArista(vertices.get(i), vertices.get(j)) ? "1" : "0";
            }
        }
        tblMatrizAdyacencia.setModel(new javax.swing.table.DefaultTableModel(datos, columnas));
    }

    private void agregarNodo() {
        String nodo = txtNodo.getText().trim();
        if (!nodo.isEmpty()) {
            grafo.agregarVertice(nodo);
            //actualizarComponentes();
            actualizarVista();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor ingresa un nombre para el nodo.");
        }
    }

    private void eliminarNodo() {
        String nodo = (String) txtNodo.getText();
        if (nodo != null) {
            grafo.eliminarVertice(nodo);
            //actualizarComponentes();
            actualizarVista();
            JOptionPane.showMessageDialog(this, "Nodo eliminado: " + nodo);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un nodo para eliminar.");
        }
    }

    private void agregarArista() {
        String origen = (String) cmbOrigenArista.getSelectedItem();
        String destino = (String) cmbDestinoArista.getSelectedItem();
        if (origen != null && destino != null && !origen.equals(destino)) {
            grafo.agregarArista(origen, destino);
            //actualizarComponentes();
            actualizarVista();
            //JOptionPane.showMessageDialog(this, "Arista agregada de " + origen + " a " + destino);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona nodos válidos de origen y destino.");
        }
    }

    private void eliminarArista() {
        String origen = (String) cmbOrigenArista.getSelectedItem();
        String destino = (String) cmbDestinoArista.getSelectedItem();
        if (origen != null && destino != null) {
            if (origen.equals(destino)) {
                JOptionPane.showMessageDialog(this, "No se pueden eliminar aristas de un nodo a sí mismo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            grafo.eliminarArista(origen, destino);
            //actualizarComponentes();
            actualizarVista();
            //JOptionPane.showMessageDialog(this, "Arista eliminada de " + origen + " a " + destino);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona nodos válidos de origen y destino.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbInicio = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbDestino = new javax.swing.JComboBox<>();
        panelBotones = new javax.swing.JPanel();
        btnBuscarLibro = new javax.swing.JButton();
        btnBuscarSeccion = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMatrizAdyacencia = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResultado = new javax.swing.JTextArea();
        panelAristasNodos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNodo = new javax.swing.JTextField();
        btnAgregarNodo = new javax.swing.JButton();
        btnEliminarNodo = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmbOrigenArista = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbDestinoArista = new javax.swing.JComboBox<>();
        btnAgregarArista = new javax.swing.JButton();
        btnEliminarArista = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.BorderLayout(40, 20));

        panelSuperior.setLayout(new java.awt.GridLayout(2, 2, 0, 10));

        jLabel1.setText("Inicio:");
        panelSuperior.add(jLabel1);

        cmbInicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelSuperior.add(cmbInicio);

        jLabel2.setText("Destino:");
        panelSuperior.add(jLabel2);

        cmbDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelSuperior.add(cmbDestino);

        add(panelSuperior, java.awt.BorderLayout.NORTH);

        panelBotones.setLayout(new java.awt.GridLayout(2, 1, 0, 40));

        btnBuscarLibro.setText("Buscar Recomendación de Libro");
        btnBuscarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroActionPerformed(evt);
            }
        });
        panelBotones.add(btnBuscarLibro);

        btnBuscarSeccion.setText("Buscar Ruta entre Secciones");
        btnBuscarSeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarSeccionActionPerformed(evt);
            }
        });
        panelBotones.add(btnBuscarSeccion);

        add(panelBotones, java.awt.BorderLayout.EAST);

        tblMatrizAdyacencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblMatrizAdyacencia);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        txtResultado.setColumns(20);
        txtResultado.setRows(5);
        jScrollPane2.setViewportView(txtResultado);

        add(jScrollPane2, java.awt.BorderLayout.SOUTH);

        panelAristasNodos.setLayout(new java.awt.GridLayout(5, 2, 4, 20));

        jLabel3.setText("Nodo:");
        panelAristasNodos.add(jLabel3);
        panelAristasNodos.add(txtNodo);

        btnAgregarNodo.setText("Agregar Nodo");
        btnAgregarNodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarNodoActionPerformed(evt);
            }
        });
        panelAristasNodos.add(btnAgregarNodo);

        btnEliminarNodo.setText("Eliminar Nodo");
        btnEliminarNodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarNodoActionPerformed(evt);
            }
        });
        panelAristasNodos.add(btnEliminarNodo);

        jLabel4.setText("Arista Origen:");
        panelAristasNodos.add(jLabel4);

        cmbOrigenArista.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelAristasNodos.add(cmbOrigenArista);

        jLabel5.setText("Arista Destino:");
        panelAristasNodos.add(jLabel5);

        cmbDestinoArista.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelAristasNodos.add(cmbDestinoArista);

        btnAgregarArista.setText("Agregar Arista");
        btnAgregarArista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarAristaActionPerformed(evt);
            }
        });
        panelAristasNodos.add(btnAgregarArista);

        btnEliminarArista.setText("Eliminar Arista");
        btnEliminarArista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarAristaActionPerformed(evt);
            }
        });
        panelAristasNodos.add(btnEliminarArista);

        add(panelAristasNodos, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroActionPerformed
        buscarRecomendacionLibro();
    }//GEN-LAST:event_btnBuscarLibroActionPerformed

    private void btnBuscarSeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarSeccionActionPerformed
        buscarRutaSeccion();
    }//GEN-LAST:event_btnBuscarSeccionActionPerformed

    private void btnAgregarNodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarNodoActionPerformed
        agregarNodo();
    }//GEN-LAST:event_btnAgregarNodoActionPerformed

    private void btnEliminarNodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarNodoActionPerformed
        eliminarNodo();
    }//GEN-LAST:event_btnEliminarNodoActionPerformed

    private void btnAgregarAristaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarAristaActionPerformed
        agregarArista();
    }//GEN-LAST:event_btnAgregarAristaActionPerformed

    private void btnEliminarAristaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAristaActionPerformed
        eliminarArista();
    }//GEN-LAST:event_btnEliminarAristaActionPerformed

    @Override
    public void cambiarBusqueda(Constantes.Busqueda busqueda) {

    }

    @Override
    public void cambiarOrdenamiento(Constantes.Ordenamiento ordenamiento) {

    }

    @Override
    public void Limpiar() {

    }

    @Override
    public void Listar() {

    }

    @Override
    public boolean Guardar() {
        return false;
    }

    @Override
    public void Activar(Constantes.Activar tipo) {

    }

    @Override
    public boolean Eliminar() {
        return false;
    }

    @Override
    public <T> T Buscar() {
        return null;
    }

    @Override
    public boolean validarCampos() {
        return true;
    }

    @Override
    public void Ordenar(boolean asc) {

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarArista;
    private javax.swing.JButton btnAgregarNodo;
    private javax.swing.JButton btnBuscarLibro;
    private javax.swing.JButton btnBuscarSeccion;
    private javax.swing.JButton btnEliminarArista;
    private javax.swing.JButton btnEliminarNodo;
    private javax.swing.JComboBox<String> cmbDestino;
    private javax.swing.JComboBox<String> cmbDestinoArista;
    private javax.swing.JComboBox<String> cmbInicio;
    private javax.swing.JComboBox<String> cmbOrigenArista;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelAristasNodos;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JTable tblMatrizAdyacencia;
    private javax.swing.JTextField txtNodo;
    private javax.swing.JTextArea txtResultado;
    // End of variables declaration//GEN-END:variables
}
