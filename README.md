# Prueba Técnica - Backend
_Este proyecto contiene el backend de Prueba Técnica_

---

## Comenzando 📟

Este proyecto está realizado con **Spring Boot 2.7.18**, utilizando **PostgreSQL 15.2** como base de datos.

### Pre-requisitos 📋

Para el desarrollo necesitarás **Docker** y **Docker Compose** instalados.

##### Este proyecto fue construido con 🛠️
- Spring Boot 2.7.18
- Maven 4.0
- Java 11
- PostgreSQL 15.2
- Flyway (migraciones de BD)

---

## Ejecución del proyecto 🚀

### Con Docker:

Para ejecutar el proyecto completo (API + Base de datos):

```bash
docker-compose up --build
```

Esto levantará:
- **API**: `http://localhost:8080`
- **PostgreSQL**: `localhost:5432`

### Endpoints disponibles:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`
- **Actuator**: `http://localhost:8080/actuator/logfile`

---

## Migraciones de Base de Datos 🗄️

Las migraciones se ejecutan automáticamente al iniciar la aplicación mediante **Flyway**.

**Ubicación de migraciones:**
src/main/resources/db/migration/
├── V1__initial_schema.sql
└── V2__initial_data.sql

**Datos de prueba incluidos:**
- **Usuario**: `testapt@yopmail.com`
- **Password**: `pruebaApt123`
- **Orden de ejemplo** con productos y pago aprobado

---

## Detener el proyecto 🛑

```bash
docker-compose down
```

Para eliminar volúmenes (reset completo de BD):
```bash
docker-compose down -v
```