import React, { useEffect, useState } from "react";
import { getEventosPendientes, rechazarEvento } from "../services/eventos.service";
import { EventoCard } from "./EventoCard";
import EventoDetalleModal from "./EventoDetalleModal";

export default function EventoList() {
  const [eventos, setEventos] = useState([]);
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null);
  const [loading, setLoading] = useState(false);

  // Cargar eventos al iniciar
  useEffect(() => {
    cargarEventos();
  }, []);

  const cargarEventos = async () => {
    try {
      const data = await getEventosPendientes();
      setEventos(data || []);
    } catch (error) {
      console.error("Error al cargar eventos:", error);
    }
  };

  // Acción al presionar “Revisar”
  const handleRevisar = (id) => {
    const evento = eventos.find((e) => e.id === id);
    if (evento) {
      setEventoSeleccionado(evento);
    }
  };

  /**
   * Acción al confirmar, rechazar o derivar
   * Recibe el evento completo (ya preparado desde el modal)
   */
  const handleAccion = async (accion, eventoData) => {
    if (!eventoData) return;
    setLoading(true);

    try {
      console.log("Acción:", accion, "Evento:", eventoData);

      if (accion === "rechazar") {
        await rechazarEvento(eventoData);
      } 
      // Si más adelante implementás “confirmar” o “derivar”, 
      // podés agregar más bloques `if` acá.

      await cargarEventos();
      setEventoSeleccionado(null);
    } catch (error) {
      console.error("Error al actualizar evento:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h1 className="text-3xl font-bold mb-6 text-gray-800">
        Registrar resultado de revisión manual
      </h1>

      {eventos.length === 0 ? (
        <p className="text-gray-600">No hay eventos pendientes de revisión.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {eventos.map((evento) => (
            <EventoCard key={evento.id} evento={evento} onRevisar={handleRevisar} />
          ))}
        </div>
      )}

      {/* Modal Detalle */}
      {eventoSeleccionado && (
        <EventoDetalleModal
          evento={eventoSeleccionado}
          onClose={() => setEventoSeleccionado(null)}
          onAccion={handleAccion}
          loading={loading}
        />
      )}
    </div>
  );
}
