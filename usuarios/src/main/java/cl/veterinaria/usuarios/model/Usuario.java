package cl.veterinaria.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un usuario de la Veterinaria San Marcos.
 * Se mapea a la tabla "usuarios" dentro de la base de datos bd_usuarios.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    // Clave primaria interna, autoincremental en MySQL.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre visible del usuario.
    @Column(nullable = false, length = 100)
    private String nombre;

    // Identificador de negocio: lo usan login, citas y pagos. No se puede repetir.
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Siempre se guarda el hash BCrypt (60 caracteres), nunca la contraseña en texto plano.
    @Column(nullable = false, length = 100)
    private String password;

    // "admin" o "cliente". Lo asigna el backend, no el formulario de registro.
    @Column(nullable = false, length = 20)
    private String rol;

    // Se ejecuta justo antes del INSERT: si nadie definió el rol, queda como "cliente".
    @PrePersist
    public void asignarRolPorDefecto() {
        if (rol == null || rol.isBlank()) {
            rol = "cliente";
        }
    }
}
