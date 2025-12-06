package AccesoADatos;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import LogicaDeNegocio.entidades.Usuario;
import oracle.jdbc.internal.OracleTypes;

public class ServicioUsuario extends Servicio {

    // SENTENCIAS SQL - ORACLE
    private static final String INSERTAR_USUARIO = "{call insertarUsuario (?,?,?,?,?,?)}";
    private static final String MODIFICAR_USUARIO = "{call modificarUsuario (?,?,?,?,?,?)}";
    private static final String ELIMINAR_USUARIO = "{call eliminarUsuario (?)}";
    private static final String BUSCAR_USUARIO_POR_ID = "{?=call buscarUsuario(?)}";
    private static final String BUSCAR_USUARIO_POR_EMAIL = "{?=call buscarUsuarioPorEmail(?)}";
    private static final String LISTAR_USUARIOS = "{?=call listarUsuarios()}";

    public ServicioUsuario() {}

    // LISTAR USUARIOS
    public Collection<Usuario> listarUsuarios() throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        ArrayList<Usuario> lista = new ArrayList<>();

        try {
            pstmt = conexion.prepareCall(LISTAR_USUARIOS);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("telefono")
                );
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }

        } catch (SQLException e) {
            throw new GlobalException("Sentencia no válida al listar usuarios");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando conexión");
            }
        }

        if (lista.isEmpty()) {
            throw new NoDataException("No hay usuarios registrados");
        }
        return lista;
    }

    // AGREGAR USUARIO
    public void insertarUsuario(Usuario u) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(INSERTAR_USUARIO);
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getNombre());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPassword());
            pstmt.setString(5, u.getTelefono());
            pstmt.setString(6, u.getRol());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la inserción del usuario");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error en inserción de usuario: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando conexión");
            }
        }
    }

    // MODIFICAR USUARIO
    public void modificarUsuario(Usuario u) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(MODIFICAR_USUARIO);
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getNombre());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPassword());
            pstmt.setString(5, u.getTelefono());
            pstmt.setString(6, u.getRol());

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se modificó ningún registro");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al modificar usuario");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando conexión");
            }
        }
    }

    // ELIMINAR USUARIO
    public void eliminarUsuario(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(ELIMINAR_USUARIO);
            pstmt.setString(1, id);

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se eliminó el usuario");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar usuario: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando conexión");
            }
        }
    }

    // BUSCAR USUARIO POR ID
    public Usuario buscarUsuarioPorId(String id) throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        Usuario u = null;

        try {
            pstmt = conexion.prepareCall(BUSCAR_USUARIO_POR_ID);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, id);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                u = new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("telefono")
                );
                u.setRol(rs.getString("rol"));
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar usuario por ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando conexión");
            }
        }
        return u;
    }

    //BUSCCAR USUIARIOS POR EMAIL
    public Usuario buscarUsuarioPorEmail(String email) throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        Usuario u = null;

        try {
            pstmt = conexion.prepareCall(BUSCAR_USUARIO_POR_EMAIL);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, email);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                u = new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("telefono")
                );
                u.setRol(rs.getString("rol"));
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar usuario por email: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando conexión");
            }
        }
        return u;
    }
}