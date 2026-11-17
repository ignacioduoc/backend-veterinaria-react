package cl.veterinaria.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos que envia el formulario de registro.
 * No incluye "rol": el backend siempre registra clientes.
 */
@Schema(description = "Datos para registrar un nuevo cliente")
public record RegistroRequest(

        @Schema(example = "Ignacio Perez")
        @NotBlank(message = "Debe ingresar su nombre")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @Schema(example = "ignacio@gmail.com")
        @NotBlank(message = "Debe ingresar su correo")
        @Email(message = "Debe ingresar un correo válido")
        @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
        String email,

        @Schema(example = "Cliente123")
        @NotBlank(message = "Debe ingresar una contraseña")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        String password
) {
}
