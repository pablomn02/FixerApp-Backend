-- Elimina y recrea la base de datos
DROP DATABASE IF EXISTS fixer_app;
CREATE DATABASE IF NOT EXISTS fixer_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fixer_app;

-- Tablas principales
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

CREATE TABLE fixer_app.password_reset_tokens (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 token VARCHAR(255) NOT NULL UNIQUE,
                                                 id_usuario BIGINT NOT NULL,
                                                 expiry_date DATETIME NOT NULL,
                                                 FOREIGN KEY (id_usuario) REFERENCES fixer_app.usuarios(id_usuario)
);

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
                               FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;
CREATE SPATIAL INDEX idx_profesionales_ubicacion ON profesionales(ubicacion);

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
                                id_usuario BIGINT,
                                id_profesional_servicio BIGINT,
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

-- Datos de ejemplo: usuarios
INSERT INTO usuarios (nombre, nombre_usuario, contraseña, email, valoracion) VALUES
                                                                                 ('Juan Pérez',    'juanperez',        'hashedpass1', 'juan@example.com', 4.5),
                                                                                 ('María Gómez',   'mariagomez',       'hashedpass2', 'maria@example.com',4.8),
                                                                                 ('Admin Uno',     'admin1',           'hashedpass3', 'admin1@example.com', NULL),
                                                                                 ('Admin Dos',     'admin2',           'hashedpass4', 'admin2@example.com', NULL),
                                                                                 ('Admin Tres',    'admin3',           'hashedpass5', 'admin3@example.com', NULL),
                                                                                 ('Admin Cuatro',  'admin4',           'hashedpass6', 'admin4@example.com', NULL),
                                                                                 ('Admin Cinco',   'admin5',           'hashedpass7', 'admin5@example.com', NULL);

-- Cliente
INSERT INTO clientes (id_usuario, preferencias) VALUES
    (1, '{"preferencia1":"servicio rápido","preferencia2":"horario matutino"}');

-- Profesional inicial
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, ubicacion) VALUES
    (2, 'Hogar', 15.50,
     '{
       "lunes":   [{"inicio":"09:00","fin":"17:00"}],
       "martes":  [{"inicio":"09:00","fin":"17:00"}],
       "miercoles": [], "jueves": [], "viernes": [], "sabado": [], "domingo": []
     }',
     5, 'Certificado profesional', 4.7, 10,
     ST_GeomFromText('POINT(2.173404 41.385064)',4326)
    );

-- Categorías
INSERT INTO categorias (nombre, descripcion) VALUES
                                                 ('Hogar','Reparaciones y mantenimiento doméstico'),
                                                 ('Belleza','Cuidado personal y estética'),
                                                 ('Educación','Enseñanza y tutorías'),
                                                 ('Tecnología','Soporte técnico y reparaciones electrónicas'),
                                                 ('Transporte','Mudanzas y logística'),
                                                 ('Jardinería','Cuidado de espacios verdes'),
                                                 ('Construcción','Obras y reformas'),
                                                 ('Cuidado','Cuidado personal y animal'),
                                                 ('Creatividad','Eventos y servicios creativos'),
                                                 ('Profesional','Consultoría y asesorías'),
                                                 ('Salud','Servicios médicos y bienestar'),
                                                 ('Automoción','Mantenimiento y reparación de vehículos'),
                                                 ('Hostelería','Servicios para eventos y gastronomía'),
                                                 ('Seguridad','Protección y vigilancia'),
                                                 ('Reparaciones','Reparaciones especializadas');

