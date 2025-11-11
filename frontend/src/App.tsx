import TableEvento from "./componets/TableEvento";
import EventoDetalle from "./componets/EventoDetalle";
import { useState, useEffect } from "react";
import type { Evento, DetalleEvento } from "./types/types";
import * as eventoService from './services/eventoService';
import { Routes, Route, useNavigate } from "react-router-dom";
import Inicio from "./componets/Inicio";

export default function App() {
  const [eventos, setEventos] = useState<Evento[]>([]);
  const [eventoSeleccionado, setEventoSeleccionado] = useState<Evento | null>(null);
  const [detalleEvento, setDetalleEvento] = useState<DetalleEvento | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const navigate = useNavigate();

  // Obtiene la lista de eventos autodetectados al cargar el componente
  useEffect(() => {
    const fetchEventos = async () => {
      try {
        const data = await eventoService.getEventosAutodetectados();
        setEventos(data);
        console.log("Eventos actualizados:", data);
      } catch (error) {
        console.error("Error al obtener eventos autodetectados:", error);
      }
    };

    fetchEventos();
  }, []);

  const handleConfirmarSeleccion = async () => {
  if (!eventoSeleccionado) {
    alert("Por favor, seleccione un evento primero.");
    return;
  }
  try {
    setIsLoading(true);
    setDetalleEvento(null); // Limpia detalle previo

    // Permitir que se muestre "Enviando..."
    await new Promise((r) => setTimeout(r, 500));

    const data = await eventoService.getDetalleEvento(eventoSeleccionado);
    setDetalleEvento(data);

    // Navega y recarga lista
    navigate("/evento/seleccionado");
    eventoService.getEventosAutodetectados().then(setEventos);

  } catch (error) {
    alert("No se pudo obtener el detalle del evento sismico.");
    console.error("Error al obtener el detalle del evento sismico:", error);
  } finally {
    setIsLoading(false);
  }
};

  const handleRechazar = async () => {
    if (!eventoSeleccionado) return;
    try {
      setIsLoading(true);

      // Permitir que se muestre "Enviando..."
      await new Promise((r) => setTimeout(r, 500));

      await eventoService.rechazarEvento(eventoSeleccionado);
      alert("Evento rechazado correctamente.");

      // Recarga lista y navega
      eventoService.getEventosAutodetectados().then(setEventos);
      navigate("/eventos-autodetectados");

    } catch (error) {
      alert("No se pudo rechazar el evento.");
      console.error("Error al rechazar evento:", error);
    } finally {
      setIsLoading(false);
    }
  };
  
  return (
    <>
    <Routes>
      <Route path="/" element={<Inicio />} />

      <Route 
        path="/eventos-autodetectados"
        element={
          <div style={{ padding: '40px 20px' }}>
            <h1 className="title">Eventos Sísmicos Autodetectados</h1>
            <TableEvento
              eventos={eventos}
              eventoSeleccionado={eventoSeleccionado}
              setEventoSeleccionado={setEventoSeleccionado}
              onConfirmarSeleccion={handleConfirmarSeleccion}
              isLoading={isLoading}
            />
          </div>
        }
      />
      <Route 
        path="/evento/seleccionado"
        element={
          <div style={{ padding: '40px 20px' }}>
            <EventoDetalle
              evento={eventoSeleccionado}
              detalleEvento={detalleEvento}
              isRejecting={isLoading}
              onRechazar={handleRechazar}
            />
          </div>
        }
      />
    </Routes>
    </>
  );
}