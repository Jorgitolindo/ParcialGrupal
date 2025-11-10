import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import { ApolloProvider } from '@apollo/client/react';
import { AuthProvider } from '../src/context/AuthContext';
import { apolloClient } from '../src/apollo/client';
export default function RootLayout() {
  return (
    <ApolloProvider client={apolloClient}>
      <AuthProvider>
        <Stack screenOptions={{ headerShown: false }}>
          <Stack.Screen name="index" />
          <Stack.Screen name="register" />
          <Stack.Screen name="home" />
        </Stack>
        <StatusBar style="auto" />
      </AuthProvider>
    </ApolloProvider>
  );
}