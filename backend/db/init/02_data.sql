-- =========================================================
-- DATOS DE BASE SÍSMICO (PostgreSQL)
-- Ejecutar después de 01_schema.sql
-- =========================================================

SET search_path TO public;

-- 1) EMPLEADOS
-- (IDs: 1=Ana, 2=Luis, 3=Juan, 4=Maravilla)
INSERT INTO empleado (nombre, apellido, mail, telefono) VALUES
('Ana', 'Gomez', 'anagomez@gmail.com', '+543511234567'),
('Luis', 'Paz', 'luispaz@gmail.com', '+543511234567'),
('Juan', 'Perez', 'juanperez@gmail.com', '+543511234567'),
('Maravilla', 'Martinez', 'maravilla@gmail.com', '+543511234567'); -- Para ServicioDeAutenticacion

-- 2) ESTADOS
-- (IDs: 1=Autodetectado, 2=Bloqueado en revisión, 3=Rechazado, 4=Pendiente de revision, 5=Pendiente)
INSERT INTO estado (nombre, tipo_estado) VALUES
('Autodetectado', 'AUTODETECTADO'),
('Bloqueado en revision', 'BLOQUEADO_EN_REVISION'),
('Rechazado', 'RECHAZADO'),
('Pendiente de revision', 'PENDIENTE_REVISION'), -- Agregado
('Pendiente', 'PENDIENTE');                      -- Agregado

-- 3) CLASIFICACION_SISMO
-- (ID: 1)
INSERT INTO clasificacion_sismo (nombre, km_profundidad_desde, km_profundidad_hasta) VALUES
('Superficial', 3.0, 6.0);

-- 4) ALCANCE_SISMO
-- (ID: 1)
INSERT INTO alcance_sismo (nombre, descripcion) VALUES
('Local', 'descripcionAlcance');

-- 5) ORIGEN_DE_GENERACION
-- (ID: 1)
INSERT INTO origen_de_generacion (nombre, descripcion) VALUES
('Interplaca', 'descripcionOrigen');

-- 6) TIPO_DE_DATO
-- (IDs: 1=Velocidad, 2=Frecuencia, 3=Longitud)
-- El schema dice que 'valor' no puede ser nulo, usamos 0 como default
INSERT INTO tipo_de_dato (denominacion, nombre_unidad_medida, valor) VALUES
('Velocidad de onda', 'Km/seg', 0),
('Frecuencia de onda', 'Hz', 0),
('Longitud', 'km/ciclo', 0);

-- 7) ESTACION_SISMOLOGICA
-- (IDs: 1=cod 100, 2=cod 200)
INSERT INTO estacion_sismologica (codigo_estacion) VALUES
(100),
(200);

-- 8) SISMOGRAFO
-- (IDs: 1=Sismo 1001/501, 2=Sismo 1002/502)
INSERT INTO sismografo (nro_serie, identificador_sismografo, fecha_adquisicion, estacion_sismologica_id) VALUES
(1001, 501, '2023-03-15', 1), -- Sismografo 1 -> Estacion 1
(1002, 502, '2023-04-20', 2); -- Sismografo 2 -> Estacion 2

-- 9) USUARIO Y SESION (Basado en obtenerSesionActual)
-- (Usuario ID: 1, Sesion ID: 1)
INSERT INTO usuario (nombre_usuario, contrasena, empleado_id) VALUES
('usuarioActual', 'password', 3); -- Usuario 'usuarioActual' está logueado como 'Juan Perez' (ID 3)

INSERT INTO sesion (fecha_hora, usuario_id) VALUES
(NOW(), 1);

-- 10) EVENTO_SISMICO
-- (IDs: 1, 2, 3, 4)
-- Todos están 'Autodetectados' (estado_id 1) y usan las mismas clasificaciones (id 1)
INSERT INTO evento_sismico (fecha_hora_ocurrencia, fecha_hora_fin, latitud_epicentro, longitud_epicentro, latitud_hipocentro, longitud_hipocentro, valor_magnitud, analista_supervisor_id, estado_actual_id, clasificacion_sismo_id, origen_generacion_id, alcance_sismo_id) VALUES
('2024-04-05 12:30:00', '2024-04-05 12:45:00', -26.8, -65.2, -27.1, -65.5, 3.6, NULL, 1, 1, 1, 1),
('2024-04-07 09:45:00', '2024-04-07 10:00:00', -30.2, -64.9, -30.5, -65.0, 2.2, NULL, 1, 1, 1, 1),
('2024-04-10 15:10:00', '2024-04-10 15:30:00', -29.3, -66.1, -29.6, -66.4, 2.1, NULL, 1, 1, 1, 1),
('2024-04-14 07:20:00', '2024-04-14 07:40:00', -32.7, -62.3, -33.0, -62.6, 2.0, NULL, 1, 1, 1, 1);

