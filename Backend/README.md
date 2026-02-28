# Backend

Java 21 + Spring Boot + PostgreSQL. REST API for price history and trade journal.

## Run

1. PostgreSQL must be running with database `s1c4database`
2. Run schema if needed: `psql -U your_user -d s1c4database -f src/main/resources/sql/schema.sql`
3. Set DB credentials in `src/main/resources/application.properties`
4. `./gradlew bootRun`

Server runs on port 8000.

## Test the API

```bash
# Get symbols
curl "http://localhost:8000/api/symbols"

# Get prices (replace symbol/dates with values you have)
curl "http://localhost:8000/api/prices?symbol=AAPL&start=2020-01-01&end=2021-01-01"

# Get all trades
curl "http://localhost:8000/api/trades"

# Get trade summary
curl "http://localhost:8000/api/trades/summary"

# Log a trade
curl -X POST "http://localhost:8000/api/trades?portfolioValue=100000" \
  -H "Content-Type: application/json" \
  -d '{"symbol":"AAPL","tradeType":"BUY","shares":10,"entryPrice":150.50,"tradeDate":"2024-01-15","thesis":"Test","stopLoss":145,"targetPrice":160,"strategyTag":"swing"}'
```
