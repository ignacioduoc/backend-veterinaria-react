package cl.veterinaria.pagos.exception;

// 409: la operación choca con el estado actual (cita ya pagada, pago ya confirmado...).
public class PagoConflictoException extends RuntimeException {

    public PagoConflictoException(String mensaje) {
        super(mensaje);
    }
}
