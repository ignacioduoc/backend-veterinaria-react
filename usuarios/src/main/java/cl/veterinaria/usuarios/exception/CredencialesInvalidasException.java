package cl.veterinaria.usuarios.exception;

/** Error de negocio que se traduce en HTTP 401 - email o contrasena incorrectos. */
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
