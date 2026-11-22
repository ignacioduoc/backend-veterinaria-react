package cl.veterinaria.pagos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pagosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Pagos")
                .description("Registra y confirma los pagos de las citas de la veterinaria (pago simulado).")
                .version("1.0.0"));
    }
}
