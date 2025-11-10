import { ApolloClient, InMemoryCache, createHttpLink } from '@apollo/client';
import Constants from 'expo-constants';

const API_URL =
  Constants.expoConfig?.extra?.apiUrl ?? 'http://192.168.1.8:8080/graphql';

const httpLink = createHttpLink({
  uri: API_URL,
});

export const apolloClient = new ApolloClient({
  link: httpLink,
  cache: new InMemoryCache(),
});