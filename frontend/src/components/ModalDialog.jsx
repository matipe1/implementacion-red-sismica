// src/components/ModalDialog.jsx
import React, { useState, useEffect, useCallback } from "react";
import modalDialogService from "../services/modalDialog.service";

export default function ModalDialog() {
  const [visible, setVisible] = useState(false);
  const [mensaje, setMensaje] = useState("");
  const [titulo, setTitulo] = useState("");
  const [boton1, setBoton1] = useState("");
  const [boton2, setBoton2] = useState("");
  const [accionBoton1, setAccionBoton1] = useState(null);
  const [accionBoton2, setAccionBoton2] = useState(null);
  const [tipo, setTipo] = useState("info");
  const [esBloqueo, setEsBloqueo] = useState(false);

  const show = useCallback((
    _mensaje = "",
    _titulo = "",
    _boton1 = "",
    _boton2 = "",
    _accionBoton1 = null,
    _accionBoton2 = null,
    _tipo = "info"
  ) => {
    // cerrar
    if (!_mensaje && !_titulo) {
      setVisible(false);
      setEsBloqueo(false);
      return;
    }

    // modo bloqueo (spinner sin botones)
    if (_mensaje === "BloquearPantalla") {
      setEsBloqueo(true);
      setVisible(true);
      setMensaje(_titulo || "Espere por favor...");
      setTitulo("");
      setBoton1("");
      setBoton2("");
      setAccionBoton1(null);
      setAccionBoton2(null);
      setTipo("info");
      return;
    }

    setEsBloqueo(false);
    setMensaje(_mensaje);
    setTitulo(_titulo);
    setBoton1(_boton1);
    setBoton2(_boton2);
    setAccionBoton1(() => _accionBoton1);
    setAccionBoton2(() => _accionBoton2);
    setTipo(_tipo);
    setVisible(true);
  }, []);

  useEffect(() => {
    modalDialogService.subscribeShow(show);
  }, [show]);

  const getColor = () => {
    switch (tipo) {
      case "warning": return "border-yellow-400";
      case "error":   return "border-red-400";
      case "success": return "border-green-400";
      default:        return "border-blue-400";
    }
  };

  if (!visible) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-[9999]">
      <div className={`bg-white rounded-xl shadow-lg p-6 w-96 border-l-4 ${getColor()}`}>
        {titulo && <h2 className="text-xl font-semibold mb-2">{titulo}</h2>}
        <p className="text-gray-700 mb-4">{mensaje}</p>

        {esBloqueo ? (
          <div className="flex items-center gap-3">
            <span className="inline-block h-5 w-5 animate-spin rounded-full border-2 border-gray-300 border-t-gray-700" />
            <span className="text-gray-600">Procesando…</span>
          </div>
        ) : (
          <div className="flex justify-end gap-3">
            {boton2 && (
              <button
                onClick={() => {
                  setVisible(false);
                  if (accionBoton2) accionBoton2();
                }}
                className="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 transition"
              >
                {boton2}
              </button>
            )}
            {boton1 && (
              <button
                onClick={() => {
                  setVisible(false);
                  if (accionBoton1) accionBoton1();
                }}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
              >
                {boton1}
              </button>
            )}
            {!boton1 && !boton2 && (
              <button
                onClick={() => setVisible(false)}
                className="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 transition"
              >
                Cerrar
              </button>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
