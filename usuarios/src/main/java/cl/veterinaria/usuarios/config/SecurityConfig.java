package cl.veterinaria.usuarios.config;

import cl.veterinaria.usuarios.security.JwtFilter;
import cl.veterinaria.usuarios.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Configuracion de seguridad del microservicio usuarios.
 * - BCrypt para cifrar contrasenas.
 * - Sin sesiones (stateless): cada peticion trae su JWT.
 * - CSRF desactivado: no usamos cookies de sesion, asi que no aplica.
 * - CORS abierto solo para el frontend (http://localhost:5173).
 */
@Configuration
public class SecurityConfig {

    // Rutas de Swagger: si no se abren, Spring Security las bloquea.
    private static final String[] RUTAS_SWAGGER = {
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        http
                .cors(cors -> {}) // usa el bean corsConfigurationSource de abajo
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(RUTAS_SWAGGER).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // preflight de CORS
                        .requestMatchers(HttpMethod.POST, "/usuarios/registro", "/usuarios/validar").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/restablecer").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                        .anyRequest().authenticated() // ej: GET /usuarios/{email}
                )
                .exceptionHandling(e -> e
                        // 401: no hay token o es invalido
                        .authenticationEntryPoint((req, res, ex) ->
                                escribirError(res, HttpStatus.UNAUTHORIZED, "Debe iniciar sesión (token ausente o inválido)"))
                        // 403: hay token, pero el rol no alcanza
                        .accessDeniedHandler((req, res, ex) ->
                                escribirError(res, HttpStatus.FORBIDDEN, "No tiene permisos para esta acción"))
                )
                // Nuestro filtro JWT corre antes del filtro de login estandar de Spring.
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${cors.allowed-origins}") List<String> origenes) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origenes);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Respuesta JSON con el mismo formato que GlobalExceptionHandler (ErrorResponse).
    private static void escribirError(jakarta.servlet.http.HttpServletResponse res,
                                      HttpStatus estado, String mensaje) throws IOException {
        res.setStatus(estado.value());
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("""
                {"fecha":"%s","estado":%d,"error":"%s","mensaje":"%s"}"""
                .formatted(LocalDateTime.now(), estado.value(), estado.getReasonPhrase(), mensaje));
    }
}
