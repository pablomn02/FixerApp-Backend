DROP DATABASE IF EXISTS fixer_app;
CREATE DATABASE IF NOT EXISTS fixer_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fixer_app;

CREATE TABLE categorias (
                            id_categoria INT PRIMARY KEY AUTO_INCREMENT,
                            nombre VARCHAR(50) NOT NULL,
                            descripcion TINYTEXT,
                            CONSTRAINT nombre_categoria_unique UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE servicios (
                           id_servicio INT PRIMARY KEY AUTO_INCREMENT,
                           nombre VARCHAR(100) NOT NULL,
                           descripcion TEXT,
                           id_categoria INT NOT NULL,
                           FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_servicios_id_categoria ON servicios (id_categoria);

CREATE TABLE usuarios (
                          id_usuario INT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(255),
                          nombre_usuario VARCHAR(255) UNIQUE,
                          contraseña VARCHAR(255),
                          email VARCHAR(255) UNIQUE,
                          valoracion FLOAT
) ENGINE=InnoDB;

CREATE TABLE clientes (
                          id_usuario INT PRIMARY KEY,
                          preferencias JSON,
                          FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

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

CREATE TABLE administradores (
                                 id_usuario INT PRIMARY KEY,
                                 nivel_acceso VARCHAR(50),
                                 ultimo_acceso DATETIME,
                                 FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

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

INSERT INTO usuarios (nombre, nombre_usuario, contraseña, email, valoracion) VALUES
                                                                                 ('Juan Pérez', 'juanperez', 'hashedpass1', 'juan@example.com', 4.5),
                                                                                 ('María Gómez', 'mariagomez', 'hashedpass2', 'maria@example.com', 4.8),
                                                                                 ('Admin Uno', 'admin1', 'hashedpass3', 'admin1@example.com', NULL),
                                                                                 ('Admin Dos', 'admin2', 'hashedpass4', 'admin2@example.com', NULL),
                                                                                 ('Admin Tres', 'admin3', 'hashedpass5', 'admin3@example.com', NULL),
                                                                                 ('Admin Cuatro', 'admin4', 'hashedpass6', 'admin4@example.com', NULL),
                                                                                 ('Admin Cinco', 'admin5', 'hashedpass7', 'admin5@example.com', NULL);

INSERT INTO clientes (id_usuario, preferencias) VALUES
    (1, '{"preferencia1": "servicio rápido", "preferencia2": "horario matutino"}');

INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones) VALUES
    (2, 'Hogar', 15.50, '{"lunes": "09:00-17:00", "martes": "09:00-17:00"}', 5, 'Certificado profesional', 4.7, 10);

INSERT INTO categorias (nombre, descripcion) VALUES
                                                 ('Hogar', 'Reparaciones y mantenimiento doméstico'),
                                                 ('Belleza', 'Cuidado personal y estética'),
                                                 ('Educación', 'Enseñanza y tutorías'),
                                                 ('Tecnología', 'Soporte técnico y reparaciones electrónicas'),
                                                 ('Transporte', 'Mudanzas y logística'),
                                                 ('Jardinería', 'Cuidado de espacios verdes'),
                                                 ('Construcción', 'Obras y reformas'),
                                                 ('Cuidado', 'Cuidado personal y animal'),
                                                 ('Creatividad', 'Eventos y servicios creativos'),
                                                 ('Profesional', 'Consultoría y asesorías'),
                                                 ('Salud', 'Servicios médicos y bienestar'),
                                                 ('Automoción', 'Mantenimiento y reparación de vehículos'),
                                                 ('Hostelería', 'Servicios para eventos y gastronomía'),
                                                 ('Seguridad', 'Protección y vigilancia'),
                                                 ('Reparaciones', 'Reparaciones especializadas');

INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES
                                                              ('Reparación de tuberías', 'Reparación de fugas y tuberías', 1),
                                                              ('Instalación eléctrica', 'Montaje de sistemas eléctricos', 1),
                                                              ('Limpieza profunda', 'Limpieza completa de hogares', 1),
                                                              ('Reparación de electrodomésticos', 'Arreglo de aparatos domésticos', 1),
                                                              ('Peluquería', 'Corte y estilismo de cabello', 2),
                                                              ('Manicura y pedicura', 'Cuidado de uñas', 2),
                                                              ('Maquillaje profesional', 'Maquillaje para eventos', 2),
                                                              ('Barbería', 'Corte y afeitado para hombres', 2),
                                                              ('Clases de matemáticas', 'Tutorías de matemáticas', 3),
                                                              ('Clases de idiomas', 'Enseñanza de idiomas', 3),
                                                              ('Clases de música', 'Lecciones de instrumentos musicales', 3),
                                                              ('Capacitación profesional', 'Cursos de habilidades laborales', 3),
                                                              ('Reparación de computadoras', 'Diagnóstico y arreglo de PCs', 4),
                                                              ('Reparación de teléfonos', 'Arreglo de dispositivos móviles', 4),
                                                              ('Instalación de software', 'Configuración de programas', 4),
                                                              ('Soporte técnico remoto', 'Asistencia técnica a distancia', 4),
                                                              ('Mudanza local', 'Transporte dentro de la ciudad', 5),
                                                              ('Mudanza nacional', 'Transporte a nivel nacional', 5),
                                                              ('Embalaje profesional', 'Servicio de embalaje', 5),
                                                              ('Transporte de carga', 'Movimiento de objetos pesados', 5),
                                                              ('Poda de árboles', 'Corte y mantenimiento de árboles', 6),
                                                              ('Diseño de jardines', 'Creación de espacios verdes', 6),
                                                              ('Corte de césped', 'Mantenimiento de césped', 6),
                                                              ('Limpieza de piscinas', 'Cuidado de piscinas', 6),
                                                              ('Construcción de tabiques', 'Levantamiento de paredes', 7),
                                                              ('Reparación de tejados', 'Arreglo de cubiertas', 7),
                                                              ('Pintura de interiores', 'Pintura de paredes internas', 7),
                                                              ('Albañilería general', 'Trabajos estructurales', 7),
                                                              ('Cuidado de mascotas', 'Paseo y cuidado de animales', 8),
                                                              ('Cuidado de ancianos', 'Asistencia a personas mayores', 8),
                                                              ('Psicología', 'Sesiones de apoyo emocional', 8),
                                                              ('Entrenamiento personal', 'Clases de fitness personalizadas', 8),
                                                              ('Fotografía de eventos', 'Cobertura fotográfica', 9),
                                                              ('Planificación de bodas', 'Organización de bodas', 9),
                                                              ('Diseño gráfico', 'Creación de diseños visuales', 9),
                                                              ('Videografía', 'Producción de videos', 9),
                                                              ('Asesoría legal', 'Consultoría jurídica', 10),
                                                              ('Asesoría financiera', 'Planificación financiera', 10),
                                                              ('Consultoría empresarial', 'Asesoramiento para negocios', 10),
                                                              ('Traducción de documentos', 'Traducción profesional', 10),
                                                              ('Enfermería a domicilio', 'Cuidado médico en casa', 11),
                                                              ('Fisioterapia', 'Rehabilitación física', 11),
                                                              ('Masajes terapéuticos', 'Masajes para relajación', 11),
                                                              ('Consulta nutricional', 'Asesoría de dieta', 11),
                                                              ('Reparación de automóviles', 'Arreglo de vehículos', 12),
                                                              ('Cambio de neumáticos', 'Reemplazo de ruedas', 12),
                                                              ('Lavado de coches', 'Limpieza de vehículos', 12),
                                                              ('Mecánica general', 'Mantenimiento automotriz', 12),
                                                              ('Catering', 'Servicio de comida para eventos', 13),
                                                              ('Chef a domicilio', 'Cocina personalizada', 13),
                                                              ('Limpieza de restaurantes', 'Higiene para negocios', 13),
                                                              ('Decoración de mesas', 'Preparación de eventos', 13),
                                                              ('Instalación de cámaras', 'Montaje de sistemas de vigilancia', 14),
                                                              ('Cerrajería avanzada', 'Cambio de cerraduras', 14),
                                                              ('Seguridad privada', 'Vigilancia personal', 14),
                                                              ('Mantenimiento de alarmas', 'Revisión de sistemas', 14),
                                                              ('Reparación de relojes', 'Arreglo de relojes', 15),
                                                              ('Restauración de joyas', 'Reparación de joyería', 15),
                                                              ('Afinación de instrumentos', 'Mantenimiento musical', 15),
                                                              ('Reparación de cámaras', 'Arreglo de equipo fotográfico', 15);

INSERT INTO administradores (id_usuario, nivel_acceso, ultimo_acceso) VALUES
                                                                          (3, 'superadmin', '2025-04-10 14:00:00'),
                                                                          (4, 'moderador', '2025-04-09 09:30:00'),
                                                                          (5, 'soporte', '2025-04-08 15:45:00'),
                                                                          (6, 'analista', '2025-04-07 12:15:00'),
                                                                          (7, 'gestor_contenido', '2025-04-06 17:00:00');

INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES
                                                                                              (2, 1, 25.00, 'Reparación de tuberías en hogares'),
                                                                                              (2, 2, 30.00, 'Instalación eléctrica básica'),
                                                                                              (2, 3, 20.00, 'Limpieza profunda de casas'),
                                                                                              (2, 5, 15.00, 'Corte de cabello profesional'),
                                                                                              (2, 9, 20.00, 'Tutorías de matemáticas'),
                                                                                              (2, 13, 25.00, 'Reparación de computadoras'),
                                                                                              (2, 17, 30.00, 'Mudanza local'),
                                                                                              (2, 21, 15.00, 'Poda de árboles'),
                                                                                              (2, 25, 35.00, 'Construcción de tabiques'),
                                                                                              (2, 29, 18.00, 'Cuidado de mascotas'),
                                                                                              (2, 33, 25.00, 'Fotografía de eventos'),
                                                                                              (2, 37, 30.00, 'Asesoría legal'),
                                                                                              (2, 41, 20.00, 'Enfermería a domicilio'),
                                                                                              (2, 45, 25.00, 'Reparación de automóviles'),
                                                                                              (2, 49, 30.00, 'Catering para eventos');

INSERT INTO contrataciones (id_usuario, id_profesional_servicio, fecha_hora, estado, duracion_estimada, costo_total) VALUES
                                                                                                                         (1, 1, '2025-04-11 10:00:00', 'pendiente', 120, 25.00),
                                                                                                                         (1, 2, '2025-04-12 14:00:00', 'confirmada', 180, 30.00),
                                                                                                                         (1, 3, '2025-04-13 09:00:00', 'completada', 90, 20.00),
                                                                                                                         (1, 4, '2025-04-14 11:30:00', 'pendiente', 60, 15.00),
                                                                                                                         (1, 5, '2025-04-15 15:00:00', 'confirmada', 120, 20.00),
                                                                                                                         (1, 6, '2025-04-16 08:00:00', 'pendiente', 150, 25.00),
                                                                                                                         (1, 7, '2025-04-17 13:00:00', 'completada', 180, 30.00),
                                                                                                                         (1, 8, '2025-04-18 16:00:00', 'cancelada', 60, 15.00),
                                                                                                                         (1, 9, '2025-04-19 10:30:00', 'pendiente', 240, 35.00),
                                                                                                                         (1, 10, '2025-04-20 12:00:00', 'confirmada', 90, 18.00),
                                                                                                                         (1, 11, '2025-04-21 09:00:00', 'pendiente', 120, 25.00),
                                                                                                                         (1, 12, '2025-04-22 14:00:00', 'completada', 90, 30.00),
                                                                                                                         (1, 13, '2025-04-23 11:00:00', 'confirmada', 120, 20.00),
                                                                                                                         (1, 14, '2025-04-24 15:00:00', 'pendiente', 180, 25.00),
                                                                                                                         (1, 15, '2025-04-25 10:00:00', 'completada', 150, 30.00);

INSERT INTO valoraciones (id_usuario, id_usuario_profesional, puntuacion, comentario, id_contratacion) VALUES
                                                                                                           (1, 2, 4, 'Buen servicio', 1),
                                                                                                           (1, 2, 5, 'Excelente trabajo', 2),
                                                                                                           (1, 2, 3, 'Regular', 3),
                                                                                                           (1, 2, 4, 'Rápido y eficiente', 4),
                                                                                                           (1, 2, 5, 'Muy satisfecho', 5),
                                                                                                           (1, 2, 4, 'Buen resultado', 6),
                                                                                                           (1, 2, 3, 'Podría mejorar', 7),
                                                                                                           (1, 2, 5, 'Perfecto', 8),
                                                                                                           (1, 2, 4, 'Bien hecho', 9),
                                                                                                           (1, 2, 5, 'Gran calidad', 10),
                                                                                                           (1, 2, 4, 'Satisfecho', 11),
                                                                                                           (1, 2, 5, 'Recomendable', 12),
                                                                                                           (1, 2, 3, 'Aceptable', 13),
                                                                                                           (1, 2, 4, 'Buen trabajo', 14),
                                                                                                           (1, 2, 5, 'Impecable', 15);