-- Servicios
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES
                                                              ('Reparación de tuberías','Reparación de fugas y tuberías',                1),
                                                              ('Instalación eléctrica','Montaje de sistemas eléctricos',               1),
                                                              ('Limpieza profunda','Limpieza completa de hogares',                     1),
                                                              ('Reparación de electrodomésticos','Arreglo de aparatos domésticos',     1),
                                                              ('Peluquería','Corte y estilismo de cabello',                            2),
                                                              ('Manicura y pedicura','Cuidado de uñas',                                2),
                                                              ('Maquillaje profesional','Maquillaje para eventos',                    2),
                                                              ('Barbería','Corte y afeitado para hombres',                             2),
                                                              ('Clases de matemáticas','Tutorías de matemáticas',                      3),
                                                              ('Clases de idiomas','Enseñanza de idiomas',                             3),
                                                              ('Clases de música','Lecciones de instrumentos musicales',              3),
                                                              ('Capacitación profesional','Cursos de habilidades laborales',          3),
                                                              ('Reparación de computadoras','Diagnóstico y arreglo de PCs',           4),
                                                              ('Reparación de teléfonos','Arreglo de dispositivos móviles',           4),
                                                              ('Instalación de software','Configuración de programas',                4),
                                                              ('Soporte técnico remoto','Asistencia técnica a distancia',             4),
                                                              ('Mudanza local','Transporte dentro de la ciudad',                      5),
                                                              ('Mudanza nacional','Transporte a nivel nacional',                     5),
                                                              ('Embalaje profesional','Servicio de embalaje',                        5),
                                                              ('Transporte de carga','Movimiento de objetos pesados',                5),
                                                              ('Poda de árboles','Corte y mantenimiento de árboles',                 6),
                                                              ('Diseño de jardines','Creación de espacios verdes',                   6),
                                                              ('Corte de césped','Mantenimiento de césped',                          6),
                                                              ('Limpieza de piscinas','Cuidado de piscinas',                         6),
                                                              ('Construcción de tabiques','Levantamiento de paredes',               7),
                                                              ('Reparación de tejados','Arreglo de cubiertas',                      7),
                                                              ('Pintura de interiores','Pintura de paredes internas',                7),
                                                              ('Albañilería general','Trabajos estructurales',                      7),
                                                              ('Cuidado de mascotas','Paseo y cuidado de animales',                  8),
                                                              ('Cuidado de ancianos','Asistencia a personas mayores',               8),
                                                              ('Psicología','Sesiones de apoyo emocional',                          8),
                                                              ('Entrenamiento personal','Clases de fitness personalizadas',         8),
                                                              ('Fotografía de eventos','Cobertura fotográfica',                     9),
                                                              ('Planificación de bodas','Organización de bodas',                    9),
                                                              ('Diseño gráfico','Creación de diseños visuales',                     9),
                                                              ('Videografía','Producción de videos',                                9),
                                                              ('Asesoría legal','Consultoría jurídica',                            10),
                                                              ('Asesoría financiera','Planificación financiera',                   10),
                                                              ('Consultoría empresarial','Asesoramiento para negocios',            10),
                                                              ('Traducción de documentos','Traducción profesional',                10),
                                                              ('Enfermería a domicilio','Cuidado médico en casa',                  11),
                                                              ('Fisioterapia','Rehabilitación física',                             11),
                                                              ('Masajes terapéuticos','Masajes para relajación',                   11),
                                                              ('Consulta nutricional','Asesoría de dieta',                         11),
                                                              ('Reparación de automóviles','Arreglo de vehículos',                 12),
                                                              ('Cambio de neumáticos','Reemplazo de ruedas',                       12),
                                                              ('Lavado de coches','Limpieza de vehículos',                         12),
                                                              ('Mecánica general','Mantenimiento automotriz',                      12),
                                                              ('Catering','Servicio de comida para eventos',                       13),
                                                              ('Chef a domicilio','Cocina personalizada',                          13),
                                                              ('Limpieza de restaurantes','Higiene para negocios',                 13),
                                                              ('Decoración de mesas','Preparación de eventos',                     13),
                                                              ('Instalación de cámaras','Montaje de sistemas de vigilancia',       14),
                                                              ('Cerrajería avanzada','Cambio de cerraduras',                       14),
                                                              ('Seguridad privada','Vigilancia personal',                          14),
                                                              ('Mantenimiento de alarmas','Revisión de sistemas',                  14),
                                                              ('Reparación de relojes','Arreglo de relojes',                      15),
                                                              ('Restauración de joyas','Reparación de joyería',                   15),
                                                              ('Afinación de instrumentos','Mantenimiento musical',               15),
                                                              ('Reparación de cámaras','Arreglo de equipo fotográfico',           15);

-- Administradores
INSERT INTO administradores (id_usuario, nivel_acceso, ultimo_acceso) VALUES
                                                                          (3,'superadmin','2025-04-10 14:00:00'),
                                                                          (4,'moderador','2025-04-09 09:30:00'),
                                                                          (5,'soporte','2025-04-08 15:45:00'),
                                                                          (6,'analista','2025-04-07 12:15:00'),
                                                                          (7,'gestor_contenido','2025-04-06 17:00:00');

-- -- -- ASIGNACIÓN DINÁMICA DE VARIOS PROFESIONALES A CADA SERVICIO -- -- --

-- 1) Creamos 3 nuevos usuarios-profesionales
INSERT INTO usuarios (nombre, nombre_usuario, contraseña, email, valoracion) VALUES
                                                                                 ('Carlos López',    'carloslopez',    'hashedpass8', 'carlos@example.com',    4.2),
                                                                                 ('Lucía Fernández', 'luciafernandez','hashedpass9', 'lucia@example.com',      4.6),
                                                                                 ('Miguel Sánchez',  'miguelsanchez', 'hashedpass10','miguel@example.com',     4.4);

