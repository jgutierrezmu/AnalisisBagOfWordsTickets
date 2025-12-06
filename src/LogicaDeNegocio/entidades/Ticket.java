package LogicaDeNegocio.entidades;

import LogicaDeNegocio.texto.BagOfWords;
import LogicaDeNegocio.texto.AnalizadorBoW;
import LogicaDeNegocio.resultados.ResultadoAnalisis;

public class Ticket {

    //ATRIBUTOS
    private String id;
    private String asunto;
    private String descripcion;
    private String estado;
    private Usuario usuario;
    private Departamento departamento;
    private BagOfWords bag;

    //CONSTRUCTOR
    public Ticket(String id, Usuario usuario, Departamento departamento, String asunto, String descripcion, String estado) {
        // Validar objetos no nulos
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo");
        if (departamento == null) throw new IllegalArgumentException("El departamento no puede ser nulo");

        this.id = id;
        this.usuario = usuario;
        this.departamento = departamento;

        // Usar setters para aprovechar validaciones
        setAsunto(asunto);
        setDescripcion(descripcion);
        setEstado(estado);

        // Se genera automáticamente al asignar la descripción
        this.bag = new BagOfWords(descripcion);
    }

    //GETTERS
    public String getId() {
        return id;
    }
    public String getAsunto() {
        return asunto;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getEstado() {
        return estado;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public Departamento getDepartamento() {
        return departamento;
    }
    public BagOfWords getBag() {
        return bag;
    }

    //SETTERS
    public void setId(String id) {
        this.id = id;
    }
    public void setAsunto(String asunto) {
        //VALIDACIONES BASICAS
        if (asunto == null || asunto.trim().isEmpty()) {
            throw new IllegalArgumentException("El asunto no puede estar vacío");
        }
        if (asunto.trim().length() < 5) {
            throw new IllegalArgumentException("El asunto debe tener al menos 5 caracteres");
        }
        this.asunto = asunto.trim();
    }
    public void setDescripcion(String descripcion) {
        //VALIDACIONES BASICAS
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del ticket no puede estar vacía");
        }
        if (descripcion.trim().length() < 5) {
            throw new IllegalArgumentException("La descripción debe tener al menos 5 caracteres");
        }
        this.descripcion = descripcion.trim();

        // Cada vez que se cambia la descripción se actualiza su BoW
        this.bag = new BagOfWords(descripcion.trim());
    }
    public void setEstado(String estado) {
        //VALIDACIONES BASICAS
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede estar vacío");
        }
        // Validar que sea un estado permitido
        String[] estadosPermitidos = {"NUEVO", "EN_PROCESO", "RESUELTO"};
        boolean estadoValido = false;
        for (String e : estadosPermitidos) {
            if (e.equals(estado.toUpperCase())) {
                estadoValido = true;
                break;
            }
        }
        if (!estadoValido) {
            throw new IllegalArgumentException("Estado no válido. Use: NUEVO, EN_PROCESO o RESUELTO");
        }
        this.estado = estado.toUpperCase();
    }
    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        this.usuario = usuario;
    }
    public void setDepartamento(Departamento departamento) {
        if (departamento == null) {
            throw new IllegalArgumentException("El departamento no puede ser nulo");
        }
        this.departamento = departamento;
    }

    //TOSTRING
    public String toString() {
        return "------------------------------" +
                "\n Ticket #" + id +
                "\n Asunto       : " + asunto +
                "\n Descripción  : " + descripcion +
                "\n Estado       : " + estado +
                "\n Usuario      : " + usuario.getNombre() +
                "\n Departamento : " + departamento.getNombre() +
                "\n------------------------------";
    }

    // METODO PARA ANALIZAR EL TICKET COMPLETO USANDO BAG OF WORDS
    public ResultadoAnalisis analizarBoW() {
        try {
            AnalizadorBoW analizador = new AnalizadorBoW();
            return analizador.analizar(descripcion);   // <-- atributo real del ticket
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
