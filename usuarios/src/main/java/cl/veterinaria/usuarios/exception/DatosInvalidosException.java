package cl.veterinaria.usuarios.exception;

/** Error de negocio que se traduce en HTTP 400 - regla de negocio no cumplida. */
public class DatosInvalidosException extends RuntimeException {
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
