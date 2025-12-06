package AccesoADatos;

import LogicaDeNegocio.enums.Emocion;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.internal.OracleTypes;

public class ServicioDiccionarioEmocional extends Servicio {

    // PROCEDIMIENTOS PL/SQL
    private static final String INSERTAR = "{call insertarDiccionarioEmocional(?,?)}";
    private static final String MODIFICAR = "{call modificarDiccionarioEmocional(?,?)}";
    private static final String ELIMINAR = "{call eliminarDiccionarioEmocional(?)}";
    private static final String BUSCAR = "{?=call buscarDiccionarioEmocional(?)}";
    private static final String LISTAR = "{?=call listarDiccionarioEmocional()}";

    public ServicioDiccionarioEmocional() {}

    //LISTAR EL DICCIONARO EMOCIONAL
    public Collection<String[]> listarDiccionarioEmocional() throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<String[]> lista = new ArrayList<>();

        try {
            pstmt = conexion.prepareCall(LISTAR);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("palabra"),
                        rs.getString("emocion")
                });
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar diccionario emocional");
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
            throw new NoDataException("No hay palabras registradas en el diccionario emocional");
        }

        return lista;
    }

    // INSERTAR PALABRA EMOCIONAL
    public void insertarDiccionarioEmocional(String palabra, Emocion emocion)
            throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(INSERTAR);
            pstmt.setString(1, palabra);
            pstmt.setString(2, emocion.name());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se insertó la palabra en el diccionario emocional");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al insertar palabra emocional: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // MODIFICAR PALABRA EMOCIONAL
    public void modificarDiccionarioEmocional(String palabra, Emocion emocion)
            throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(MODIFICAR);
            pstmt.setString(1, palabra);
            pstmt.setString(2, emocion.name());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se modificó ninguna palabra");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al modificar palabra emocional");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // ELIMINAR PALABRA EMOCIONAL
    public void eliminarDiccionarioEmocional(String palabra)
            throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(ELIMINAR);
            pstmt.setString(1, palabra);

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se eliminó la palabra del diccionario emocional");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar palabra emocional: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // BUSCAR PALABRA EMOCIONAL
    public String[] buscarDiccionarioEmocional(String palabra)
            throws GlobalException, NoDataException {

        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        String[] resultado = null;

        try {
            pstmt = conexion.prepareCall(BUSCAR);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, palabra);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                resultado = new String[]{
                        rs.getString("palabra"),
                        rs.getString("emocion")
                };
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar palabra emocional");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }

        return resultado;
    }

    //METODO PARA EXTRAER DICCIONARIO Y CONVERTIRLO EN UN HASHMAP
    public Map<String, Emocion> obtenerMapaCompleto() throws GlobalException, NoDataException {

        Map<String, Emocion> mapa = new HashMap<>();
        Collection<String[]> registros = listarDiccionarioEmocional();

        for (String[] fila : registros) {

            String palabra = fila[0];
            String emocionTxt = fila[1];

            if (palabra == null || emocionTxt == null) continue;

            try {
                Emocion emo = Emocion.valueOf(emocionTxt.trim().toUpperCase());
                mapa.put(palabra.trim().toLowerCase(), emo);
            } catch (IllegalArgumentException ex) {
                System.out.println("⚠ Valor de emoción inválido en la BD: " + emocionTxt);
            }
        }

        return mapa;
    }


}
