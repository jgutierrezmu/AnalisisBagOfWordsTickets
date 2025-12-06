package LogicaDeNegocio.diccionarios;

import AccesoADatos.ServicioDiccionarioEmocional;
import LogicaDeNegocio.enums.Emocion;

import java.util.HashMap;
import java.util.Map;

public class DiccionarioEmocional {

    private Map<String, Emocion> palabras;
    private ServicioDiccionarioEmocional servicio;

    public DiccionarioEmocional() {
        palabras = new HashMap<>();
        servicio = new ServicioDiccionarioEmocional();
        cargarDesdeBD();
    }

    //GETTERS
    public Map<String, Emocion> getPalabras() {
        return palabras;
    }
    public ServicioDiccionarioEmocional getServicio() {
        return servicio;
    }

    //SETTERS
    public void setPalabras(Map<String, Emocion> palabras) {
        this.palabras = palabras;
    }
    public void setServicio(ServicioDiccionarioEmocional servicio) {
        this.servicio = servicio;
    }

    //LLENAR DICCINARIO A PARTIR DE BASE DE DATOS DE ORACLE
    private void cargarDesdeBD() {
        try {
            for (String[] fila : servicio.listarDiccionarioEmocional()) {
                String palabra = fila[0];
                Emocion emocion = Emocion.valueOf(fila[1]); // convierte String → Enum
                palabras.put(palabra, emocion);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el diccionario emocional: " + e.getMessage());
        }
    }
}


//package LogicaDeNegocio.diccionarios;
//
//import AccesoADatos.ServicioDiccionarioEmocional;
//import LogicaDeNegocio.enums.Emocion;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class DiccionarioEmocional implements Diccionario<Emocion> {
//
//    private Map<String, Emocion> palabras;
//    private ServicioDiccionarioEmocional servicio;
//
//    public DiccionarioEmocional() {
//        palabras = new HashMap<>();
//        servicio = new ServicioDiccionarioEmocional();
//        cargarDesdeBD();
//    }
//
//    // Cargar todo el diccionario desde Oracle
//    private void cargarDesdeBD() {
//        try {
//            for (String[] fila : servicio.listarDiccionarioEmocional()) {
//                String palabra = fila[0];
//                Emocion emocion = Emocion.valueOf(fila[1]); // convierte String → Enum
//                palabras.put(palabra, emocion);
//            }
//        } catch (Exception e) {
//            System.out.println("No se pudo cargar el diccionario emocional: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void agregarPalabra(String palabra, Emocion emocion) {
//        palabras.put(palabra, emocion);
//
//        try {
//            servicio.insertarDiccionarioEmocional(palabra, emocion);
//        } catch (Exception e) {
//            System.out.println("Error al insertar palabra emocional en BD: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void eliminarPalabra(String palabra) {
//        palabras.remove(palabra);
//
//        try {
//            servicio.eliminarDiccionarioEmocional(palabra);
//        } catch (Exception e) {
//            System.out.println("Error al eliminar palabra emocional en BD: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public Emocion obtenerValor(String palabra) {
//        return palabras.get(palabra);
//    }
//
//    // Permitir modificar emoción
//    public void modificarPalabra(String palabra, Emocion emocion) {
//        palabras.put(palabra, emocion);
//
//        try {
//            servicio.modificarDiccionarioEmocional(palabra, emocion);
//        } catch (Exception e) {
//            System.out.println("Error al modificar palabra emocional en BD: " + e.getMessage());
//        }
//    }
//
//    // Acceder a todas las palabras si es necesario
//    public Map<String, Emocion> getDiccionario() {
//        return palabras;
//    }
//}



/*package LogicaDeNegocio.diccionarios;

import LogicaDeNegocio.enums.Emocion;
import java.util.HashMap;
import java.util.Map;

public class DiccionarioEmocional implements Diccionario<Emocion> {

    private Map<String, Emocion> palabras;

    public DiccionarioEmocional() {
        palabras = new HashMap<>();
        palabras.put("molesto", Emocion.ENOJO);
        palabras.put("furioso", Emocion.ENOJO);
        palabras.put("triste", Emocion.TRISTEZA);
        palabras.put("deprimido", Emocion.TRISTEZA);
        palabras.put("feliz", Emocion.ALEGRIA);
        palabras.put("contento", Emocion.ALEGRIA);
        palabras.put("asustado", Emocion.MIEDO);
        palabras.put("miedo", Emocion.MIEDO);
        palabras.put("sorprendido", Emocion.SORPRESA);
    }

    @Override
    public void agregarPalabra(String palabra, Emocion emocion) {
        palabras.put(palabra.toLowerCase(), emocion);
    }

    @Override
    public void eliminarPalabra(String palabra) {
        palabras.remove(palabra.toLowerCase());
    }

    @Override
    public Emocion obtenerValor(String palabra) {
        return palabras.get(palabra.toLowerCase());
    }
}
*/
