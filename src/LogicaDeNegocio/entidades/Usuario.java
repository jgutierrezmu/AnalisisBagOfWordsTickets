package LogicaDeNegocio.entidades;

public class Usuario {

    //ATRIBUTOS
    private String id;
    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String rol;

    //CONSTRUCTOR SOBRECARGADO
    public Usuario(String id, String nombre, String email, String password, String telefono) {
        // Validaciones de parámetros obligatorios
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        this.id = id;
        this.nombre = nombre.trim();

        // Usar setters para aprovechar sus validaciones
        setEmail(email);
        setPassword(password);
        setTelefono(telefono);

        this.rol = "estudiante";
    }

    //SETTERS
    public void setId(String id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setEmail(String email) {
        //VALIDACIONES BASICAS
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.email = email.toLowerCase().trim();
    }
    public void setPassword(String password) {
        //VALIDACIONES BASICAS, DEBE TENER 6 CARACTERES MINIMOS
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        this.password = password;
    }
    public void setTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty()) {
            // VALIDAICONES BASICAS, NUMEROS Y AL MENOS 8
            if (!telefono.matches("[+]?[0-9]+")) {
                throw new IllegalArgumentException("El teléfono solo puede contener números y opcionalmente +");
            }
            if (telefono.length() < 8) {
                throw new IllegalArgumentException("El teléfono debe tener al menos 8 dígitos");
            }
        }
        this.telefono = telefono;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    //GETTERS
    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getTelefono() {
        return telefono;
    }
    public String getRol() {
        return rol;
    }

    //TOSTRING
    public String toString() {
        return  "------------------------------" +
                "\n ID        : " + id +
                "\n Nombre    : " + nombre +
                "\n Email     : " + email +
                "\n Teléfono  : " + telefono +
                "\n Rol       : " + rol +
                "\n------------------------------";
    }

}
