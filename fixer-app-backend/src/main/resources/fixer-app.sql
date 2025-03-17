-- Crear la base de datos fixer_app
DROP DATABASE IF EXISTS fixer_app;
CREATE DATABASE IF NOT EXISTS fixer_app
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE fixer_app;

-- Tabla Servicios (independiente, no tiene FKs)
CREATE TABLE servicios (
                           id_servicio INT PRIMARY KEY AUTO_INCREMENT,
                           nombre VARCHAR(100) NOT NULL,
                           descripcion TEXT
) ENGINE=InnoDB;

-- Tabla Usuario (independiente, no tiene FKs)
CREATE TABLE usuarios (
                          id_usuario INT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL,
                          telefono VARCHAR(15),
                          contraseña VARCHAR(255) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          latitud DECIMAL(10,8) NOT NULL DEFAULT 0.0, -- Reemplazo de POINT
                          longitud DECIMAL(11,8) NOT NULL DEFAULT 0.0, -- Reemplazo de POINT
                          disponibilidad INT DEFAULT 1
) ENGINE=InnoDB;

-- Tabla Cliente (depende de Usuario)
CREATE TABLE clientes (
                          id_cliente INT PRIMARY KEY,
                          FOREIGN KEY (id_cliente) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Profesional (depende de Usuario)
CREATE TABLE profesionales (
                               id_profesional INT PRIMARY KEY,
                               especialidad VARCHAR(100),
                               precio_hora DECIMAL(10,2),
                               horario_disponible JSON,
                               FOREIGN KEY (id_profesional) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Administrador (depende de Usuario)
CREATE TABLE administradores (
                                 id_admin INT PRIMARY KEY,
                                 FOREIGN KEY (id_admin) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Profesional_Servicio (depende de Profesional y Servicios)
CREATE TABLE profesionales_servicios (
                                         id_profesional_servicio INT PRIMARY KEY AUTO_INCREMENT,
                                         id_profesional INT,
                                         id_servicio INT,
                                         precio DECIMAL(10,2),
                                         descripcion_servicio TEXT,
                                         FOREIGN KEY (id_profesional) REFERENCES profesionales(id_profesional) ON DELETE CASCADE,
                                         FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Contrataciones (depende de Cliente y Profesional_Servicio)
CREATE TABLE contrataciones (
                                id_contratacion INT PRIMARY KEY AUTO_INCREMENT,
                                id_cliente INT,
                                id_profesional_servicio INT,
                                fecha_hora DATETIME,
                                estado ENUM('pendiente', 'aceptada', 'finalizada', 'cancelada') DEFAULT 'pendiente',
                                FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE SET NULL,
                                FOREIGN KEY (id_profesional_servicio) REFERENCES profesionales_servicios(id_profesional_servicio) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Tabla Valoraciones (depende de Cliente, Profesional y Contrataciones)
CREATE TABLE valoraciones (
                              id_valoracion INT PRIMARY KEY AUTO_INCREMENT,
                              id_cliente INT,
                              id_profesional INT,
                              puntuacion INT CHECK (puntuacion BETWEEN 1 AND 5),
                              comentario TEXT,
                              fecha_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                              id_contratacion INT,
                              FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE SET NULL,
                              FOREIGN KEY (id_profesional) REFERENCES profesionales(id_profesional) ON DELETE SET NULL,
                              FOREIGN KEY (id_contratacion) REFERENCES contrataciones(id_contratacion) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Insertar datos de ejemplo
INSERT INTO servicios (nombre, descripcion) VALUES
                                                ('Limpieza', 'Servicio de limpieza residencial'),
                                                ('Fontanería', 'Reparación de tuberías y grifos');

INSERT INTO usuarios (nombre, telefono, contraseña, email, latitud, longitud, disponibilidad) VALUES
                                                                                                  ('Juan Pérez', '123456789', 'hashedpass1', 'juan@example.com', 40.7128, -74.0060, 1),
                                                                                                  ('María Gómez', '987654321', 'hashedpass2', 'maria@example.com', 41.3851, 2.1734, 1),
                                                                                                  ('Admin Test', '555555555', 'hashedpass3', 'admin@example.com', 0.0, 0.0, 1);

INSERT INTO clientes (id_cliente) VALUES (1);

INSERT INTO profesionales (id_profesional, especialidad, precio_hora, horario_disponible) VALUES
    (2, 'Limpieza', 15.50, '{"lunes": "09:00-17:00", "martes": "09:00-17:00"}');

INSERT INTO administradores (id_admin) VALUES (3);

INSERT INTO profesionales_servicios (id_profesional, id_servicio, precio, descripcion_servicio) VALUES
    (2, 1, 15.50, 'Limpieza de casas y oficinas');

INSERT INTO contrataciones (id_cliente, id_profesional_servicio, fecha_hora, estado) VALUES
    (1, 1, '2025-03-18 10:00:00', 'pendiente');

INSERT INTO valoraciones (id_cliente, id_profesional, puntuacion, comentario, id_contratacion) VALUES
    (1, 2, 4, 'Buen servicio, puntual', 1);

-- Verificar datos insertados
SELECT * FROM servicios;
SELECT * FROM usuarios;
SELECT * FROM clientes;
SELECT * FROM profesionales;
SELECT * FROM administradores;
SELECT * FROM profesionales_servicios;
SELECT * FROM contrataciones;
SELECT * FROM valoraciones;