-- 2) Insertamos sus datos en profesionales
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, ubicacion)
SELECT u.id_usuario,
       CASE u.nombre_usuario
           WHEN 'carloslopez'    THEN 'Tecnología'
           WHEN 'luciafernandez' THEN 'Hogar'
           WHEN 'miguelsanchez'  THEN 'Belleza'
           END AS especialidad,
       CASE u.nombre_usuario
           WHEN 'carloslopez'    THEN 18.00
           WHEN 'luciafernandez' THEN 17.00
           WHEN 'miguelsanchez'  THEN 22.00
           END AS precio_hora,
       '{}' AS horario_disponible,
       CASE u.nombre_usuario
           WHEN 'carloslopez'    THEN 3
           WHEN 'luciafernandez' THEN 4
           WHEN 'miguelsanchez'  THEN 6
           END AS experiencia,
       CASE u.nombre_usuario
           WHEN 'carloslopez'    THEN 'Certificado IT'
           WHEN 'luciafernandez' THEN 'Certificado Hogar'
           WHEN 'miguelsanchez'  THEN 'Certificado Belleza'
           END AS certificaciones,
       u.valoracion AS calificacion_promedio,
       0 AS total_contrataciones,
       CASE u.nombre_usuario
           WHEN 'carloslopez'    THEN ST_GeomFromText('POINT(2.170048 41.385063)',4326)
           WHEN 'luciafernandez' THEN ST_GeomFromText('POINT(2.175000 41.388000)',4326)
           WHEN 'miguelsanchez'  THEN ST_GeomFromText('POINT(2.180000 41.390000)',4326)
           END AS ubicacion
FROM usuarios u
WHERE u.nombre_usuario IN ('carloslopez','luciafernandez','miguelsanchez');

-- 3) Para cada servicio, asignamos TODOS los profesionales (incluye el inicial id=2 y los 3 nuevos)
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio)
SELECT
    p.id_usuario,
    s.id_servicio,
    ROUND(p.precio_hora * (0.8 + RAND()*0.4), 2) AS precio,
    CONCAT('Servicio \"', s.nombre, '\" prestado por ', u.nombre) AS descripcion_servicio
FROM profesionales p
         CROSS JOIN servicios s
         JOIN usuarios u ON u.id_usuario = p.id_usuario;

-- Contrataciones de ejemplo (siguen apuntando a los primeros id_profesional_servicio)
INSERT INTO contrataciones (id_usuario, id_profesional_servicio, fecha_hora, estado, duracion_estimada, costo_total) VALUES
                                                                                                                         (1,  1, '2025-04-11 10:00:00', 'pendiente', 120, 25.00),
                                                                                                                         (1,  2, '2025-04-12 14:00:00', 'confirmada',180, 30.00),
                                                                                                                         (1,  3, '2025-04-13 09:00:00', 'completada', 90, 20.00),
                                                                                                                         (1,  4, '2025-04-14 11:30:00','pendiente',  60, 15.00),
                                                                                                                         (1,  5, '2025-04-15 15:00:00','confirmada',120, 20.00),
                                                                                                                         (1,  6, '2025-04-16 08:00:00','pendiente', 150, 25.00),
                                                                                                                         (1,  7, '2025-04-17 13:00:00','completada',180, 30.00),
                                                                                                                         (1,  8, '2025-04-18 16:00:00','cancelada', 60, 15.00),
                                                                                                                         (1,  9, '2025-04-19 10:30:00','pendiente',240, 35.00),
                                                                                                                         (1, 10, '2025-04-20 12:00:00','confirmada', 90, 18.00),
                                                                                                                         (1, 11, '2025-04-21 09:00:00','pendiente',120, 25.00),
                                                                                                                         (1, 12, '2025-04-22 14:00:00','completada', 90, 30.00),
                                                                                                                         (1, 13, '2025-04-23 11:00:00','confirmada',120, 20.00),
                                                                                                                         (1, 14, '2025-04-24 15:00:00','pendiente',180, 25.00),
                                                                                                                         (1, 15, '2025-04-25 10:00:00','completada',150, 30.00);

-- Valoraciones de ejemplo
INSERT INTO valoraciones (id_usuario, id_usuario_profesional, puntuacion, comentario, id_contratacion) VALUES
                                                                                                           (1, 2, 4, 'Buen servicio',      1),
                                                                                                           (1, 2, 5, 'Excelente trabajo',  2),
                                                                                                           (1, 2, 3, 'Regular',            3),
                                                                                                           (1, 2, 4, 'Rápido y eficiente', 4),
                                                                                                           (1, 2, 5, 'Muy satisfecho',     5),
                                                                                                           (1, 2, 4, 'Buen resultado',     6),
                                                                                                           (1, 2, 3, 'Podría mejorar',     7),
                                                                                                           (1, 2, 5, 'Perfecto',           8),
                                                                                                           (1, 2, 4, 'Bien hecho',         9),
                                                                                                           (1, 2, 5, 'Gran calidad',      10),
                                                                                                           (1, 2, 4, 'Satisfecho',        11),
                                                                                                           (1, 2, 5, 'Recomendable',      12),
                                                                                                           (1, 2, 3, 'Aceptable',         13),
                                                                                                           (1, 2, 4, 'Buen trabajo',      14),
                                                                                                           (1, 2, 5, 'Impecable',         15);