
package vista.modulos;

import datos.AlmacenDatos;
import datos.tad.arbol.Arbol;
import datos.tad.arbol.Nodo;
import javax.swing.JOptionPane;
import utilitarios.Constantes;
import vista.comun.Vista;

public class VistaRecomendacion extends Vista {

    /**
     * Creates new form VistaRecomendacion
     */
    private Arbol<String[]> arbol1;
    private Arbol<String[]> arbol2;
    
    public VistaRecomendacion(Constantes.Ordenamiento ordenamiento, Constantes.Busqueda busqueda) {
        super("Recomendacion");
        initComponents();
        this.TAD = "TAD Arbol binario";
        arbol1 = construirArbol(AlmacenDatos.construirArbolRecomendacion1());
        arbol2 = construirArbol(AlmacenDatos.construirArbolRecomendacion2());
    }
    private Arbol<String[]> construirArbol(Nodo<String[]> raiz) {
        Arbol<String[]> arbol = new Arbol<>();
        arbol.setRaiz(raiz);
        return arbol;
    }
    private void recomendarLibro(Arbol<String[]> arbol) {
        Nodo<String[]> nodoActual = arbol.getRaiz();
        while (nodoActual.getIzquierdo() != null && nodoActual.getDerecho() != null) {
            int respuesta = JOptionPane.showOptionDialog(
                    this,
                    nodoActual.getDato(),
                    "Pregunta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Opción 1", "Opción 2"},
                    "Opción 1"
            );
            nodoActual = (respuesta == JOptionPane.YES_OPTION) ? nodoActual.getIzquierdo() : nodoActual.getDerecho();
        }
        lblRecomendacion.setText("<html>Libro recomendado: " + nodoActual.getDato()[0] + "<br>Descripción: " + nodoActual.getDato()[1] + "</html>");

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnRecomendacion1 = new javax.swing.JButton();
        btnRecomendacion2 = new javax.swing.JButton();
        lblRecomendacion = new javax.swing.JLabel();

        btnRecomendacion1.setText("Recomendacion según Género");
        btnRecomendacion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecomendacion1ActionPerformed(evt);
            }
        });

        btnRecomendacion2.setText("Recomendacion según Narrativa");
        btnRecomendacion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecomendacion2ActionPerformed(evt);
            }
        });

        lblRecomendacion.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblRecomendacion.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblRecomendacion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblRecomendacion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRecomendacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRecomendacion2, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addComponent(btnRecomendacion1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                .addGap(175, 175, 175))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnRecomendacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRecomendacion2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRecomendacion, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRecomendacion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecomendacion1ActionPerformed
        recomendarLibro(arbol1);
    }//GEN-LAST:event_btnRecomendacion1ActionPerformed

    private void btnRecomendacion2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecomendacion2ActionPerformed
        recomendarLibro(arbol2);
    }//GEN-LAST:event_btnRecomendacion2ActionPerformed

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
        return false;
    }

    @Override
    public void Ordenar(boolean asc) {
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRecomendacion1;
    private javax.swing.JButton btnRecomendacion2;
    private javax.swing.JLabel lblRecomendacion;
    // End of variables declaration//GEN-END:variables
}
