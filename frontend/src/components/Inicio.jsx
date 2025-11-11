import { useNavigate } from "react-router-dom";
import "../App.css";

export default function Inicio() {
  const navigate = useNavigate();

  return (
    <div className="inicio-container">
      {/* 🌋 Ícono representativo del sistema sísmico */}
      <div className="inicio-icono">🌋</div>

      <h2 className="inicio-title">
        Registrar resultado de revisión manual
      </h2>

      <button
        className="inicio-btn"
        onClick={() => navigate("/revisiones")}
      >
        Registrar revisión
      </button>
    </div>
  );
}
