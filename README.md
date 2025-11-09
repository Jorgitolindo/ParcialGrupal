# 💧 Sistema AguaPago - Microservicios

Sistema de gestión de pagos de agua desarrollado con arquitectura de microservicios usando Spring Boot y GraphQL.

## 📁 Estructura del Proyecto
ParcialGrupal/
├── usuarios-service/ # Microservicio de gestión de usuarios y clientes
├── api-gateway/ # API Gateway (punto de entrada único)
└── (futuros servicios)


## 🚀 Microservicios

### 1. **Usuarios Service** (Puerto 8081)
- ✅ Gestión de usuarios (ADMIN y CLIENTE)
- ✅ Gestión de perfiles de cliente
- ✅ Autenticación con BCrypt
- ✅ GraphQL API
- ✅ PostgreSQL

**Tecnologías:**
- Spring Boot 3.5.7
- Spring Data JPA
- Spring GraphQL
- PostgreSQL
- Lombok

### 2. **API Gateway** (Puerto 8080)
- ✅ Punto de entrada único
- ✅ Enrutamiento a microservicios
- ✅ CORS configurado
- ✅ Spring Cloud Gateway

## 🔧 Requisitos Previos

- ☕ Java JDK 17+
- 📦 Maven 3.9+
- 🐘 PostgreSQL 16+
- 🔧 Git

## 📊 Arquitectura
Frontend (Web/Mobile)
↓
API Gateway (8080)
↓
┌────────┬─────────┬──────────┐
↓ ↓ ↓ ↓
Usuarios Pagos Inventarios Pedidos
(8081) (8082) (8083) (8084)
↓ ↓ ↓ ↓
PostgreSQL (Base de datos por servicio)


