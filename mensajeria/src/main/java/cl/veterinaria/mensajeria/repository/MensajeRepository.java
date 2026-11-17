package cl.veterinaria.mensajeria.repository;

import cl.veterinaria.mensajeria.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // Los mensajes más nuevos primero (igual que la tabla del panel admin).
    List<Mensaje> findAllByOrderByFechaDesc();
}
