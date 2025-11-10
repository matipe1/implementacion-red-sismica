import { useNavigate } from "react-router-dom";
import "../App.css";

export default function Inicio() {
  const navigate = useNavigate();

  return (
    <div className="inicio-container">
      <img
        src="/img/Img-sismo.jpg"
        alt="Imagen sismo"
        className="inicio-img"
      />
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