-- 11) CAMBIO_ESTADO
-- (8 registros, 2 por cada evento)
INSERT INTO cambio_estado (fecha_hora_desde, fecha_hora_hasta, responsable_inspeccion_id, estado_id, evento_sismico_id) VALUES
-- Evento 1
('2024-04-05 12:30:00', '2024-04-05 14:00:00', 1, 5, 1), -- Ana (1) pone en Pendiente (5)
('2024-04-06 08:00:00', NULL, 2, 1, 1), -- Luis (2) pone en Autodetectado (1) (Estado actual)
-- Evento 2
('2024-04-05 12:30:00', '2024-04-05 14:00:00', 1, 5, 2),
('2024-04-06 08:00:00', NULL, 2, 1, 2),
-- Evento 3
('2024-04-05 12:30:00', '2024-04-05 14:00:00', 1, 5, 3),
('2024-04-06 08:00:00', NULL, 2, 1, 3),
-- Evento 4
('2024-04-05 12:30:00', '2024-04-05 14:00:00', 1, 5, 4),
('2024-04-06 08:00:00', NULL, 2, 1, 4);

-- 12) SERIE_TEMPORAL
-- (8 registros: 2 series por cada uno de los 4 eventos)
-- (IDs 1, 3, 5, 7) son copias de la "Serie 1" del mock, vinculadas al Sismografo 1
-- (IDs 2, 4, 6, 8) son copias de la "Serie 2" del mock, vinculadas al Sismografo 2
INSERT INTO serie_temporal (fecha_hora_inicio_registro, frecuencia_muestreo, fecha_hora_registro, alerta_alarma, evento_sismico_id, sismografo_id) VALUES
-- Evento 1
('2025-02-21 19:05:41', '50', '2025-02-21 19:05:41', false, 1, 1), -- Serie 1 (ID 1)
('2025-03-05 14:30:00', '100', '2025-03-05 14:30:00', false, 1, 2), -- Serie 2 (ID 2)
-- Evento 2
('2025-02-21 19:05:41', '50', '2025-02-21 19:05:41', false, 2, 1), -- Serie 1 (ID 3)
('2025-03-05 14:30:00', '100', '2025-03-05 14:30:00', false, 2, 2), -- Serie 2 (ID 4)
-- Evento 3
('2025-02-21 19:05:41', '50', '2025-02-21 19:05:41', false, 3, 1), -- Serie 1 (ID 5)
('2025-03-05 14:30:00', '100', '2025-03-05 14:30:00', false, 3, 2), -- Serie 2 (ID 6)
-- Evento 4
('2025-02-21 19:05:41', '50', '2025-02-21 19:05:41', false, 4, 1), -- Serie 1 (ID 7)
('2025-03-05 14:30:00', '100', '2025-03-05 14:30:00', false, 4, 2); -- Serie 2 (ID 8)

-- 13) MUESTRA_SISMICA
-- (20 registros: (4 * 3 Muestras de Serie 1) + (4 * 2 Muestras de Serie 2))
INSERT INTO muestra_sismica (fecha_hora_muestra, serie_temporal_id) VALUES
-- Muestras de SerieTemporal 1 (Evento 1)
('2025-02-21 19:05:41', 1), -- Muestra 1
('2025-02-21 19:10:41', 1), -- Muestra 2
('2025-02-21 19:15:41', 1), -- Muestra 3
-- Muestras de SerieTemporal 2 (Evento 1)
('2025-03-05 14:30:00', 2), -- Muestra 4
('2025-03-05 14:35:00', 2), -- Muestra 5
-- Muestras de SerieTemporal 3 (Evento 2)
('2025-02-21 19:05:41', 3), -- Muestra 6
('2025-02-21 19:10:41', 3), -- Muestra 7
('2025-02-21 19:15:41', 3), -- Muestra 8
-- Muestras de SerieTemporal 4 (Evento 2)
('2025-03-05 14:30:00', 4), -- Muestra 9
('2025-03-05 14:35:00', 4), -- Muestra 10
-- Muestras de SerieTemporal 5 (Evento 3)
('2025-02-21 19:05:41', 5), -- Muestra 11
('2025-02-21 19:10:41', 5), -- Muestra 12
('2025-02-21 19:15:41', 5), -- Muestra 13
-- Muestras de SerieTemporal 6 (Evento 3)
('2025-03-05 14:30:00', 6), -- Muestra 14
('2025-03-05 14:35:00', 6), -- Muestra 15
-- Muestras de SerieTemporal 7 (Evento 4)
('2025-02-21 19:05:41', 7), -- Muestra 16
('2025-02-21 19:10:41', 7), -- Muestra 17
('2025-02-21 19:15:41', 7), -- Muestra 18
-- Muestras de SerieTemporal 8 (Evento 4)
('2025-03-05 14:30:00', 8), -- Muestra 19
('2025-03-05 14:35:00', 8); -- Muestra 20

