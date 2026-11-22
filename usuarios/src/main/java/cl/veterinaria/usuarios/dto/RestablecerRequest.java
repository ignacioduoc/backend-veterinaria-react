package cl.veterinaria.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos del paso 2 de "Restablecer contrasena" del frontend:
 * el correo de la cuenta y la nueva contrasena.
 */
@Schema(description = "Datos para restablecer la contrasena de un cliente")
public record RestablecerRequest(

        @Schema(example = "ignacio@gmail.com")
        @NotBlank(message = "Debe ingresar su correo")
        @Email(message = "Debe ingresar un correo válido")
        String email,

        @Schema(example = "NuevaClave456")
        @NotBlank(message = "Debe ingresar la nueva contraseña")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        String nuevaPassword
) {
}
