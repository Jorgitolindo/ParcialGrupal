import { useEffect } from 'react';
import { gql } from '@apollo/client';
import { useQuery } from '@apollo/client/react';
import { useNavigate } from 'react-router-dom';
import { getSession, clearSession } from '../utils/session';
import '../App.css';

const OBTENER_USUARIOS = gql`
  query ObtenerTodosLosUsuarios($usuarioEmail: String!) {
    obtenerTodosLosUsuarios(usuarioEmail: $usuarioEmail) {
      id
      nombre
      apellido
      email
      rol
      activo
    }
  }
`;

const OBTENER_CLIENTES = gql`
  query ObtenerTodosLosClientes {
    obtenerTodosLosClientes {
      id
      codigoCliente
      direccion
      telefono
      documentoIdentidad
      createdAt
      usuario {
        id
        nombre
        apellido
        email
      }
    }
  }
`;

export function AdminDashboard() {
  const navigate = useNavigate();
  const session = getSession();

  useEffect(() => {
    if (!session || session.rol !== 'ADMIN') {
      navigate('/', { replace: true });
    }
  }, [session, navigate]);

  const { data: usuariosData, loading: loadingUsuarios, error: errorUsuarios } =
    useQuery(OBTENER_USUARIOS, {
      skip: !session,
      variables: { usuarioEmail: session?.email ?? '' },
    });

  const { data: clientesData, loading: loadingClientes, error: errorClientes } =
    useQuery(OBTENER_CLIENTES, {
      skip: !session,
    });

  if (!session || session.rol !== 'ADMIN') {
    return null;
  }

  const handleLogout = () => {
    clearSession();
    navigate('/', { replace: true });
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div>
          <h1>Panel de administración</h1>
          <p>
            Sesión: {session.nombre ?? 'Administrador'} — {session.email}
          </p>
        </div>
        <button className="logout-button" onClick={handleLogout}>
          Cerrar sesión
        </button>
      </header>

      <section className="dashboard-section">
        <h2>Usuarios registrados</h2>
        {loadingUsuarios && <div className="loading">Cargando usuarios…</div>}
        {errorUsuarios && (
          <div className="error">Error al obtener usuarios: {errorUsuarios.message}</div>
        )}
        {usuariosData && usuariosData.obtenerTodosLosUsuarios.length > 0 ? (
          <div className="table">
            <div className="table-header">
              <span>Nombre</span>
              <span>Email</span>
              <span>Rol</span>
              <span>Estado</span>
            </div>
            {usuariosData.obtenerTodosLosUsuarios.map((usuario) => (
              <div className="table-row" key={usuario.id}>
                <span>
                  {usuario.nombre} {usuario.apellido}
                </span>
                <span>{usuario.email}</span>
                <span>{usuario.rol}</span>
                <span>{usuario.activo ? 'Activo' : 'Inactivo'}</span>
              </div>
            ))}
          </div>
        ) : (
          <p className="empty-state">No hay usuarios registrados.</p>
        )}
      </section>

      <section className="dashboard-section">
        <h2>Clientes</h2>
        {loadingClientes && <div className="loading">Cargando clientes…</div>}
        {errorClientes && (
          <div className="error">Error al obtener clientes: {errorClientes.message}</div>
        )}
        {clientesData && clientesData.obtenerTodosLosClientes.length > 0 ? (
          <div className="table">
            <div className="table-header">
              <span>Cliente</span>
              <span>Código</span>
              <span>Teléfono</span>
              <span>Dirección</span>
            </div>
            {clientesData.obtenerTodosLosClientes.map((cliente) => (
              <div className="table-row" key={cliente.id}>
                <span>
                  {cliente.usuario.nombre} {cliente.usuario.apellido}
                  <br />
                  <small>{cliente.usuario.email}</small>
                </span>
                <span>{cliente.codigoCliente}</span>
                <span>{cliente.telefono ?? '—'}</span>
                <span>{cliente.direccion ?? '—'}</span>
              </div>
            ))}
          </div>
        ) : (
          <p className="empty-state">No hay clientes registrados.</p>
        )}
      </section>
    </div>
  );
}