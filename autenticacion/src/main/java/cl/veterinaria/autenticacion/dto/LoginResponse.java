package cl.veterinaria.autenticacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Respuesta del login: el token y los datos que el frontend muestra (tipo Sesion).
 */
@Schema(description = "Sesión iniciada")
public record LoginResponse(
        @Schema(description = "JWT para enviar en Authorization: Bearer <token>") String token,
        @Schema(example = "Ignacio Perez") String nombre,
        @Schema(example = "ignacio@gmail.com") String email,
        @Schema(example = "cliente", allowableValues = {"admin", "cliente"}) String rol,
        @Schema(description = "Segundos de validez del token", example = "3600") long expiraEn
) {
}
