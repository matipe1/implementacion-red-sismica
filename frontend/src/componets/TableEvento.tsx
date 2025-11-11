import type {Evento} from '../types/types';
import { useNavigate } from 'react-router-dom';

type TableEventoProps = {
  eventos: Evento[];
  eventoSeleccionado: Evento | null;
  setEventoSeleccionado: (evento: Evento) => void;
  onConfirmarSeleccion: () => void;
  isLoading: boolean;
};

export default function TableEvento({eventos, setEventoSeleccionado, eventoSeleccionado, onConfirmarSeleccion, isLoading}: TableEventoProps) {
  const navigate = useNavigate();

  return (
    <div style={{ maxWidth: '1000px', margin: '0 auto' }}>
      <table className="tabla-eventos">
        <thead>
          <tr>
            <th></th>
            <th>Fecha y Hora</th>
            <th>Ubicación</th>
            <th>Magnitud</th>
          </tr>
        </thead>
        <tbody>
          {Array.isArray(eventos) && eventos.length > 0 ? (
            eventos.map((evento) => {
            const isSelected = eventoSeleccionado === evento;

            return (
              <tr
              key={evento.latitudEpicentro + evento.longitudEpicentro}
              className={isSelected ? 'evento-seleccionado' : ''}
              onClick={() => setEventoSeleccionado(evento)}
              style={{ cursor: "pointer" }}
            >
              <td>
                <input 
                  type="radio"
                  name="evento-seleccionado"
                  className="radio-select"
                  checked={isSelected}
                  onChange={() => setEventoSeleccionado(evento)}
                />
              </td>
              <td>{evento.fechaHoraOcurrencia}</td>
              <td>
                Epicentro ({evento.latitudEpicentro}, {evento.longitudEpicentro}) - 
                Hipocentro ({evento.latitudHipocentro}, {evento.longitudHipocentro})
              </td>
              <td>{evento.valorMagnitud}</td>
            </tr>
            )
          })
          ) : (
            <tr>
              <td colSpan={5} style={{ textAlign: 'center', color: '#6b7280' }}>
                No hay eventos autodetectados disponibles.
              </td>
            </tr>
          )}
        </tbody>
      </table>
      
      <div style={{ textAlign: 'right', marginTop: '20px' }}>
        <button
          className="btn btn-primario btn-primario:hover" // Aplicamos clase de botón
          onClick={onConfirmarSeleccion}
          disabled={!eventoSeleccionado || isLoading}
        >
          {isLoading ? "Cargando..." : "Confirmar Selección"}
        </button>
        <button
          className="btn btn-cancelar btn-cancelar:hover ms-2"
          onClick={() => navigate("/")}
        >
          Cancelar
        </button>
      </div>
    </div>
  );
}