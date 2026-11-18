package cl.veterinaria.pagos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ej: "VSM-482913". Lo genera el servidor.
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    // Id de la cita (la maneja otro microservicio).
    // Es solo un número: NO hay llave foránea hacia otra base de datos.
    // Unique: cada cita se paga una sola vez.
    @Column(name = "cita_id", nullable = false, unique = true)
    private Long citaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MetodoPago metodo;

    // Pesos chilenos, sin decimales. Ej: 45000 (el front lo muestra como "$45.000").
    @Column(nullable = false)
    private Long monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estado;

    // Solo con tarjeta. Nunca se guarda el número completo ni el CVV.
    @Column(length = 4)
    private String ultimos4;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    void asignarFecha() {
        this.fecha = LocalDateTime.now();
    }
}
