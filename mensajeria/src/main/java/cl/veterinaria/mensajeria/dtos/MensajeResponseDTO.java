package cl.veterinaria.mensajeria.dtos;

import java.time.LocalDateTime;

public record MensajeResponseDTO(
        Long id,
        String nombre,
        String correo,
        String texto,
        LocalDateTime fecha
) {
}
