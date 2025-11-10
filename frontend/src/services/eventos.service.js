// src/services/eventos.service.js
import http from "./http.service";

export async function getEventosPendientes() {
  const res = await http.get("/eventos?estado=pendiente");
  return res.data;
}

export async function getEventoById(id) {
  const res = await http.get(`/eventos/${id}`);
  return res.data;
}

export async function actualizarEvento(id, data) {
  const res = await http.put(`/eventos/${id}`, data);
  return res.data;
}
// src/services/eventos.service.js
import http from "./http.service";

/**
 * 1️⃣ Obtener todos los eventos autodetectados que aún no fueron revisados
 * GET /api/revisiones/eventos-autodetectados
 */
export async function getEventosPendientes() {
  const res = await http.get("/revisiones/eventos-autodetectados");
  return res.data;
}

/**
 * 2️⃣ Enviar el evento seleccionado al backend para obtener su detalle
 *    (series temporales, datos sísmicos, etc.)
 * POST /api/revisiones/eventos/seleccionar
 */
export async function seleccionarEvento(evento) {
  const res = await http.post("/revisiones/eventos/seleccionar", evento);
  return res.data;
}

/**
 * 3️⃣ Rechazar un evento sísmico (actualiza estado, registra fecha y responsable)
 * POST /api/revisiones/eventos/rechazar
 */
export async function rechazarEvento(evento) {
  const res = await http.post("/revisiones/eventos/rechazar", evento);
  return res.data;
}
