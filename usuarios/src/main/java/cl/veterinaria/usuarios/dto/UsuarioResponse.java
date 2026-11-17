package cl.veterinaria.usuarios.dto;

import cl.veterinaria.usuarios.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Lo que el servicio devuelve hacia afuera. NUNCA incluye la contrasena.
 * Tiene la misma forma que el tipo "Sesion" del frontend (nombre, email, rol).
 */
@Schema(description = "Datos publicos de un usuario (sin contrasena)")
public record UsuarioResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Ignacio Perez") String nombre,
        @Schema(example = "ignacio@gmail.com") String email,
        @Schema(example = "cliente", allowableValues = {"admin", "cliente"}) String rol
) {
    // Convierte la entidad en DTO en un solo lugar.
    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}
