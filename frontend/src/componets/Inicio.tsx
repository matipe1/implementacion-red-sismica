import { useNavigate } from "react-router-dom";

export default function Inicio() {
  const navigate = useNavigate();

  return (
    <div className="inicio-container">
      <div className="inicio-icono">🌋</div>
      <h2 className="inicio-title">
        Registrar resultado de revisión manual
      </h2>
      <button
        className="inicio-btn"
        onClick={() => navigate("/eventos-autodetectados")}
      >
        Registrar revisión
      </button>
    </div>
  );
}