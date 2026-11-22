package cl.veterinaria.pagos.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record PagoRequestDTO(

        @Schema(description = "Id de la cita que se paga", example = "1761222000000")
        @NotNull(message = "Debe indicar la cita")
        @Positive(message = "El id de la cita no es válido")
        Long citaId,

        @Schema(description = "Método de pago", example = "tarjeta", allowableValues = {"tarjeta", "transferencia"})
        @NotBlank(message = "Debe indicar el método de pago")
        @Pattern(regexp = "tarjeta|transferencia", message = "El método debe ser tarjeta o transferencia")
        String metodo,

        @Schema(description = "Monto en pesos chilenos, sin puntos ni símbolo", example = "45000")
        @NotNull(message = "Debe indicar el monto")
        @Positive(message = "El monto debe ser mayor a 0")
        Long monto,

        @Schema(description = "Últimos 4 dígitos de la tarjeta (solo con tarjeta)", example = "4242")
        @Pattern(regexp = "\\d{4}", message = "Los últimos 4 dígitos deben ser numéricos")
        String ultimos4
) {
}
