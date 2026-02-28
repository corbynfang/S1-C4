# S1-C4 Project

Paper trading simulation desktop app for evaluating portfolio decisions, risk management, and behavioral consistency.

## Tech Stack

| Layer      | Technology                      |
|------------|---------------------------------|
| Backend    | Java 21, Spring Boot, Gradle    |
| Database   | PostgreSQL                      |
| Frontend   | Electron + Vite + React + TypeScript |

| Service    | URL                   |
|------------|-----------------------|
| Backend    | `http://localhost:8000` |
| Frontend   | `http://localhost:3000` |
| PostgreSQL | `localhost:5432`      |

## Project Structure

```
S1-C4/
├── Backend/          # Spring Boot REST API
├── Frontend/         # Electron + React app
├── docker-compose.yml
└── README.md         # This file
```

## Quick Start

**Run full stack (all offline):**

```bash
# 1. Start PostgreSQL in background
docker compose up -d

# 2. Run Backend (connects to DB) – leave running
cd Backend
./gradlew bootRun

# 3. Run Frontend / Electron – in a new terminal
cd Frontend
npm install
npm run dev
```

The app shows **"Backend: Connected (DB ok)"** in the header when PostgreSQL → Backend → Electron is working. If you see **"Offline"**, ensure the Backend is running on port 8000.

**First-time setup:** Copy `Backend/src/main/resources/application.properties.example` to `application.properties`. For Docker PostgreSQL use `postgres`/`postgres`.

## Backend

REST API for price history and trade journal. See [Backend/README.md](Backend/README.md) for run instructions and API tests.

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/symbols` | All stock symbols |
| GET | `/api/prices?symbol=AAPL&start=2020-01-01&end=2021-01-01` | Price history for chart |
| GET | `/api/trades` | All trades |
| GET | `/api/trades/{id}` | Single trade |
| GET | `/api/trades/summary` | Behavioral stats |
| POST | `/api/trades?portfolioValue=100000` | Log a trade |
| PUT | `/api/trades/{id}/notes` | Update reflection notes |

### Features

- **Price History** – Historical OHLCV data from CSV datasets (AAPL, KO, MSFT, etc.)
- **Trade Journal** – Log trades, discipline scoring (0–100), violations, behavioral summary

## Frontend

Electron desktop app with React. Stock trading tool with portfolio and market order components. See [Frontend/README.md](Frontend/README.md). (Still in development)

## Database

| Table          | Purpose                              |
|----------------|--------------------------------------|
| securities     | Security metadata                    |
| price_history  | OHLCV price data                     |
| trade_journal  | Trades with discipline score & notes |

Schema: `Backend/src/main/resources/sql/schema.sql`

Docker default: user `postgres`, password `postgres`, database `s1c4database`

## Ports

| Service   | Port |
|-----------|------|
| Backend   | 8000 |
| Frontend  | 3000 |
| PostgreSQL| 5432 |
