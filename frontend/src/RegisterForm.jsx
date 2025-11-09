import { useState } from 'react';
import { gql } from '@apollo/client';
import { useMutation } from '@apollo/client/react';

const REGISTRAR_USUARIO_MUTATION = gql`
  mutation RegistrarUsuario($input: RegistroUsuarioInput!) {
    registrarUsuario(input: $input) {
      id
      nombre
      apellido
      email
      rol
      activo
    }
  }
`;

export function RegisterForm() {
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    rol: 'CLIENTE',
    direccion: '',
    telefono: '',
    documentoIdentidad: '',
  });

  const [registrarUsuario, { data, loading, error }] = useMutation(REGISTRAR_USUARIO_MUTATION);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await registrarUsuario({
        variables: {
          input: formData,
        },
      });
    } catch (err) {
      console.error('Error en registro:', err);
    }
  };

  return (
    <div className="card">
      <h2>Registro de usuario</h2>
      <p className="card-subtitle">
        Crea cuentas ADMIN o CLIENTE. Para CLIENTE puedes añadir datos opcionales.
      </p>
  
      <form className="form-grid" onSubmit={handleSubmit}>
        <label>
          Nombre *
          <input
            type="text"
            placeholder="María"
            value={formData.nombre}
            onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
            required
          />
        </label>
  
        <label>
          Apellido *
          <input
            type="text"
            placeholder="Torrez"
            value={formData.apellido}
            onChange={(e) => setFormData({ ...formData, apellido: e.target.value })}
            required
          />
        </label>
  
        <label>
          Email *
          <input
            type="email"
            placeholder="usuario@aguapago.com"
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            required
          />
        </label>
  
        <label>
          Contraseña *
          <input
            type="password"
            placeholder="Mínimo 6 caracteres"
            value={formData.password}
            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            required
          />
        </label>
  
        <label>
          Rol *
          <select
            value={formData.rol}
            onChange={(e) => setFormData({ ...formData, rol: e.target.value })}
          >
            <option value="CLIENTE">CLIENTE</option>
            <option value="ADMIN">ADMIN</option>
          </select>
        </label>
  
        <div className="divider">
          <span>Datos opcionales para CLIENTE</span>
        </div>
  
        <label>
          Dirección
          <input
            type="text"
            placeholder="Av. Las Américas #123"
            value={formData.direccion}
            onChange={(e) => setFormData({ ...formData, direccion: e.target.value })}
          />
        </label>
  
        <label>
          Teléfono
          <input
            type="text"
            placeholder="71234567"
            value={formData.telefono}
            onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
          />
        </label>
  
        <label>
          Documento de Identidad
          <input
            type="text"
            placeholder="CI / NIT / Pasaporte"
            value={formData.documentoIdentidad}
            onChange={(e) =>
              setFormData({ ...formData, documentoIdentidad: e.target.value })
            }
          />
        </label>
  
        <div className="form-actions">
          <button type="submit" disabled={loading}>
            {loading ? 'Registrando…' : 'Registrar usuario'}
          </button>
        </div>
      </form>
  
      {error && <div className="error">Error: {error.message}</div>}
  
      {data?.registrarUsuario && (
        <div className="result">
          <h3>Usuario registrado</h3>
          <pre>{JSON.stringify(data.registrarUsuario, null, 2)}</pre>
        </div>
      )}
    </div>
  );
}