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
  // 🧩 Convertimos el formato al que espera el backend
  const body = {
    fechaHoraOcurrencia: formatearFecha(evento.fechaHoraOcurrencia),
    latitudEpicentro: parseFloat(evento.latitudEpicentro),
    longitudEpicentro: parseFloat(evento.longitudEpicentro),
    latitudHipocentro: parseFloat(evento.latitudHipocentro),
    longitudHipocentro: parseFloat(evento.longitudHipocentro),
    valorMagnitud: parseFloat(evento.valorMagnitud),
  };

  console.log("🛰 Enviando evento al backend:", body);

  const res = await http.post("/revisiones/eventos/seleccionar", body);
  return res.data;
}

/**
 * 3️⃣ Rechazar un evento sísmico
 * POST /api/revisiones/eventos/rechazar
 */
export async function rechazarEvento(evento) {
  const body = {
    fechaHoraOcurrencia: formatearFecha(evento.fechaHoraOcurrencia),
    latitudEpicentro: parseFloat(evento.latitudEpicentro),
    longitudEpicentro: parseFloat(evento.longitudEpicentro),
    latitudHipocentro: parseFloat(evento.latitudHipocentro),
    longitudHipocentro: parseFloat(evento.longitudHipocentro),
    valorMagnitud: parseFloat(evento.valorMagnitud),
  };

  console.log("❌ Rechazando evento:", body);

  const res = await http.post("/revisiones/eventos/rechazar", body);
  return res.data;
}

/**
 * 🔧 Función auxiliar: formatear fecha tipo '2024-04-05 12:30:00'
 */
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
