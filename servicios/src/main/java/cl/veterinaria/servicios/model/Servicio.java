package cl.veterinaria.servicios.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, length = 255)
    private String imagen;

    // Opcional: puede venir vacío ("Precio por confirmar" en el front).
    @Column(length = 20)
    private String precio;

    // Opcional: precio antes de una oferta.
    @Column(name = "precio_anterior", length = 20)
    private String precioAnterior;
}