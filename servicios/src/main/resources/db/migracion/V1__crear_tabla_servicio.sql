-- Catálogo de servicios de la veterinaria.
CREATE TABLE servicio (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    imagen VARCHAR(255) NOT NULL,        
    precio VARCHAR(20) NULL,             
    precio_anterior VARCHAR(20) NULL,    
    PRIMARY KEY (id)
);