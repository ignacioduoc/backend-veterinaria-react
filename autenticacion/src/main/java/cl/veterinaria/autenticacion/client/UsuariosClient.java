package cl.veterinaria.autenticacion.client;

import cl.veterinaria.autenticacion.dto.LoginRequest;
import cl.veterinaria.autenticacion.dto.UsuarioValidado;
import cl.veterinaria.autenticacion.exception.CredencialesInvalidasException;
import cl.veterinaria.autenticacion.exception.ServicioNoDisponibleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Cliente HTTP hacia el microservicio usuarios.
 * autenticacion no tiene base de datos: la unica forma de saber si las
 * credenciales son correctas es preguntarle al dueno de los datos.
 */
@Component
public class UsuariosClient {

    private final RestClient restClient;

    public UsuariosClient(@Value("${usuarios.url}") String usuariosUrl) {
        this.restClient = RestClient.create(usuariosUrl);
    }

    /** Llama a POST /usuarios/validar. Devuelve el usuario o lanza la excepcion adecuada. */
    public UsuarioValidado validar(LoginRequest credenciales) {
        try {
            return restClient.post()
                    .uri("/usuarios/validar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(credenciales)
                    .retrieve()
                    .body(UsuarioValidado.class);
        } catch (HttpClientErrorException e) {
            // 401 (clave mala) o 400 (datos invalidos): para quien hace login es lo mismo.
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
            }
            throw new ServicioNoDisponibleException("Error al consultar el servicio de usuarios", e);
        } catch (RestClientException e) {
            // usuarios apagado, error 5xx, timeout...
            throw new ServicioNoDisponibleException("El servicio de usuarios no está disponible", e);
        }
    }
}
