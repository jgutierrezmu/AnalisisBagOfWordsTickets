package Control;

import AccesoADatos.ServicioDiccionarioEmocional;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import LogicaDeNegocio.enums.Emocion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//CONTROLADOR DE DICCIONARIO EMOCIONAL
public class ControladorDiccionarioEmocional {

    private ServicioDiccionarioEmocional servicio; // Instancia del servicio

    // CONSTRUCTOR
    public ControladorDiccionarioEmocional() {
        this.servicio = new ServicioDiccionarioEmocional();
    }

    // REGISTRAR NUEVA PALABRA
    public boolean registrar(String palabra, Emocion emocion) {
        try {
            // Validar que NO exista ya la palabra
            String[] existente = servicio.buscarDiccionarioEmocional(palabra);

            if (existente != null) {
                System.out.println("La palabra ya existe en el diccionario emocional: " + palabra);
                return false;
            }

            // Insertar nueva palabra
            servicio.insertarDiccionarioEmocional(palabra, emocion);
            System.out.println("Palabra emocional registrada: " + palabra + " → " + emocion);
            return true;

        } catch (GlobalException | NoDataException e) {
            System.out.println("Error registrando palabra emocional: " + e.getMessage());
            return false;
        }
    }

    //LISTAR DICCIONARIO
    public Collection<String[]> obtenerTodos() {
        try {
            return servicio.listarDiccionarioEmocional();
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error listando diccionario emocional: " + e.getMessage());
            return null;
        }
    }

    // ACTUALIZAR PALABRA
    public boolean actualizar(String palabra, Emocion emocion) {
        try {
            servicio.modificarDiccionarioEmocional(palabra, emocion);
            System.out.println("Palabra modificada: " + palabra + " → " + emocion);
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error actualizando diccionario emocional: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR PALABRA
    public boolean eliminar(String palabra) {
        try {
            servicio.eliminarDiccionarioEmocional(palabra);
            System.out.println("Palabra eliminada del diccionario emocional: " + palabra);
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error eliminando palabra emocional: " + e.getMessage());
            return false;
        }
    }

    // BUSCAR PALABRA
    public String[] buscar(String palabra) {
        try {
            return servicio.buscarDiccionarioEmocional(palabra);
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error buscando palabra emocional: " + e.getMessage());
            return null;
        }
    }

}
