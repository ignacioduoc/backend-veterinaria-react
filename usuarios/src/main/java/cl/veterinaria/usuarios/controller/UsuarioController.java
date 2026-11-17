package cl.veterinaria.usuarios.controller;

import cl.veterinaria.usuarios.dto.ErrorResponse;
import cl.veterinaria.usuarios.dto.MensajeResponse;
import cl.veterinaria.usuarios.dto.RegistroRequest;
import cl.veterinaria.usuarios.dto.RestablecerRequest;
import cl.veterinaria.usuarios.dto.UsuarioResponse;
import cl.veterinaria.usuarios.dto.ValidarRequest;
import cl.veterinaria.usuarios.exception.AccesoDenegadoException;
import cl.veterinaria.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Capa HTTP: recibe la peticion, valida el cuerpo con @Valid,
 * llama al servicio y elige el codigo de respuesta.
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Registro, validación de credenciales y gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    @Operation(summary = "Registrar un cliente", description = "Público. El rol siempre queda como \"cliente\".")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "El correo ya está registrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(datos));
    }

    @PostMapping("/validar")
    @Operation(summary = "Validar credenciales",
            description = "Lo usa el microservicio autenticacion en el login. Devuelve el usuario sin contraseña.")
    @ApiResponse(responseCode = "200", description = "Credenciales correctas")
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UsuarioResponse> validar(@Valid @RequestBody ValidarRequest datos) {
        return ResponseEntity.ok(usuarioService.validarCredenciales(datos));
    }

    @GetMapping("/{email}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar usuario por email",
            description = "Requiere token. Un cliente solo puede ver su propia cuenta; el admin puede ver cualquiera.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "401", description = "Sin token o token inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Intenta ver la cuenta de otra persona", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "No existe", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email, Authentication auth) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean esSuCuenta = auth.getName().equalsIgnoreCase(email.trim());

        if (!esAdmin && !esSuCuenta) {
            throw new AccesoDenegadoException("Solo puede consultar su propia cuenta");
        }
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @PutMapping("/restablecer")
    @Operation(summary = "Restablecer contraseña",
            description = "Paso 2 del flujo del frontend. No aplica a la cuenta de administrador.")
    @ApiResponse(responseCode = "200", description = "Contraseña actualizada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o contraseña igual a la anterior", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Es la cuenta del administrador", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "No existe una cuenta con ese correo", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<MensajeResponse> restablecer(@Valid @RequestBody RestablecerRequest datos) {
        usuarioService.restablecerPassword(datos);
        return ResponseEntity.ok(new MensajeResponse("Contraseña actualizada correctamente"));
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar todos los usuarios", description = "Solo admin.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    @ApiResponse(responseCode = "401", description = "Sin token o token inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "El usuario no es admin", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}
