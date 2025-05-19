DROP DATABASE IF EXISTS fixer_app;
CREATE DATABASE fixer_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fixer_app;


CREATE TABLE categorias (
                            id_categoria BIGINT PRIMARY KEY AUTO_INCREMENT,
                            nombre VARCHAR(50) NOT NULL,
                            descripcion TINYTEXT,
                            CONSTRAINT nombre_categoria_unique UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE servicios (
                           id_servicio BIGINT PRIMARY KEY AUTO_INCREMENT,
                           nombre VARCHAR(100) NOT NULL,
                           descripcion TEXT,
                           id_categoria BIGINT NOT NULL,
                           FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE CASCADE
) ENGINE=InnoDB;
CREATE INDEX idx_servicios_id_categoria ON servicios (id_categoria);

CREATE TABLE usuarios (
                          id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(255),
                          nombre_usuario VARCHAR(255) UNIQUE,
                          contraseña VARCHAR(255),
                          email VARCHAR(255) UNIQUE,
                          valoracion FLOAT,
                          tipo_usuario ENUM('cliente', 'profesional', 'administrador') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE password_reset_tokens (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       token VARCHAR(255) NOT NULL UNIQUE,
                                       id_usuario BIGINT NOT NULL,
                                       expiry_date DATETIME NOT NULL,
                                       FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE clientes (
                          id_usuario BIGINT PRIMARY KEY,
                          preferencias JSON,
                          FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE profesionales (
                               id_usuario BIGINT PRIMARY KEY,
                               especialidad VARCHAR(100),
                               precio_hora DECIMAL(10,2),
                               horario_disponible JSON,
                               experiencia BIGINT,
                               certificaciones TEXT,
                               calificacion_promedio FLOAT,
                               total_contrataciones BIGINT,
                               latitude DECIMAL(9,6) NOT NULL,
                               longitude DECIMAL(9,6) NOT NULL,
                               servicio_id BIGINT NULL,
                               FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
                               FOREIGN KEY (servicio_id) REFERENCES servicios(id_servicio) ON DELETE SET NULL
) ENGINE=InnoDB;
CREATE INDEX idx_profesionales_coords ON profesionales (latitude, longitude);

CREATE TABLE administradores (
                                 id_usuario BIGINT PRIMARY KEY,
                                 nivel_acceso VARCHAR(50),
                                 ultimo_acceso DATETIME,
                                 FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE profesional_servicios (
                                       id_profesional_servicio BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       id_usuario BIGINT,
                                       id_servicio BIGINT,
                                       precio DECIMAL(10,2),
                                       descripcion_servicio TEXT,
                                       FOREIGN KEY (id_usuario) REFERENCES profesionales(id_usuario) ON DELETE CASCADE,
                                       FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio) ON DELETE CASCADE,
                                       CONSTRAINT uq_usuario_servicio UNIQUE (id_usuario, id_servicio)
) ENGINE=InnoDB;
CREATE INDEX idx_profesional_servicios_id_usuario ON profesional_servicios (id_usuario);
CREATE INDEX idx_profesional_servicios_id_servicio ON profesional_servicios (id_servicio);

CREATE TABLE contrataciones (
                                id_contratacion BIGINT PRIMARY KEY AUTO_INCREMENT,
                                id_usuario BIGINT NULL,
                                id_profesional_servicio BIGINT NULL,
                                fecha_hora DATETIME,
                                estado ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA', 'EN_CURSO', 'COMPLETADA') NOT NULL DEFAULT 'PENDIENTE',
                                duracion_estimada BIGINT,
                                costo_total DECIMAL(10,2),
                                FOREIGN KEY (id_usuario) REFERENCES clientes(id_usuario) ON DELETE SET NULL,
                                FOREIGN KEY (id_profesional_servicio) REFERENCES profesional_servicios(id_profesional_servicio) ON DELETE SET NULL
) ENGINE=InnoDB;
CREATE INDEX idx_contrataciones_id_usuario ON contrataciones (id_usuario);
CREATE INDEX idx_contrataciones_id_profesional_servicio ON contrataciones (id_profesional_servicio);

CREATE TABLE valoraciones (
                              id_valoracion BIGINT PRIMARY KEY AUTO_INCREMENT,
                              id_usuario BIGINT,
                              id_usuario_profesional BIGINT,
                              puntuacion BIGINT CHECK (puntuacion BETWEEN 1 AND 5),
                              comentario TEXT,
                              fecha_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                              id_contratacion BIGINT,
                              FOREIGN KEY (id_usuario) REFERENCES clientes(id_usuario) ON DELETE SET NULL,
                              FOREIGN KEY (id_usuario_profesional) REFERENCES profesionales(id_usuario) ON DELETE SET NULL,
                              FOREIGN KEY (id_contratacion) REFERENCES contrataciones(id_contratacion) ON DELETE SET NULL
) ENGINE=InnoDB;
CREATE INDEX idx_valoraciones_id_usuario ON valoraciones (id_usuario);
CREATE INDEX idx_valoraciones_id_usuario_profesional ON valoraciones (id_usuario_profesional);
CREATE INDEX idx_valoraciones_id_contratacion ON valoraciones (id_contratacion);


CREATE TABLE horas_ocupadas (
                                id_hora_ocupada BIGINT PRIMARY KEY AUTO_INCREMENT,
                                id_profesional_servicio BIGINT NOT NULL,
                                fecha DATE NOT NULL,
                                hora_inicio TIME NOT NULL,
                                hora_fin TIME NOT NULL,
                                estado VARCHAR(20) DEFAULT 'ocupado',
                                FOREIGN KEY (id_profesional_servicio) REFERENCES profesional_servicios(id_profesional_servicio) ON DELETE CASCADE
);
CREATE INDEX idx_horas_ocupadas_fecha ON horas_ocupadas (fecha);
CREATE INDEX idx_horas_ocupadas_id_prof ON horas_ocupadas (id_profesional_servicio);
