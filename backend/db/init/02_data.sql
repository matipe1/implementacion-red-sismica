-- =========================================================
-- DATOS DE BASE SÍSMICO (PostgreSQL)
-- =========================================================

SET search_path TO public;

-- =========================================================
-- 1) EMPLEADOS
-- =========================================================
INSERT INTO empleado (mail, nombre, apellido, telefono) VALUES
('anagomez@gmail.com', 'Ana', 'Gomez', '+543511234567'),
('luispaz@gmail.com', 'Luis', 'Paz', '+543511234567'),
('juanperez@gmail.com', 'Juan', 'Perez', '+543511234567'),
('maravilla@gmail.com', 'Maravilla', 'Martinez', '+543511234567');

-- =====================================================
-- ESTADOS CREADOS DINÁMICAMENTE (como si la app los hubiera generado)
-- =====================================================
-- Estados iniciales (autodetectado) para cada evento
INSERT INTO autodetectado (id, nombre) VALUES
(nextval('estado_seq'), 'Autodetectado'),
(nextval('estado_seq'), 'Autodetectado'),
(nextval('estado_seq'), 'Autodetectado'),
(nextval('estado_seq'), 'Autodetectado');

-- Estado adicional para el evento 4 (bloqueado en revisión)
INSERT INTO bloqueado_en_revision (id, nombre) VALUES
(nextval('estado_seq'), 'Bloqueado en revisión');

-- =========================================================
-- 3) CLASIFICACIÓN, ALCANCE Y ORIGEN
-- =========================================================
INSERT INTO clasificacion_sismo (nombre, km_profundidad_desde, km_profundidad_hasta)
VALUES ('Superficial', 3.0, 6.0);

INSERT INTO alcance_sismo (nombre, descripcion)
VALUES ('Local', 'Zona de influencia reducida');

INSERT INTO origen_de_generacion (nombre, descripcion)
VALUES ('Interplaca', 'Movimiento entre placas tectónicas');

-- =========================================================
-- 4) TIPOS DE DATO
-- =========================================================
INSERT INTO tipo_de_dato (denominacion, nombre_unidad_medida, valor_umbral) VALUES
('Velocidad de onda', 'Km/s', 5.0),
('Frecuencia de onda', 'Hz', 12.0),
('Longitud de onda', 'km/ciclo', 1.0);

-- =========================================================
-- 5) ESTACIONES Y SISMÓGRAFOS
-- =========================================================
INSERT INTO estacion_sismologica (codigo_estacion) VALUES
(100), (200);

INSERT INTO sismografo (nro_serie, identificador_sismografo, fecha_adquisicion, estacion_sismologica_codigo) VALUES
(1001, 501, '2023-03-15', 100),
(1002, 502, '2023-04-20', 200);

-- =========================================================
-- 6) USUARIO Y SESIÓN (para simular empleado logueado)
-- =========================================================
INSERT INTO usuario (nombre_usuario, contrasena, empleado_mail)
VALUES ('admin', 'admin123', 'maravilla@gmail.com');

INSERT INTO sesion (fecha_hora, usuario_id)
VALUES (NOW(), 1);
-- =========================================================
-- 7) EVENTOS SÍSMICOS
-- =========================================================
INSERT INTO evento_sismico (
    fecha_hora_ocurrencia, fecha_hora_fin,
    latitud_epicentro, longitud_epicentro, latitud_hipocentro, longitud_hipocentro,
    valor_magnitud, analista_supervisor_mail, estado_actual_id,
    clasificacion_sismo_nombre, origen_generacion_nombre, alcance_sismo_nombre
) VALUES
-- E1, E2, E3 en autodetectado con un solo cambio de estado
('2024-04-05 12:30:00', '2024-04-05 12:45:00', -26.8, -65.2, -27.1, -65.5, 3.6, NULL, 1, 'Superficial', 'Interplaca', 'Local'),
('2024-04-07 09:45:00', '2024-04-07 10:00:00', -30.2, -64.9, -30.5, -65.0, 2.2, NULL, 2, 'Superficial', 'Interplaca', 'Local'),
('2024-04-10 15:10:00', '2024-04-10 15:30:00', -29.3, -66.1, -29.6, -66.4, 2.1, NULL, 3, 'Superficial', 'Interplaca', 'Local'),
-- E4 en bloqueado en revision con dos cambios de estado
('2024-04-14 07:20:00', '2024-04-14 07:40:00', -32.7, -62.3, -33.0, -62.6, 2.0, 'luispaz@gmail.com', 5, 'Superficial', 'Interplaca', 'Local');

