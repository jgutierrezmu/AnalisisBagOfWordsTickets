package AccesoADatos;

import LogicaDeNegocio.entidades.Ticket;
import LogicaDeNegocio.entidades.Usuario;
import LogicaDeNegocio.entidades.Departamento;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.internal.OracleTypes;

public class ServicioTicket extends Servicio {

    // PROCEDIMIENTOS Y FUNCIONES
    private static final String insertarTicket = "{call insertarTicket(?,?,?,?,?,?)}";
    private static final String modificarTicket = "{call modificarTicket(?,?,?,?)}";
    private static final String eliminarTicket = "{call eliminarTicket(?)}";
    private static final String buscarTicket = "{?=call buscarTicket(?)}";
    private static final String listarTickets = "{?=call listarTickets()}";
    private static final String listarTicketsPorUsuario = "{?=call listarTicketsPorUsuario(?)}";
    private static final String listarTicketsPorDepartamento = "{?=call listarTicketsPorDepartamento(?)}";

    // INSERTAR TICKET
    public void insertar(Ticket ticket) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(insertarTicket);
            pstmt.setString(1, ticket.getId());
            pstmt.setString(2, ticket.getUsuario().getId());
            pstmt.setString(3, ticket.getDepartamento().getId());
            pstmt.setString(4, ticket.getAsunto());
            pstmt.setString(5, ticket.getDescripcion());
            pstmt.setString(6, ticket.getEstado());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new NoDataException("No se pudo insertar el ticket");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al insertar ticket: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error al cerrar conexión");
            }
        }
    }

    // MODIFICAR TICKET
    public void modificar(Ticket ticket) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(modificarTicket);
            pstmt.setString(1, ticket.getId());
            pstmt.setString(2, ticket.getAsunto());
            pstmt.setString(3, ticket.getDescripcion());
            pstmt.setString(4, ticket.getEstado());

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se modificó ningún ticket");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al modificar ticket");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // ELIMINAR TICKET
    public void eliminar(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(eliminarTicket);
            pstmt.setString(1, id);

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se eliminó ningún ticket");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar ticket: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // BUSCAR TICKET POR ID - CORREGIDO CON PASSWORD
    public Ticket buscar(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        Ticket ticket = null;

        try {
            pstmt = conexion.prepareCall(buscarTicket);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, id);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                // Crear Usuario completo CON PASSWORD REAL
                Usuario usuario = new Usuario(
                        rs.getString("usuario_id"),
                        rs.getString("usuario_nombre"),
                        rs.getString("usuario_email"),
                        rs.getString("usuario_password"),
                        rs.getString("usuario_telefono")
                );
                usuario.setRol(rs.getString("usuario_rol"));

                // Crear Departamento completo
                Departamento depto = new Departamento(
                        rs.getString("departamento_id"),
                        rs.getString("departamento_nombre"),
                        rs.getString("departamento_descripcion"),
                        rs.getString("departamento_contacto")
                );

                ticket = new Ticket(
                        rs.getString("ticket_id"),
                        usuario,
                        depto,
                        rs.getString("asunto"),
                        rs.getString("ticket_descripcion"),
                        rs.getString("estado")
                );
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar ticket: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }

        if (ticket == null) {
            throw new NoDataException("No se encontró el ticket con ID: " + id);
        }

        return ticket;
    }

    // LISTAR TODOS LOS TICKETS - CORREGIDO CON PASSWORD
    public ArrayList<Ticket> listar() throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Ticket> lista = new ArrayList<>();

        try {
            pstmt = conexion.prepareCall(listarTickets);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                // Crear Usuario completo CON PASSWORD REAL
                Usuario usuario = new Usuario(
                        rs.getString("usuario_id"),
                        rs.getString("usuario_nombre"),
                        rs.getString("usuario_email"),
                        rs.getString("usuario_password"),
                        rs.getString("usuario_telefono")
                );
                usuario.setRol(rs.getString("usuario_rol"));

                // Crear Departamento completo
                Departamento depto = new Departamento(
                        rs.getString("departamento_id"),
                        rs.getString("departamento_nombre"),
                        rs.getString("departamento_descripcion"),
                        rs.getString("departamento_contacto")
                );

                Ticket ticket = new Ticket(
                        rs.getString("ticket_id"),
                        usuario,
                        depto,
                        rs.getString("asunto"),
                        rs.getString("ticket_descripcion"),
                        rs.getString("estado")
                );

                lista.add(ticket);
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar tickets: " + e.getMessage());
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
            throw new NoDataException("No hay tickets registrados");
        }

        return lista;
    }

    // LISTAR TICKETS POR USUARIO - CORREGIDO CON PASSWORD
    public ArrayList<Ticket> listarPorUsuario(String usuarioId) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Ticket> lista = new ArrayList<>();

        try {
            pstmt = conexion.prepareCall(listarTicketsPorUsuario);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, usuarioId);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                // Crear Usuario completo CON PASSWORD REAL
                Usuario usuario = new Usuario(
                        rs.getString("usuario_id"),
                        rs.getString("usuario_nombre"),
                        rs.getString("usuario_email"),
                        rs.getString("usuario_password"),
                        rs.getString("usuario_telefono")
                );
                usuario.setRol(rs.getString("usuario_rol"));

                // Crear Departamento completo
                Departamento depto = new Departamento(
                        rs.getString("departamento_id"),
                        rs.getString("departamento_nombre"),
                        rs.getString("departamento_descripcion"),
                        rs.getString("departamento_contacto")
                );

                Ticket ticket = new Ticket(
                        rs.getString("ticket_id"),
                        usuario,
                        depto,
                        rs.getString("asunto"),
                        rs.getString("ticket_descripcion"),
                        rs.getString("estado")
                );

                lista.add(ticket);
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar tickets del usuario: " + e.getMessage());
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
            throw new NoDataException("El usuario no tiene tickets");
        }

        return lista;
    }

    // LISTAR TICKETS POR DEPARTAMENTO - CORREGIDO CON PASSWORD
    public ArrayList<Ticket> listarPorDepartamento(String deptoId) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Ticket> lista = new ArrayList<>();

        try {
            pstmt = conexion.prepareCall(listarTicketsPorDepartamento);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, deptoId);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                // Crear Usuario completo CON PASSWORD REAL
                Usuario usuario = new Usuario(
                        rs.getString("usuario_id"),
                        rs.getString("usuario_nombre"),
                        rs.getString("usuario_email"),
                        rs.getString("usuario_password"),
                        rs.getString("usuario_telefono")
                );
                usuario.setRol(rs.getString("usuario_rol"));

                // Crear Departamento completo
                Departamento depto = new Departamento(
                        rs.getString("departamento_id"),
                        rs.getString("departamento_nombre"),
                        rs.getString("departamento_descripcion"),
                        rs.getString("departamento_contacto")
                );

                Ticket ticket = new Ticket(
                        rs.getString("ticket_id"),
                        usuario,
                        depto,
                        rs.getString("asunto"),
                        rs.getString("ticket_descripcion"),
                        rs.getString("estado")
                );

                lista.add(ticket);
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar tickets del departamento: " + e.getMessage());
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
            throw new NoDataException("El departamento no tiene tickets");
        }

        return lista;
    }
}