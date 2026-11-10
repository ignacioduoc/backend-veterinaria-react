# Backend Veterinaria San Marcos

Microservicios del backend de la web de la Veterinaria San Marcos.
Cada carpeta es un proyecto Maven independiente (Java 21, Spring Boot 4.0.9, MySQL).

| Microservicio | Responsabilidad | Puerto | Base de datos |
|---|---|---|---|
| `usuarios` | Registro, login, roles y restablecer contraseña | 8081 | `bd_usuarios` |
| `servicios` | Catálogo de servicios de la veterinaria | 8082 | `bd_servicios` |
| `citas` | Agendar y listar citas | 8083 | `bd_citas` |
| `pagos` | Pagos de las citas | 8084 | `bd_pagos` |
| `mensajeria` | Mensajes del formulario de contacto | 8085 | `bd_mensajes` |

## Cómo ejecutar un microservicio

Entrar a su carpeta y ejecutar:

```
./mvnw spring-boot:run
```

(En Windows: `mvnw.cmd spring-boot:run`)

Frontend: https://github.com/trembo25/web-veterinaria-react
