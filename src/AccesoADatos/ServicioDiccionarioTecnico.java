package AccesoADatos;

import LogicaDeNegocio.enums.CategoriaTec;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.internal.OracleTypes;

public class ServicioDiccionarioTecnico extends Servicio {

    private static final String INSERTAR = "{call insertarDiccionarioTecnico(?,?)}";
    private static final String MODIFICAR = "{call modificarDiccionarioTecnico(?,?)}";
    private static final String ELIMINAR = "{call eliminarDiccionarioTecnico(?)}";
    private static final String BUSCAR = "{?=call buscarDiccionarioTecnico(?)}";
    private static final String LISTAR = "{?=call listarDiccionarioTecnico()}";

    public ServicioDiccionarioTecnico() {}

    //LISTAR DICCIONARIO TECNICO
    public Collection<String[]> listarDiccionarioTecnico() throws GlobalException, NoDataException {
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
                        rs.getString("categoria")
                });
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar diccionario técnico");
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
            throw new NoDataException("No hay palabras en el diccionario técnico");
        }

        return lista;
    }

    // INSERTAR PALABRA TECNICA
    public void insertarDiccionarioTecnico(String palabra, CategoriaTec categoria)
            throws GlobalException, NoDataException {

        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(INSERTAR);
            pstmt.setString(1, palabra);
            pstmt.setString(2, categoria.name());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se insertó la palabra técnica");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al insertar palabra técnica: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // MODIFICAR PALABRA TECNICA
    public void modificarDiccionarioTecnico(String palabra, CategoriaTec categoria)
            throws GlobalException, NoDataException {

        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(MODIFICAR);
            pstmt.setString(1, palabra);
            pstmt.setString(2, categoria.name());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se modificó la palabra técnica");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al modificar palabra técnica");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // ELIMINAR PALABRA TECNICA
    public void eliminarDiccionarioTecnico(String palabra)
            throws GlobalException, NoDataException {

        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(ELIMINAR);
            pstmt.setString(1, palabra);

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se eliminó la palabra técnica");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar palabra técnica: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }
    }

    // BUSCAR PALABRA TECNICA
    public String[] buscarDiccionarioTecnico(String palabra)
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
                        rs.getString("categoria")
                };
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar palabra técnica");
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
    public Map<String, CategoriaTec> obtenerMapaCompleto() throws GlobalException, NoDataException {

        Map<String, CategoriaTec> mapa = new HashMap<>();
        Collection<String[]> registros = listarDiccionarioTecnico();

        for (String[] fila : registros) {

            String palabra = fila[0];
            String categoriaTxt = fila[1];

            if (palabra == null || categoriaTxt == null) continue;

            try {
                CategoriaTec categoria = CategoriaTec.valueOf(categoriaTxt.trim().toUpperCase());
                mapa.put(palabra.trim().toLowerCase(), categoria);
            } catch (IllegalArgumentException ex) {
                System.out.println("Valor de categoría inválido en la BD: " + categoriaTxt);
            }
        }

        return mapa;
    }

}
