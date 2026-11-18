package cl.veterinaria.pagos.repository;

import cl.veterinaria.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByCitaId(Long citaId);

    boolean existsByCitaId(Long citaId);

    boolean existsByCodigo(String codigo);

    List<Pago> findAllByOrderByFechaDesc();
}
