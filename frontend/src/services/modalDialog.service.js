import React, { useState, useEffect } from "react";
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

  // Se suscribe al servicio al montar el componente
  useEffect(() => {
    modalDialogService.subscribeShow(show);
  }, []);

  // Función principal invocada desde el servicio
  const show = (
    _mensaje = "",
    _titulo = "",
    _boton1 = "",
    _boton2 = "",
    _accionBoton1 = null,
    _accionBoton2 = null,
    _tipo = "info"
  ) => {
    if (!_mensaje) {
      setVisible(false);
      return;
    }

    setMensaje(_mensaje);
    setTitulo(_titulo);
    setBoton1(_boton1);
    setBoton2(_boton2);
    setAccionBoton1(() => _accionBoton1);
    setAccionBoton2(() => _accionBoton2);
    setTipo(_tipo);
    setVisible(true);
  };

  // Colores según tipo
  const getColor = () => {
    switch (tipo) {
      case "warning":
        return "border-yellow-400 text-yellow-700";
      case "error":
        return "border-red-400 text-red-700";
      case "success":
        return "border-green-400 text-green-700";
      default:
        return "border-blue-400 text-blue-700";
    }
  };

  // Render
  if (!visible) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
      <div className={`bg-white rounded-xl shadow-lg p-6 w-96 border-l-4 ${getColor()}`}>
        <h2 className="text-xl font-semibold mb-2">{titulo}</h2>
        <p className="text-gray-700 mb-4">{mensaje}</p>

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
        </div>
      </div>
    </div>
  );
}
