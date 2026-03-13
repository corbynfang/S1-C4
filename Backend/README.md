# Backend

Java 21 + Spring Boot + PostgreSQL. REST API for price history, trade journal, and student accounts.

## Run

1. PostgreSQL must be running with database `s1c4database`
2. Run schema if needed:
   ```bash
   psql -U your_user -d s1c4database -f src/main/resources/sql/schema.sql
   psql -U your_user -d s1c4database -f src/main/resources/sql/schema-accounts.sql
   ```
3. Set DB credentials in `src/main/resources/application.properties`
4. `./gradlew bootRun`

Server runs on port 8000.

## API Reference

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
| POST | `/api/accounts` | Create account |
| GET | `/api/accounts/{id}/trades` | Trades for this account |
| GET | `/api/accounts/{id}/progress` | Progress summary (trade count, discipline, win/loss) |

## Test the API

```bash
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

curl -X POST "http://localhost:8000/api/accounts" \
  -H "Content-Type: application/json" \
  -d '{"displayName":"Jane Student","email":"jane@university.edu"}'

curl "http://localhost:8000/api/accounts/1/trades"
curl "http://localhost:8000/api/accounts/1/progress"
```

## Run Tests

```bash
./gradlew test
```
