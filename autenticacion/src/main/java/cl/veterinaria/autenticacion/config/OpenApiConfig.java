package cl.veterinaria.autenticacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autenticacionOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Veterinaria San Marcos - Microservicio Autenticación")
                .version("1.0.0")
                .description("Login: valida credenciales con el microservicio usuarios y emite un JWT "
                        + "(sub = email, rol, nombre) firmado con HS256."));
    }
}
