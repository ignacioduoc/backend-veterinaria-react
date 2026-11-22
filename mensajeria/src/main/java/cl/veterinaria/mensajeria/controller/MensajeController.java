package cl.veterinaria.mensajeria.controller;

import cl.veterinaria.mensajeria.dtos.MensajeRequestDTO;
import cl.veterinaria.mensajeria.dtos.MensajeResponseDTO;
import cl.veterinaria.mensajeria.exception.ErrorResponse;
import cl.veterinaria.mensajeria.service.MensajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
@Tag(name = "Mensajes", description = "Mensajes del formulario de contacto")
public class MensajeController {

    private final MensajeService service;

    @Operation(summary = "Enviar un mensaje", description = "Guarda un mensaje del formulario de contacto de la página.")
    @ApiResponse(responseCode = "201", description = "Mensaje guardado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<MensajeResponseDTO> crear(@Valid @RequestBody MensajeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar mensajes", description = "Devuelve todos los mensajes, los más nuevos primero.")
    @ApiResponse(responseCode = "200", description = "Lista de mensajes")
    @GetMapping
    public List<MensajeResponseDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener un mensaje por id")
    @ApiResponse(responseCode = "200", description = "Mensaje encontrado")
    @ApiResponse(responseCode = "404", description = "El mensaje no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public MensajeResponseDTO obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @Operation(summary = "Eliminar un mensaje")
    @ApiResponse(responseCode = "204", description = "Mensaje eliminado")
    @ApiResponse(responseCode = "404", description = "El mensaje no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}