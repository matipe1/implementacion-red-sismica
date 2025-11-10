import axios from "axios";
import modalService from "./modalDialog.service";

const http = axios.create({
  baseURL: "http://localhost:3000/api", // 👈 Incluye /api (coherente con backend)
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor de solicitud (request)
http.interceptors.request.use(
  (request) => {
    modalService.BloquearPantalla?.(true);

    // Token de sesión (si estás usando Keycloak o JWT)
    const accessToken = sessionStorage.getItem("accessToken");
    if (accessToken) {
      request.headers.Authorization = `Bearer ${accessToken}`;
    }

    return request;
  },
  (error) => {
    console.error("Error en axios request:", error);
    modalService.BloquearPantalla?.(false);
    return Promise.reject(error);
  }
);

// Interceptor de respuesta (response)
http.interceptors.response.use(
  (response) => {
    modalService.BloquearPantalla?.(false);
    return response;
  },
  (error) => {
    console.error("Error en axios response:", error);
    modalService.BloquearPantalla?.(false);

    const mensaje =
      error?.response?.data?.message ||
      "Actualmente tenemos inconvenientes en el servidor. Por favor, intente más tarde.";

    modalService.Alert?.(mensaje);
    return Promise.reject({ ...error, message: mensaje });
  }
);

export default http;
    