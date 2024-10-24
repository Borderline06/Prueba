
package vista.comun;

import utilitarios.Constantes;


public interface IVista {
    public void Limpiar();
    public void Listar();
    public boolean Guardar();
    public void Activar(Constantes.Activar tipo);
    public boolean Eliminar();
    public <T> T Buscar();
    public boolean validarCampos();
    public void Ordenar(boolean asc);
}
