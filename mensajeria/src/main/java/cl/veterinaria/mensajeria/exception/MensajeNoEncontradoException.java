package cl.veterinaria.mensajeria.exception;

public class MensajeNoEncontradoException extends RuntimeException {

    public MensajeNoEncontradoException(Long id) {
        super("No se encontró el mensaje con id " + id);
    }
}
