import { createContext, useCallback, useEffect, useMemo, useState } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

type Session = {
  id: string;
  email: string;
  rol: 'CLIENTE' | 'ADMIN';
  nombre?: string | null;
};

type AuthContextValue = {
  session: Session | null;
  loading: boolean;
  login: (session: Session) => Promise<void>;
  logout: () => Promise<void>;
};

export const AuthContext = createContext<AuthContextValue>({
  session: null,
  loading: true,
  login: async () => undefined,
  logout: async () => undefined,
});

const STORAGE_KEY = 'aguapago-session';

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [session, setSession] = useState<Session | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    (async () => {
      try {
        const raw = await AsyncStorage.getItem(STORAGE_KEY);
        if (raw) {
          const parsed = JSON.parse(raw);
          setSession(parsed);
        }
      } catch (error) {
        console.warn('No se pudo recuperar la sesión', error);
        await AsyncStorage.removeItem(STORAGE_KEY);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const login = useCallback(async (newSession: Session) => {
    setSession(newSession);
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(newSession));
  }, []);

  const logout = useCallback(async () => {
    setSession(null);
    await AsyncStorage.removeItem(STORAGE_KEY);
  }, []);

  const value = useMemo(
    () => ({
      session,
      loading,
      login,
      logout,
    }),
    [session, loading, login, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}