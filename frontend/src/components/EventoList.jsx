import React, { useEffect, useState } from "react";
import { getEventosPendientes } from "../services/eventos.service";
import EventoCard from "./EventoCard";
import EventoDetalleModal from "./EventoDetalleModal";

export default function EventoList() {
  const [eventos, setEventos] = useState([]);
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null);
  const [mostrarModal, setMostrarModal] = useState(false);

  useEffect(() => {
    cargarEventos();
  }, []);

  async function cargarEventos() {
    try {
      const data = await getEventosPendientes();
      setEventos(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error("Error al cargar eventos:", error);
    }
  }

  const handleRevisar = (evento) => {
    console.log("Evento seleccionado:", evento);
    setEventoSeleccionado(evento);
    setMostrarModal(true);
  };

  const handleCerrarModal = (refrescar = false) => {
    setMostrarModal(false);
    setEventoSeleccionado(null);
    if (refrescar) cargarEventos();
  };

  return (
    <div className="page-container">
      <div className="content">
        <h1 className="title">Registrar resultado de revisión manual</h1>

        {eventos.length === 0 ? (
          <div className="empty-message">
            <p>No hay eventos pendientes de revisión.</p>
          </div>
        ) : (
          <div className="card-grid">
            {eventos.map((evento, index) => (
              <EventoCard
                key={index}
                evento={evento}
                onRevisar={() => handleRevisar(evento)}
              />
            ))}
          </div>
        )}

        {mostrarModal && eventoSeleccionado && (
          <EventoDetalleModal
            eventoBase={eventoSeleccionado}
            onClose={handleCerrarModal}
          />
        )}
      </div>
    </div>
  );
}
