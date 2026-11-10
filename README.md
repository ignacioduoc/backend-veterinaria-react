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
