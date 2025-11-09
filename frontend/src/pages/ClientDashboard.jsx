import { useEffect } from 'react';
import { gql } from '@apollo/client';
import { useQuery } from '@apollo/client/react';
import { useNavigate } from 'react-router-dom';
import { getSession, clearSession } from '../utils/session';
import '../App.css';

const OBTENER_USUARIO = gql`
  query ObtenerUsuarioPorId($id: ID!, $usuarioEmail: String!) {
    obtenerUsuarioPorId(id: $id, usuarioEmail: $usuarioEmail) {
      id
      nombre
      apellido
      email
      rol
      activo
      createdAt
    }
  }
`;

const OBTENER_CLIENTE = gql`
  query ObtenerClientePorUsuarioId($usuarioId: ID!) {
    obtenerClientePorUsuarioId(usuarioId: $usuarioId) {
      id
      codigoCliente
      direccion
      telefono
      documentoIdentidad
      createdAt
    }
  }
`;

export function ClientDashboard() {
  const navigate = useNavigate();
  const session = getSession();

  useEffect(() => {
    if (!session || session.rol !== 'CLIENTE') {
      navigate('/', { replace: true });
    }
  }, [session, navigate]);

  const usuarioId = session?.id ?? '';

  const {
    data: usuarioData,
    loading: loadingUsuario,
    error: errorUsuario,
  } = useQuery(OBTENER_USUARIO, {
    skip: !session,
    variables: { id: usuarioId, usuarioEmail: session?.email ?? '' },
  });

  const {
    data: clienteData,
    loading: loadingCliente,
    error: errorCliente,
  } = useQuery(OBTENER_CLIENTE, {
    skip: !session,
    variables: { usuarioId },
  });

  if (!session || session.rol !== 'CLIENTE') {
    return null;
  }

  const handleLogout = () => {
    clearSession();
    navigate('/', { replace: true });
  };

  const usuario = usuarioData?.obtenerUsuarioPorId ?? null;
  const cliente = clienteData?.obtenerClientePorUsuarioId ?? null;

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div>
          <h1>Mi cuenta</h1>
          <p>{session.email}</p>
        </div>
        <button className="logout-button" onClick={handleLogout}>
          Cerrar sesión
        </button>
      </header>

      <section className="dashboard-section">
        <h2>Datos personales</h2>
        {loadingUsuario && <div className="loading">Cargando datos…</div>}
        {errorUsuario && (
          <div className="error">Error al obtener datos: {errorUsuario.message}</div>
        )}
        {usuario ? (
          <ul className="detail-list">
            <li>
              <strong>Nombre:</strong> {usuario.nombre} {usuario.apellido}
            </li>
            <li>
              <strong>Email:</strong> {usuario.email}
            </li>
            <li>
              <strong>Estado:</strong> {usuario.activo ? 'Activo' : 'Inactivo'}
            </li>
            <li>
              <strong>Creado:</strong>{' '}
              {usuario.createdAt
                ? new Date(usuario.createdAt).toLocaleString()
                : 'Sin registro'}
            </li>
          </ul>
        ) : (
          <p className="empty-state">No se encontraron tus datos.</p>
        )}
      </section>

      <section className="dashboard-section">
        <h2>Perfil de cliente</h2>
        {loadingCliente && <div className="loading">Cargando perfil…</div>}
        {errorCliente && (
          <div className="error">Error al obtener perfil: {errorCliente.message}</div>
        )}
        {cliente ? (
          <ul className="detail-list">
            <li>
              <strong>Código cliente:</strong> {cliente.codigoCliente}
            </li>
            <li>
              <strong>Dirección:</strong> {cliente.direccion ?? '—'}
            </li>
            <li>
              <strong>Teléfono:</strong> {cliente.telefono ?? '—'}
            </li>
            <li>
              <strong>Documento:</strong> {cliente.documentoIdentidad ?? '—'}
            </li>
            <li>
              <strong>Creado:</strong>{' '}
              {cliente.createdAt
                ? new Date(cliente.createdAt).toLocaleString()
                : 'Sin registro'}
            </li>
          </ul>
        ) : (
          <p className="empty-state">
            Todavía no tienes un perfil de cliente asignado.
          </p>
        )}
      </section>
    </div>
  );
}