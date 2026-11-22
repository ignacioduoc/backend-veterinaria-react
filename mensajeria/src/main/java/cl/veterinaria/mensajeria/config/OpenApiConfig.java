package cl.veterinaria.mensajeria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mensajeriaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Mensajería")
                .description("Gestiona los mensajes del formulario de contacto de la veterinaria.")
                .version("1.0.0"));
    }
}