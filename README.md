# S1-C4 Project

Paper trading simulation desktop app for evaluating portfolio decisions, risk management, and behavioral consistency.

## Tech Stack

| Layering     | Technology              |
|-----------|-------------------------|
| Backend   | Java 21, Spring Boot, Gradle |
| Database  | PostgreSQL              |
| Frontend  | Electron + Vite + React + TypeScript (not yet developed) |
| Backend   | `localhost:8000`        |
| Frontend  | `localhost:3000` (when running) |

## Backend (Current Focus)

The backend provides:

- **Database:** PostgreSQL with `securities` and `price_history` tables
- **CSV loader:** Loads 15 datasets from `Backend/src/main/resources/datasets/*.csv` into the database
- **Data:** Historical OHLCV for AAPL, SPY, KO, MSFT, JPM, and others

### Quick Start (Docker)

```bash
# 1. Start PostgreSQL
docker compose up -d

# 2. Configure Backend
cd Backend
cp src/main/resources/application.properties.example src/main/resources/application.properties

# 3. Load CSV data (first time only)
# Set app.load-csv=true in application.properties, run once, then set to false (Very Important)

# 4. Run
./gradlew bootRun
```

See [Backend/README.md](Backend/README.md) for manual setup and details.

## Frontend

Electron + React + TypeScript app scaffolded. Not yet developed. Will run on `localhost:3000` and connect to the backend API.

## Database

- **Tables:** `securities`, `price_history`
- **Schema:** `Backend/src/main/resources/sql/schema.sql`
- **Credentials (Docker):** `postgres` / `postgres`, database `s1c4database`

## Ports

| Service   | Port |
|----------|------|
| Backend  | 8000 |
| Frontend | 3000 |
| PostgreSQL | 5432 |
