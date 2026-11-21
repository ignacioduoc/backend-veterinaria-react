package cl.veterinaria.servicios.controller;

import cl.veterinaria.servicios.dto.ServicioRequest;
import cl.veterinaria.servicios.model.Servicio;
import cl.veterinaria.servicios.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/servicios")
@CrossOrigin(origins = "http://localhost:5173")
public class ServicioController {

    private final ServicioService service;

    public ServicioController(ServicioService service) {
        this.service = service;
    }

    // GET /servicios -> 200 con la lista (público: la usa la página de inicio).
    @GetMapping
    public List<Servicio> listar() {
        return service.listar();
    }

    // GET /servicios/3 -> 200, o 404 si no existe.
    @GetMapping("/{id}")
    public Servicio buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /servicios -> 201 si se crea, 400 si los datos no son válidos.
    @PostMapping
    public ResponseEntity<Servicio> crear(@Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    // PUT /servicios/3 -> 200 con el servicio editado, 400 o 404.
    @PutMapping("/{id}")
    public Servicio actualizar(@PathVariable Long id,
                               @Valid @RequestBody ServicioRequest request) {
        return service.actualizar(id, request);
    }

    // DELETE /servicios/3 -> 204, o 404 si no existe.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}