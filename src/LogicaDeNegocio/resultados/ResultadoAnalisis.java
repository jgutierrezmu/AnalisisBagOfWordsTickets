package LogicaDeNegocio.resultados;

import LogicaDeNegocio.enums.Emocion;
import LogicaDeNegocio.enums.CategoriaTec;

import java.util.List;
import java.util.Map;

public class ResultadoAnalisis {

    private Emocion emocionPredominante;
    private CategoriaTec categoriaPredominante;

    private List<String> palabrasEncontradasEmocionales;
    private List<String> palabrasEncontradasTecnicas;

    private Map<String, Integer> vectorFrecuencias;
    private String textoNormalizado;

    //Constructor
    public ResultadoAnalisis(
            Emocion emocionPredominante,
            CategoriaTec categoriaPredominante,
            List<String> palabrasEncontradasEmocionales,
            List<String> palabrasEncontradasTecnicas,
            Map<String, Integer> vectorFrecuencias,
            String textoNormalizado) {

        this.emocionPredominante = emocionPredominante;
        this.categoriaPredominante = categoriaPredominante;
        this.palabrasEncontradasEmocionales = palabrasEncontradasEmocionales;
        this.palabrasEncontradasTecnicas = palabrasEncontradasTecnicas;
        this.vectorFrecuencias = vectorFrecuencias;
        this.textoNormalizado = textoNormalizado;
    }

    //GETTERS
    public Emocion getEmocionPredominante() {
        return emocionPredominante;
    }
    public CategoriaTec getCategoriaPredominante() {
        return categoriaPredominante;
    }
    public List<String> getPalabrasEncontradasEmocionales() {
        return palabrasEncontradasEmocionales;
    }
    public List<String> getPalabrasEncontradasTecnicas() {
        return palabrasEncontradasTecnicas;
    }
    public Map<String, Integer> getVectorFrecuencias() {
        return vectorFrecuencias;
    }
    public String getTextoNormalizado() {
        return textoNormalizado;
    }
}