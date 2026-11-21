package cl.veterinaria.servicios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServicioRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar 100 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String descripcion;

    @NotBlank(message = "La imagen es obligatoria")
    private String imagen;

    // Opcionales: pueden venir vacíos o no venir.
    @Size(max = 20)
    private String precio;

    @Size(max = 20)
    private String precioAnterior;
}