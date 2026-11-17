package cl.veterinaria.usuarios.config;

import cl.veterinaria.usuarios.model.Usuario;
import cl.veterinaria.usuarios.repository.UsuarioRepository;
import cl.veterinaria.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea la cuenta de administrador al arrancar, si aun no existe.
 * El registro publico solo crea clientes, asi que el admin tiene que nacer aqui.
 * La contrasena viene de una variable de entorno: nunca queda escrita en el repo.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInicial implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.nombre}")
    private String nombre;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    @Override
    public void run(String... args) {
        if (password == null || password.isBlank()) {
            log.warn("ADMIN_PASSWORD no está definida: no se crea la cuenta de administrador.");
            return;
        }
        String emailAdmin = email.trim().toLowerCase();
        if (usuarioRepository.existsByEmail(emailAdmin)) {
            return; // ya existe, no se toca
        }
        Usuario admin = new Usuario();
        admin.setNombre(nombre);
        admin.setEmail(emailAdmin);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRol(UsuarioService.ROL_ADMIN);
        usuarioRepository.save(admin);
        log.info("Cuenta de administrador creada: {}", emailAdmin);
    }
}
