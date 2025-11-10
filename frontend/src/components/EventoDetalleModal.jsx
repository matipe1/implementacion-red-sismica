import { useState, useEffect } from "react";
import { seleccionarEvento } from "../services/eventos.service";
import modalService from "../services/modalDialog.service";

export default function EventoDetalleModal({ evento, onClose, onAccion, loading }) {
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null);
  const [observacion, setObservacion] = useState("");

  // Guarda el evento recibido localmente al abrir el modal
  useEffect(() => {
    if (evento) {
      setEventoSeleccionado(evento);
    }
  }, [evento]);

  // Llama al backend cuando se selecciona un evento
  const obtenerDatosSismicos = async () => {
    if (!eventoSeleccionado) return;
    try {
      modalService.BloquearPantalla?.(true);
      console.log("➡️ Enviando evento al backend:", eventoSeleccionado);

      // Llamada al endpoint correcto
      const res = await seleccionarEvento(eventoSeleccionado);

      console.log("✅ Datos sísmicos recibidos:", res);
      modalService.Alert?.("Datos sísmicos cargados correctamente.", "Éxito", "Aceptar", "", null, null, "success");
    } catch (error) {
      console.error("❌ Error al obtener datos sísmicos:", error);
      modalService.Alert?.(
        "No se pudieron cargar los datos sísmicos. Intente nuevamente.",
        "Error",
        "Aceptar",
        "",
        null,
        null,
        "error"
      );
    } finally {
      modalService.BloquearPantalla?.(false);
    }
  };

  // Se ejecuta automáticamente al montar el modal (pasos 2–5 del CU)
  useEffect(() => {
    if (eventoSeleccionado) obtenerDatosSismicos();
  }, [eventoSeleccionado]);

  // Manejo de acciones finales (confirmar / rechazar / derivar)
  const manejarAccion = async (accion) => {
    if (!eventoSeleccionado) return;

    const payload = {
      ...eventoSeleccionado,
      observacion,
      accion,
    };

    console.log("🚀 Enviando acción:", payload);
    try {
      modalService.BloquearPantalla?.(true);
      await onAccion(accion, payload);

      // Mensajes personalizados según la acción
      if (accion === "rechazar") {
        modalService.Alert?.(
          "El evento fue rechazado correctamente.",
          "Revisión completada",
          "Aceptar",
          "",
          null,
          null,
          "success"
        );
      } else if (accion === "confirmar") {
        modalService.Alert?.(
          "El evento fue confirmado correctamente.",
          "Revisión completada",
          "Aceptar",
          "",
          null,
          null,
          "success"
        );
      } else if (accion === "derivar") {
        modalService.Alert?.(
          "El evento fue derivado a un experto.",
          "Revisión completada",
          "Aceptar",
          "",
          null,
          null,
          "info"
        );
      }
    } catch (error) {
      console.error("❌ Error al procesar acción:", error);
      modalService.Alert?.(
        "Hubo un error al procesar la acción. Intente nuevamente.",
        "Error",
        "Aceptar",
        "",
        null,
        null,
        "error"
      );
    } finally {
      modalService.BloquearPantalla?.(false);
    }
  };

  return (
    <div className="modal">
      <div className="modal-content max-w-2xl">
        {/* --- Título --- */}
        <h3 className="text-2xl font-semibold text-gray-800 mb-4 border-b pb-2">
          Detalle del Evento Sísmico
        </h3>

        {/* --- Datos del evento --- */}
        <div className="grid grid-cols-2 gap-3 text-gray-700 mb-4">
          <p><strong>Fecha:</strong> {new Date(evento.fecha).toLocaleString()}</p>
          <p><strong>Magnitud:</strong> {evento.magnitud}</p>
          <p><strong>Ubicación:</strong> {evento.ubicacion}</p>
          <p><strong>Alcance:</strong> {evento.alcance}</p>
          <p><strong>Clasificación:</strong> {evento.clasificacion ?? "No especificada"}</p>
          <p><strong>Origen:</strong> {evento.origen ?? "Desconocido"}</p>
        </div>

        {/* --- Botón sin funcionalidad --- */}
        <div className="flex justify-end mb-4">
          <button className="btn-secondary" disabled>
            Ver Estaciones Sismológicas (sin funcionalidad)
          </button>
        </div>

        {/* --- Observaciones --- */}
        <div className="mt-5">
          <label className="block text-gray-700 font-semibold mb-1">
            Observaciones:
          </label>
          <textarea
            placeholder="Escriba observaciones de la revisión..."
            value={observacion}
            onChange={(e) => setObservacion(e.target.value)}
            className="border border-gray-300 rounded-lg w-full p-2 focus:ring-2 focus:ring-blue-400 outline-none"
            rows={3}
          />
        </div>

        {/* --- Acciones --- */}
        <div className="flex justify-end gap-3 mt-6">
          <button
            onClick={() => manejarAccion("confirmar")}
            disabled={loading}
            className="btn-primary"
          >
            Confirmar
          </button>

          <button
            onClick={() => manejarAccion("rechazar")}
            disabled={loading}
            className="btn-danger"
          >
            Rechazar
          </button>

          <button
            onClick={() => manejarAccion("derivar")}
            disabled={loading}
            className="btn-secondary"
          >
            Solicitar revisión a experto
          </button>

          <button onClick={onClose} className="btn-secondary">
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
}
