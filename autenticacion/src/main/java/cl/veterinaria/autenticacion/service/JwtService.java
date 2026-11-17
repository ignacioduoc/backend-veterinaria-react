package cl.veterinaria.autenticacion.service;

import cl.veterinaria.autenticacion.dto.UsuarioValidado;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Crea y firma los JWT.
 * Contrato (usuarios y los demas servicios lo verifican igual):
 *   sub = email, rol = "admin" | "cliente", nombre, iat, exp. Firma HS256.
 */
@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionSegundos;

    public JwtService(@Value("${jwt.secret}") String secreto,
                      @Value("${jwt.expiracion-minutos}") long expiracionMinutos) {
        // hmacShaKeyFor exige al menos 32 bytes; con una clave corta el servicio no arranca.
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionSegundos = expiracionMinutos * 60;
    }

    public String generarToken(UsuarioValidado usuario) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(usuario.email())
                .claim("rol", usuario.rol())
                .claim("nombre", usuario.nombre())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(expiracionSegundos)))
                .signWith(clave) // con una SecretKey de 256 bits jjwt usa HS256
                .compact();
    }

    public long getExpiracionSegundos() {
        return expiracionSegundos;
    }
}
