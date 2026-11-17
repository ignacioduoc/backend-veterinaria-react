package cl.veterinaria.autenticacion.service;

import cl.veterinaria.autenticacion.client.UsuariosClient;
import cl.veterinaria.autenticacion.dto.LoginRequest;
import cl.veterinaria.autenticacion.dto.LoginResponse;
import cl.veterinaria.autenticacion.dto.UsuarioValidado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

/** Flujo del login: preguntar a usuarios y, si responde OK, emitir el token. */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuariosClient usuariosClient;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest datos) {
        LoginRequest limpio = new LoginRequest(datos.email().trim().toLowerCase(Locale.ROOT), datos.password());

        UsuarioValidado usuario = usuariosClient.validar(limpio);
        String token = jwtService.generarToken(usuario);

        return new LoginResponse(token, usuario.nombre(), usuario.email(), usuario.rol(),
                jwtService.getExpiracionSegundos());
    }
}
