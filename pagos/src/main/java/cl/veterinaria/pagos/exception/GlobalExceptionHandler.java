package cl.veterinaria.pagos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404: el pago no existe.
    @ExceptionHandler(PagoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(PagoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    // 409: la cita ya tiene pago, o el pago ya estaba confirmado.
    @ExceptionHandler(PagoConflictoException.class)
    public ResponseEntity<ErrorResponse> conflicto(PagoConflictoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, ex.getMessage()));
    }

    // 400: datos que no tienen sentido juntos (ej: tarjeta sin últimos 4 dígitos).
    @ExceptionHandler(PagoInvalidoException.class)
    public ResponseEntity<ErrorResponse> invalido(PagoInvalidoException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, ex.getMessage()));
    }

    // 400: falló alguna validación del DTO.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Datos inválidos", errores));
    }

    // 400: JSON mal formado o vacío.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "El cuerpo de la petición no es válido"));
    }

    // 400: un id en la URL que no es número (ej: /api/pagos/abc/confirmar).
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "El parámetro \"" + ex.getName() + "\" no es válido"));
    }

    // 500: cualquier error no previsto.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generico(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Error interno del servidor"));
    }
}
