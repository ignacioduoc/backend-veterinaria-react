package cl.veterinaria.pagos.exception;

// 400: los datos individualmente son válidos, pero no tienen sentido juntos.
public class PagoInvalidoException extends RuntimeException {

    public PagoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
