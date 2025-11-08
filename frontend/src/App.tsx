import TableEvento from "./componets/TableEvento";
import EventoDetalle from "./componets/eventoDetalleSeleccionado";
import { useState, useEffect } from "react"
import type { Evento,  DetalleProm } from "./types/types";
import axios from 'axios';
import {Routes, Route, useNavigate} from 'react-router-dom';


function App() {


  const [selectedEvento, setSelectedEvento] = useState<Evento | null>(null);
  const [eventos, setEventos] = useState<Evento[]>([]);
  const [detalle, setDetalle] = useState<DetalleProm>();
  const navigate = useNavigate();

  const API_EVENTOS_AUTODETECTADOS = "http://localhost:8080/api/revisiones/eventos-autodetectados";
  const API_EVENTO_SELECCIONADO = "http://localhost:8080/api/revisiones/eventos/seleccionar";

  useEffect(() => {
  const getEventosAutodetectados = async (): Promise<Evento[] | void> => {
    try {
      const response = await axios.get<Evento[]>(API_EVENTOS_AUTODETECTADOS);
      setEventos(response.data);
      console.log("Datos recibidos del backend:", response.data);
    } catch (error) {
      console.error("Error al obtener los eventos autodetectados:", error);
    }
  };  
  getEventosAutodetectados();
}, []); 

useEffect(() => {
  if (!selectedEvento) return; // evita llamar al backend sin datos

  const postEventoSismicoDetalle = async () => {
    try{
      const response = await axios.post<DetalleProm>(API_EVENTO_SELECCIONADO,selectedEvento, 
        {headers: {"Content-type": "application/json"}}
      );
      setDetalle(response.data)
      console.log(response.data);
    } catch (error) { 
      console.log("Error al obtener todos los detalles del evento sismico")
    }
  } 
  postEventoSismicoDetalle()
}, [selectedEvento])


  

  return (
    <>

    <div className="container-fluid d-flex flex-column align-items-center overflow-auto justify-content-center vh-100 " 
      style={{ maxHeight: '100vh' }}>

      <Routes>
          <Route path="/" element={
              <>
                <h1>Registrar resultado de revision manual</h1>
                <button className="btn btn-primary p-2 m-2"
                        onClick={() => navigate("/eventos")}
                >Registrar revision</button>
              </>
          }>
          </Route>
          <Route path="/eventos" element={
            <>
              <TableEvento 
                  eventos={eventos} 
                  setSelectedEvento={setSelectedEvento}
                  selectedEvento={selectedEvento}
                  navigate={navigate}
                    />
            </>
          }>
          </Route>
          <Route path="/evento/seleccionado" element={
              <>
                <EventoDetalle    detalle={detalle!} />
              </>
          }>
          </Route>
      </Routes>



    </div>
    </>
  )
}

export default App