-- =========================================================
-- 8) CAMBIOS DE ESTADO
-- =========================================================
INSERT INTO cambio_estado (fecha_hora_desde, fecha_hora_hasta, responsable_inspeccion_mail, estado_id, evento_sismico_id) VALUES
-- E1, E2 y E3 en autodetectado con un solo cambio
('2024-04-05 12:30:00', NULL, NULL, 1, 1),
('2024-04-07 09:45:00', NULL, NULL, 2, 2),
('2024-04-10 15:10:00', NULL, NULL, 3, 3),
-- E4 en bloqueado en revision con dos cambios (autodetectado → bloqueado en revisión)
('2024-04-14 07:20:00', '2024-04-14 08:00:00', NULL, 4, 4),
('2024-04-14 08:00:00', NULL, NULL, 5, 4);

-- =========================================================
-- 9) SERIES TEMPORALES, MUESTRAS
-- =========================================================
INSERT INTO serie_temporal (fecha_hora_inicio_registro, frecuencia_muestreo, fecha_hora_registro, alerta_alarma, evento_sismico_id, sismografo_id) VALUES
-- Evento 1
('2025-02-21 19:05:41', 50.00, '2025-02-21 19:05:41', false, 1, 1),
('2025-02-21 19:05:41', 100.00, '2025-02-21 19:05:41', false, 1, 2),
-- Evento 2
('2025-02-22 10:00:00', 50.00, '2025-02-22 10:00:00', false, 2, 1),
('2025-02-22 10:00:00', 100.00, '2025-02-22 10:00:00', false, 2, 2),
-- Evento 3
('2025-02-21 19:05:41', 50.00, '2025-02-21 19:05:41', false, 3, 1),
('2025-03-05 14:30:00', 100.00, '2025-03-05 14:30:00', false, 3, 2),
-- Evento 4
('2025-02-21 19:05:41', 50.00, '2025-02-21 19:05:41', false, 4, 1),
('2025-03-05 14:30:00', 100.00, '2025-03-05 14:30:00', false, 4, 2);


INSERT INTO muestra_sismica (fecha_hora_muestra, serie_temporal_id) VALUES
-- Muestras de SerieTemporal 1
('2025-02-21 19:05:41', 1),
('2025-02-21 19:10:41', 1),
('2025-02-21 19:15:41', 1),

-- Muestras de SerieTemporal 2
('2025-02-21 19:05:41', 2),
('2025-02-21 19:10:41', 2),
('2025-02-21 19:15:41', 2),

-- Muestras de SerieTemporal 3
('2025-02-22 10:00:00', 3),
('2025-02-22 10:05:00', 3),
('2025-02-22 10:10:00', 3),

-- Muestras de SerieTemporal 4
('2025-02-22 10:00:00', 4),
('2025-02-22 10:05:00', 4),
('2025-02-22 10:10:00', 4),

-- Muestras de SerieTemporal 5
('2025-02-21 19:05:41', 5),
('2025-02-21 19:10:41', 5),
('2025-02-21 19:15:41', 5),

-- Muestras de SerieTemporal 6
('2025-03-05 14:30:00', 6),
('2025-03-05 14:35:00', 6),
('2025-03-05 14:45:00', 6),

-- Muestras de SerieTemporal 7
('2025-02-21 19:05:41', 7),
('2025-02-21 19:10:41', 7),
('2025-02-21 19:15:41', 7),

-- Muestras de SerieTemporal 8
('2025-03-05 14:30:00', 8),
('2025-03-05 14:35:00', 8),
('2025-03-05 14:40:00', 8);

-- =========================================================
-- 10) DETALLE_MUESTRA_SISMICA
-- (72 registros: 24 muestras * 3 detalles c/u)
-- =========================================================
INSERT INTO detalle_muestra_sismica (valor, tipo_dato_denominacion, muestra_sismica_id) VALUES
-- Serie 1 (Evento 1 - Estación 100)
('5.0', 'Velocidad de onda', 1), ('10.0', 'Frecuencia de onda', 1), ('0.7', 'Longitud de onda', 1),
('5.3', 'Velocidad de onda', 2), ('10.1', 'Frecuencia de onda', 2), ('0.6', 'Longitud de onda', 2),
('5.5', 'Velocidad de onda', 3), ('10.2', 'Frecuencia de onda', 3), ('0.65', 'Longitud de onda', 3),

-- Serie 2 (Evento 1 - Estación 200)
('4.8', 'Velocidad de onda', 4), ('12.0', 'Frecuencia de onda', 4), ('0.6', 'Longitud de onda', 4),
('4.9', 'Velocidad de onda', 5), ('11.8', 'Frecuencia de onda', 5), ('0.62', 'Longitud de onda', 5),
('5.1', 'Velocidad de onda', 6), ('11.6', 'Frecuencia de onda', 6), ('0.64', 'Longitud de onda', 6),

