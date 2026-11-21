package cl.veterinaria.citas.controller;

import cl.veterinaria.citas.dto.CitaRequest;
import cl.veterinaria.citas.model.Cita;
import cl.veterinaria.citas.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "http://localhost:5173")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    // POST /citas -> 201 si se crea, 400 si los datos no son válidos.
    @PostMapping
    public ResponseEntity<Cita> crear(@Valid @RequestBody CitaRequest request) {
        Cita creada = service.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // GET /citas  o  GET /citas?clienteEmail=ana@correo.cl
    @GetMapping
    public List<Cita> listar(@RequestParam(required = false) String clienteEmail) {
        if (clienteEmail != null) {
            return service.listarPorCliente(clienteEmail);
        }
        return service.listar();
    }

    // GET /citas/5 -> 200, o 404 si no existe.
    @GetMapping("/{id}")
    public Cita buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // DELETE /citas/5 -> 204 sin cuerpo, o 404 si no existe.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}