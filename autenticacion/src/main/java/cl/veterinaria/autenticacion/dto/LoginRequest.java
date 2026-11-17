package cl.veterinaria.autenticacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Lo que envia el formulario de login. */
@Schema(description = "Credenciales para iniciar sesión")
public record LoginRequest(

        @Schema(example = "ignacio@gmail.com")
        @NotBlank(message = "Debe ingresar su correo")
        @Email(message = "Ingrese un correo válido")
        String email,

        @Schema(example = "Cliente123")
        @NotBlank(message = "Debe ingresar su contraseña")
        String password
) {
}
