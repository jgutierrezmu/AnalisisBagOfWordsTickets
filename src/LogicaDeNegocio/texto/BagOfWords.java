package LogicaDeNegocio.texto;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors; //Libreria para poder guardar los tokens fuertes

//Clase que modela el comportamiento de Bag of words de cada tiquete
public class BagOfWords {

    //Atributos
    private String textoOriginal; //Texto proveniente del ticket
    private String textoNormalizado; //Texto normalizado del ticket
    private List<String> tokens; //Palabras separadas para el texto
    private List<String> tokensFiltrados; //Palabras filtradas del ticket
    private Map<String,Integer> vectorFrecuencias; //Vector que contiene palabra clave con su frecuencia

    //Constructor sobrecargado, no se usa default porque nunca se van a instanciar objetos BagOfWords vacios
    public BagOfWords(String descripcionTicket) {
        //VALIDACIONES BASICAS
        if (descripcionTicket == null || descripcionTicket.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía para crear Bag of Words");
        }
        if (descripcionTicket.trim().length() < 5) {
            throw new IllegalArgumentException("La descripción debe tener al menos 5 caracteres para análisis");
        }
        this.textoOriginal = descripcionTicket;
        this.tokens = new ArrayList<>();
        this.tokensFiltrados = new ArrayList<>();
        this.vectorFrecuencias = new HashMap<>();

        procesarTexto();
    }

    //GETTERS
    public List<String> getPalabrasClave() {
        return new ArrayList<>(vectorFrecuencias.keySet());
    }
    public Map<String, Integer> getVectorFrecuencias() {
        return vectorFrecuencias;
    }
    public String getTextoOriginal() {
        return textoOriginal;
    }
    public String getTextoNormalizado() {
        return textoNormalizado;
    }

    //Metodo para procesar el texto de la descripción del ticket para el análisis de Bag of Words
    public void procesarTexto () {

        //Primer paso: Normalizar el texto (pasar a minusculas, quitar tildes y caracteres no validos)

        //a. Pasar texto a minusculas
        String texto = textoOriginal.toLowerCase();

        //b. Quitar tildes usando Normalizer
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        //c. Quitar caracteres no válidos (mantener solo letras y espacios)
        String expresionRegular = "[^a-zñ\\s]";
        this.textoNormalizado = texto.replaceAll(expresionRegular, "");

        //Segundo paso: Determinar los tokens
        //El metodo "split" con "\\s+" separa por espacios múltiples y aisla cada palabra
        this.tokens = Arrays.asList(textoNormalizado.split("\\s+"));

        //Tercer paso: Eliminar stopwords
        //Variable local para stopwords (Estas palabras cortas son las que se eliminan del texto tokenizado)
        List<String> stopWords = Arrays.asList("el", "la", "de", "y", "en", "a", "los", "las",
                "un", "una", "es", "son", "con", "por", "para",
                "se", "su", "sus", "del", "al");

        //Extraer las palabras con significado fuerte
        //Se recorre la lista "tokens" y para cada palabra se evaluan dos condiciones: Si es menor que dos
        //caracteres o si es parte de la lista "stopwords". Si se cumple alguna de estas dos, se elimina
        this.tokensFiltrados = tokens.stream()
                .filter(t -> !stopWords.contains(t) && t.length() > 2)
                .collect(Collectors.toList());

        //Cuarto paso: Contar frecuencia para cada token
        for (String token : tokensFiltrados) {
            vectorFrecuencias.put(token, vectorFrecuencias.getOrDefault(token, 0) + 1);
        }
    }

}
