package cl.veterinaria.servicios.service;

import cl.veterinaria.servicios.dto.ServicioRequest;
import cl.veterinaria.servicios.model.Servicio;
import cl.veterinaria.servicios.repository.ServicioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository repository;

    public ServicioService(ServicioRepository repository) {
        this.repository = repository;
    }

    public List<Servicio> listar() {
        return repository.findAll();
    }

    public Servicio buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
    }

    public Servicio crear(ServicioRequest request) {
        Servicio servicio = new Servicio();
        copiarDatos(request, servicio);
        return repository.save(servicio);
    }

    // Busca el servicio existente, le copia los datos nuevos y lo guarda.
    public Servicio actualizar(Long id, ServicioRequest request) {
        Servicio servicio = buscarPorId(id);
        copiarDatos(request, servicio);
        return repository.save(servicio);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado");
        }
        repository.deleteById(id);
    }

    // Lo usan crear y actualizar: así la copia de campos se escribe una sola vez.
    private void copiarDatos(ServicioRequest request, Servicio servicio) {
        servicio.setTitulo(request.getTitulo());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setImagen(request.getImagen());
        servicio.setPrecio(request.getPrecio());
        servicio.setPrecioAnterior(request.getPrecioAnterior());
    }
}