-- 14) DETALLE_MUESTRA_SISMICA
-- (60 registros: 20 muestras * 3 detalles c/u)
-- (Técnicamente el mock tenía detalles diferentes para cada muestra, los replicamos)
INSERT INTO detalle_muestra_sismica (valor, tipo_dato_id, muestra_sismica_id) VALUES
-- Muestra 1 (de Serie 1)
('5', 1, 1), ('10', 2, 1), ('0.7', 3, 1),
-- Muestra 2 (de Serie 1)
('7.02', 1, 2), ('10', 2, 2), ('0.5', 3, 2),
-- Muestra 3 (de Serie 1)
('6.99', 1, 3), ('10.01', 2, 3), ('0.7', 3, 3),
-- Muestra 4 (de Serie 2)
('4.5', 1, 4), ('12', 2, 4), ('0.6', 3, 4),
-- Muestra 5 (de Serie 2)
('4.8', 1, 5), ('11.5', 2, 5), ('0.65', 3, 5),
-- Muestra 6 (de Serie 1)
('5', 1, 6), ('10', 2, 6), ('0.7', 3, 6),
-- Muestra 7 (de Serie 1)
('7.02', 1, 7), ('10', 2, 7), ('0.5', 3, 7),
-- Muestra 8 (de Serie 1)
('6.99', 1, 8), ('10.01', 2, 8), ('0.7', 3, 8),
-- Muestra 9 (de Serie 2)
('4.5', 1, 9), ('12', 2, 9), ('0.6', 3, 9),
-- Muestra 10 (de Serie 2)
('4.8', 1, 10), ('11.5', 2, 10), ('0.65', 3, 10),
-- Muestra 11 (de Serie 1)
('5', 1, 11), ('10', 2, 11), ('0.7', 3, 11),
-- Muestra 12 (de Serie 1)
('7.02', 1, 12), ('10', 2, 12), ('0.5', 3, 12),
-- Muestra 13 (de Serie 1)
('6.99', 1, 13), ('10.01', 2, 13), ('0.7', 3, 13),
-- Muestra 14 (de Serie 2)
('4.5', 1, 14), ('12', 2, 14), ('0.6', 3, 14),
-- Muestra 15 (de Serie 2)
('4.8', 1, 15), ('11.5', 2, 15), ('0.65', 3, 15),
-- Muestra 16 (de Serie 1)
('5', 1, 16), ('10', 2, 16), ('0.7', 3, 16),
-- Muestra 17 (de Serie 1)
('7.02', 1, 17), ('10', 2, 17), ('0.5', 3, 17),
-- Muestra 18 (de Serie 1)
('6.99', 1, 18), ('10.01', 2, 18), ('0.7', 3, 18),
-- Muestra 19 (de Serie 2)
('4.5', 1, 19), ('12', 2, 19), ('0.6', 3, 19),
-- Muestra 20 (de Serie 2)
('4.8', 1, 20), ('11.5', 2, 20), ('0.65', 3, 20);

-- =========================================================
-- REINICIO DE SECUENCIAS
-- (Asegura que los próximos INSERTs manuales no colisionen)
-- =========================================================
SELECT setval('empleado_id_seq', (SELECT MAX(id) FROM empleado));
SELECT setval('estado_id_seq', (SELECT MAX(id) FROM estado));
SELECT setval('clasificacion_sismo_id_seq', (SELECT MAX(id) FROM clasificacion_sismo));
SELECT setval('alcance_sismo_id_seq', (SELECT MAX(id) FROM alcance_sismo));
SELECT setval('origen_de_generacion_id_seq', (SELECT MAX(id) FROM origen_de_generacion));
SELECT setval('tipo_de_dato_id_seq', (SELECT MAX(id) FROM tipo_de_dato));
SELECT setval('estacion_sismologica_id_seq', (SELECT MAX(id) FROM estacion_sismologica));
SELECT setval('sismografo_id_seq', (SELECT MAX(id) FROM sismografo));
SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));
SELECT setval('sesion_id_seq', (SELECT MAX(id) FROM sesion));
SELECT setval('evento_sismico_id_seq', (SELECT MAX(id) FROM evento_sismico));
SELECT setval('cambio_estado_id_seq', (SELECT MAX(id) FROM cambio_estado));
SELECT setval('serie_temporal_id_seq', (SELECT MAX(id) FROM serie_temporal));
SELECT setval('muestra_sismica_id_seq', (SELECT MAX(id) FROM muestra_sismica));
SELECT setval('detalle_muestra_sismica_id_seq', (SELECT MAX(id) FROM detalle_muestra_sismica));