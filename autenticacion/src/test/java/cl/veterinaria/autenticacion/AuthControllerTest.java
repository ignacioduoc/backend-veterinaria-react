package cl.veterinaria.autenticacion;

import cl.veterinaria.autenticacion.client.UsuariosClient;
import cl.veterinaria.autenticacion.dto.UsuarioValidado;
import cl.veterinaria.autenticacion.exception.CredencialesInvalidasException;
import cl.veterinaria.autenticacion.exception.ServicioNoDisponibleException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Se reemplaza UsuariosClient por un simulacro: asi se prueba sin levantar usuarios. */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UsuariosClient usuariosClient;

    @Value("${jwt.secret}")
    private String secreto;

    private static final String CUERPO = "{\"email\":\"Ana@Test.cl\",\"password\":\"Cliente123\"}";

    @Test
    void loginCorrectoDevuelveTokenConEmailYRol() throws Exception {
        when(usuariosClient.validar(any()))
                .thenReturn(new UsuarioValidado(1L, "Ana", "ana@test.cl", "cliente"));

        String json = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(CUERPO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("cliente"))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andReturn().getResponse().getContentAsString();

        String token = json.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(token).getPayload();
        assertThat(claims.getSubject()).isEqualTo("ana@test.cl");
        assertThat(claims.get("rol", String.class)).isEqualTo("cliente");
    }

    @Test
    void credencialesMalasDa401() throws Exception {
        when(usuariosClient.validar(any())).thenThrow(new CredencialesInvalidasException("Correo o contraseña incorrectos"));
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(CUERPO))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void datosInvalidosDa400() throws Exception {
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"malo\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.email").exists())
                .andExpect(jsonPath("$.campos.password").exists());
    }

    @Test
    void usuariosCaidoDa503() throws Exception {
        when(usuariosClient.validar(any())).thenThrow(new ServicioNoDisponibleException("El servicio de usuarios no está disponible", null));
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(CUERPO))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void swaggerDisponible() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }
}
