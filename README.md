# Backend Veterinaria San Marcos

Microservicios del backend de la web de la Veterinaria San Marcos.
Cada carpeta es un proyecto Maven independiente (Java 21, Spring Boot 4.0.9).

| Microservicio | Responsabilidad | Puerto | Base de datos |
|---|---|---|---|
| `usuarios` | Registro, roles, restablecer contraseña y validación de credenciales | 8081 | `bd_usuarios` |
| `servicios` | Catálogo de servicios de la veterinaria | 8082 | `bd_servicios` |
| `citas` | Agendar y listar citas | 8083 | `bd_citas` |
| `pagos` | Pagos de las citas | 8084 | `bd_pagos` |
| `mensajeria` | Mensajes del formulario de contacto | 8085 | `bd_mensajes` |
| `autenticacion` | Login y emisión de tokens JWT | 8086 | No tiene (consulta a `usuarios`) |
| `eureka-server` | Registro y descubrimiento de servicios | 8761 | No tiene |
| `api-gateway` | Puerta de entrada única para el front | 8080 | No tiene |

## Autenticación

El login lo gestiona `autenticacion`, que no guarda datos propios: consulta a `usuarios`
si las credenciales son válidas y, si lo son, emite un token JWT con el email y el rol
(`admin` o `cliente`) del usuario.

## Cómo ejecutar un microservicio

Entrar a su carpeta y ejecutar:

```
./mvnw spring-boot:run
```

(En Windows: `mvnw.cmd spring-boot:run`)

Cada servicio usa su propio puerto, definido en su `application.properties`.

## Ramas

- `main`: versión estable.
- `develop`: trabajo en curso; los cambios se integran aquí antes de pasar a `main`.

Frontend: https://github.com/trembo25/web-veterinaria-react


## contribuidor Gabriel





## Ejecutar todo con Docker

```
cp .env.example .env      # completa MYSQL_ROOT_PASSWORD, JWT_SECRET y ADMIN_PASSWORD
docker compose up --build
```

Cada microservicio con base de datos comparte el mismo puerto 3307 con diferentes bds independientes en su interior

| Qué | URL |
|---|---|
| API Gateway (lo que usa el front) | http://localhost:8080 |
| Swagger unificado (todos los ms) | http://localhost:8080/swagger-ui.html |
| Panel de Eureka | http://localhost:8761 |
| Swagger de cada ms | `http://localhost:<puerto>/swagger-ui.html` (8081 a 8086) |

Rutas del gateway: `/auth/**`, `/usuarios/**`, `/servicios/**`, `/citas/**`,
`/api/pagos/**` y `/api/mensajes/**`. El CORS se configura solo en el gateway
(`CORS_ALLOWED_ORIGINS`).

Sin Docker se sigue pudiendo correr cada ms con `./mvnw spring-boot:run`
(Eureka y gateway también). Orden recomendado: `eureka-server`, microservicios, `api-gateway`.
