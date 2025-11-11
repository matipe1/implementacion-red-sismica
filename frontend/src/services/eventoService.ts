import axios from "axios";
import type { Evento, DetalleEvento } from "../types/types";

const API_BASE = "http://localhost:8080/api/revisiones";

// obtiene la lista de eventos autodetectados
export const getEventosAutodetectados = async (): Promise<Evento[]> => {
  try {
    const response = await axios.get(`${API_BASE}/eventos-autodetectados`);
    const data = response.data;
    // Garantiza siempre un array
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Error al obtener eventos autodetectados:", error);
    return [];
  }
};

// envía el evento seleccionado para obtener el detalle correspondiente
export const getDetalleEvento = async (evento: Evento): Promise<DetalleEvento> => {
    try {
        const payload = {
          fechaHoraOcurrencia: evento.fechaHoraOcurrencia,
          latitudEpicentro: evento.latitudEpicentro,
          longitudEpicentro: evento.longitudEpicentro,
          latitudHipocentro: evento.latitudHipocentro,
          longitudHipocentro: evento.longitudHipocentro,
          valorMagnitud: evento.valorMagnitud,
        };

        const response = await axios.post<DetalleEvento>(
            `${API_BASE}/eventos/seleccionar`,
            payload,
            { headers: { "Content-Type": "application/json" } }
        );
        console.log("POST exitoso, detalle recibido:", response.data);
        return response.data;

    } catch (error) {
        console.error("Error al seleccionar el evento:", error);
        throw error;
    }
};

// envía el evento seleccionado para rechazarlo
export const rechazarEvento = async (evento: Evento): Promise<void> => {
  try {
    const payload = {
      fechaHoraOcurrencia: evento.fechaHoraOcurrencia,
      latitudEpicentro: evento.latitudEpicentro,
      longitudEpicentro: evento.longitudEpicentro,
      latitudHipocentro: evento.latitudHipocentro,
      longitudHipocentro: evento.longitudHipocentro,
      valorMagnitud: evento.valorMagnitud,
    };

    await axios.post(`${API_BASE}/eventos/rechazar`, payload, {
      headers: { "Content-Type": "application/json" },
    });

    console.log("Evento rechazado correctamente");
  } catch (error) {
    console.error("Error al rechazar el evento:", error);
    throw error;
  }
};