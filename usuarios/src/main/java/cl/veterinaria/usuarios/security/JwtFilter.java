package cl.veterinaria.usuarios.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Se ejecuta una vez por peticion, antes de llegar al controlador.
 * Si viene "Authorization: Bearer <token>" valido, deja registrado quien es el usuario
 * y su rol. Si no viene o es invalido, no hace nada: SecurityConfig decide si
 * la ruta era publica o responde 401.
 */
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String cabecera = request.getHeader("Authorization");

        if (cabecera != null && cabecera.startsWith("Bearer ")) {
            String token = cabecera.substring(7);

            jwtService.leerToken(token).ifPresent((Claims claims) -> {
                String email = claims.getSubject();
                String rol = claims.get("rol", String.class);

                if (email != null && rol != null) {
                    // Spring Security espera los roles con prefijo "ROLE_" (hasRole("ADMIN") -> "ROLE_ADMIN").
                    var permisos = List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase(Locale.ROOT)));
                    var autenticacion = new UsernamePasswordAuthenticationToken(email, null, permisos);
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            });
        }

        chain.doFilter(request, response);
    }
}
