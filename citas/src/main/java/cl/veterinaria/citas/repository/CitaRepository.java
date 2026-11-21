package cl.veterinaria.citas.repository;

import cl.veterinaria.citas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Spring genera: SELECT * FROM cita WHERE cliente_email = ?
    List<Cita> findByClienteEmail(String clienteEmail);
}