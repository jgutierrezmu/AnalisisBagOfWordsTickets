package Control;

import AccesoADatos.ServicioUsuario;
import LogicaDeNegocio.entidades.Usuario;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import java.util.Collection;

//CLASE DE CONTROLADOR PARA USUARIOS
public class ControladorUsuario {

    private ServicioUsuario servicioUsuario; //Instancia de servicio usuario

    //CONSTRUCTOR
    public ControladorUsuario() {
        this.servicioUsuario = new ServicioUsuario();
    }

    //METODO PARA REGISTRAR UN USUARIO
    public boolean registrar(Usuario usuario) {
        try {
            // Validar que el ID no exista
            Usuario existentePorId = servicioUsuario.buscarUsuarioPorId(usuario.getId());
            if (existentePorId != null) {
                System.out.println("Usuario ya existe con ID: " + usuario.getId());
                return false;
            }

            // Validar que el email no exista
            Usuario existentePorEmail = servicioUsuario.buscarUsuarioPorEmail(usuario.getEmail());
            if (existentePorEmail != null) {
                System.out.println("Email ya registrado: " + usuario.getEmail());
                return false;
            }

            // Si pasa ambas validaciones, insertar
            servicioUsuario.insertarUsuario(usuario);
            System.out.println("Usuario registrado: " + usuario.getId() + " - " + usuario.getEmail());
            return true;

        } catch (GlobalException | NoDataException e) {
            System.out.println("Error registrando usuario: " + e.getMessage());
            return false;
        }
    }

    //METODO PARA REGRESAR TODOS LOS USUAARIOS REGISTRADOS
    public Collection<Usuario> obtenerTodos() {
        try {
            return servicioUsuario.listarUsuarios();
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error obteniendo usuarios: " + e.getMessage());
            return null;
        }
    }

    //METODO PARA ACTUALIZAR LE INFO DE UN USUARIO
    public boolean actualizar(Usuario usuario) {
        try {
            servicioUsuario.modificarUsuario(usuario);
            System.out.println("Usuario actualizado: " + usuario.getEmail());
            return true;

        } catch (GlobalException | NoDataException e) {
            System.out.println("Error actualizando usuario: " + e.getMessage());
            return false;
        }
    }

    //METODO PARA ELIMINAR UN USUARIO
    public boolean eliminar(String id) {
        try {
            servicioUsuario.eliminarUsuario(id);
            System.out.println("Usuario eliminado ID: " + id);
            return true;
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error eliminando usuario: " + e.getMessage());
            return false;
        }
    }

    //METODO PARA BUSCAR UN USUARIO POR SU ID
    public Usuario buscarPorId(String id) {
        try {
            return servicioUsuario.buscarUsuarioPorId(id);
        } catch (GlobalException | NoDataException e) {
            System.out.println("Error buscando usuario por ID: " + e.getMessage());
            return null;
        }
    }

}