import type {  DetalleProm} from "../types/types";
import { useNavigate } from "react-router-dom";






function EventoDetalle({ detalle }: { detalle: DetalleProm }) {

    if (!detalle) {
    return <p>Cargando detalle...</p>;
    }

    const navigate = useNavigate();

    const handleVolverTabla = () => {
        navigate("/eventos");
    };

    

    return (
        <>
            

            <div className="container mt-5 "  style={{ maxHeight: '100vh' }}>
                <h2 className="text-center mb-4">Detalle del Evento Sísmico</h2>

                {/* --- Datos del Evento Sismico --- */}
                <div className="card mb-4 shadow-sm">
                    <div className="card-header bg-primary text-white">
                        <h4 className="mb-0">Datos del Evento</h4>
                    </div>
                    <div className="card-body">
                        <p><strong>Magnitud:</strong> {detalle.eventoSismico.valorMagnitud}</p>
                        <p><strong>Latitud epicentro:</strong> {detalle.eventoSismico.latitudEpicentro}</p>
                        <p><strong>Longitud epicentro:</strong> {detalle.eventoSismico.longitudEpicentro}</p>
                    </div>
                </div>

                {/* --- Datos Sísmicos --- */}
                <div className="card mb-4 shadow-sm">
                    <div className="card-header bg-info text-white">
                        <h4 className="mb-0">Datos Sísmicos</h4>
                    </div>
                    <div className="card-body">
                        <p><strong>Alcance:</strong> {detalle.datosSismicos.alcanceSismo}</p>
                        <p><strong>Clasificación:</strong> {detalle.datosSismicos.clasificacionSismo}</p>
                        <p><strong>Origen de generación:</strong> {detalle.datosSismicos.origenDeGeneracion}</p>
                    </div>
                </div>

                {/* --- Series Temporales --- */}
                <div className="card mb-4 shadow-sm">
                    <div className="card-header bg-warning text-dark">
                        <h4 className="mb-0">Series Temporales</h4>
                    </div>
                    <div className="card-body">
                        {detalle.seriesTemporales.map((serie, i) => (
                            <div key={i} className="mb-3 border-bottom pb-3">
                                <h5 className="text-secondary">📡 Estación {serie.codigoEstacion}</h5>

                                {serie.muestrasSismicas.map((muestra, j) => (
                                    <div key={j} className="ms-3">
                                        <p><strong>Fecha muestra:</strong> {muestra.fechaHoraMuestra}</p>
                                        {muestra.detalles.map((detalleMuestra, k) => (
                                            <div key={k} className="ms-4 mb-2">
                                                <p>Valor: <strong>{detalleMuestra.valor}</strong></p>
                                                <p>
                                                    Tipo: {detalleMuestra.tipoDeDato.denominacion} (
                                                    {detalleMuestra.tipoDeDato.nombreUnidadMedida})
                                                </p>
                                            </div>
                                        ))}
                                    </div>
                                ))}
                            </div>
                        ))}
                    </div>
                </div>
                
                {/* --- Cambios de estado --- */}
                <div className="card mb-4 shadow-sm">
                    <div className="card-header bg-secondary text-white">
                        <h4 className="mb-0">Cambios de Estado</h4>
                    </div>
                    <div className="card-body">
                        {detalle.cambiosDeEstado.map((cambio, i) => (
                            <div key={i} className="mb-3 border-bottom pb-2">
                                <p><strong>Desde:</strong> {cambio.fechaHoraDesde}</p>
                                <p><strong>Hasta:</strong> {cambio.fechaHoraHasta ?? "Actual"}</p>
                                <p><strong>Responsable:</strong> {cambio.responsableInspeccion ?? "No asignado"}</p>
                                <p><strong>Estado:</strong> <span className="badge bg-light text-dark">{cambio.estado}</span></p>
                            </div>
                        ))}
                    </div>
                </div>
                
                <div className="text-center p-5 ">
                    
                    <button className="btn btn-danger " onClick={handleVolverTabla}> Rechazar evento </button>
                    <button className="btn btn-warning ms-3" > Solicitar revisión a experto </button>
                    <button className="btn btn-info ms-3" > Visualizar mapa </button>
                    <button className="btn btn-light ms-3" > Modificar datos </button>
                    <button className="btn btn-secondary ms-3" onClick={handleVolverTabla}> Cancelar  </button>
                </div>
            </div>

        </>
    );
}
export default EventoDetalle;