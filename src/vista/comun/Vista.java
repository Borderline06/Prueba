package vista.comun;

import javax.swing.JInternalFrame;
import modelo.comun.Clase;
import utilitarios.Constantes;

public abstract class Vista extends javax.swing.JPanel implements IVista {

    public String nombre;
    public String TAD;
    private JInternalFrame internalFrame;

    public Vista(String nombre) {
        this.nombre = nombre;
    }

    //todas las clases que hereden por defecto tendran el metodo inicializar        
    public void InicializarVista() {
        Limpiar();
        Listar();
    }

    public void setInternalFrame(JInternalFrame internalFrame) {
        this.internalFrame = internalFrame;
    }

    public JInternalFrame getInternalFrame() {
        return internalFrame;
    }

    public abstract void cambiarBusqueda(Constantes.Busqueda busqueda);

    public abstract void cambiarOrdenamiento(Constantes.Ordenamiento ordenamiento);
}
