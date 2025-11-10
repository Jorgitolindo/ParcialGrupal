import { useState } from 'react';
import { gql } from '@apollo/client';
import { useMutation } from '@apollo/client/react';
import { router } from 'expo-router';
import {
  Alert,
  KeyboardAvoidingView,
  Platform,
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { useAuth } from '../src/hooks/useAuth';

type LoginResponse = {
  id?: string | null;
  nombre?: string | null;
  email: string;
  rol: 'CLIENTE' | 'ADMIN';
  mensaje?: string | null;
};

type LoginMutationData = {
  login: LoginResponse | null;
};

type LoginVariables = {
  email: string;
  password: string;
};

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

export default function LoginScreen() {
  const { login: loginSession } = useAuth();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [login, { loading }] = useMutation<LoginMutationData, LoginVariables>(
    LOGIN_MUTATION,
  );

  const handleLogin = async () => {
    if (!formData.email || !formData.password) {
      Alert.alert('Campos requeridos', 'Ingresa email y contraseña.');
      return;
    }

    try {
      const { data } = await login({
        variables: {
          email: formData.email,
          password: formData.password,
        },
      });

      const usuario = data?.login;

      if (!usuario) {
        Alert.alert('Error', 'Credenciales inválidas.');
        return;
      }

      if (usuario.rol !== 'CLIENTE') {
        Alert.alert(
          'Acceso restringido',
          'La app móvil es solo para clientes.'
        );
        return;
      }

      await loginSession({
        id: usuario.id ?? '',
        email: usuario.email,
        rol: usuario.rol,
        nombre: usuario.nombre,
      });

      router.replace('/home');
    } catch (err: any) {
      Alert.alert('Error al ingresar', err.message ?? 'Inténtalo nuevamente.');
    }
  };

  return (
    <SafeAreaView style={styles.safe}>
      <KeyboardAvoidingView
        style={styles.container}
        behavior={Platform.select({ ios: 'padding', android: undefined })}
      >
        <View style={styles.card}>
          <Text style={styles.title}>AguaPago</Text>
          <Text style={styles.subtitle}>App para clientes</Text>

          <TextInput
            style={styles.input}
            placeholder="Email"
            keyboardType="email-address"
            autoCapitalize="none"
            value={formData.email}
            onChangeText={(text) => setFormData((prev) => ({ ...prev, email: text }))}
          />

          <TextInput
            style={styles.input}
            placeholder="Contraseña"
            secureTextEntry
            value={formData.password}
            onChangeText={(text) =>
              setFormData((prev) => ({ ...prev, password: text }))
            }
          />

          <TouchableOpacity
            style={[styles.button, loading && styles.buttonDisabled]}
            onPress={handleLogin}
            disabled={loading}
          >
            <Text style={styles.buttonText}>
              {loading ? 'Ingresando...' : 'Ingresar'}
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.linkButton}
            onPress={() => router.push('/register')}
          >
            <Text style={styles.linkText}>¿No tienes cuenta? Regístrate</Text>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: {
    flex: 1,
    backgroundColor: '#0f172a',
  },
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 24,
  },
  card: {
    backgroundColor: '#ffffff',
    borderRadius: 24,
    paddingVertical: 32,
    paddingHorizontal: 24,
    gap: 18,
    elevation: 6,
    shadowColor: '#1f2937',
    shadowOffset: { width: 0, height: 12 },
    shadowOpacity: 0.18,
    shadowRadius: 16,
  },
  title: {
    fontSize: 32,
    fontWeight: '700',
    color: '#0f172a',
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 14,
    textAlign: 'center',
    color: '#64748b',
    marginBottom: 8,
  },
  input: {
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#d1d5db',
    paddingHorizontal: 16,
    paddingVertical: 12,
    backgroundColor: '#f8fafc',
    fontSize: 16,
    color: '#0f172a',
  },
  button: {
    borderRadius: 12,
    backgroundColor: '#2563eb',
    paddingVertical: 14,
  },
  buttonDisabled: {
    opacity: 0.75,
  },
  buttonText: {
    textAlign: 'center',
    color: '#ffffff',
    fontWeight: '600',
    fontSize: 16,
  },
  linkButton: {
    paddingVertical: 10,
  },
  linkText: {
    textAlign: 'center',
    color: '#1d4ed8',
    fontWeight: '600',
  },
});