import React, { useEffect, useState } from "react";
import { getEstacionesByEvento } from "../services/estaciones.service";
import EstacionCard from "./EstacionCard";

export default function EstacionesList({ eventoId, onVerSismograma }) {
  const [estaciones, setEstaciones] = useState([]);

  useEffect(() => {
    async function cargarEstaciones() {
      const data = await getEstacionesByEvento(eventoId);
      setEstaciones(data);
    }
    if (eventoId) cargarEstaciones();
  }, [eventoId]);

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
      {estaciones.map((e) => (
        <EstacionCard key={e.id} estacion={e} onVerSismograma={onVerSismograma} />
      ))}
    </div>
  );
}
