package cl.veterinaria.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Respuesta simple con un mensaje de exito. */
@Schema(description = "Mensaje de confirmacion")
public record MensajeResponse(
        @Schema(example = "Contrasena actualizada correctamente") String mensaje
) {
}
