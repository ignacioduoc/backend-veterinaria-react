package cl.veterinaria.autenticacion.exception;

/** HTTP 503: no se pudo hablar con el microservicio usuarios (apagado o con error). */
public class ServicioNoDisponibleException extends RuntimeException {
    public ServicioNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
