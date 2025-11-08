import type {Evento} from "../types/types";
import axios from "axios";

const API_EVENTOS_AUTODETECTADOS =
    "http://localhost:8080/api/revisiones/eventos-autodetectados";

const getEventosAutodetectados = async (): Promise<Evento[] | void> => {
    try {
        const response = await axios.get<Evento[]>(API_EVENTOS_AUTODETECTADOS);
        console.log(response.data);
      return response.data; // 👈 opcional: devuelve los datos para usarlos fuera
    } catch (error) {
        console.error("Error al obtener los eventos autodetectados:", error);
    }
};

export default getEventosAutodetectados;
