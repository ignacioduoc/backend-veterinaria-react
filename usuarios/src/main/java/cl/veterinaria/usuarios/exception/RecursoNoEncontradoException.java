package cl.veterinaria.usuarios.exception;

/** Error de negocio que se traduce en HTTP 404 - el recurso pedido no existe. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
