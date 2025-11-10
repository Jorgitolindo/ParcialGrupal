import { gql } from '@apollo/client';
import { useMutation } from '@apollo/client/react';
import { router } from 'expo-router';
import { useState } from 'react';
import {
  ActivityIndicator,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';

type FormState = {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  direccion: string;
  telefono: string;
  documentoIdentidad: string;
};

type FormErrors = Partial<Record<keyof FormState, string>>;

const initialState: FormState = {
  nombre: '',
  apellido: '',
  email: '',
  password: '',
  direccion: '',
  telefono: '',
  documentoIdentidad: '',
};

type RegistrarUsuarioData = {
  registrarUsuario: {
    id: string;
    nombre: string;
    apellido: string;
    email: string;
    rol: string;
    activo: boolean;
  };
};

type RegistrarUsuarioVariables = {
  input: {
    nombre: string;
    apellido: string;
    email: string;
    password: string;
    rol: 'CLIENTE' | 'ADMIN';
    direccion?: string;
    telefono?: string;
    documentoIdentidad?: string;
  };
};

const REGISTRAR_USUARIO = gql`
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

export default function RegisterScreen() {
  const [form, setForm] = useState<FormState>(initialState);
  const [errors, setErrors] = useState<FormErrors>({});
  const [submitting, setSubmitting] = useState(false);
  const [serverError, setServerError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [registrarUsuarioMutation] = useMutation<
    RegistrarUsuarioData,
    RegistrarUsuarioVariables
  >(REGISTRAR_USUARIO);

  const handleChange = (key: keyof FormState, value: string) => {
    setForm((prev) => ({ ...prev, [key]: value }));
    if (errors[key]) {
      setErrors((prev) => ({ ...prev, [key]: undefined }));
    }
  };

  const validate = () => {
    const newErrors: FormErrors = {};
    if (!form.nombre.trim()) {
      newErrors.nombre = 'El nombre es obligatorio';
    }
    if (!form.apellido.trim()) {
      newErrors.apellido = 'El apellido es obligatorio';
    }
    if (!form.email.trim()) {
      newErrors.email = 'El correo es obligatorio';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      newErrors.email = 'Ingresa un correo válido';
    }
    if (!form.password.trim()) {
      newErrors.password = 'La contraseña es obligatoria';
    } else if (form.password.length < 6) {
      newErrors.password = 'Mínimo 6 caracteres';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (submitting) return;
    if (!validate()) return;
    setSubmitting(true);
    setServerError(null);
    setSuccessMessage(null);

    const input: RegistrarUsuarioVariables['input'] = {
      nombre: form.nombre.trim(),
      apellido: form.apellido.trim(),
      email: form.email.trim().toLowerCase(),
      password: form.password,
      rol: 'CLIENTE',
      direccion: form.direccion.trim() || undefined,
      telefono: form.telefono.trim() || undefined,
      documentoIdentidad: form.documentoIdentidad.trim() || undefined,
    };

    try {
      await registrarUsuarioMutation({
        variables: { input },
      });
      setForm(initialState);
      setSuccessMessage('Registro exitoso. Ahora puedes iniciar sesión.');
      setTimeout(() => {
        router.replace('/');
      }, 1200);
    } catch (error) {
      const message =
        error instanceof Error ? error.message : 'Ocurrió un error inesperado';
      setServerError(message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.content}>
      <View style={styles.card}>
        <Text style={styles.title}>Crear cuenta</Text>
        <Text style={styles.subtitle}>
          Registra tus datos para comenzar a gestionar tu servicio de AguaPago.
        </Text>

        <View style={styles.formGroup}>
          <Label>Nombre</Label>
          <TextInput
            value={form.nombre}
            onChangeText={(value) => handleChange('nombre', value)}
            placeholder="Ej. Juan"
            style={styles.input}
            autoCapitalize="words"
          />
          {errors.nombre && <ErrorText>{errors.nombre}</ErrorText>}
        </View>

        <View style={styles.formGroup}>
          <Label>Apellido</Label>
          <TextInput
            value={form.apellido}
            onChangeText={(value) => handleChange('apellido', value)}
            placeholder="Ej. Pérez"
            style={styles.input}
            autoCapitalize="words"
          />
          {errors.apellido && <ErrorText>{errors.apellido}</ErrorText>}
        </View>

        <View style={styles.formGroup}>
          <Label>Correo electrónico</Label>
          <TextInput
            value={form.email}
            onChangeText={(value) => handleChange('email', value)}
            placeholder="tu@email.com"
            style={styles.input}
            keyboardType="email-address"
            autoCapitalize="none"
            autoCorrect={false}
          />
          {errors.email && <ErrorText>{errors.email}</ErrorText>}
        </View>

        <View style={styles.formGroup}>
          <Label>Contraseña</Label>
          <TextInput
            value={form.password}
            onChangeText={(value) => handleChange('password', value)}
            placeholder="Mínimo 6 caracteres"
            style={styles.input}
            secureTextEntry
          />
          {errors.password && <ErrorText>{errors.password}</ErrorText>}
        </View>

        <View style={styles.separator} />

        <Text style={styles.sectionTitle}>Datos del perfil (opcional)</Text>

        <View style={styles.formGroup}>
          <Label>Dirección</Label>
          <TextInput
            value={form.direccion}
            onChangeText={(value) => handleChange('direccion', value)}
            placeholder="Calle y número"
            style={styles.input}
          />
        </View>

        <View style={styles.formGroup}>
          <Label>Teléfono</Label>
          <TextInput
            value={form.telefono}
            onChangeText={(value) => handleChange('telefono', value)}
            placeholder="Ej. 987654321"
            style={styles.input}
            keyboardType="phone-pad"
          />
        </View>

        <View style={styles.formGroup}>
          <Label>Documento de identidad</Label>
          <TextInput
            value={form.documentoIdentidad}
            onChangeText={(value) => handleChange('documentoIdentidad', value)}
            placeholder="DNI o similar"
            style={styles.input}
          />
        </View>

        {serverError && (
          <Text style={styles.serverError}>{serverError}</Text>
        )}
        {successMessage && (
          <Text style={styles.success}>{successMessage}</Text>
        )}

        <TouchableOpacity
          style={[
            styles.submitButton,
            submitting && styles.submitButtonDisabled,
          ]}
          onPress={handleSubmit}
          disabled={submitting}
        >
          {submitting ? (
            <ActivityIndicator color="#ffffff" />
          ) : (
            <Text style={styles.submitText}>Registrarme</Text>
          )}
        </TouchableOpacity>

        <Text style={styles.loginPrompt}>
          ¿Ya tienes cuenta?{' '}
          <Text style={styles.loginLink} onPress={() => router.push('/')}
          >
            Inicia sesión
          </Text>
        </Text>
      </View>
    </ScrollView>
  );
}

function Label({ children }: { children: React.ReactNode }) {
  return <Text style={styles.label}>{children}</Text>;
}

function ErrorText({ children }: { children: React.ReactNode }) {
  return <Text style={styles.error}>{children}</Text>;
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0f172a',
  },
  content: {
    padding: 20,
  },
  card: {
    backgroundColor: '#ffffff',
    borderRadius: 24,
    padding: 20,
    gap: 18,
    shadowColor: '#0f172a',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.12,
    shadowRadius: 16,
    elevation: 5,
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
  formGroup: {
    gap: 6,
  },
  label: {
    fontSize: 13,
    fontWeight: '600',
    color: '#1d4ed8',
  },
  input: {
    borderWidth: 1,
    borderColor: 'rgba(148, 163, 184, 0.35)',
    borderRadius: 12,
    paddingHorizontal: 14,
    paddingVertical: 12,
    fontSize: 15,
    color: '#0f172a',
    backgroundColor: 'rgba(241, 245, 249, 0.7)',
  },
  separator: {
    height: 1,
    backgroundColor: 'rgba(148, 163, 184, 0.3)',
    marginVertical: 10,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#0f172a',
  },
  submitButton: {
    marginTop: 8,
    paddingVertical: 14,
    borderRadius: 14,
    backgroundColor: '#2563eb',
    alignItems: 'center',
  },
  submitButtonDisabled: {
    opacity: 0.7,
  },
  submitText: {
    color: '#ffffff',
    fontWeight: '600',
    fontSize: 16,
  },
  loginPrompt: {
    textAlign: 'center',
    color: '#64748b',
    fontSize: 14,
  },
  loginLink: {
    color: '#2563eb',
    fontWeight: '600',
  },
  error: {
    color: '#b91c1c',
    fontSize: 12,
    fontWeight: '500',
  },
  serverError: {
    backgroundColor: 'rgba(185, 28, 28, 0.12)',
    color: '#991b1b',
    borderRadius: 12,
    padding: 12,
    borderWidth: 1,
    borderColor: 'rgba(185, 28, 28, 0.3)',
    fontWeight: '600',
  },
  success: {
    backgroundColor: 'rgba(34, 197, 94, 0.12)',
    color: '#166534',
    borderRadius: 12,
    padding: 12,
    borderWidth: 1,
    borderColor: 'rgba(34, 197, 94, 0.3)',
    fontWeight: '600',
  },
});