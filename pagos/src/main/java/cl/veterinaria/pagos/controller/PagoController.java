package cl.veterinaria.pagos.controller;

import cl.veterinaria.pagos.dtos.PagoRequestDTO;
import cl.veterinaria.pagos.dtos.PagoResponseDTO;
import cl.veterinaria.pagos.exception.ErrorResponse;
import cl.veterinaria.pagos.service.PagoService;
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
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Pagos de las citas de la veterinaria")
public class PagoController {

    private final PagoService service;

    @Operation(summary = "Registrar un pago",
            description = "Tarjeta: queda pagado. Transferencia: queda pendiente hasta que el admin la confirme.")
    @ApiResponse(responseCode = "201", description = "Pago registrado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "La cita ya tiene un pago",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrar(@Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(dto));
    }

    @Operation(summary = "Listar pagos", description = "Todos los pagos, los más nuevos primero.")
    @GetMapping
    public List<PagoResponseDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener el pago de una cita")
    @ApiResponse(responseCode = "200", description = "Pago encontrado")
    @ApiResponse(responseCode = "404", description = "La cita no tiene pago",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/cita/{citaId}")
    public PagoResponseDTO obtenerPorCita(@PathVariable Long citaId) {
        return service.obtenerPorCita(citaId);
    }

    @Operation(summary = "Confirmar una transferencia", description = "Pasa el pago de pendiente a pagado.")
    @ApiResponse(responseCode = "200", description = "Pago confirmado")
    @ApiResponse(responseCode = "404", description = "El pago no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "No es una transferencia pendiente",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/{id}/confirmar")
    public PagoResponseDTO confirmar(@PathVariable Long id) {
        return service.confirmar(id);
    }
}
