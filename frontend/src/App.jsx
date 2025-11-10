import { BrowserRouter, Routes, Route } from "react-router-dom";
import Inicio from "./components/Inicio";
import EventoList from "./components/EventoList";
import ModalDialog from "./components/ModalDialog"; // opcional, si lo usás global
import "./App.css";

export default function App() {
  return (
    <BrowserRouter>
      {/* Modal global (si lo usás en otras partes del sistema) */}
      <ModalDialog />

      <main className="app-main">
        <Routes>
          {/* 🏠 Pantalla inicial */}
          <Route path="/" element={<Inicio />} />

          {/* 📋 Pantalla de eventos autodetectados */}
          <Route path="/revisiones" element={<EventoList />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
