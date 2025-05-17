-- 1. Crear Categorías
INSERT INTO categorias (nombre, descripcion) VALUES
                                                 ('Hogar', 'Servicios para el mantenimiento del hogar'),
                                                 ('Belleza', 'Servicios de cuidado personal'),
                                                 ('Tecnología', 'Reparaciones y soporte técnico'),
                                                 ('Transporte', 'Mudanzas y logística'),
                                                 ('Educación', 'Clases particulares y tutorías'),
                                                 ('Salud', 'Servicios médicos y bienestar'),
                                                 ('Mascotas', 'Cuidado y adiestramiento de animales'),
                                                 ('Eventos', 'Organización y gestión de eventos'),
                                                 ('Construcción', 'Servicios de construcción y reformas'),
                                                 ('Gastronomía', 'Cocina y catering profesional');

-- 2. Crear Servicios (20 en total)
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES
                                                              ('Reparación de tuberías', 'Reparación de fugas y tuberías', 1),
                                                              ('Instalaciones eléctricas', 'Montaje de sistemas eléctricos', 1),
                                                              ('Corte de cabello', 'Peluquería para hombres y mujeres', 2),
                                                              ('Tratamientos faciales', 'Limpiezas y cuidado de la piel', 2),
                                                              ('Reparación de PC', 'Arreglo de computadoras y laptops', 3),
                                                              ('Reparación de móviles', 'Reparación de celulares', 3),
                                                              ('Mudanzas locales', 'Traslado de bienes en la misma ciudad', 4),
                                                              ('Mudanzas nacionales', 'Traslados entre ciudades', 4),
                                                              ('Clases de matemáticas', 'Tutorías personalizadas', 5),
                                                              ('Clases de inglés', 'Clases particulares de inglés', 5),
                                                              ('Consulta médica', 'Medicina general y chequeos', 6),
                                                              ('Terapia física', 'Rehabilitación de lesiones', 6),
                                                              ('Paseo de perros', 'Servicio de paseo de mascotas', 7),
                                                              ('Cuidado de gatos', 'Visitas a domicilio para gatos', 7),
                                                              ('Organización de bodas', 'Planificación completa de bodas', 8),
                                                              ('Organización de cumpleaños', 'Organización de fiestas', 8),
                                                              ('Obras de albañilería', 'Construcción de estructuras', 9),
                                                              ('Pintura de interiores', 'Decoración y pintura de hogares', 9),
                                                              ('Chef privado', 'Cocina personalizada a domicilio', 10),
                                                              ('Catering para eventos', 'Servicio de comidas para eventos', 10);

-- 3. Insertar Usuarios Clientes
INSERT INTO usuarios (nombre, nombre_usuario, contraseña, email, valoracion) VALUES
                                                                                 ('Laura Pérez', 'cliente1', 'pass123', 'cliente1@example.com', 4.7),
                                                                                 ('Miguel Torres', 'cliente2', 'pass123', 'cliente2@example.com', 4.6),
                                                                                 ('Sofía García', 'cliente3', 'pass123', 'cliente3@example.com', 4.8),
                                                                                 ('Andrés Díaz', 'cliente4', 'pass123', 'cliente4@example.com', 4.5),
                                                                                 ('Lucía Romero', 'cliente5', 'pass123', 'cliente5@example.com', 4.7);

-- 4. Asociar Usuarios a Clientes
INSERT INTO clientes (id_usuario, preferencias) VALUES
                                                    (1, '{"preferencia":"rápido"}'),
                                                    (2, '{"preferencia":"económico"}'),
                                                    (3, '{"preferencia":"cercano"}'),
                                                    (4, '{"preferencia":"flexible"}'),
                                                    (5, '{"preferencia":"calidad"}');

-- 5. Insertar Usuarios Profesionales
INSERT INTO usuarios (nombre, nombre_usuario, contraseña, email, valoracion) VALUES
                                                                                 ('Juan Fernández', 'pro1', 'pass123', 'pro1@example.com', 4.8),
                                                                                 ('Carmen López', 'pro2', 'pass123', 'pro2@example.com', 4.7),
                                                                                 ('Luis Gómez', 'pro3', 'pass123', 'pro3@example.com', 4.6),
                                                                                 ('Marta Sánchez', 'pro4', 'pass123', 'pro4@example.com', 4.9),
                                                                                 ('Diego Navarro', 'pro5', 'pass123', 'pro5@example.com', 4.7),
                                                                                 ('Paula Molina', 'pro6', 'pass123', 'pro6@example.com', 4.8),
                                                                                 ('Francisco Díaz', 'pro7', 'pass123', 'pro7@example.com', 4.6),
                                                                                 ('Andrea Castillo', 'pro8', 'pass123', 'pro8@example.com', 4.8),
                                                                                 ('Hugo Romero', 'pro9', 'pass123', 'pro9@example.com', 4.7),
                                                                                 ('Sara Vargas', 'pro10', 'pass123', 'pro10@example.com', 4.9);

