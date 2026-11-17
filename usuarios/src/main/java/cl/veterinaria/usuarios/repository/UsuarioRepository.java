package cl.veterinaria.usuarios.repository;

import cl.veterinaria.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Acceso a la tabla "usuarios".
 * Al extender JpaRepository ya tenemos save, findAll, findById, delete, etc.
 * Spring Data genera el SQL de los metodos a partir de su nombre.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // SELECT * FROM usuarios WHERE email = ?
    // Optional obliga a manejar el caso "no existe" (evita NullPointerException).
    Optional<Usuario> findByEmail(String email);

    // SELECT COUNT(*) > 0 FROM usuarios WHERE email = ?  (mas liviano que traer el usuario)
    boolean existsByEmail(String email);
}
