package cl.veterinaria.pagos.dtos;

import java.time.LocalDateTime;

// metodo y estado van en minúscula ("tarjeta", "pagado") para calzar con el tipo Pago del front.
public record PagoResponseDTO(
        Long id,
        String codigo,
        Long citaId,
        String metodo,
        Long monto,
        String estado,
        String ultimos4,
        LocalDateTime fecha
) {
}
