import { Link } from 'react-router-dom';
import { RegisterForm } from '../RegisterForm';
import '../App.css';

export function RegisterPage() {
  return (
    <div className="app">
      <header className="hero">
        <h1>Registro de usuarios</h1>
        <p>Crea cuentas para nuevos administradores o clientes del sistema.</p>
      </header>

      <div className="card">
        <RegisterForm />
      </div>

      <div className="back-link">
        <Link to="/">← Volver al inicio de sesión</Link>
      </div>
    </div>
  );
}
