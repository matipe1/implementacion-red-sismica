// src/components/RegistrarRevision.jsx
import { useEffect, useState } from "react";
import { getEventosPendientes, actualizarEvento } from "../services/eventos.service";
import EventoDetalleModal from "./EventoDetalleModal";

export default function RegistrarRevision() {
  const [eventos, setEventos] = useState([]);
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function cargarEventos() {
      const data = await getEventosPendientes();
      setEventos(data);
    }
    cargarEventos();
  }, []);

  async function manejarAccion(accion, observacion = "") {
    if (!eventoSeleccionado) return;
    setLoading(true);
    await actualizarEvento(eventoSeleccionado.id, { accion, observacion });
    setLoading(false);
    setEventoSeleccionado(null);
    const data = await getEventosPendientes();
    setEventos(data);
  }

  return (
    <div className="p-4">
      <h2>Registrar resultado de revisión manual</h2>
      <table className="tabla-eventos">
        <thead>
          <tr>
            <th>Fecha</th>
            <th>Magnitud</th>
            <th>Ubicación</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          {eventos.map(e => (
            <tr key={e.id}>
              <td>{new Date(e.fecha).toLocaleString()}</td>
              <td>{e.magnitud}</td>
              <td>{e.ubicacion}</td>
              <td>
                <button onClick={() => setEventoSeleccionado(e)}>Revisar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {eventoSeleccionado && (
        <EventoDetalleModal
          evento={eventoSeleccionado}
          onClose={() => setEventoSeleccionado(null)}
          onAccion={manejarAccion}
          loading={loading}
        />
      )}
    </div>
  );
}
