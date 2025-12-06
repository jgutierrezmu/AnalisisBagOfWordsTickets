package AccesoADatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//CLASE BASE PARA LOS SERVICIOS DE ORACLE (DAO)
public class Servicio {

    protected Connection conexion = null;

    public Servicio() {}

    //ESTABLECER CONEXION
    protected void conectar() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@//localhost:1521/xe";
            String username = "SYSTEM";
            String password = "1234";
            conexion = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //CERRAR CONEXION
    protected void desconectar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }

}
