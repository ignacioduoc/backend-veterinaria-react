package cl.veterinaria.citas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_email", nullable = false, length = 150)
    private String clienteEmail;

    @Column(name = "cliente_nombre", nullable = false, length = 150)
    private String clienteNombre;

    @Column(nullable = false, length = 100)
    private String mascota;

    @Column(nullable = false, length = 150)
    private String servicio;

    @Column(nullable = false)
    private LocalDateTime fecha;
}