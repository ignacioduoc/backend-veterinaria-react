package cl.veterinaria.usuarios.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato UNICO para todos los errores del servicio.
 * Asi el frontend siempre sabe donde leer el mensaje.
 */
@Schema(description = "Formato estandar de error")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje,
        // Solo en errores de validacion (400): campo -> mensaje.
        Map<String, String> campos
) {
    public static ErrorResponse de(int estado, String error, String mensaje) {
        return new ErrorResponse(LocalDateTime.now(), estado, error, mensaje, null);
    }
}
