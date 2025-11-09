import { useState } from 'react';
import { gql } from '@apollo/client';
import { useMutation } from '@apollo/client/react';
import { Link, useNavigate } from 'react-router-dom';

import '../App.css';


const LOGIN_MUTATION = gql`
  mutation Login($email: String!, $password: String!) {
    login(input: { email: $email, password: $password }) {
      id
      nombre
      email
      rol
      mensaje
    }
  }
`;

export function LoginPage() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [successMessage, setSuccessMessage] = useState('');
  const [login, {  loading, error }] = useMutation(LOGIN_MUTATION);
  const navigate = useNavigate();


  const handleSubmit = async (event) => {
    event.preventDefault();
    setSuccessMessage('');
    try {
      const { data } = await login({
        variables: {
          email: formData.email,
          password: formData.password,
        },
      });

      if (data?.login) {
        const usuario = data.login;
        // Guardamos email, rol e id (si viene) para otras vistas
        localStorage.setItem(
          'session',
          JSON.stringify({
            id: usuario.id,
            email: usuario.email,
            rol: usuario.rol,
            nombre: usuario.nombre,
          })
        );

        const mensaje = usuario.mensaje ?? 'Inicio de sesión exitoso.';
        setSuccessMessage(mensaje);

        // Redirigir según rol
        if (usuario.rol === 'ADMIN') {
          navigate('/dashboard');
        } else if (usuario.rol === 'CLIENTE') {
          navigate('/cliente');
        }
      }
    } catch (err) {
      console.error('Error en login:', err);
    }
  };

  return (
    <div className="app">
      <header className="hero">
        <h1>Bienvenido a AguaPago</h1>
        <p>Ingresa con tu cuenta para gestionar usuarios y clientes.</p>
      </header>

      <div className="card">
        <form className="form-grid" onSubmit={handleSubmit}>
          <label>
            Email
            <input
              type="email"
              placeholder="usuario@aguapago.com"
              value={formData.email}
              onChange={(e) =>
                setFormData({ ...formData, email: e.target.value })
              }
              required
            />
          </label>

          <label>
            Contraseña
            <input
              type="password"
              placeholder="********"
              value={formData.password}
              onChange={(e) =>
                setFormData({ ...formData, password: e.target.value })
              }
              required
            />
          </label>

          <div className="button-row">
            <button type="submit" disabled={loading}>
              {loading ? 'Ingresando…' : 'Ingresar'}
            </button>

            <Link className="secondary-button" to="/registrar">
              Registrar
            </Link>
          </div>
        </form>
        {successMessage && <div className="success">{successMessage}</div>}
        {error && <div className="error">Error: {error.message}</div>}

        
      </div>
    </div>
  );
}
