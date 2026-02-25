# S1-C4 Project

Java 21 + Spring Boot + PostgreSQL.

## Quick Start (Docker)

1. **Start PostgreSQL:**
   ```bash
   cd S1-C4
   docker compose up -d
   ```
   Schema runs automatically on first start.

2. **Configure the app:**
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   Edit if needed. Docker uses `postgres` / `postgres` by default.

3. **Load CSV data (first time only):**  
   Set `app.load-csv=true` in `application.properties`, then run. Set back to `false` after.

4. **Run the backend:**
   ```bash
   ./gradlew bootRun
   ```

## Manual Setup (no Docker)

1. Install PostgreSQL, create database `s1c4database`
2. Run schema: `psql -U your_user -d s1c4database -f src/main/resources/sql/schema.sql`
3. Copy `application.properties.example` to `application.properties` and set your DB credentials
4. Run `./gradlew bootRun`
