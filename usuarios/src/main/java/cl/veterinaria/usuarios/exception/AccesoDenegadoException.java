package cl.veterinaria.usuarios.exception;

/** Error de negocio que se traduce en HTTP 403 - autenticado, pero sin permiso. */
public class AccesoDenegadoException extends RuntimeException {
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
