-- Tabla de citas. El pago NO se guarda aquí, vive en el microservicio pagos.
CREATE TABLE cita (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cliente_email VARCHAR(150) NOT NULL,
    cliente_nombre VARCHAR(150) NOT NULL,
    mascota VARCHAR(100) NOT NULL,
    servicio VARCHAR(150) NOT NULL,
    fecha DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_cita_cliente_email (cliente_email)
);