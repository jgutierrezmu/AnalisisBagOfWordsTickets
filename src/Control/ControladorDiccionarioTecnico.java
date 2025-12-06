package Control;

import AccesoADatos.ServicioDiccionarioTecnico;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import LogicaDeNegocio.enums.CategoriaTec;

import java.util.Collection;

//CLASE CONTROLADOR PARA DICCIONARIO TECNICO
public class ControladorDiccionarioTecnico {

    private ServicioDiccionarioTecnico servicio; // Instancia del servicio

    // CONSTRUCTOR
    public ControladorDiccionarioTecnico() {
        this.servicio = new ServicioDiccionarioTecnico();
    }

    // REGISTRAR NUEVA PALABRA TÉCNICA
    public boolean registrar(String palabra, CategoriaTec categoria) {
        try {
            // Validar que NO exista
            String[] existente = servicio.buscarDiccionarioTecnico(palabra);

            if (existente != null) {
                System.out.println("La palabra ya existe en el diccionario técnico: " + palabra);
                return false;
            }

            servicio.insertarDiccionarioTecnico(palabra, categoria);
            System.out.println("Palabra técnica registrada: " + palabra + " → " + categoria);
            return true;

        } catch (GlobalException | NoDataException e) {
            System.out.println("Error registrando palabra técnica: " + e.getMessage());
            return false;
        }
    }

    //LISTAR DICCIONARIO
    public Collection<String[]> obtenerTodos() {
        try {
            return servicio.listarDiccionarioTecnico();
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error listando diccionario técnico: " + e.getMessage());
            return null;
        }
    }

    // ACTUALIZAR PALABRA
    public boolean actualizar(String palabra, CategoriaTec categoria) {
        try {
            servicio.modificarDiccionarioTecnico(palabra, categoria);
            System.out.println("Palabra modificada: " + palabra + " → " + categoria);
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error actualizando diccionario técnico: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR PALABRA
    public boolean eliminar(String palabra) {
        try {
            servicio.eliminarDiccionarioTecnico(palabra);
            System.out.println("Palabra eliminada del diccionario técnico: " + palabra);
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error eliminando palabra técnica: " + e.getMessage());
            return false;
        }
    }

    // BUSCAR PALABRA
    public String[] buscar(String palabra) {
        try {
            return servicio.buscarDiccionarioTecnico(palabra);
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error buscando palabra técnica: " + e.getMessage());
            return null;
        }
    }
}
