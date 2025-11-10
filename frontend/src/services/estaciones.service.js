// src/services/estaciones.service.js
import http from "./http.service";

export async function getEstacionesByEvento(eventoId) {
  const res = await http.get(`/eventos/${eventoId}/estaciones`);
  return res.data; // devuelve estaciones con sus mediciones
}

export async function getSeriesTemporales(estacionId) {
  const res = await http.get(`/estaciones/${estacionId}/series-temporales`);
  return res.data; // datos crudos para sismograma
}
