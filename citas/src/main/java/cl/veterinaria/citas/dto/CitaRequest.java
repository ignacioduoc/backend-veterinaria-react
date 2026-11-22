package cl.veterinaria.citas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CitaRequest {

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String clienteEmail;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String clienteNombre;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    private String mascota;

    @NotBlank(message = "El servicio es obligatorio")
    private String servicio;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;
}