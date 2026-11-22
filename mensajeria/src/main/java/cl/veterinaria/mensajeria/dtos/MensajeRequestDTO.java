package cl.veterinaria.mensajeria.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MensajeRequestDTO(

        @Schema(description = "Nombre de quien escribe", example = "Juan Pérez")
        @NotBlank(message = "Debe ingresar su nombre")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @Schema(description = "Correo para responder", example = "juan@correo.cl")
        @NotBlank(message = "Debe ingresar su correo")
        @Email(message = "Ingrese un correo válido")
        @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
        String correo,

        @Schema(description = "Contenido del mensaje", example = "Hola, quiero agendar una hora para mi perro")
        @NotBlank(message = "Debe escribir un mensaje")
        @Size(max = 2000, message = "El mensaje no puede superar los 2000 caracteres")
        String texto
) {
}