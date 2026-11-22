package cl.veterinaria.usuarios.service;

import cl.veterinaria.usuarios.dto.RegistroRequest;
import cl.veterinaria.usuarios.dto.RestablecerRequest;
import cl.veterinaria.usuarios.dto.UsuarioResponse;
import cl.veterinaria.usuarios.dto.ValidarRequest;
import cl.veterinaria.usuarios.exception.AccesoDenegadoException;
import cl.veterinaria.usuarios.exception.ConflictoException;
import cl.veterinaria.usuarios.exception.CredencialesInvalidasException;
import cl.veterinaria.usuarios.exception.DatosInvalidosException;
import cl.veterinaria.usuarios.exception.RecursoNoEncontradoException;
import cl.veterinaria.usuarios.model.Usuario;
import cl.veterinaria.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * Reglas de negocio de los usuarios.
 * El controlador solo recibe y responde HTTP; las decisiones se toman aqui.
 */
@Service
@RequiredArgsConstructor // Lombok: constructor con los campos final -> inyeccion por constructor
public class UsuarioService {

    public static final String ROL_ADMIN = "admin";
    public static final String ROL_CLIENTE = "cliente";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /** Registra siempre un CLIENTE con la contrasena cifrada. */
    @Transactional
    public UsuarioResponse registrar(RegistroRequest datos) {
        String email = normalizarEmail(datos.email());

        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictoException("Ese correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(datos.nombre().trim());
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(datos.password())); // BCrypt
        usuario.setRol(ROL_CLIENTE); // el rol NUNCA lo decide quien se registra

        return UsuarioResponse.desde(usuarioRepository.save(usuario));
    }

    /**
     * Lo usa el microservicio autenticacion en el login.
     * Mismo mensaje si el email no existe o si la clave es incorrecta,
     * para no revelar que correos estan registrados.
     */
    @Transactional(readOnly = true)
    public UsuarioResponse validarCredenciales(ValidarRequest datos) {
        Usuario usuario = usuarioRepository.findByEmail(normalizarEmail(datos.email()))
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        // matches() cifra la clave recibida con la "sal" del hash guardado y compara.
        if (!passwordEncoder.matches(datos.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }
        return UsuarioResponse.desde(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        return UsuarioResponse.desde(obtenerUsuario(normalizarEmail(email)));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponse::desde)
                .toList();
    }

    /** Mismas reglas que el frontend: solo clientes y la clave nueva debe ser distinta. */
    @Transactional
    public void restablecerPassword(RestablecerRequest datos) {
        Usuario usuario = obtenerUsuario(normalizarEmail(datos.email()));

        if (ROL_ADMIN.equals(usuario.getRol())) {
            throw new AccesoDenegadoException(
                    "La contraseña del administrador no se puede restablecer desde aquí");
        }
        if (passwordEncoder.matches(datos.nuevaPassword(), usuario.getPassword())) {
            throw new DatosInvalidosException("La nueva contraseña debe ser distinta a la anterior");
        }

        usuario.setPassword(passwordEncoder.encode(datos.nuevaPassword()));
        // No hace falta save(): dentro de @Transactional, JPA detecta el cambio y hace el UPDATE.
    }

    private Usuario obtenerUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No encontramos una cuenta con ese correo"));
    }

    // "  Nacho@Gmail.com " -> "nacho@gmail.com" (igual que hace el frontend)
    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}
