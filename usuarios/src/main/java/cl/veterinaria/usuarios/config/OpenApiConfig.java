package cl.veterinaria.usuarios.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Datos generales que muestra Swagger UI y el boton "Authorize"
 * para pegar el JWT y probar los endpoints protegidos.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usuariosOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Veterinaria San Marcos - Microservicio Usuarios")
                        .version("1.0.0")
                        .description("Registro de clientes, validación de credenciales para el login, "
                                + "restablecimiento de contraseña y consulta de usuarios."))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token emitido por el microservicio autenticacion (POST /auth/login)")));
    }
}
