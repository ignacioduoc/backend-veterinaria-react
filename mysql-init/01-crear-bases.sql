-- Se ejecuta solo la primera vez que se crea el volumen de MySQL.
-- Una base de datos independiente por microservicio, todas en el mismo servidor (puerto 3307).
CREATE DATABASE IF NOT EXISTS bd_usuarios   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_servicios  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_citas      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_pagos      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_mensajes   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
