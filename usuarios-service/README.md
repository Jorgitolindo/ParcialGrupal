# 💧 Microservicio de Usuarios - Sistema de Gestión de Agua

Microservicio desarrollado con Spring Boot y GraphQL para la gestión de usuarios y clientes en un sistema de pago de servicios de agua.

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.5.7**
- **GraphQL**
- **PostgreSQL 16**
- **Maven 3.9+**
- **Spring Security**
- **JPA/Hibernate**
- **Lombok**

---

## 📋 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- [Java JDK 17 o superior](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [PostgreSQL 16+](https://www.postgresql.org/download/)
- [Git](https://git-scm.com/downloads)

---

## 🔧 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/usuarios-service.git
cd usuarios-service
```

### 2. Crear la base de datos

Abre una terminal de PostgreSQL o usa PgAdmin y ejecuta:

```sql
CREATE DATABASE aguapago_db;
```

### 3. Configurar credenciales de PostgreSQL

Edita el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/aguapago_db
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA_AQUI
```

**Reemplaza `TU_CONTRASEÑA_AQUI` con tu contraseña de PostgreSQL.**

---

## ▶️ Ejecución

### Opción 1: Con Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Opción 2: Con Maven Wrapper (sin Maven instalado)

```bash
./mvnw spring-boot:run
```

La aplicación se iniciará en: **http://localhost:8080**

---

## 🧪 Probar la API

### GraphiQL (Interfaz Web)

Abre tu navegador en: **http://localhost:8080/graphiql**

### Ejemplo de registro de usuario

```graphql
mutation {
  registrarUsuario(input: {
    nombre: "Juan"
    apellido: "Perez"
    email: "juan@gmail.com"
    password: "123456"
    rol: "CLIENTE"
    direccion: "Av. 6 de Agosto #123"
    telefono: "77123456"
  }) {
    id
    nombre
    email
    rol
  }
}
```

### Ejemplo de login

```graphql
mutation {
  login(input: {
    email: "juan@gmail.com"
    password: "123456"
  }) {
    id
    nombre
    email
    rol
    mensaje
  }
}
```

---

## 📊 Estructura del Proyecto
