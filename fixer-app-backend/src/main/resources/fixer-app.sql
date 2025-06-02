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

INSERT INTO categorias (nombre, descripcion) VALUES ('Hogar', 'Servicios relacionados con el hogar');
INSERT INTO categorias (nombre, descripcion) VALUES ('Educación', 'Clases particulares y formación');
INSERT INTO categorias (nombre, descripcion) VALUES ('Salud', 'Cuidado y bienestar físico');
INSERT INTO categorias (nombre, descripcion) VALUES ('Tecnología', 'Asistencia tecnológica');
INSERT INTO categorias (nombre, descripcion) VALUES ('Construcción', 'Obras y reformas');
INSERT INTO categorias (nombre, descripcion) VALUES ('Belleza', 'Servicios estéticos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Mascotas', 'Cuidado de animales');
INSERT INTO categorias (nombre, descripcion) VALUES ('Eventos', 'Organización de celebraciones');
INSERT INTO categorias (nombre, descripcion) VALUES ('Automoción', 'Reparación y mantenimiento de vehículos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Consultoría', 'Asesoría profesional');
INSERT INTO categorias (nombre, descripcion) VALUES ('Arte', 'Servicios artísticos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Jardinería', 'Cuidado de jardines');
INSERT INTO categorias (nombre, descripcion) VALUES ('Seguridad', 'Vigilancia y protección');
INSERT INTO categorias (nombre, descripcion) VALUES ('Reparaciones', 'Arreglo de electrodomésticos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Limpieza', 'Limpieza doméstica y profesional');

INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Limpieza doméstica profunda', 'Limpieza exhaustiva de hogares, incluyendo cocinas, baños y áreas comunes.', 1);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Reparación de electrodomésticos', 'Diagnóstico y reparación de electrodomésticos como lavadoras y refrigeradores.', 1);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Fontanería doméstica', 'Reparaciones de tuberías, grifos y desatascos en el hogar.', 1);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Clases particulares de matemáticas', 'Clases personalizadas de matemáticas para estudiantes de primaria y secundaria.', 2);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Cursos de idiomas', 'Enseñanza de inglés, francés o alemán con enfoque comunicativo.', 2);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Preparación de exámenes oficiales', 'Preparación para exámenes como Selectividad o certificaciones internacionales.', 2);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Fisioterapia a domicilio', 'Tratamientos de rehabilitación y alivio del dolor en el hogar.', 3);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Nutrición personalizada', 'Planes de alimentación adaptados a necesidades individuales.', 3);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Entrenamiento personal', 'Sesiones de ejercicio físico con entrenador personal.', 3);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Reparación de ordenadores', 'Diagnóstico y reparación de problemas en ordenadores y portátiles.', 4);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Instalación de redes Wi-Fi', 'Configuración y optimización de redes inalámbricas domésticas.', 4);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Soporte técnico remoto', 'Asistencia técnica para software y dispositivos vía remoto.', 4);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Reformas de baños', 'Renovación completa de baños, incluyendo alicatado y fontanería.', 5);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Construcción de muros', 'Construcción de muros de hormigón, ladrillo o piedra.', 5);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Pintura de interiores', 'Pintura profesional de paredes y techos en interiores.', 5);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Peluquería a domicilio', 'Cortes, tintes y peinados realizados en el hogar del cliente.', 6);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Manicura y pedicura', 'Cuidado estético de uñas con esmaltado permanente o tradicional.', 6);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Maquillaje profesional', 'Maquillaje para eventos especiales, bodas o sesiones fotográficas.', 6);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Paseo de perros', 'Servicio de paseo diario para perros de todas las razas.', 7);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Cuidado de mascotas en casa', 'Atención a mascotas durante ausencias del dueño.', 7);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Adiestramiento canino', 'Entrenamiento para mejorar el comportamiento de perros.', 7);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Organización de bodas', 'Planificación integral de bodas, desde decoración hasta catering.', 8);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Fotografía de eventos', 'Cobertura fotográfica profesional para eventos y celebraciones.', 8);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Animación de fiestas infantiles', 'Entretenimiento para niños con juegos y actividades.', 8);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Cambio de aceite y filtros', 'Mantenimiento básico de vehículos con cambio de aceite y filtros.', 9);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Reparación de frenos', 'Diagnóstico y reparación de sistemas de frenado de vehículos.', 9);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Revisión pre-ITV', 'Inspección y preparación de vehículos para pasar la ITV.', 9);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Asesoría fiscal', 'Asesoramiento en declaraciones de impuestos y planificación fiscal.', 10);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Consultoría de negocios', 'Estrategias para mejorar la gestión y rentabilidad de empresas.', 10);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Asesoría jurídica', 'Asesoramiento legal en contratos, herencias y otros temas.', 10);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Retratos personalizados', 'Creación de retratos artísticos en pintura o dibujo.', 11);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Murales decorativos', 'Pintura de murales personalizados para hogares o negocios.', 11);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Clases de dibujo', 'Enseñanza de técnicas de dibujo para todas las edades.', 11);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Mantenimiento de jardines', 'Cuidado de césped, poda y limpieza de jardines.', 12);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Diseño de paisajes', 'Creación de diseños personalizados para jardines y exteriores.', 12);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Instalación de riego automático', 'Sistemas de riego para jardines y áreas verdes.', 12);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Instalación de alarmas', 'Instalación de sistemas de seguridad y alarmas para hogares.', 13);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Vigilancia privada', 'Servicios de seguridad para eventos o propiedades.', 13);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Asesoría en seguridad', 'Evaluación y mejora de sistemas de seguridad doméstica.', 13);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Reparación de lavadoras', 'Diagnóstico y reparación de lavadoras de todas las marcas.', 14);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Reparación de frigoríficos', 'Reparación de frigoríficos y sistemas de refrigeración.', 14);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Mantenimiento de aires acondicionados', 'Revisión y reparación de equipos de aire acondicionado.', 14);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Limpieza de oficinas', 'Limpieza profesional para oficinas y locales comerciales.', 15);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Limpieza post-obra', 'Limpieza a fondo tras reformas o construcciones.', 15);
INSERT INTO servicios (nombre, descripcion, id_categoria) VALUES ('Limpieza de cristales', 'Limpieza profesional de ventanas y cristales en edificios.', 15);

INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (1, 'Abraham Salinas-Mulet', 'ebarcena0', 'hashed_pass', 'calcaraz@costa.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (2, 'Susana Gámez Cobos', 'emmamartinez1', 'hashed_pass', 'nico89@manzanares.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (3, 'Genoveva Ripoll Agullo', 'abascalmiguela2', 'hashed_pass', 'tomasvera@hotmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (4, 'Rodolfo Ismael Gomila Recio', 'ncastrillo3', 'hashed_pass', 'silviacasals@gmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (5, 'Sofía de Páez', 'amaya824', 'hashed_pass', 'sebastianmoya@navarro.org', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (6, 'Salud del Garrido', 'rosendo125', 'hashed_pass', 'hcabrera@gual.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (7, 'Natalio Muro Carnero', 'tamaritjose-manuel6', 'hashed_pass', 'wiriarte@perez.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (8, 'Ana Tirado Pons', 'valcarcellola7', 'hashed_pass', 'espanollara@abascal.org', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (9, 'Clímaco Flor Crespo', 'aroa278', 'hashed_pass', 'linaresprimitiva@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (10, 'Ligia del Santana', 'estefaniafabregat9', 'hashed_pass', 'carlos57@gmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (11, 'Brígida Valenciano Alcalde', 'leivamaria-jesus10', 'hashed_pass', 'asdrubalayuso@hotmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (12, 'Flora Quero Arteaga', 'domitila5711', 'hashed_pass', 'aruiz@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (13, 'Demetrio Marti Hernando', 'yayala12', 'hashed_pass', 'macaria49@gmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (14, 'Benito Vera Abascal', 'ainaracamino13', 'hashed_pass', 'ncatala@anton-valera.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (15, 'Bibiana Perea-Izaguirre', 'epifanio1614', 'hashed_pass', 'raquelmuro@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (16, 'Rolando Albero Casares', 'cruzeufemia15', 'hashed_pass', 'torrecillaarmida@pedro-lamas.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (17, 'Ileana Coronado Benitez', 'khidalgo16', 'hashed_pass', 'ruth02@alcalde.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (18, 'Jose Luis Pepe Fuente Crespi', 'albinanogues17', 'hashed_pass', 'olalla41@hotmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (19, 'Jose Francisco Villar', 'vbolanos18', 'hashed_pass', 'cuevasborja@murcia.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (20, 'Claudio Arranz Pazos', 'arizaxiomara19', 'hashed_pass', 'palaciomanola@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (21, 'Alexandra de Vall', 'olasa20', 'hashed_pass', 'candelapacheco@canals-marques.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (22, 'Nacho Calatayud', 'valeriovilaplana21', 'hashed_pass', 'ccabezas@figueroa.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (23, 'Natividad del Bas', 'aranarufina22', 'hashed_pass', 'xblanco@gmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (24, 'Gertrudis Alfonso-Barón', 'melania2023', 'hashed_pass', 'aurorabanos@pons-quintanilla.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (25, 'Loida Martín Duarte', 'ipalau24', 'hashed_pass', 'gerardo64@roca-mir.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (26, 'Lorenza del Silva', 'ledesmaalfredo25', 'hashed_pass', 'itorrent@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (27, 'Adolfo Campillo Berenguer', 'casalsnereida26', 'hashed_pass', 'juanitaplanas@esteban.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (28, 'María Ángeles Mascaró Cantón', 'royozacarias27', 'hashed_pass', 'santamarianatividad@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (29, 'Manola Morales Torrecilla', 'mansonazario28', 'hashed_pass', 'piedadbarbera@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (30, 'Máxima Ferrera Piña', 'nolmedo29', 'hashed_pass', 'eduardocasanovas@hotmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (31, 'María Cristina Vilanova Juliá', 'jose9330', 'hashed_pass', 'gasconmohamed@gmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (32, 'Carmen de Perales', 'dbelda31', 'hashed_pass', 'nidia33@pinol-jerez.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (33, 'Rafael Sarabia Seco', 'vinashector32', 'hashed_pass', 'andres-felipemontserrat@estevez.net', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (34, 'Chuy Gómez Puerta', 'danilo0633', 'hashed_pass', 'jennyleal@roldan-quintana.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (35, 'Nazario Cuesta Álamo', 'hector8534', 'hashed_pass', 'eva-mariaavila@vicens.net', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (36, 'Selena Alexandra Talavera Ángel', 'puenteeugenio35', 'hashed_pass', 'bibianacamps@valls.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (37, 'Jerónimo Gallo Alonso', 'luznico36', 'hashed_pass', 'rbanos@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (38, 'Piedad Morell Alonso', 'olalla1637', 'hashed_pass', 'gil57@espanol.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (39, 'Lidia del Santamaría', 'rodriguezana-belen38', 'hashed_pass', 'jose34@hotmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (40, 'Leopoldo Fiol Conesa', 'icarrillo39', 'hashed_pass', 'fajardosamuel@azorin.org', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (41, 'Ciro Plana Palomares', 'sanchezmireia40', 'hashed_pass', 'hoyosrosa@yahoo.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (42, 'Che Losa Barriga', 'benavidesmaria-del-carmen41', 'hashed_pass', 'martiriouriarte@gascon.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (43, 'Benigno Alfonso', 'gpazos42', 'hashed_pass', 'isidoracantero@peinado.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (44, 'Ema Álvaro Ribes', 'paz4343', 'hashed_pass', 'estefaniazurita@casals.org', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (45, 'Rosalina Narváez Ocaña', 'paula7844', 'hashed_pass', 'eutropio02@hotmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (46, 'Julie Burgos Riquelme', 'ferrerpepito45', 'hashed_pass', 'redondoteofila@gmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (47, 'Clotilde Candela Tena Mora', 'adadiareyes46', 'hashed_pass', 'rmontesinos@hotmail.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (48, 'Tito Quesada Villa', 'tnebot47', 'hashed_pass', 'david28@nunez.es', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (49, 'Silvestre Quero Cazorla', 'epifaniorojas48', 'hashed_pass', 'ccano@benavente.com', 0, 'profesional');
INSERT INTO usuarios (id_usuario, nombre, nombre_usuario, contraseña, email, valoracion, tipo_usuario) VALUES (50, 'Francisco Javier Alcolea Quero', 'augustoiborra49', 'hashed_pass', 'carbonellche@hotmail.com', 0, 'profesional');

INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (1, 'Scientist, forensic', 37.38, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 1, 'Dolores dolorem alias exercitationem doloribus voluptates.', 4.48, 31, 43.412345, -5.912345, 9);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (2, 'Environmental health practitioner', 40.78, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Ea deleniti recusandae maiores dignissimos expedita libero suscipit.', 3.17, 54, 43.298765, -5.789123, 2);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (3, 'Learning disability nurse', 18.28, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 8, 'Nam molestias voluptate consequatur.', 4.01, 3, 43.451234, -5.876543, 13);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (4, 'Accountant, chartered certified', 40.06, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Minus molestiae soluta magnam expedita aut minima.', 3.84, 57, 43.367890, -5.834567, 18);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (5, 'Retail buyer', 43.33, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 1, 'Porro sint molestias nostrum ex dolore minus.', 4.52, 20, 43.321456, -5.901234, 22);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (6, 'Research scientist (medical)', 24.73, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 7, 'Aspernatur doloremque non aperiam laboriosam mollitia blanditiis iure.', 4.91, 43, 43.398765, -5.823456, 6);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (7, 'Buyer, retail', 28.3, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 12, 'Nam adipisci totam voluptas.', 4.69, 77, 43.345678, -5.876543, 3);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (8, 'Legal executive', 40.54, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Dolorem soluta possimus nulla.', 3.25, 48, 43.376543, -5.856789, 36);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (9, 'Risk manager', 25.26, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 20, 'Veniam labore voluptas quisquam ipsum.', 4.77, 46, 43.332345, -5.890123, 13);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (10, 'Civil engineer, contracting', 39.66, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 2, 'Fuga quasi eligendi consequuntur eius excepturi ducimus itaque.', 4.32, 98, 43.409876, -5.823456, 6);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (11, 'Ecologist', 44.94, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 4, 'Eaque vero enim doloribus eaque.', 3.76, 58, 43.365432, -5.867890, 11);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (12, 'Agricultural engineer', 27.96, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 7, 'Eius odit dolorum adipisci dicta nam dicta.', 4.34, 89, 43.387654, -5.845678, 39);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (13, 'Engineer, production', 37.22, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Illum dicta dolor.', 4.46, 20, 43.343210, -5.901234, 25);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (14, 'Financial risk analyst', 24.45, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Quod voluptatem aspernatur voluptas nobis.', 3.44, 41, 43.398765, -5.834567, 15);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (15, 'Event organiser', 43.76, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 11, 'Sunt ratione quisquam ut minus.', 3.8, 8, 43.321456, -5.876543, 37);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (16, 'Chartered loss adjuster', 45.67, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 11, 'Ea dignissimos porro laudantium harum.', 3.43, 63, 43.376543, -5.856789, 42);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (17, 'Maintenance engineer', 31.06, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 9, 'Earum exercitationem minima velit.', 3.28, 95, 43.332345, -5.890123, 35);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (18, 'Chemist, analytical', 24.2, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 19, 'Porro dolorem deserunt.', 3.86, 74, 43.409876, -5.823456, 24);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (19, 'Engineer, maintenance', 22.68, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 5, 'Eos et animi id assumenda et.', 4.02, 11, 43.365432, -5.867890, 8);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (20, 'Nurse, learning disability', 20.35, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 6, 'Excepturi amet ullam hic odio blanditiis.', 4.58, 54, 43.387654, -5.845678, 5);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (21, 'Maintenance engineer', 28.47, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 20, 'Consequuntur maiores voluptatem nobis eum.', 4.99, 67, 43.343210, -5.901234, 36);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (22, 'Technical brewer', 45.13, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 1, 'Natus nemo laudantium suscipit.', 4.36, 14, 43.398765, -5.834567, 18);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (23, 'Production manager', 41.9, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 11, 'Magni suscipit nesciunt magni cumque sit.', 3.22, 55, 43.321456, -5.876543, 30);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (24, 'Midwife', 15.11, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 9, 'Aperiam occaecati ut tempora rerum quis.', 4.94, 97, 43.376543, -5.856789, 33);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (25, 'Investment analyst', 46.94, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 10, 'Ipsa esse qui aliquam eaque at.', 4.68, 64, 43.332345, -5.890123, 13);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (26, 'Financial adviser', 20.35, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 6, 'Atque harum dolores.', 4.08, 99, 43.409876, -5.823456, 1);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (27, 'Conservator, museum/gallery', 35.96, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 16, 'Nulla eos quisquam animi praesentium minima.', 3.04, 46, 43.365432, -5.867890, 16);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (28, 'Arboriculturist', 17.03, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 19, 'Sequi fuga saepe error magni soluta.', 4.89, 10, 43.387654, -5.845678, 5);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (29, 'Administrator, arts', 49.23, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Adipisci iste totam nam vero fugiat.', 4.53, 16, 43.343210, -5.901234, 36);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (30, 'Training and development officer', 20.78, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 17, 'Similique optio dignissimos exercitationem.', 4.74, 54, 43.398765, -5.834567, 35);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (31, 'Warehouse manager', 41.43, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 7, 'Harum ea sit nisi nostrum.', 4.43, 51, 43.321456, -5.876543, 29);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (32, 'Journalist, newspaper', 46.49, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 15, 'Nihil excepturi fugiat maxime illum voluptatum.', 3.24, 28, 43.376543, -5.856789, 22);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (33, 'Psychotherapist', 15.74, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Expedita voluptatem soluta dignissimos commodi esse corrupti.', 3.46, 28, 43.332345, -5.890123, 5);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (34, 'Soil scientist', 39.77, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 2, 'Nemo dolorem quos quis nihil.', 3.46, 4, 43.409876, -5.823456, 5);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (35, 'Risk manager', 33.0, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 9, 'Quasi voluptates ipsum perspiciatis pariatur fugit ut.', 4.34, 27, 43.365432, -5.867890, 9);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (36, 'Accountant, chartered certified', 40.32, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 19, 'Error perferendis odio nesciunt sit excepturi.', 4.15, 31, 43.387654, -5.845678, 27);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (37, 'Advertising copywriter', 21.66, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 4, 'Facilis ut necessitatibus tenetur ipsum maiores cumque.', 4.32, 45, 43.343210, -5.901234, 27);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (38, 'Forensic scientist', 31.35, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 2, 'Aspernatur explicabo mollitia sunt provident perferendis architecto aliquid.', 4.35, 82, 43.398765, -5.834567, 4);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (39, 'Print production planner', 29.09, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 11, 'Quo veniam molestiae possimus.', 4.6, 13, 43.321456, -5.876543, 13);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (40, 'Solicitor, Scotland', 21.66, '{"miércoles": [{"inicio": "08:00", "fin": "16:00"}]}', 15, 'Laborum sed occaecati aperiam corrupti eius et.', 3.28, 23, 43.376543, -5.856789, 30);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (41, 'Programmer, systems', 23.74, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 3, 'Ipsa quia explicabo tempora.', 3.89, 70, 43.332345, -5.890123, 4);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (42, 'Research scientist (life sciences)', 37.83, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Fugit eligendi doloremque molestias optio eos.', 4.67, 11, 43.409876, -5.823456, 11);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (43, 'Nurse, mental health', 29.22, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 16, 'Nam repellendus illo rerum.', 3.43, 51, 43.365432, -5.867890, 11);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (44, 'Administrator', 28.26, '{"jueves": [{"inicio": "08:00", "fin": "16:00"}]}', 13, 'Ducimus inventore corporis dolorem aliquam explicabo iusto sequi.', 3.53, 100, 43.387654, -5.845678, 19);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (45, 'Banker', 29.81, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 18, 'Voluptatum sed iure quasi aliquid adipisci hic.', 4.32, 62, 43.343210, -5.901234, 13);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (46, 'Structural engineer', 25.38, '{"lunes": [{"inicio": "08:00", "fin": "16:00"}]}', 2, 'Nisi recusandae ad quia ad.', 4.16, 69, 43.398765, -5.834567, 21);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (47, 'Academic librarian', 17.0, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 19, 'Modi doloremque ratione a fugit temporibus magni magni.', 3.95, 67, 43.321456, -5.876543, 4);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (48, 'Freight forwarder', 48.63, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 3, 'Temporibus beatae impedit animi asperiores.', 4.7, 8, 43.376543, -5.856789, 5);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (49, 'Surveyor, hydrographic', 38.63, '{"martes": [{"inicio": "08:00", "fin": "16:00"}]}', 8, 'Officia molestias vero dolorum.', 3.81, 72, 43.332345, -5.890123, 38);
INSERT INTO profesionales (id_usuario, especialidad, precio_hora, horario_disponible, experiencia, certificaciones, calificacion_promedio, total_contrataciones, latitude, longitude, servicio_id) VALUES (50, 'Secretary, company', 35.81, '{"viernes": [{"inicio": "08:00", "fin": "16:00"}]}', 20, 'Facere error suscipit quo cum.', 3.16, 84, 43.409876, -5.823456, 37);

INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (1, 9, 37.38, 'Análisis forense de alta precisión para resolver casos complejos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (1, 3, 35.00, 'Reparaciones de fontanería doméstica con materiales de calidad.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (1, 1, 30.00, 'Limpieza profunda de hogares con productos ecológicos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (2, 2, 40.78, 'Reparación de electrodomésticos con garantía de servicio.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (2, 3, 38.00, 'Soluciones rápidas para problemas de fontanería en el hogar.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (2, 1, 35.00, 'Limpieza exhaustiva de cocinas y baños.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (3, 13, 18.28, 'Reformas completas de baños con acabados modernos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (3, 15, 20.00, 'Pintura de interiores con colores personalizados.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (3, 14, 22.00, 'Construcción de muros resistentes y estéticos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (4, 18, 40.06, 'Maquillaje profesional para bodas y eventos especiales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (4, 16, 38.00, 'Cortes de pelo modernos y personalizados a domicilio.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (4, 17, 35.00, 'Manicura con esmaltado de larga duración.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (5, 22, 43.33, 'Fotografía profesional para eventos y celebraciones.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (5, 23, 40.00, 'Organización de bodas con atención a cada detalle.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (5, 24, 38.00, 'Animación infantil con actividades creativas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (6, 6, 24.73, 'Fisioterapia personalizada para recuperación de lesiones.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (6, 7, 26.00, 'Planes de nutrición adaptados a objetivos personales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (6, 8, 28.00, 'Entrenamiento físico con rutinas personalizadas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (7, 3, 28.3, 'Reparaciones de fontanería con servicio urgente.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (7, 1, 25.00, 'Limpieza profunda para hogares y oficinas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (7, 2, 30.00, 'Reparación de electrodomésticos con repuestos originales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (8, 36, 40.54, 'Mantenimiento de jardines con poda profesional.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (8, 37, 42.00, 'Diseño de paisajes para jardines únicos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (8, 38, 45.00, 'Instalación de sistemas de riego eficientes.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (9, 13, 25.26, 'Construcción de muros con materiales de alta calidad.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (9, 14, 28.00, 'Reformas de baños con acabados modernos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (9, 15, 30.00, 'Pintura de interiores con pintura ecológica.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (10, 6, 39.66, 'Fisioterapia a domicilio con equipos portátiles.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (10, 7, 35.00, 'Asesoramiento nutricional para dietas saludables.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (10, 8, 37.00, 'Entrenamiento personalizado para mejorar el rendimiento.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (11, 11, 44.94, 'Clases de dibujo para principiantes y avanzados.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (11, 32, 40.00, 'Retratos artísticos en técnicas tradicionales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (11, 33, 42.00, 'Murales decorativos para hogares y negocios.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (12, 39, 27.96, 'Instalación de alarmas con tecnología avanzada.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (12, 40, 30.00, 'Servicios de vigilancia para eventos privados.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (12, 41, 32.00, 'Asesoría en seguridad para hogares y empresas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (13, 25, 37.22, 'Cambio de aceite y filtros con productos de calidad.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (13, 26, 35.00, 'Reparación de frenos con garantía de seguridad.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (13, 27, 33.00, 'Revisión pre-ITV para garantizar el cumplimiento normativo.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (14, 15, 24.45, 'Pintura de interiores con acabados profesionales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (14, 13, 26.00, 'Construcción de muros con diseño personalizado.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (14, 14, 28.00, 'Reformas de baños con materiales duraderos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (15, 37, 43.76, 'Organización de bodas con planificación integral.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (15, 23, 40.00, 'Fotografía profesional para eventos especiales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (15, 24, 38.00, 'Animación de fiestas infantiles con juegos educativos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (16, 42, 45.67, 'Reparación de lavadoras con repuestos originales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (16, 43, 43.00, 'Reparación de frigoríficos con servicio rápido.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (16, 44, 40.00, 'Mantenimiento de aires acondicionados para máximo rendimiento.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (17, 35, 31.06, 'Instalación de sistemas de riego automático.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (17, 36, 33.00, 'Mantenimiento de jardines con poda y limpieza.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (17, 37, 35.00, 'Diseño de paisajes para espacios exteriores.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (18, 24, 24.2, 'Animación infantil con actividades lúdicas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (18, 22, 26.00, 'Fotografía de eventos con edición profesional.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (18, 23, 28.00, 'Organización de bodas con decoración personalizada.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (19, 8, 22.68, 'Entrenamiento personal con programas adaptados.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (19, 6, 24.00, 'Fisioterapia a domicilio para rehabilitación.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (19, 7, 26.00, 'Asesoramiento nutricional para bienestar integral.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (20, 5, 20.35, 'Clases de matemáticas personalizadas para estudiantes.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (20, 4, 22.00, 'Cursos de idiomas con enfoque práctico.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (20, 6, 24.00, 'Preparación de exámenes oficiales con material actualizado.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (21, 36, 28.47, 'Mantenimiento de jardines con técnicas sostenibles.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (21, 37, 30.00, 'Diseño de paisajes para jardines residenciales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (21, 38, 32.00, 'Instalación de riego automático con eficiencia hídrica.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (22, 18, 45.13, 'Maquillaje profesional para eventos y sesiones fotográficas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (22, 16, 43.00, 'Peluquería a domicilio con cortes modernos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (22, 17, 40.00, 'Manicura y pedicura con esmaltado de calidad.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (23, 30, 41.9, 'Asesoría fiscal con optimización de impuestos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (23, 31, 40.00, 'Consultoría de negocios para pequeñas empresas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (23, 32, 38.00, 'Asesoría jurídica con enfoque en contratos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (24, 33, 15.11, 'Retratos personalizados en óleo o acuarela.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (24, 32, 17.00, 'Murales decorativos para espacios únicos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (24, 11, 20.00, 'Clases de dibujo con técnicas avanzadas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (25, 13, 46.94, 'Construcción de muros con acabados estéticos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (25, 14, 45.00, 'Reformas de baños con diseño funcional.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (25, 15, 43.00, 'Pintura de interiores con materiales de calidad.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (26, 1, 20.35, 'Limpieza doméstica profunda con atención al detalle.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (26, 2, 22.00, 'Reparación de electrodomésticos con diagnóstico rápido.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (26, 3, 24.00, 'Fontanería doméstica con soluciones duraderas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (27, 16, 35.96, 'Peluquería a domicilio con productos profesionales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (27, 17, 33.00, 'Manicura y pedicura con esmaltado personalizado.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (27, 18, 30.00, 'Maquillaje para eventos con técnicas modernas.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (28, 5, 17.03, 'Clases de matemáticas con enfoque práctico.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (28, 4, 19.00, 'Cursos de idiomas para todos los niveles.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (28, 6, 21.00, 'Preparación de exámenes oficiales con material didáctico.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (29, 36, 49.23, 'Mantenimiento de jardines con poda profesional.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (29, 37, 47.00, 'Diseño de paisajes para exteriores únicos.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (29, 38, 45.00, 'Instalación de sistemas de riego automático.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (30, 35, 20.78, 'Instalación de riego para jardines residenciales.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (30, 36, 22.00, 'Mantenimiento de jardines con técnicas sostenibles.');
INSERT INTO profesional_servicios (id_usuario, id_servicio, precio, descripcion_servicio) VALUES (30, 37, 24.00, 'Diseño de paisajes para espacios exteriores.');