-- 6. Insertar Profesionales
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, ubicacion, servicio_id) VALUES
                                                                                                                                                                                             (6, 'Fontanero', 25.00, '{"lunes":[{"inicio":"08:00","fin":"16:00"}]}', 5, 'Certificado de Fontanería', 4.8, 12, ST_GeomFromText('POINT(2.17 41.38)',4326), 1),
                                                                                                                                                                                             (7, 'Electricista', 30.00, '{"martes":[{"inicio":"09:00","fin":"17:00"}]}', 7, 'Diploma Electricidad', 4.7, 10, ST_GeomFromText('POINT(2.18 41.39)',4326), 2),
                                                                                                                                                                                             (8, 'Estilista', 28.00, '{"miércoles":[{"inicio":"10:00","fin":"18:00"}]}', 6, 'Certificado Estética', 4.6, 9, ST_GeomFromText('POINT(2.19 41.40)',4326), 3),
                                                                                                                                                                                             (9, 'Técnico Informático', 35.00, '{"jueves":[{"inicio":"07:00","fin":"15:00"}]}', 8, 'Certificado Técnico', 4.9, 15, ST_GeomFromText('POINT(2.20 41.41)',4326), 5),
                                                                                                                                                                                             (10, 'Profesor de Inglés', 22.00, '{"viernes":[{"inicio":"16:00","fin":"20:00"}]}', 4, 'Licenciado en Lenguas', 4.7, 11, ST_GeomFromText('POINT(2.21 41.42)',4326), 10);

-- 7. Insertar Profesional_Servicios (varios profesionales por servicio)
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES
                                                                                              (6, 1, 25.00, 'Reparación de fugas de agua'),
                                                                                              (6, 2, 30.00, 'Instalaciones eléctricas básicas'),
                                                                                              (7, 2, 28.00, 'Instalaciones eléctricas avanzadas'),
                                                                                              (8, 3, 30.00, 'Corte de cabello y estilismo'),
                                                                                              (8, 4, 35.00, 'Tratamientos faciales de belleza'),
                                                                                              (9, 5, 28.00, 'Reparación de computadoras'),
                                                                                              (9, 6, 30.00, 'Arreglo de móviles Android e iPhone'),
                                                                                              (10, 9, 22.00, 'Tutorías personalizadas de inglés'),
                                                                                              (10, 10, 24.00, 'Preparación exámenes de inglés');

-- 8. Insertar Contrataciones (clientes contratando profesionales)
INSERT INTO contrataciones (id_usuario, id_profesional_servicio, fecha_hora, estado, duracion_estimada, costo_total) VALUES
                                                                                                                         (1, 1, '2025-06-01 10:00:00', 'confirmada', 120, 50.00),
                                                                                                                         (2, 2, '2025-06-02 12:00:00', 'pendiente', 90, 45.00),
                                                                                                                         (3, 3, '2025-06-03 09:30:00', 'confirmada', 60, 28.00),
                                                                                                                         (4, 4, '2025-06-04 16:00:00', 'confirmada', 90, 70.00),
                                                                                                                         (5, 5, '2025-06-05 11:00:00', 'confirmada', 120, 44.00);

-- 9. Insertar Valoraciones
INSERT INTO valoraciones (id_usuario, id_usuario_profesional, puntuacion, comentario, id_contratacion) VALUES
                                                                                                           (1, 6, 5, '¡Muy buen fontanero!', 1),
                                                                                                           (2, 7, 4, 'Electricista puntual.', 2),
                                                                                                           (3, 8, 5, 'Muy contenta con el corte.', 3),
                                                                                                           (4, 9, 5, 'Reparación de PC perfecta.', 4),
                                                                                                           (5, 10, 4, 'Gran profesor de inglés.', 5);
