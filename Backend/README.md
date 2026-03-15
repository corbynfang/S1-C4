# Backend

Java 21 + Spring Boot + PostgreSQL. REST API for price history, trade journal, student accounts, and auth (register/login).

## Run

1. **Start PostgreSQL** (from project root, where `docker-compose.yml` is):
   ```bash
   docker compose up -d
   ```
   Schema scripts run automatically on **first** startup only (`01-schema.sql`, then `02-schema-accounts.sql`). If the DB already existed without the `accounts` table, apply the accounts schema manually:
   ```bash
   docker exec -i s1c4-postgres psql -U postgres -d s1c4database < Backend/src/main/resources/sql/schema-accounts.sql
   ```
   To reset the DB and re-run all init scripts: `docker compose down -v && docker compose up -d`.

2. **Configure** `src/main/resources/application.properties` (copy from `application.properties.example` if needed). Docker defaults: `postgres`/`postgres`, database `s1c4database`, port 5432.

3. **Start the server:**
   ```bash
   ./gradlew bootRun
   ```
   Server runs on **port 8000**.

## API Reference

### Auth (register / login)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new account (body: username, password, displayName, riskTolerance). Returns account without passwordHash. |
| POST | `/api/auth/login` | Login (body: username, password). Returns account info on success, 401 on failure. |

### Price History (for charts)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/symbols` | List of all ticker symbols |
| GET | `/api/prices?symbol=AAPL&start=2020-01-01&end=2021-01-01` | OHLCV price history |

### Trade Journal

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/trades` | All trades |
| GET | `/api/trades/{id}` | Single trade |
| GET | `/api/trades/summary` | Behavioral stats (total trades, avg discipline, win/loss, violations) |
| POST | `/api/trades?portfolioValue=100000` | Log a trade |
| PUT | `/api/trades/{id}/notes` | Update reflection notes |

### Accounts (student progress tracking)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/accounts` | List all accounts |
| GET | `/api/accounts/{id}` | Get account by ID |
| POST | `/api/accounts` | Create account (requires full entity; prefer `POST /api/auth/register` for new users) |
| GET | `/api/accounts/{id}/trades` | Trades for this account |
| GET | `/api/accounts/{id}/progress` | Progress summary (trade count, discipline, win/loss) |

## Test the API

```bash
# Auth: register
curl -X POST http://localhost:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"student1","password":"secret123","displayName":"Alice","riskTolerance":"MODERATE"}'

# Auth: login (200 = success, 401 = wrong password)
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"student1","password":"secret123"}'

# Symbols
curl "http://localhost:8000/api/symbols"

# Prices
curl "http://localhost:8000/api/prices?symbol=AAPL&start=2020-01-01&end=2021-01-01"

# Trades
curl "http://localhost:8000/api/trades"
curl "http://localhost:8000/api/trades/summary"

# Log a trade (optional: include accountId to link to student)
curl -X POST "http://localhost:8000/api/trades?portfolioValue=100000" \
  -H "Content-Type: application/json" \
  -d '{"symbol":"AAPL","tradeType":"BUY","shares":10,"entryPrice":150.50,"tradeDate":"2024-01-15","thesis":"Test","stopLoss":145,"targetPrice":160,"strategyTag":"swing"}'

# Accounts
curl "http://localhost:8000/api/accounts"
curl "http://localhost:8000/api/accounts/1/trades"
curl "http://localhost:8000/api/accounts/1/progress"
```

## Run Tests

```bash
./gradlew test
```
