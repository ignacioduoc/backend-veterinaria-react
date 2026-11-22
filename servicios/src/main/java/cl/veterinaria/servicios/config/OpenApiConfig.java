package cl.veterinaria.servicios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI serviciosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Servicios")
                .description("Catálogo de servicios de la veterinaria (consultar, crear, editar y eliminar).")
                .version("1.0.0"));
    }
}
