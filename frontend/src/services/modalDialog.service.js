// src/services/modalDialog.service.js
let ModalDialog_Show = null;   // la función show del componente ModalDialog
let cntBloquearPantalla = 0;   // contador reentrante para el loader

export const subscribeShow = (fnShow) => {
  ModalDialog_Show = fnShow;
};

export const Alert = (
  mensaje,
  titulo = "Atención",
  boton1 = "Aceptar",
  boton2 = "",
  accionBoton1 = null,
  accionBoton2 = null,
  tipo = "info"
) => {
  if (!ModalDialog_Show) return;
  ModalDialog_Show(mensaje, titulo, boton1, boton2, accionBoton1, accionBoton2, tipo);
};

export const Confirm = (
  mensaje,
  titulo = "Confirmar",
  boton1 = "Aceptar",
  boton2 = "Cancelar",
  accionBoton1 = null,
  accionBoton2 = null,
  tipo = "warning"
) => {
  if (!ModalDialog_Show) return;
  ModalDialog_Show(mensaje, titulo, boton1, boton2, accionBoton1, accionBoton2, tipo);
};

/**
 * Muestra/oculta overlay de bloqueo. Tu http.service ya la usa.
 * Cuando se bloquea, el componente mostrará un spinner sin botones.
 */
export const BloquearPantalla = (bloquear) => {
  if (bloquear) cntBloquearPantalla++;
  else cntBloquearPantalla = Math.max(0, cntBloquearPantalla - 1);

  if (!ModalDialog_Show) return;

  if (bloquear && cntBloquearPantalla === 1) {
    ModalDialog_Show("BloquearPantalla", "Espere por favor...", "", "", null, null, "info");
  }
  if (!bloquear && cntBloquearPantalla === 0) {
    ModalDialog_Show("", "", "", "", null, null); // cierra
  }
};

const modalDialogService = { subscribeShow, Alert, Confirm, BloquearPantalla };
export default modalDialogService;
