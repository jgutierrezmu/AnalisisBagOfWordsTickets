package Control;

import AccesoADatos.ServicioDepartamento;
import LogicaDeNegocio.entidades.Departamento;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import java.util.Collection;

//CLASE DE CONTROLADOR PARA DEPARTAMENTOS
public class ControladorDepartamento {

    private ServicioDepartamento servicioDepartamento;

    //CONSTRUCTOR
    public ControladorDepartamento() {
        this.servicioDepartamento = new ServicioDepartamento();
    }

    //METODO PARA CREAR UN DEPARTAMENTO
    public boolean crearDepartamento(Departamento departamento) {
        try {
            servicioDepartamento.insertar(departamento);
            System.out.println("Departamento creado: " + departamento.getNombre());
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error creando departamento: " + e.getMessage());
            return false;
        }
    }

    //REGRESAR TODODS LOS DEPARTAMENTOS REGISTRADOS
    public Collection<Departamento> obtenerTodos() {
        try {
            return servicioDepartamento.listarDepartamentos();
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error obteniendo departamentos: " + e.getMessage());
            return null;
        }
    }

    //BUSCAR UN DEPARTAMENTO POR ID
    public Departamento buscarPorId(String id) {
        try {
            return servicioDepartamento.buscar(id);
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error buscando departamento: " + e.getMessage());
            return null;
        }
    }

    //ACTUALIZAR INFORMACION DE UN DEPARTAMENTO
    public boolean actualizarDepartamento(Departamento departamento) {
        try {
            servicioDepartamento.modificar(departamento);
            System.out.println("Departamento actualizado: " + departamento.getNombre());
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error actualizando departamento: " + e.getMessage());
            return false;
        }
    }

    //ELIMINAR UN DEPARTAMENTO SEGUN ID
    public boolean eliminarDepartamento(String id) {
        try {
            servicioDepartamento.eliminar(id);
            System.out.println("Departamento eliminado ID: " + id);
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error eliminando departamento: " + e.getMessage());
            return false;
        }
    }
}