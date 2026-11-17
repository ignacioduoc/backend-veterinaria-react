package cl.veterinaria.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Credenciales que envia el microservicio autenticacion para saber si son correctas.
 */
@Schema(description = "Credenciales a validar")
public record ValidarRequest(

        @Schema(example = "ignacio@gmail.com")
        @NotBlank(message = "Debe ingresar su correo")
        @Email(message = "Debe ingresar un correo válido")
        String email,

        @Schema(example = "Cliente123")
        @NotBlank(message = "Debe ingresar su contraseña")
        String password
) {
}
