package AccesoADatos;

import LogicaDeNegocio.entidades.Departamento;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import oracle.jdbc.internal.OracleTypes;

public class ServicioDepartamento extends Servicio {

    // CALLS A PROCEDIMIENTOS/FUNCIONES SQL
    private static final String insertarDepartamento = "{call insertarDepartamento(?,?,?,?)}";
    private static final String modificarDepartamento = "{call modificarDepartamento(?,?,?,?)}";
    private static final String eliminarDepartamento = "{call eliminarDepartamento(?)}";
    private static final String buscarDepartamento = "{?=call buscarDepartamento(?)}";
    private static final String listarDepartamentos = "{?=call listarDepartamentos()}";

    public ServicioDepartamento() {}

    // LISTAR DEPARTAMENTOS
    public Collection<Departamento> listarDepartamentos() throws GlobalException, NoDataException {
        conectar();
        ArrayList<Departamento> lista = new ArrayList<>();
        CallableStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conexion.prepareCall(listarDepartamentos);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                Departamento dep = new Departamento(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("contacto")
                );
                lista.add(dep);
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar departamentos");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }

        if (lista.isEmpty()) {
            throw new NoDataException("No hay departamentos registrados");
        }

        return lista;
    }

    // INSERTAR DEPARTAMENTO
    public void insertar(Departamento dep) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(insertarDepartamento);
            pstmt.setString(1, dep.getId());
            pstmt.setString(2, dep.getNombre());
            pstmt.setString(3, dep.getDescripcion());
            pstmt.setString(4, dep.getContacto());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la inserción del departamento");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al insertar departamento: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // MODIFICAR
    public void modificar(Departamento dep) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(modificarDepartamento);
            pstmt.setString(1, dep.getId());
            pstmt.setString(2, dep.getNombre());
            pstmt.setString(3, dep.getDescripcion());
            pstmt.setString(4, dep.getContacto());

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se modificó ningún departamento");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al modificar departamento");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // ELIMINAR
    public void eliminar(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(eliminarDepartamento);
            pstmt.setString(1, id);

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se eliminó ningún departamento");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar departamento: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // BUSCAR POR ID
    public Departamento buscar(String id) throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        Departamento dep = null;

        try {
            pstmt = conexion.prepareCall(buscarDepartamento);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, id);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                dep = new Departamento(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("contacto")
                );
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar departamento");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
        return dep;
    }
}
