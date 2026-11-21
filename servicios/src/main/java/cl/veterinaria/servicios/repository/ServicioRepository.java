package cl.veterinaria.servicios.repository;

import cl.veterinaria.servicios.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}