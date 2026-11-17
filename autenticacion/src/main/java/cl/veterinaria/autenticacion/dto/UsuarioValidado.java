package cl.veterinaria.autenticacion.dto;

/**
 * Respuesta de POST /usuarios/validar del microservicio usuarios.
 * Es una copia del UsuarioResponse de usuarios: cada microservicio
 * tiene sus propias clases, no se comparten entre proyectos.
 */
public record UsuarioValidado(Long id, String nombre, String email, String rol) {
}
