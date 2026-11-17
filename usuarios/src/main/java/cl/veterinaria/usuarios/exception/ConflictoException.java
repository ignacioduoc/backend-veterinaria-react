package cl.veterinaria.usuarios.exception;

/** Error de negocio que se traduce en HTTP 409 - el dato ya existe (ej: email repetido). */
public class ConflictoException extends RuntimeException {
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
