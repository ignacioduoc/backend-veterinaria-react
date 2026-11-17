package cl.veterinaria.usuarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Verifica los JWT que emite el microservicio autenticacion.
 * Este servicio NO crea tokens: solo comprueba la firma y la fecha de expiracion.
 *
 * Contrato del token (debe coincidir con autenticacion):
 *   sub = email del usuario, rol = "admin" | "cliente", firmado con HS256.
 */
@Service
public class JwtService {

    private final SecretKey clave;

    public JwtService(@Value("${jwt.secret}") String secreto) {
        // hmacShaKeyFor exige al menos 32 bytes (256 bits); si es mas corta, el servicio no arranca.
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    /** Devuelve los datos del token si la firma es valida y no expiro; si no, Optional vacio. */
    public Optional<Claims> leerToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty(); // firma falsa, token expirado o mal formado
        }
    }
}
