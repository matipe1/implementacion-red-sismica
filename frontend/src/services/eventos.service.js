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
 * POST /api/revisiones/eventos/seleccionar
 */
export async function seleccionarEvento(evento) {
  const body = normalizarEventoParaBack(evento);
  console.log("🛰 Enviando evento al backend:", body);
  const res = await http.post("/revisiones/eventos/seleccionar", body, {
    headers: { "Content-Type": "application/json" },
  });
  return res.data;
}

/**
 * 3️⃣ Rechazar un evento sísmico
 * POST /api/revisiones/eventos/rechazar
 */
export async function rechazarEvento(evento) {
  const body = normalizarEventoParaBack(evento);
  console.log("❌ Rechazando evento:", body);
  const res = await http.post("/revisiones/eventos/rechazar", body, {
    headers: { "Content-Type": "application/json" },
  });
  return res.data;
}

/* ---------- helpers ---------- */

function normalizarEventoParaBack(e) {
  return {
    fechaHoraOcurrencia: formatearFecha(e.fechaHoraOcurrencia),
    latitudEpicentro: parseFloat(e.latitudEpicentro),
    longitudEpicentro: parseFloat(e.longitudEpicentro),
    latitudHipocentro: parseFloat(e.latitudHipocentro),
    longitudHipocentro: parseFloat(e.longitudHipocentro),
    valorMagnitud: parseFloat(e.valorMagnitud),
  };
}

/** 🔧 '2024-04-05 12:30:00' */
function formatearFecha(fecha) {
  if (!fecha) return null;
  try {
    const d = new Date(fecha);
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, "0");
    const dd = String(d.getDate()).padStart(2, "0");
    const hh = String(d.getHours()).padStart(2, "0");
    const mi = String(d.getMinutes()).padStart(2, "0");
    const ss = String(d.getSeconds()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`;
  } catch {
    return fecha;
  }
}
