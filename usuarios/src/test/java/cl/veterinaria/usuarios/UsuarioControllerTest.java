package cl.veterinaria.usuarios;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de punta a punta del controlador con H2 en memoria
 * (configuracion en src/test/resources/application.properties).
 */
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Value("${jwt.secret}")
    private String secreto;

    // Simula el token que emitiria el microservicio autenticacion.
    private String token(String email, String rol) {
        return "Bearer " + Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    private void registrar(String email) throws Exception {
        mvc.perform(post("/usuarios/registro").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Test\",\"email\":\"" + email + "\",\"password\":\"Cliente123\"}"));
    }

    @Test
    void registroCreaClienteSinDevolverPassword() throws Exception {
        mvc.perform(post("/usuarios/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana\",\"email\":\"Ana@Test.cl\",\"password\":\"Cliente123\",\"rol\":\"admin\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@test.cl"))
                .andExpect(jsonPath("$.rol").value("cliente"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void registroDuplicadoDa409() throws Exception {
        registrar("dup@test.cl");
        mvc.perform(post("/usuarios/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Otro\",\"email\":\"dup@test.cl\",\"password\":\"Cliente123\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void registroInvalidoDa400ConCampos() throws Exception {
        mvc.perform(post("/usuarios/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"email\":\"malo\",\"password\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nombre").exists())
                .andExpect(jsonPath("$.campos.email").exists())
                .andExpect(jsonPath("$.campos.password").exists());
    }

    @Test
    void validarCorrectoE401() throws Exception {
        registrar("val@test.cl");
        mvc.perform(post("/usuarios/validar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"val@test.cl\",\"password\":\"Cliente123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("cliente"));
        mvc.perform(post("/usuarios/validar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"val@test.cl\",\"password\":\"Incorrecta1\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void buscarPorEmailRespetaPermisos() throws Exception {
        registrar("yo@test.cl");
        registrar("otro@test.cl");
        mvc.perform(get("/usuarios/yo@test.cl")).andExpect(status().isUnauthorized());
        mvc.perform(get("/usuarios/yo@test.cl").header("Authorization", token("yo@test.cl", "cliente")))
                .andExpect(status().isOk());
        mvc.perform(get("/usuarios/otro@test.cl").header("Authorization", token("yo@test.cl", "cliente")))
                .andExpect(status().isForbidden());
        mvc.perform(get("/usuarios/nadie@test.cl").header("Authorization", token("admin@sanmarcos.cl", "admin")))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarSoloAdmin() throws Exception {
        mvc.perform(get("/usuarios").header("Authorization", token("x@test.cl", "cliente")))
                .andExpect(status().isForbidden());
        mvc.perform(get("/usuarios").header("Authorization", token("admin@sanmarcos.cl", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    void restablecerPassword() throws Exception {
        registrar("reset@test.cl");
        String cuerpo = "{\"email\":\"reset@test.cl\",\"nuevaPassword\":\"Nueva12345\"}";
        mvc.perform(put("/usuarios/restablecer").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isOk());
        mvc.perform(put("/usuarios/restablecer").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest()); // igual a la anterior
        mvc.perform(put("/usuarios/restablecer").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@sanmarcos.cl\",\"nuevaPassword\":\"Otra12345\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/usuarios/validar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"reset@test.cl\",\"password\":\"Nueva12345\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void swaggerDisponible() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }
}
