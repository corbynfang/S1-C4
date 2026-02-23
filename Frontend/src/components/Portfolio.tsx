// Portfolio Component
// Displays the user's cash balance and owned stocks

import { Portfolio } from "../services/portfolioService";
import { getStockByTicker } from "../services/mockData";

// Props for this component
interface PortfolioProps {
  portfolio: Portfolio; // Current portfolio state
}

export function PortfolioComponent({ portfolio }: PortfolioProps) {
  // Calculate total portfolio value (cash + all stock positions)
  const calculatePortfolioValue = (): number => {
    let total = portfolio.cashBalance;

    portfolio.stocks.forEach((stock) => {
      const stockInfo = getStockByTicker(stock.ticker);
      if (stockInfo) {
        total += stock.quantity * stockInfo.currentPrice;
      }
    });

    return total;
  };

  const totalValue = calculatePortfolioValue();

  return (
    <div style={styles.container}>
      <h2>Portfolio</h2>

      <div style={styles.balanceBox}>
        <h3>Cash Balance: ${portfolio.cashBalance.toFixed(2)}</h3>
      </div>

      <div style={styles.holdingsSection}>
        <h3>Holdings</h3>

        {portfolio.stocks.length === 0 ? (
          <p style={styles.noStocks}>You don't own any stocks yet.</p>
        ) : (
          // Display stocks in a table format
          <table style={styles.table}>
            <thead>
              <tr style={styles.tableHeader}>
                <th style={styles.tableCell}>Ticker</th>
                <th style={styles.tableCell}>Quantity</th>
                <th style={styles.tableCell}>Avg Cost</th>
                <th style={styles.tableCell}>Current Value</th>
              </tr>
            </thead>
            <tbody>
              {portfolio.stocks.map((stock) => {
                const stockInfo = getStockByTicker(stock.ticker);
                const currentValue = stock.quantity * (stockInfo?.currentPrice || 0);

                return (
                  <tr key={stock.ticker} style={styles.tableRow}>
                    <td style={styles.tableCell}>{stock.ticker}</td>
                    <td style={styles.tableCell}>{stock.quantity}</td>
                    <td style={styles.tableCell}>
                      ${stock.averageCost.toFixed(2)}
                    </td>
                    <td style={styles.tableCell}>
                      ${currentValue.toFixed(2)}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>

      <div style={styles.totalBox}>
        <h3>Total Portfolio Value: ${totalValue.toFixed(2)}</h3>
      </div>
    </div>
  );
}

// Inline styles for this component
// Can be moved to separate CSS file later
const styles = {
  container: {
    border: "1px solid #ccc",
    padding: "20px",
    margin: "20px 0",
    borderRadius: "8px",
    backgroundColor: "#f9f9f9",
  } as React.CSSProperties,

  balanceBox: {
    backgroundColor: "#e8f5e9",
    border: "2px solid #4caf50",
    padding: "15px",
    marginBottom: "20px",
    borderRadius: "4px",
  } as React.CSSProperties,

  holdingsSection: {
    marginBottom: "20px",
  } as React.CSSProperties,

  noStocks: {
    fontStyle: "italic",
    color: "#666",
    padding: "20px",
    backgroundColor: "#f5f5f5",
    borderRadius: "4px",
    textAlign: "center",
  } as React.CSSProperties,

  table: {
    width: "100%",
    borderCollapse: "collapse" as const,
    marginBottom: "20px",
  } as React.CSSProperties,

  tableHeader: {
    backgroundColor: "#f0f0f0",
    fontWeight: "bold",
  } as React.CSSProperties,

  tableRow: {
    borderBottom: "1px solid #ddd",
  } as React.CSSProperties,

  tableCell: {
    padding: "12px",
    textAlign: "right" as const,
  } as React.CSSProperties,

  totalBox: {
    backgroundColor: "#fff3e0",
    border: "2px solid #ff9800",
    padding: "15px",
    borderRadius: "4px",
  } as React.CSSProperties,
};
