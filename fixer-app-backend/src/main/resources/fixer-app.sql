-- Crear base de datos
DROP DATABASE IF EXISTS fixer_app;
CREATE DATABASE IF NOT EXISTS fixer_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fixer_app;

-- Tabla Categorías
CREATE TABLE categorias (
                            id_categoria INT PRIMARY KEY AUTO_INCREMENT,
                            nombre VARCHAR(50) NOT NULL,
                            descripcion TINYTEXT,
                            CONSTRAINT nombre_categoria_unique UNIQUE (nombre)
) ENGINE=InnoDB;

-- Tabla Servicios
CREATE TABLE servicios (
                           id_servicio INT PRIMARY KEY AUTO_INCREMENT,
                           nombre VARCHAR(100) NOT NULL,
                           descripcion TEXT,
                           id_categoria INT NOT NULL,
                           FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_servicios_id_categoria ON servicios (id_categoria);

-- Tabla Usuarios
CREATE TABLE usuarios (
                          id_usuario INT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(255),
                          nombre_usuario VARCHAR(255) UNIQUE,
                          contraseña VARCHAR(255),
                          email VARCHAR(255) UNIQUE,
                          valoracion FLOAT
) ENGINE=InnoDB;

-- Tabla Clientes
CREATE TABLE clientes (
                          id_usuario INT PRIMARY KEY,
                          preferencias JSON,
                          FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Profesionales
CREATE TABLE profesionales (
                               id_usuario INT PRIMARY KEY,
                               especialidad VARCHAR(100),
                               precio_hora DECIMAL(10,2),
                               horario_disponible JSON,
                               experiencia INT,
                               certificaciones TEXT,
                               calificacion_promedio FLOAT,
                               total_contrataciones INT,
                               FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Administradores
CREATE TABLE administradores (
                                 id_usuario INT PRIMARY KEY,
                                 nivel_acceso VARCHAR(50),
                                 ultimo_acceso DATETIME,
                                 FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Profesional_Servicios
CREATE TABLE profesional_servicios (
                                       id_profesional_servicio INT PRIMARY KEY AUTO_INCREMENT,
                                       id_usuario INT,
                                       id_servicio INT,
                                       precio DECIMAL(10,2),
                                       descripcion_servicio TEXT,
                                       FOREIGN KEY (id_usuario) REFERENCES profesionales(id_usuario) ON DELETE CASCADE,
                                       FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_profesional_servicios_id_usuario ON profesional_servicios (id_usuario);
CREATE INDEX idx_profesional_servicios_id_servicio ON profesional_servicios (id_servicio);

-- Tabla Contrataciones
CREATE TABLE contrataciones (
                                id_contratacion INT PRIMARY KEY AUTO_INCREMENT,
                                id_usuario INT,
                                id_profesional_servicio INT,
                                fecha_hora DATETIME,
                                estado VARCHAR(20) DEFAULT 'pendiente',
                                duracion_estimada INT,
                                costo_total DECIMAL(10,2),
                                FOREIGN KEY (id_usuario) REFERENCES clientes(id_usuario) ON DELETE SET NULL,
                                FOREIGN KEY (id_profesional_servicio) REFERENCES profesional_servicios(id_profesional_servicio) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_contrataciones_id_usuario ON contrataciones (id_usuario);
CREATE INDEX idx_contrataciones_id_profesional_servicio ON contrataciones (id_profesional_servicio);

-- Tabla Valoraciones
CREATE TABLE valoraciones (
                              id_valoracion INT PRIMARY KEY AUTO_INCREMENT,
                              id_usuario INT,
                              id_usuario_profesional INT,
                              puntuacion INT CHECK (puntuacion BETWEEN 1 AND 5),
                              comentario TEXT,
                              fecha_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                              id_contratacion INT,
                              FOREIGN KEY (id_usuario) REFERENCES clientes(id_usuario) ON DELETE SET NULL,
                              FOREIGN KEY (id_usuario_profesional) REFERENCES profesionales(id_usuario) ON DELETE SET NULL,
                              FOREIGN KEY (id_contratacion) REFERENCES contrataciones(id_contratacion) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_valoraciones_id_usuario ON valoraciones (id_usuario);
CREATE INDEX idx_valoraciones_id_usuario_profesional ON valoraciones (id_usuario_profesional);
CREATE INDEX idx_valoraciones_id_contratacion ON valoraciones (id_contratacion);