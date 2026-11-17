# Microservicio `usuarios`

Puerto **8081** · Base de datos **bd_usuarios** (MySQL) · Package `cl.veterinaria.usuarios`

Registra clientes, valida credenciales para el login (lo llama `autenticacion`), restablece contraseñas y permite consultar usuarios. Las contraseñas se guardan **siempre** cifradas con BCrypt.

## Cómo ejecutarlo

1. Tener MySQL encendido en Laragon (la base `bd_usuarios` se crea sola).
2. Copiar `src/main/resources/application-local.properties.example` como `application-local.properties` (misma carpeta) y completar:
   - `jwt.secret`: clave de **al menos 32 caracteres**, la misma que usará `autenticacion`.
   - `admin.password`: contraseña del admin que se crea al arrancar.
   - Este archivo está en el `.gitignore`: **nunca se sube**.
   - Alternativa: variables de entorno `JWT_SECRET`, `ADMIN_PASSWORD`, `DB_USER`, `DB_PASSWORD`.
3. Ejecutar `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).
4. Swagger: http://localhost:8081/swagger-ui.html (botón **Authorize** para pegar un JWT).

## Endpoints

| Método | Ruta | Acceso | Respuestas |
|---|---|---|---|
| POST | `/usuarios/registro` | Público | 201, 400, 409 |
| POST | `/usuarios/validar` | Público (lo usa `autenticacion`) | 200, 400, 401 |
| GET | `/usuarios/{email}` | Token: el propio cliente o admin | 200, 401, 403, 404 |
| PUT | `/usuarios/restablecer` | Público | 200, 400, 403, 404 |
| GET | `/usuarios` | Token de admin | 200, 401, 403 |

No hay `DELETE` porque el proyecto no lo pide para usuarios.

## Contrato del JWT (debe coincidir con `autenticacion`)

- Algoritmo **HS256**, clave = `jwt.secret` (UTF-8).
- `sub` = email del usuario · `rol` = `"admin"` o `"cliente"` · `exp` = expiración.
- Se envía como `Authorization: Bearer <token>`.

## Estructura

```
config/       SecurityConfig (BCrypt, stateless, CORS, reglas de acceso), OpenApiConfig (Swagger), AdminInicial
security/     JwtService (verifica firma y expiración), JwtFilter (lee el header Authorization)
controller/   UsuarioController (HTTP + documentación Swagger)
service/      UsuarioService (reglas de negocio)
repository/   UsuarioRepository (Spring Data JPA)
model/        Usuario (entidad)
dto/          Requests con validaciones, UsuarioResponse (sin password), ErrorResponse
exception/    Excepciones de negocio + GlobalExceptionHandler (las traduce a 400/401/403/404/409)
```

## Pruebas

- Automáticas (H2 en memoria, no necesitan MySQL): `./mvnw test`
- Postman: importar `postman/usuarios.postman_collection.json`, poner en las variables de la colección `jwtSecret` y `adminPassword` (los mismos valores del servicio) y ejecutar la colección completa con **Run**. La colección genera sus propios tokens de prueba, así que no necesita `autenticacion` levantado.
