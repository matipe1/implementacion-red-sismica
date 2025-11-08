import type {Evento} from '../types/types';
import type { NavigateFunction } from "react-router-dom";

type TableEventoProps = {
    eventos: Evento[] ,
    setSelectedEvento: (value: Evento | null) => void,
    selectedEvento: Evento | null, 
    navigate: NavigateFunction
};

function TableEvento({eventos, setSelectedEvento, selectedEvento, navigate}: TableEventoProps) {

    const handleSelect = (evento: Evento) => {
        setSelectedEvento(evento);
    }
    
    return (
        <div className="container">
            <h4 className='fw-light mb-3 fs-1'>Selecciona un evento: </h4>
            <table className="tableCss">
                <thead>
                    <tr>
                        <th>Fecha y Hora de Ocurrencia</th>
                        <th>Ubicacion</th>
                        <th>Magnitud</th>
                    </tr>
                </thead>
                <tbody>
                    {eventos.map((evento,index) => (
                        <tr key={index}
                            onClick={() => handleSelect(evento)}
                            style={{ cursor: "pointer", backgroundColor: (selectedEvento === evento ? "#1ba1b8ff" : "transparent") }}
                        >
                            <td>{evento.fechaHoraOcurrencia}</td>
                            <td>Epicentro: Latitud: {evento.latitudEpicentro} Longitud: {evento.longitudEpicentro} 
                                - Hipocentro: Latitud: {evento.latitudHipocentro} Longitud: {evento.longitudHipocentro}</td>
                            <td>{evento.valorMagnitud}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        <div className='mt-5'>
            <button className='border-o btn btn-secondary '  
                    onClick={() => navigate("/")}
            >Cancelar</button>
            <button className='border-o btn btn-secondary ms-2'
                    onClick={() => {
                        if(selectedEvento ) {
                        navigate("/evento/seleccionado")
                    } 
                    }  }
            >Seleccionar</button>
        </div>

        </div>
    )
}

export default TableEvento;