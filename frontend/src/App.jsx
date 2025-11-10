import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Inicio from "./components/Inicio";
import Productos from "./components/Productos";
import PedidoCarrito from "./components/PedidoCarrito";
import CrearPedido from "./components/CrearPedido";
import Pedidos from "./components/Pedidos";
import Clientes from "./components/Clientes";
import PedidoDetalle from "./components/PedidoDetalle";
import ModalDialog from "./components/ModalDialog"; // 👈 importante
import "./App.css";

export default function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen flex flex-col bg-gray-50">
        {/* Header global */}
        <Header />

        {/* Contenido principal */}
        <main className="flex-grow p-5">
          <Routes>
            <Route path="/" element={<Inicio />} />
            <Route path="/productos" element={<Productos />} />
            <Route path="/carrito" element={<PedidoCarrito />} />
            <Route path="/crear-pedido" element={<CrearPedido />} />
            <Route path="/pedidos" element={<Pedidos />} />
            <Route path="/clientes" element={<Clientes />} />
            <Route path="/pedidos/:id" element={<PedidoDetalle />} />
          </Routes>
        </main>

        {/* Modal global (alertas, confirmaciones, bloqueos) */}
        <ModalDialog />
      </div>
    </BrowserRouter>
  );
}
