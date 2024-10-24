
package vista;

import java.awt.Image;
import javax.swing.ImageIcon;

public class VistaNosotros extends javax.swing.JPanel {

    /**
     * Creates new form VistaNosotros
     */
    public VistaNosotros() {
        initComponents();
        //definir los nombres de los integrantes
        MostrarIntegrantes();
    }
    private void MostrarIntegrantes() {
        //agregar logo
        
        
        Image imagenEscalada = VentanaPrincipal.logoUTP.getScaledInstance(488, 128, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(imagenEscalada));
    
        //String texto = "<html> Jorge Jorge<br>Brenda Brenda<br>Sandro Sandro</html>";
         // Información de los integrantes
        String[][] integrantes = {
            {"Jorge Jorge", "Ingeniería de Software"},
            {"Brenda Brenda", "Ingeniería de Sistemas"},
            {"Sandro Sandro", "Ingeniería de Sistemas"}
        };

        // Construir el texto a mostrar en el JLabel
        // Stringbuilder es mas eficiente que concatenar cadenas en un for.
        StringBuilder sb = new StringBuilder("<html><h3>Integrantes del Proyecto:</h3><ul>");
        for (String[] integrante : integrantes) {
            sb.append("<li>")
              .append(integrante[0])
              .append(" - ")
              .append(integrante[1])
              .append("</li>");
        }
        sb.append("</ul></html>");
        
        lblSobreNosotros.setText(sb.toString());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblSobreNosotros = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();

        jLabel1.setText("Sobre nosotros:");

        lblSobreNosotros.setText("lblSobreNosotros");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSobreNosotros, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSobreNosotros, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblSobreNosotros;
    // End of variables declaration//GEN-END:variables

    
}
