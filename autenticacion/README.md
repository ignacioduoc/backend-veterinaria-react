# Microservicio `autenticacion`

Puerto **8086** · **Sin base de datos** · Package `cl.veterinaria.autenticacion`

Recibe email y contraseña en `POST /auth/login`, se los pregunta a `usuarios` (`POST /usuarios/validar`) y, si son correctos, emite un **JWT** con el email y el rol.

## Cómo ejecutarlo

1. Levantar primero `usuarios` (puerto 8081).
2. Copiar `src/main/resources/application-local.properties.example` como `application-local.properties` y poner en `jwt.secret` **la misma clave** que usa `usuarios` (mínimo 32 caracteres). Alternativa: variable de entorno `JWT_SECRET`.
3. `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).
4. Swagger: http://localhost:8086/swagger-ui.html

## Endpoint

| Método | Ruta | Acceso | Respuestas |
|---|---|---|---|
| POST | `/auth/login` | Público | 200, 400, 401, 503 (usuarios apagado) |

Respuesta 200: `{ "token", "nombre", "email", "rol", "expiraEn" }`.

## Contrato del JWT

HS256 con `jwt.secret` · `sub` = email · `rol` = `admin`/`cliente` · `nombre` · `iat` · `exp` (60 minutos, `jwt.expiracion-minutos`).

## Estructura

```
config/      SecurityConfig (stateless, CSRF off, /auth/** abierto, CORS), OpenApiConfig
client/      UsuariosClient (RestClient → POST /usuarios/validar)
service/     AuthService (flujo del login), JwtService (crea y firma el token)
controller/  AuthController
dto/         LoginRequest, LoginResponse, UsuarioValidado, ErrorResponse
exception/   401, 503 y GlobalExceptionHandler
```

## Pruebas

- Automáticas (simulan a `usuarios`, no hace falta levantarlo): `./mvnw test`
- Postman: `postman/autenticacion.postman_collection.json` (necesita `usuarios` levantado; completar `adminPassword`).
