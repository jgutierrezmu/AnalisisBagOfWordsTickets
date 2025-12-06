package LogicaDeNegocio.texto;

import AccesoADatos.ServicioDiccionarioEmocional;
import AccesoADatos.ServicioDiccionarioTecnico;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;

import LogicaDeNegocio.enums.Emocion;
import LogicaDeNegocio.enums.CategoriaTec;
import LogicaDeNegocio.resultados.ResultadoAnalisis;

import java.util.*;

//CLASE QUE REALIZA ANALISIS BoW
public class AnalizadorBoW {

    private ServicioDiccionarioEmocional servicioEmo;
    private ServicioDiccionarioTecnico servicioTec;

    public AnalizadorBoW() {
        this.servicioEmo = new ServicioDiccionarioEmocional();
        this.servicioTec = new ServicioDiccionarioTecnico();
    }

    //MEOTO PARA ANALIZAR TEXTO Y RETORNAR VALOR
    public ResultadoAnalisis analizar(String texto)
            throws GlobalException, NoDataException {

        //1. Bag of Words para procesar el texto
        BagOfWords bow = new BagOfWords(texto);
        List<String> tokens = bow.getPalabrasClave();

        //2. Cargar diccionarios completos desde BD
        Map<String, Emocion> dicEmo = servicioEmo.obtenerMapaCompleto();
        Map<String, CategoriaTec> dicTec = servicioTec.obtenerMapaCompleto();

        //3. Contadores para emociones y categorías
        Map<Emocion, Integer> contadorEmociones = new EnumMap<>(Emocion.class);
        Map<CategoriaTec, Integer> contadorCategorias = new EnumMap<>(CategoriaTec.class);

        //4. Lista de palabras encontradas
        List<String> hitsEmocionales = new ArrayList<>();
        List<String> hitsTecnicos = new ArrayList<>();

        //5. Comparar tokens con diccionarios
        for (String token : tokens) {

            //Diccionario emocional
            if (dicEmo.containsKey(token)) {
                Emocion emo = dicEmo.get(token);
                hitsEmocionales.add(token);
                contadorEmociones.put(emo, contadorEmociones.getOrDefault(emo, 0) + 1);
            }

            //Diccionario técnico
            if (dicTec.containsKey(token)) {
                CategoriaTec cat = dicTec.get(token);
                hitsTecnicos.add(token);
                contadorCategorias.put(cat, contadorCategorias.getOrDefault(cat, 0) + 1);
            }
        }

        //6. Determinar emoción predominante
        Emocion emocionPredominante = contadorEmociones.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        //7. Determinar categoría predominante
        CategoriaTec categoriaPredominante = contadorCategorias.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        //8. Retornar resultado
        return new ResultadoAnalisis(
                emocionPredominante,
                categoriaPredominante,
                hitsEmocionales,
                hitsTecnicos,
                bow.getVectorFrecuencias(),
                bow.getTextoNormalizado()
        );
    }
}
