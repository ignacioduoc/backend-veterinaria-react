package cl.veterinaria.autenticacion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/** Mismo formato de error que el microservicio usuarios. */
@Schema(description = "Formato estándar de error")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje,
        Map<String, String> campos
) {
    public static ErrorResponse de(int estado, String error, String mensaje) {
        return new ErrorResponse(LocalDateTime.now(), estado, error, mensaje, null);
    }
}
