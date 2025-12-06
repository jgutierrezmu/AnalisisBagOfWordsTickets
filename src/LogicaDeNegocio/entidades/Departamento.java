package LogicaDeNegocio.entidades;

public class Departamento {

    //ATRIBUTOS
    private String id;
    private String nombre;
    private String descripcion;
    private String contacto;

    //CONSTRUCTOR
    public Departamento(String id, String nombre, String descripcion, String contacto) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del departamento no puede estar vacío");
        }

        this.id = id;

        // Usar setters para validaciones
        setNombre(nombre);
        setDescripcion(descripcion);
        setContacto(contacto);
    }

    //GETTERS
    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getContacto() {
        return contacto;
    }

    //SETTERS
    public void setId(String id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        //VALIDACIONES BASICAS
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del departamento no puede estar vacío");
        }
        if (nombre.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
        this.nombre = nombre.trim();
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setContacto(String contacto) {
        if (contacto != null && !contacto.trim().isEmpty()) {
            if (!contacto.contains("@") || !contacto.contains(".")) {
                throw new IllegalArgumentException("El contacto debe ser un email válido");
            }
        }
        this.contacto = contacto;
    }

    //TOSTRING
    public String toString() {
        return  "------------------------------" +
                "\n ID          : " + id +
                "\n Nombre      : " + nombre +
                "\n Descripción : " + descripcion +
                "\n Contacto    : " + contacto +
                "\n------------------------------";
    }

}
