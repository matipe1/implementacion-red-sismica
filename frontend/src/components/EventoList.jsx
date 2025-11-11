import React, { useEffect, useState } from "react";
import { getEventosPendientes, seleccionarEvento } from "../services/eventos.service";
import EventoDetalleModal from "./EventoDetalleModal";

export default function EventoList() {
  const [eventos, setEventos] = useState([]);
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null);
  const [detalleEvento, setDetalleEvento] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    cargarEventos();
  }, []);

  const cargarEventos = async () => {
    try {
      const data = await getEventosPendientes();
      setEventos(data || []);
    } catch (err) {
      console.error("❌ Error al cargar eventos:", err);
      setError("No se pudieron cargar los eventos desde el servidor.");
    }
  };

  const handleRevisar = async (evento) => {
    setLoading(true);
    try {
      console.log("📍 Evento seleccionado:", evento);
      setEventoSeleccionado(evento);

      // Llamada al backend para obtener detalle
      const detalle = await seleccionarEvento(evento);
      console.log("📦 Detalle recibido:", detalle);
      setDetalleEvento(detalle);
    } catch (err) {
      console.error("❌ Error al obtener detalle:", err);
      setError("No se pudo cargar el detalle del evento sísmico.");
    } finally {
      setLoading(false);
    }
  };

  const handleCerrarModal = () => {
    setEventoSeleccionado(null);
    setDetalleEvento(null);
  };

  if (error) {
    return (
      <div className="error-container">
        <h2>Atención</h2>
        <p>{error}</p>
        <button onClick={() => setError(null)} className="btn-primario">
          Aceptar
        </button>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="content">
        <h1 className="title">🌋 Registrar resultado de revisión manual</h1>

        {eventos.length === 0 ? (
          <div className="empty-message">
            <p>No hay eventos pendientes de revisión.</p>
          </div>
        ) : (
          <div className="card-grid">
            {eventos.map((evento, i) => (
              <div key={i} className="evento-card">
                <h3 className="evento-titulo">🌍 Evento sísmico</h3>

                <p>
                  <b>Fecha:</b>{" "}
                  {new Date(evento.fechaHoraOcurrencia).toLocaleString()}
                </p>
                <p>
                  <b>Magnitud:</b> {evento.valorMagnitud}
                </p>
                <p>
                  <b>Lat. Epicentro:</b> {evento.latitudEpicentro}
                </p>
                <p>
                  <b>Long. Epicentro:</b> {evento.longitudEpicentro}
                </p>
                <p>
                  <b>Lat. Hipocentro:</b> {evento.latitudHipocentro}
                </p>
                <p>
                  <b>Long. Hipocentro:</b> {evento.longitudHipocentro}
                </p>

                <button
                  className="btn-revisar"
                  onClick={() => handleRevisar(evento)}
                >
                  Revisar
                </button>
              </div>
            ))}
          </div>
        )}

        {loading && (
          <div className="modal">
            <div className="modal-content loading">
              <p>Cargando datos del evento sísmico...</p>
            </div>
          </div>
        )}

        {eventoSeleccionado && detalleEvento && (
          <EventoDetalleModal
            evento={eventoSeleccionado}
            detalle={detalleEvento}
            onClose={handleCerrarModal}
          />
        )}
      </div>
    </div>
  );
}
