import { gql } from '@apollo/client';
import { useQuery } from '@apollo/client/react';
import { router } from 'expo-router';
import { useEffect } from 'react';
import {
  ActivityIndicator,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { useAuth } from '../src/hooks/useAuth';

type UsuarioQueryData = {
  obtenerUsuarioPorId: {
    id: string;
    nombre: string;
    apellido: string;
    email: string;
    rol: 'CLIENTE' | 'ADMIN';
    activo: boolean;
    createdAt?: string | null;
  } | null;
};

type UsuarioQueryVariables = {
  id: string;
  usuarioEmail: string;
};

type ClienteQueryData = {
  obtenerClientePorUsuarioId: {
    id: string;
    codigoCliente: string;
    direccion?: string | null;
    telefono?: string | null;
    documentoIdentidad?: string | null;
    createdAt?: string | null;
  } | null;
};

type ClienteQueryVariables = {
  usuarioId: string;
};

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

const OBTENER_PERFIL_CLIENTE = gql`
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

export default function HomeScreen() {
  const { session, logout, loading: sessionLoading } = useAuth();

  const {
    data: usuarioData,
    loading: loadingUsuario,
    error: errorUsuario,
  } = useQuery<UsuarioQueryData, UsuarioQueryVariables>(OBTENER_USUARIO, {
    skip: sessionLoading || !session,
    variables: {
      id: session?.id ?? '',
      usuarioEmail: session?.email ?? '',
    },
  });

  const {
    data: clienteData,
    loading: loadingCliente,
    error: errorCliente,
  } = useQuery<ClienteQueryData, ClienteQueryVariables>(OBTENER_PERFIL_CLIENTE, {
    skip: sessionLoading || !session,
    variables: {
      usuarioId: session?.id ?? '',
    },
  });

  const shouldRedirect = !sessionLoading && (!session || session.rol !== 'CLIENTE');

  useEffect(() => {
    if (shouldRedirect) {
      router.replace('/');
    }
  }, [shouldRedirect]);

  if (sessionLoading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#2563eb" />
      </View>
    );
  }

  if (shouldRedirect) {
    return null;
  }

  const usuario = usuarioData?.obtenerUsuarioPorId;
  const cliente = clienteData?.obtenerClientePorUsuarioId;

  const formatDate = (value?: string | null) => {
    if (!value) return 'No registrado';
    return new Date(value).toLocaleString();
  };

  const handleLogout = async () => {
    await logout();
    router.replace('/');
  };

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.content}>
      <View style={styles.card}>
        <Text style={styles.title}>Hola, {session?.nombre ?? 'Cliente'} 👋</Text>
        <Text style={styles.subtitle}>
          Estos son los datos asociados a tu cuenta AguaPago.
        </Text>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Información de usuario</Text>
          {loadingUsuario && <ActivityIndicator color="#2563eb" />}
          {errorUsuario && (
            <Text style={styles.error}>
              Error al obtener tus datos: {errorUsuario.message}
            </Text>
          )}
          {usuario ? (
            <View style={styles.detailList}>
              <DetailRow label="Nombre completo">
                {usuario.nombre} {usuario.apellido}
              </DetailRow>
              <DetailRow label="Correo electrónico">{usuario.email}</DetailRow>
              <DetailRow label="Estado">{usuario.activo ? 'Activo' : 'Inactivo'}</DetailRow>
              <DetailRow label="Creado">{formatDate(usuario.createdAt)}</DetailRow>
            </View>
          ) : (
            !loadingUsuario && (
              <Text style={styles.emptyState}>No se encontraron datos de usuario.</Text>
            )
          )}
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Perfil de cliente</Text>
          {loadingCliente && <ActivityIndicator color="#2563eb" />}
          {errorCliente && (
            <Text style={styles.error}>
              Error al obtener el perfil: {errorCliente.message}
            </Text>
          )}
          {cliente ? (
            <View style={styles.detailList}>
              <DetailRow label="Código cliente">{cliente.codigoCliente}</DetailRow>
              <DetailRow label="Dirección">{cliente.direccion ?? 'No registrada'}</DetailRow>
              <DetailRow label="Teléfono">{cliente.telefono ?? 'No registrado'}</DetailRow>
              <DetailRow label="Documento">{cliente.documentoIdentidad ?? 'No registrado'}</DetailRow>
              <DetailRow label="Creado">{formatDate(cliente.createdAt)}</DetailRow>
            </View>
          ) : (
            !loadingCliente && (
              <Text style={styles.emptyState}>
                No tienes perfil de cliente registrado.
              </Text>
            )
          )}
        </View>

        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Text style={styles.logoutText}>Cerrar sesión</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}

function DetailRow({
  label,
  children,
}: {
  label: string;
  children: React.ReactNode;
}) {
  return (
    <View style={styles.detailRow}>
      <Text style={styles.detailLabel}>{label}</Text>
      <Text style={styles.detailValue}>{children}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0f172a',
  },
  content: {
    padding: 20,
  },
  centered: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#0f172a',
  },
  card: {
    backgroundColor: '#ffffff',
    borderRadius: 24,
    padding: 20,
    gap: 18,
    shadowColor: '#0f172a',
    shadowOffset: { width: 0, height: 12 },
    shadowOpacity: 0.12,
    shadowRadius: 16,
    elevation: 6,
  },
  title: {
    fontSize: 26,
    fontWeight: '700',
    color: '#0f172a',
  },
  subtitle: {
    fontSize: 14,
    color: '#64748b',
  },
  section: {
    gap: 12,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: '#1d4ed8',
  },
  detailList: {
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(37, 99, 235, 0.12)',
    backgroundColor: 'rgba(37, 99, 235, 0.04)',
    padding: 14,
    gap: 12,
  },
  detailRow: {
    gap: 4,
  },
  detailLabel: {
    fontSize: 12,
    fontWeight: '600',
    color: '#2563eb',
    textTransform: 'uppercase',
  },
  detailValue: {
    fontSize: 15,
    color: '#0f172a',
  },
  emptyState: {
    padding: 12,
    borderRadius: 12,
    backgroundColor: 'rgba(148, 163, 184, 0.12)',
    borderWidth: 1,
    borderColor: 'rgba(148, 163, 184, 0.3)',
    color: '#334155',
  },
  error: {
    color: '#b91c1c',
    fontWeight: '600',
  },
  logoutButton: {
    marginTop: 12,
    paddingVertical: 14,
    borderRadius: 14,
    backgroundColor: '#dc2626',
  },
  logoutText: {
    textAlign: 'center',
    color: '#ffffff',
    fontWeight: '600',
  },
});