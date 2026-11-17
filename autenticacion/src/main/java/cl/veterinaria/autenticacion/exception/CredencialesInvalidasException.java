package cl.veterinaria.autenticacion.exception;

/** HTTP 401: usuarios respondio que el correo o la contrasena no son correctos. */
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
