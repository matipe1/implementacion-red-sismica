-- =========================================================
-- ESQUEMA BASE SÍSMICO (PostgreSQL)
-- Ordenado para evitar ALTER TABLE
-- =========================================================

-- 1) EMPLEADO
CREATE TABLE IF NOT EXISTS empleado (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    mail VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(30)
);

-- 2) ESTADO
CREATE TABLE IF NOT EXISTS estado (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo_estado VARCHAR(100) NOT NULL  -- columna para discriminar subclases
);

-- 3) CLASIFICACION_SISMO
CREATE TABLE IF NOT EXISTS clasificacion_sismo (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    km_profundidad_desde DOUBLE PRECISION NOT NULL,
    km_profundidad_hasta DOUBLE PRECISION NOT NULL
);

-- 4) ALCANCE_SISMO
CREATE TABLE IF NOT EXISTS alcance_sismo (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- 5) ORIGEN_DE_GENERACION
CREATE TABLE IF NOT EXISTS origen_de_generacion (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- 6) TIPO_DE_DATO
CREATE TABLE IF NOT EXISTS tipo_de_dato (
    id BIGSERIAL PRIMARY KEY,
    denominacion VARCHAR(100) NOT NULL,
    nombre_unidad_medida VARCHAR(100) NOT NULL,
    valor_umbral INTEGER NOT NULL
);

-- 7) ESTACION_SISMOLOGICA
CREATE TABLE IF NOT EXISTS estacion_sismologica (
    id BIGSERIAL PRIMARY KEY,
    codigo_estacion INTEGER NOT NULL UNIQUE
);

-- =========================================================
-- ENTIDADES DEPENDIENTES
-- =========================================================

-- 8) SISMOGRAFO
CREATE TABLE IF NOT EXISTS sismografo (
    id BIGSERIAL PRIMARY KEY,
    nro_serie INTEGER NOT NULL UNIQUE,
    identificador_sismografo INTEGER NOT NULL UNIQUE,
    fecha_adquisicion DATE NOT NULL,
    estacion_sismologica_id BIGINT,
    CONSTRAINT fk_sismografo_estacion FOREIGN KEY (estacion_sismologica_id) REFERENCES estacion_sismologica(id)
);

-- 9) EVENTO_SISMICO
CREATE TABLE IF NOT EXISTS evento_sismico (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_ocurrencia TIMESTAMP NOT NULL,
    fecha_hora_fin TIMESTAMP,
    latitud_epicentro DOUBLE PRECISION NOT NULL,
    longitud_epicentro DOUBLE PRECISION NOT NULL,
    latitud_hipocentro DOUBLE PRECISION NOT NULL,
    longitud_hipocentro DOUBLE PRECISION NOT NULL,
    valor_magnitud DOUBLE PRECISION NOT NULL,
    analista_supervisor_id BIGINT,
    estado_actual_id BIGINT,
    clasificacion_sismo_id BIGINT,
    origen_generacion_id BIGINT,
    alcance_sismo_id BIGINT,
    CONSTRAINT fk_evento_empleado FOREIGN KEY (analista_supervisor_id) REFERENCES empleado(id),
    CONSTRAINT fk_evento_estado FOREIGN KEY (estado_actual_id) REFERENCES estado(id),
    CONSTRAINT fk_evento_clasificacion FOREIGN KEY (clasificacion_sismo_id) REFERENCES clasificacion_sismo(id),
    CONSTRAINT fk_evento_origen FOREIGN KEY (origen_generacion_id) REFERENCES origen_de_generacion(id),
    CONSTRAINT fk_evento_alcance FOREIGN KEY (alcance_sismo_id) REFERENCES alcance_sismo(id)
);

-- 10) CAMBIO_ESTADO
CREATE TABLE IF NOT EXISTS cambio_estado (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_desde TIMESTAMP NOT NULL,
    fecha_hora_hasta TIMESTAMP,
    responsable_inspeccion_id BIGINT,
    estado_id BIGINT,
    evento_sismico_id BIGINT,
    CONSTRAINT fk_cambio_estado_empleado FOREIGN KEY (responsable_inspeccion_id) REFERENCES empleado(id),
    CONSTRAINT fk_cambio_estado_estado FOREIGN KEY (estado_id) REFERENCES estado(id),
    CONSTRAINT fk_cambio_evento FOREIGN KEY (evento_sismico_id) REFERENCES evento_sismico(id) ON DELETE CASCADE
);

-- 11) SERIE_TEMPORAL
CREATE TABLE IF NOT EXISTS serie_temporal (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_inicio_registro TIMESTAMP NOT NULL,
    frecuencia_muestreo VARCHAR(50) NOT NULL,
    fecha_hora_registro TIMESTAMP NOT NULL,
    alerta_alarma BOOLEAN NOT NULL, -- deberia ser not null ?
    evento_sismico_id BIGINT,
    sismografo_id BIGINT,
    CONSTRAINT fk_serie_evento FOREIGN KEY (evento_sismico_id) REFERENCES evento_sismico(id) ON DELETE CASCADE,
    CONSTRAINT fk_serie_sismografo FOREIGN KEY (sismografo_id) REFERENCES sismografo(id)
);

-- 12) MUESTRA_SISMICA
CREATE TABLE IF NOT EXISTS muestra_sismica (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_muestra TIMESTAMP NOT NULL,
    serie_temporal_id BIGINT,
    CONSTRAINT fk_muestra_serie FOREIGN KEY (serie_temporal_id) REFERENCES serie_temporal(id) ON DELETE CASCADE
);

-- 13) DETALLE_MUESTRA_SISMICA
CREATE TABLE IF NOT EXISTS detalle_muestra_sismica (
    id BIGSERIAL PRIMARY KEY,
    valor VARCHAR(255) NOT NULL,
    tipo_dato_id BIGINT,
    muestra_sismica_id BIGINT,
    CONSTRAINT fk_detalle_tipo_dato FOREIGN KEY (tipo_dato_id) REFERENCES tipo_de_dato(id),
    CONSTRAINT fk_detalle_muestra FOREIGN KEY (muestra_sismica_id) REFERENCES muestra_sismica(id) ON DELETE CASCADE
);

-- 14) USUARIO
CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    empleado_id BIGINT,
    CONSTRAINT fk_usuario_empleado FOREIGN KEY (empleado_id) REFERENCES empleado(id)
);

-- 15) SESION
CREATE TABLE IF NOT EXISTS sesion (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    usuario_id BIGINT,
    CONSTRAINT fk_sesion_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);