-- 1. Eliminar y recrear la base de datos
DROP DATABASE IF EXISTS fixer_app;
CREATE DATABASE fixer_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fixer_app;

-- 2. Crear las tablas (corregidas)
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
                          valoracion FLOAT
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
                               ubicacion GEOMETRY NOT NULL,
                               servicio_id BIGINT NULL,
                               FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
                               FOREIGN KEY (servicio_id) REFERENCES servicios(id_servicio) ON DELETE SET NULL
) ENGINE=InnoDB;
CREATE SPATIAL INDEX idx_profesionales_ubicacion ON profesionales (ubicacion);

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
                                       FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio) ON DELETE CASCADE
) ENGINE=InnoDB;
CREATE INDEX idx_profesional_servicios_id_usuario ON profesional_servicios (id_usuario);
CREATE INDEX idx_profesional_servicios_id_servicio ON profesional_servicios (id_servicio);

CREATE TABLE contrataciones (
                                id_contratacion BIGINT PRIMARY KEY AUTO_INCREMENT,
                                id_usuario BIGINT NULL,
                                id_profesional_servicio BIGINT NULL,
                                fecha_hora DATETIME,
                                estado VARCHAR(20) DEFAULT 'pendiente',
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

-- 3. Insertar datos de prueba
-- Categorías
INSERT INTO categorias (nombre, descripcion) VALUES
                                                 ('Hogar', 'Reparaciones en el hogar'),
                                                 ('Educación', 'Servicios educativos'),
                                                 ('Salud', 'Cuidados de salud'),
                                                 ('Tecnología', 'Servicios tecnológicos'),
                                                 ('Construcción', 'Obras y construcción'),
                                                 ('Belleza', 'Servicios de belleza'),
                                                 ('Mascotas', 'Cuidado animal'),
                                                 ('Eventos', 'Organización de eventos'),
                                                 ('Automoción', 'Servicios de vehículos'),
                                                 ('Consultoría', 'Servicios de asesoría');

-- Servicios
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES
                                                              ('Electricidad general', 'Instalaciones eléctricas', 1),
                                                              ('Fontanería', 'Arreglo de tuberías', 1),
                                                              ('Reparación de electrodomésticos', 'Reparación técnica', 1),
                                                              ('Pintura de interiores', 'Pintura y decoración', 1),

                                                              ('Clases de matemáticas', 'Clases particulares de matemáticas', 2),
                                                              ('Clases de inglés', 'Clases particulares de inglés', 2),
                                                              ('Apoyo escolar', 'Refuerzo escolar', 2),
                                                              ('Música y arte', 'Clases de instrumentos musicales', 2),

                                                              ('Fisioterapia', 'Tratamiento físico', 3),
                                                              ('Psicología', 'Atención psicológica', 3),
                                                              ('Nutrición', 'Asesoramiento nutricional', 3),
                                                              ('Entrenamiento personal', 'Preparador físico', 3),

                                                              ('Reparación de ordenadores', 'Mantenimiento y reparación de PC', 4),
                                                              ('Desarrollo web', 'Creación de páginas web', 4),
                                                              ('Soporte técnico', 'Asistencia remota y presencial', 4),
                                                              ('Instalación de software', 'Software y actualizaciones', 4),

                                                              ('Albañilería', 'Construcción de estructuras', 5),
                                                              ('Carpintería', 'Trabajos de madera', 5),
                                                              ('Reformas integrales', 'Renovaciones completas', 5),
                                                              ('Tejados', 'Reparación de techos', 5),

                                                              ('Peluquería', 'Corte y peinado', 6),
                                                              ('Manicura', 'Cuidado de uñas', 6),
                                                              ('Maquillaje', 'Servicios de maquillaje', 6),
                                                              ('Spa y masajes', 'Relajación y masajes terapéuticos', 6),

                                                              ('Paseador de perros', 'Paseos diarios', 7),
                                                              ('Cuidado de mascotas', 'Guardería y veterinario', 7),
                                                              ('Adiestramiento canino', 'Educación de perros', 7),
                                                              ('Peluquería canina', 'Baños y cortes para mascotas', 7),

                                                              ('Organización de bodas', 'Servicios integrales para bodas', 8),
                                                              ('Fotografía de eventos', 'Fotografía profesional', 8),
                                                              ('Decoración de eventos', 'Decoración para fiestas', 8),
                                                              ('Catering', 'Servicio de comida para eventos', 8),

                                                              ('Mecánica general', 'Mantenimiento de coches', 9),
                                                              ('Cambio de neumáticos', 'Sustitución de ruedas', 9),
                                                              ('Lavado de coches', 'Limpieza profesional', 9),
                                                              ('Electricidad automotriz', 'Revisión eléctrica de coches', 9),

                                                              ('Asesoría legal', 'Abogados y trámites legales', 10),
                                                              ('Asesoría financiera', 'Consultoría de finanzas', 10),
                                                              ('Consultoría de negocios', 'Planes de negocio y expansión', 10),
                                                              ('Traducción', 'Traducción profesional', 10);

-- Usuarios y profesionales
-- 10 Profesionales y 5 Clientes
INSERT INTO usuarios (nombre, nombre_usuario, contraseña, email, valoracion) VALUES
                                                                                 ('Juan Pérez', 'juanp', 'pass123', 'juan@example.com', 4.5),
                                                                                 ('María López', 'marial', 'pass123', 'maria@example.com', 4.7),
                                                                                 ('Carlos Ruiz', 'carlosr', 'pass123', 'carlos@example.com', 4.2),
                                                                                 ('Laura Sánchez', 'lauras', 'pass123', 'laura@example.com', 4.8),
                                                                                 ('Pedro Torres', 'pedrot', 'pass123', 'pedro@example.com', 4.6),

                                                                                 ('Cliente Uno', 'cliente1', 'pass123', 'cliente1@example.com', 0),
                                                                                 ('Cliente Dos', 'cliente2', 'pass123', 'cliente2@example.com', 0),
                                                                                 ('Cliente Tres', 'cliente3', 'pass123', 'cliente3@example.com', 0),
                                                                                 ('Cliente Cuatro', 'cliente4', 'pass123', 'cliente4@example.com', 0),
                                                                                 ('Cliente Cinco', 'cliente5', 'pass123', 'cliente5@example.com', 0);

-- Profesionales asignados
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, ubicacion, servicio_id)
VALUES
    (1, 'Electricista', 25, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 5, 'Certificado de Electricidad', 4.5, 10, ST_GeomFromText('POINT(2.17 41.38)',4326), 1),
    (2, 'Fontanero', 20, '{"martes": [{"inicio": "09:00", "fin": "17:00"}]}', 6, 'Certificado de Fontanería', 4.7, 12, ST_GeomFromText('POINT(2.18 41.39)',4326), 2),
    (3, 'Profesor de Matemáticas', 18, '{"miércoles": [{"inicio": "10:00", "fin": "18:00"}]}', 4, 'Licenciatura Matemáticas', 4.2, 8, ST_GeomFromText('POINT(2.19 41.40)',4326), 5),
    (4, 'Fisioterapeuta', 30, '{"jueves": [{"inicio": "08:00", "fin": "14:00"}]}', 7, 'Fisioterapia Universitaria', 4.8, 15, ST_GeomFromText('POINT(2.20 41.41)',4326), 9),
    (5, 'Desarrollador Web', 28, '{"viernes": [{"inicio": "09:00", "fin": "17:00"}]}', 3, 'Certificado Full Stack', 4.6, 6, ST_GeomFromText('POINT(2.21 41.42)',4326), 14);

-- Asignaciones múltiples (profesional_servicios)
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES
                                                                                              (1, 1, 25, 'Servicio eléctrico básico.'),
                                                                                              (2, 2, 20, 'Arreglo de tuberías general.'),
                                                                                              (3, 5, 18, 'Clases de matemáticas de primaria.'),
                                                                                              (4, 9, 30, 'Rehabilitación física y terapias.'),
                                                                                              (5, 14, 28, 'Desarrollo de webs modernas.');
