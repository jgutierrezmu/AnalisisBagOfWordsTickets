package LogicaDeNegocio.diccionarios;

import AccesoADatos.ServicioDiccionarioTecnico;
import LogicaDeNegocio.enums.CategoriaTec;

import java.util.HashMap;
import java.util.Map;

public class DiccionarioTecnico {

    private Map<String, CategoriaTec> palabras;
    private ServicioDiccionarioTecnico servicio;

    public DiccionarioTecnico() {
        palabras = new HashMap<>();
        servicio = new ServicioDiccionarioTecnico();
        cargarDesdeBD();
    }

    //GETTERS
    public Map<String, CategoriaTec> getPalabras() {
        return palabras;
    }
    public ServicioDiccionarioTecnico getServicio() {
        return servicio;
    }

    //SETTERS
    public void setPalabras(Map<String, CategoriaTec> palabras) {
        this.palabras = palabras;
    }
    public void setServicio(ServicioDiccionarioTecnico servicio) {
        this.servicio = servicio;
    }

    //CARGAR DICCIONARIO A PARTIR DE UNA BASE DE DATOS DE ORACLE
    private void cargarDesdeBD() {
        try {
            for (String[] fila : servicio.listarDiccionarioTecnico()) {
                String palabra = fila[0];
                CategoriaTec cat = CategoriaTec.valueOf(fila[1]);
                palabras.put(palabra, cat);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el diccionario técnico: " + e.getMessage());
        }
    }
}




//package LogicaDeNegocio.diccionarios;
//
//import AccesoADatos.ServicioDiccionarioTecnico;
//import LogicaDeNegocio.enums.CategoriaTec;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class DiccionarioTecnico implements Diccionario<CategoriaTec> {
//
//    private Map<String, CategoriaTec> palabras;
//    private ServicioDiccionarioTecnico servicio;
//
//    public DiccionarioTecnico() {
//        palabras = new HashMap<>();
//        servicio = new ServicioDiccionarioTecnico();
//        cargarDesdeBD();
//    }
//
//    private void cargarDesdeBD() {
//        try {
//            for (String[] fila : servicio.listarDiccionarioTecnico()) {
//                String palabra = fila[0];
//                CategoriaTec cat = CategoriaTec.valueOf(fila[1]);
//                palabras.put(palabra, cat);
//            }
//        } catch (Exception e) {
//            System.out.println("No se pudo cargar el diccionario técnico: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void agregarPalabra(String palabra, CategoriaTec cat) {
//        palabras.put(palabra, cat);
//
//        try {
//            servicio.insertarDiccionarioTecnico(palabra, cat);
//        } catch (Exception e) {
//            System.out.println("Error al insertar palabra técnica en BD: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void eliminarPalabra(String palabra) {
//        palabras.remove(palabra);
//
//        try {
//            servicio.eliminarDiccionarioTecnico(palabra);
//        } catch (Exception e) {
//            System.out.println("Error al eliminar palabra técnica en BD: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public CategoriaTec obtenerValor(String palabra) {
//        return palabras.get(palabra);
//    }
//
//    public void modificarPalabra(String palabra, CategoriaTec cat) {
//        palabras.put(palabra, cat);
//
//        try {
//            servicio.modificarDiccionarioTecnico(palabra, cat);
//        } catch (Exception e) {
//            System.out.println("Error al modificar diccionario técnico: " + e.getMessage());
//        }
//    }
//
//    public Map<String, CategoriaTec> getDiccionario() {
//        return palabras;
//    }
//}









/*package LogicaDeNegocio.diccionarios;

import LogicaDeNegocio.enums.CategoriaTec;
import java.util.HashMap;
import java.util.Map;

public class DiccionarioTecnico implements Diccionario<CategoriaTec> {

    private Map<String, CategoriaTec> palabras;

    public DiccionarioTecnico() {
        palabras = new HashMap<>();

        // Palabras precargadas
        palabras.put("correo", CategoriaTec.SOPORTE_TI);
        palabras.put("email", CategoriaTec.SOPORTE_TI);
        palabras.put("impresora", CategoriaTec.MANTENIMIENTO);
        palabras.put("servidor", CategoriaTec.INFRAESTRUCTURA);
        palabras.put("red", CategoriaTec.INFRAESTRUCTURA);
        palabras.put("factura", CategoriaTec.FINANZAS);
        palabras.put("pago", CategoriaTec.FINANZAS);
        palabras.put("rol", CategoriaTec.RRHH);
        palabras.put("vacaciones", CategoriaTec.RRHH);
    }

    @Override
    public void agregarPalabra(String palabra, CategoriaTec categoria) {
        palabras.put(palabra.toLowerCase(), categoria);
    }

    @Override
    public void eliminarPalabra(String palabra) {
        palabras.remove(palabra.toLowerCase());
    }

    @Override
    public CategoriaTec obtenerValor(String palabra) {
        return palabras.get(palabra.toLowerCase());
    }
}
*/