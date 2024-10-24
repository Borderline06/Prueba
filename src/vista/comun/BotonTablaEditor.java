package vista.comun;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class BotonTablaEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private JButton boton;
    private String texto;
    private boolean estaPresionado;
    private JTable tabla;
    private ActionListener actionListener;


    public BotonTablaEditor(JTable table) {
        boton = new JButton();
        boton.setOpaque(true);
        boton.addActionListener(this);
        this.tabla = table;
    }
    public BotonTablaEditor(JTable table, ActionListener actionListener) {
        boton = new JButton();
        boton.setOpaque(true);
        boton.addActionListener(this);
        this.tabla = table;
        this.actionListener = actionListener;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (value != null) {
            boton.setText(value.toString());
        } else {
            boton.setText("Acción");
        }
        texto = boton.getText();
        estaPresionado = true;
        return boton;
    }

    @Override
    public Object getCellEditorValue() {
        if (estaPresionado) { 
            Object id = ((DefaultTableModel)tabla.getModel()).getValueAt(tabla.getSelectedRow(), 0);
            if ("Eliminar".equals(texto)) {
                JOptionPane.showMessageDialog(boton, "Eliminando Id: " + id);
               // ((DefaultTableModel)tabla.getModel()).removeRow(tabla.getSelectedRow());
                actionListener.actionPerformed(new ActionEvent(boton, ActionEvent.ACTION_PERFORMED, "" + tabla.getSelectedRow()));                
            } else if ("Detalle".equals(texto)) {                
                actionListener.actionPerformed(new ActionEvent(boton, ActionEvent.ACTION_PERFORMED, "" + id));
            }
            
        }
        estaPresionado = false;
        return texto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
    }

}
