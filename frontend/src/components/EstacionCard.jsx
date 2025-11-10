import React from "react";

export default function EstacionCard({ estacion, onVerSismograma }) {
  return (
    <div className="bg-white rounded-xl shadow p-4 hover-scale">
      <h3 className="text-lg font-semibold text-gray-800">{estacion.nombre}</h3>
      <p className="text-gray-600">📍 Lat: {estacion.latitud}, Lon: {estacion.longitud}</p>
      <p className="text-gray-600">
        Velocidad: {estacion.velocidadOnda} m/s — Frecuencia: {estacion.frecuenciaOnda} Hz — Longitud: {estacion.longitudOnda} m
      </p>
      <button
        onClick={() => onVerSismograma(estacion.id)}
        className="mt-3 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
      >
        Ver Sismograma
      </button>
    </div>
  );
}
