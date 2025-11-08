-- =========================================================
-- ESQUEMA BASE SÍSMICO (PostgreSQL)
-- Ordenado para evitar ALTER TABLE
-- =========================================================

-- 1) EMPLEADO
CREATE TABLE IF NOT EXISTS empleado (
    mail VARCHAR(150) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(30)
);

-- 2) ESTADOS
CREATE SEQUENCE estado_seq START 1 INCREMENT 1;

-- Estados concretos (todas con id + nombre)
CREATE TABLE pendiente_revision (
  id BIGINT PRIMARY KEY DEFAULT nextval('estado_seq'),
  nombre VARCHAR(60) NOT NULL
);

CREATE TABLE pendiente (
  id BIGINT PRIMARY KEY DEFAULT nextval('estado_seq'),
  nombre VARCHAR(60) NOT NULL
);

CREATE TABLE rechazado (
  id BIGINT PRIMARY KEY DEFAULT nextval('estado_seq'),
  nombre VARCHAR(60) NOT NULL
);

CREATE TABLE bloqueado_en_revision (
  id BIGINT PRIMARY KEY DEFAULT nextval('estado_seq'),
  nombre VARCHAR(60) NOT NULL
);

CREATE TABLE autoconfirmado (
  id BIGINT PRIMARY KEY DEFAULT nextval('estado_seq'),
  nombre VARCHAR(60) NOT NULL
);

CREATE TABLE autodetectado (
  id BIGINT PRIMARY KEY DEFAULT nextval('estado_seq'),
  nombre VARCHAR(60) NOT NULL
);

-- 3) CLASIFICACION_SISMO
CREATE TABLE IF NOT EXISTS clasificacion_sismo (
    nombre VARCHAR(100) PRIMARY KEY,
    km_profundidad_desde DOUBLE PRECISION NOT NULL,
    km_profundidad_hasta DOUBLE PRECISION NOT NULL
);

-- 4) ALCANCE_SISMO
CREATE TABLE IF NOT EXISTS alcance_sismo (
    nombre VARCHAR(100) PRIMARY KEY,
    descripcion VARCHAR(255)
);

-- 5) ORIGEN_DE_GENERACION
CREATE TABLE IF NOT EXISTS origen_de_generacion (
    nombre VARCHAR(100) PRIMARY KEY,
    descripcion VARCHAR(255)
);

-- 6) TIPO_DE_DATO
CREATE TABLE IF NOT EXISTS tipo_de_dato (
    denominacion VARCHAR(100) PRIMARY KEY,
    nombre_unidad_medida VARCHAR(100) NOT NULL,
    valor_umbral NUMERIC(4, 2) NOT NULL
);

-- 7) ESTACION_SISMOLOGICA
CREATE TABLE IF NOT EXISTS estacion_sismologica (
    codigo_estacion INTEGER PRIMARY KEY
);

-- =========================================================
-- ENTIDADES DEPENDIENTES
-- =========================================================

-- 8) SISMOGRAFO
CREATE TABLE IF NOT EXISTS sismografo (
    nro_serie INTEGER NOT NULL UNIQUE,
    identificador_sismografo INTEGER PRIMARY KEY,
    fecha_adquisicion DATE NOT NULL,
    estacion_sismologica_codigo INTEGER,
    CONSTRAINT fk_sismografo_estacion FOREIGN KEY (estacion_sismologica_codigo) REFERENCES estacion_sismologica(codigo_estacion)
);

-- 9) EVENTO_SISMICO
CREATE TABLE IF NOT EXISTS evento_sismico (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_ocurrencia TIMESTAMP NOT NULL,
    fecha_hora_fin TIMESTAMP,
    latitud_epicentro NUMERIC(9, 6) NOT NULL,
    longitud_epicentro NUMERIC(9, 6) NOT NULL,
    latitud_hipocentro NUMERIC(9, 6) NOT NULL,
    longitud_hipocentro NUMERIC(9, 6) NOT NULL,
    valor_magnitud NUMERIC(4, 2) NOT NULL,
    analista_supervisor_mail VARCHAR(150),
    estado_actual_id BIGINT,
    clasificacion_sismo_nombre VARCHAR(100),
    origen_generacion_nombre VARCHAR(100),
    alcance_sismo_nombre VARCHAR(100),
    CONSTRAINT fk_evento_empleado FOREIGN KEY (analista_supervisor_mail) REFERENCES empleado(mail),
    CONSTRAINT fk_evento_clasificacion FOREIGN KEY (clasificacion_sismo_nombre) REFERENCES clasificacion_sismo(nombre),
    CONSTRAINT fk_evento_origen FOREIGN KEY (origen_generacion_nombre) REFERENCES origen_de_generacion(nombre),
    CONSTRAINT fk_evento_alcance FOREIGN KEY (alcance_sismo_nombre) REFERENCES alcance_sismo(nombre)
);

-- 10) CAMBIO_ESTADO
CREATE TABLE IF NOT EXISTS cambio_estado (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_desde TIMESTAMP NOT NULL,
    fecha_hora_hasta TIMESTAMP,
    responsable_inspeccion_mail VARCHAR(150),
    estado_id BIGINT,
    evento_sismico_id BIGINT,
    CONSTRAINT fk_cambio_estado_empleado FOREIGN KEY (responsable_inspeccion_mail) REFERENCES empleado(mail),
    CONSTRAINT fk_cambio_evento FOREIGN KEY (evento_sismico_id) REFERENCES evento_sismico(id) ON DELETE CASCADE
);

-- 11) SERIE_TEMPORAL
CREATE TABLE IF NOT EXISTS serie_temporal (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_inicio_registro TIMESTAMP NOT NULL,
    frecuencia_muestreo NUMERIC(6, 2) NOT NULL,
    fecha_hora_registro TIMESTAMP NOT NULL,
    alerta_alarma BOOLEAN NOT NULL,
    evento_sismico_id BIGINT,
    sismografo_identificador INTEGER,
    CONSTRAINT fk_serie_evento FOREIGN KEY (evento_sismico_id) REFERENCES evento_sismico(id) ON DELETE CASCADE,
    CONSTRAINT fk_serie_sismografo FOREIGN KEY (sismografo_identificador) REFERENCES sismografo(identificador_sismografo)
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
    tipo_dato_denominacion VARCHAR(100),
    muestra_sismica_id BIGINT,
    CONSTRAINT fk_detalle_tipo_dato FOREIGN KEY (tipo_dato_denominacion) REFERENCES tipo_de_dato(denominacion),
    CONSTRAINT fk_detalle_muestra FOREIGN KEY (muestra_sismica_id) REFERENCES muestra_sismica(id) ON DELETE CASCADE
);

-- 14) USUARIO
CREATE TABLE IF NOT EXISTS usuario (
    nombre_usuario VARCHAR(100) PRIMARY KEY,
    contrasena VARCHAR(255) NOT NULL,
    empleado_mail VARCHAR(150),
    CONSTRAINT fk_usuario_empleado FOREIGN KEY (empleado_mail) REFERENCES empleado(mail)
);

-- 15) SESION
CREATE TABLE IF NOT EXISTS sesion (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    usuario_nombre VARCHAR(100),
    CONSTRAINT fk_sesion_usuario FOREIGN KEY (usuario_nombre) REFERENCES usuario(nombre_usuario)
);