package cl.veterinaria.citas.service;

import cl.veterinaria.citas.dto.CitaRequest;
import cl.veterinaria.citas.model.Cita;
import cl.veterinaria.citas.repository.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository repository;

    public CitaService(CitaRepository repository) {
        this.repository = repository;
    }

    // Convierte el DTO en entidad y la guarda.
    public Cita guardar(CitaRequest request) {
        Cita cita = new Cita();
        cita.setClienteEmail(request.getClienteEmail());
        cita.setClienteNombre(request.getClienteNombre());
        cita.setMascota(request.getMascota());
        cita.setServicio(request.getServicio());
        cita.setFecha(request.getFecha());
        return repository.save(cita);
    }

    public List<Cita> listar() {
        return repository.findAll();
    }

    public List<Cita> listarPorCliente(String clienteEmail) {
        return repository.findByClienteEmail(clienteEmail);
    }

    public Cita buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
    }

    public void eliminar(Long id) {
        // deleteById no avisa si el id no existe, por eso se comprueba antes.
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada");
        }
        repository.deleteById(id);
    }
}