-- Serie 3 (Evento 2 - Estación 100)
('6.0', 'Velocidad de onda', 7), ('13.0', 'Frecuencia de onda', 7), ('0.75', 'Longitud de onda', 7),
('6.2', 'Velocidad de onda', 8), ('12.9', 'Frecuencia de onda', 8), ('0.70', 'Longitud de onda', 8),
('6.3', 'Velocidad de onda', 9), ('12.7', 'Frecuencia de onda', 9), ('0.72', 'Longitud de onda', 9),

-- Serie 4 (Evento 2 - Estación 200)
('4.7', 'Velocidad de onda', 10), ('11.9', 'Frecuencia de onda', 10), ('0.66', 'Longitud de onda', 10),
('4.9', 'Velocidad de onda', 11), ('11.7', 'Frecuencia de onda', 11), ('0.68', 'Longitud de onda', 11),
('5.0', 'Velocidad de onda', 12), ('11.6', 'Frecuencia de onda', 12), ('0.65', 'Longitud de onda', 12),

-- Serie 5 (Evento 3 - Estación 100)
('7.0', 'Velocidad de onda', 13), ('13.5', 'Frecuencia de onda', 13), ('0.8', 'Longitud de onda', 13),
('7.1', 'Velocidad de onda', 14), ('13.2', 'Frecuencia de onda', 14), ('0.82', 'Longitud de onda', 14),
('7.3', 'Velocidad de onda', 15), ('13.0', 'Frecuencia de onda', 15), ('0.85', 'Longitud de onda', 15),

-- Serie 6 (Evento 3 - Estación 200)
('6.8', 'Velocidad de onda', 16), ('12.8', 'Frecuencia de onda', 16), ('0.78', 'Longitud de onda', 16),
('6.9', 'Velocidad de onda', 17), ('12.9', 'Frecuencia de onda', 17), ('0.76', 'Longitud de onda', 17),
('7.0', 'Velocidad de onda', 18), ('13.1', 'Frecuencia de onda', 18), ('0.79', 'Longitud de onda', 18),

-- Serie 7 (Evento 4 - Estación 100)
('4.4', 'Velocidad de onda', 19), ('10.9', 'Frecuencia de onda', 19), ('0.55', 'Longitud de onda', 19),
('4.5', 'Velocidad de onda', 20), ('11.0', 'Frecuencia de onda', 20), ('0.57', 'Longitud de onda', 20),
('4.6', 'Velocidad de onda', 21), ('11.1', 'Frecuencia de onda', 21), ('0.58', 'Longitud de onda', 21),

-- Serie 8 (Evento 4 - Estación 200)
('4.3', 'Velocidad de onda', 22), ('10.8', 'Frecuencia de onda', 22), ('0.52', 'Longitud de onda', 22),
('4.5', 'Velocidad de onda', 23), ('11.0', 'Frecuencia de onda', 23), ('0.54', 'Longitud de onda', 23),
('4.7', 'Velocidad de onda', 24), ('11.2', 'Frecuencia de onda', 24), ('0.56', 'Longitud de onda', 24);

-- =========================================================
-- 11) REINICIO DE SECUENCIAS
-- =========================================================
SELECT setval('estado_seq', (SELECT MAX(id) FROM (
  SELECT MAX(id) FROM autodetectado
  UNION ALL
  SELECT MAX(id) FROM bloqueado_en_revision
  UNION ALL
  SELECT MAX(id) FROM pendiente
  UNION ALL
  SELECT MAX(id) FROM pendiente_revision
  UNION ALL
  SELECT MAX(id) FROM rechazado
  UNION ALL
  SELECT MAX(id) FROM autoconfirmado
) t));
SELECT setval('estado_seq', (SELECT MAX(id) FROM autodetectado));
SELECT setval('sismografo_id_seq', (SELECT MAX(id) FROM sismografo));
SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));
SELECT setval('sesion_id_seq', (SELECT MAX(id) FROM sesion));
SELECT setval('evento_sismico_id_seq', (SELECT MAX(id) FROM evento_sismico));
SELECT setval('cambio_estado_id_seq', (SELECT MAX(id) FROM cambio_estado));
SELECT setval('serie_temporal_id_seq', (SELECT MAX(id) FROM serie_temporal));
SELECT setval('muestra_sismica_id_seq', (SELECT MAX(id) FROM muestra_sismica));
SELECT setval('detalle_muestra_sismica_id_seq', (SELECT MAX(id) FROM detalle_muestra_sismica));