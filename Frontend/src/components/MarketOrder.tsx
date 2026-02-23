// Market Order Component
// Displays a form to buy or sell stocks
import { useState } from "react";
import { Portfolio } from "../services/portfolioService";
import { getAllTickers, getStockByTicker } from "../services/mockData";

// Props for this component
interface MarketOrderProps {
  portfolio: Portfolio; // Current portfolio state
  onOrderExecuted: (portfolio: Portfolio) => void; // Called when order executes
}

export function MarketOrder({ portfolio, onOrderExecuted }: MarketOrderProps) {
  // Form state
  const [orderType, setOrderType] = useState<"buy" | "sell">("buy");
  const [selectedTicker, setSelectedTicker] = useState(getAllTickers()[0]);
  const [quantity, setQuantity] = useState<number | "">("");
  const [message, setMessage] = useState("");
  const [isError, setIsError] = useState(false);

  // Calculate estimated cost or proceeds with 1% fee
  const getEstimatedAmount = (): {
    baseCost: number;
    fee: number;
    total: number;
  } | null => {
    if (quantity === "" || quantity <= 0) {
      return null;
    }

    const stock = getStockByTicker(selectedTicker);
    if (!stock) {
      return null;
    }

    const baseCost = stock.currentPrice * quantity;
    const fee = baseCost * 0.01; // 1% broker fee
    const total = baseCost + fee;

    return { baseCost, fee, total };
  };

  const estimated = getEstimatedAmount();

  // Handle submitting an order
  const handleSubmitOrder = async () => {
    const { executeBuyOrder, executeSellOrder } = await import(
      "../services/portfolioService"
    );

    if (quantity === "" || quantity <= 0) {
      setMessage("Please enter a valid quantity");
      setIsError(true);
      return;
    }

    let result;
    if (orderType === "buy") {
      result = executeBuyOrder(portfolio, selectedTicker, quantity);
    } else {
      result = executeSellOrder(portfolio, selectedTicker, quantity);
    }

    setMessage(result.message);
    setIsError(!result.success);

    if (result.success && result.portfolio) {
      setQuantity("");
      onOrderExecuted(result.portfolio);

      setTimeout(() => {
        setMessage("");
      }, 3000);
    }
  };

  return (
    <div style={styles.container}>
      <h2>Market Order</h2>

      <div style={styles.formGroup}>
        <label>Order Type:</label>
        <select
          value={orderType}
          onChange={(e) =>
            setOrderType(e.target.value as "buy" | "sell")
          }
          style={styles.select}
        >
          <option value="buy">Buy</option>
          <option value="sell">Sell</option>
        </select>
      </div>

      {/* Ticker Symbol Selector */}
      <div style={styles.formGroup}>
        <label>Ticker Symbol:</label>
        <select
          value={selectedTicker}
          onChange={(e) => setSelectedTicker(e.target.value)}
          style={styles.select}
        >
          {getAllTickers().map((ticker) => (
            <option key={ticker} value={ticker}>
              {ticker}
            </option>
          ))}
        </select>
      </div>

      <div style={styles.formGroup}>
        <label>Quantity:</label>
        <input
          type="number"
          value={quantity}
          onChange={(e) => setQuantity(e.target.value === "" ? "" : Number(e.target.value))}
          placeholder="Enter number of shares"
          style={styles.input}
          min="1"
        />
      </div>

      {estimated && (
        <div style={styles.estimatedBox}>
          <p>
            <strong>Stock Price:</strong> $
            {getStockByTicker(selectedTicker)?.currentPrice.toFixed(2)}
          </p>
          <p>
            <strong>Base {orderType === "buy" ? "Cost" : "Proceeds"}:</strong> $
            {estimated.baseCost.toFixed(2)}
          </p>
          <p>
            <strong>Broker Fee (1%):</strong> $
            {estimated.fee.toFixed(2)}
          </p>
          <p style={styles.totalAmount}>
            <strong>
              Total {orderType === "buy" ? "Cost" : "Proceeds (after fee)"}:
            </strong>{" "}
            ${estimated.total.toFixed(2)}
          </p>
        </div>
      )}

      <button onClick={handleSubmitOrder} style={styles.submitButton}>
        Submit {orderType === "buy" ? "Buy" : "Sell"} Order
      </button>

      {message && (
        <div style={{ ...styles.message, borderColor: isError ? "red" : "green" }}>
          <p style={{ color: isError ? "red" : "green" }}>{message}</p>
        </div>
      )}
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

  formGroup: {
    marginBottom: "15px",
    display: "flex",
    flexDirection: "column" as const,
  } as React.CSSProperties,

  select: {
    padding: "8px",
    marginTop: "5px",
    fontSize: "14px",
    borderRadius: "4px",
    border: "1px solid #ddd",
  } as React.CSSProperties,

  input: {
    padding: "8px",
    marginTop: "5px",
    fontSize: "14px",
    borderRadius: "4px",
    border: "1px solid #ddd",
    maxWidth: "300px",
  } as React.CSSProperties,

  estimatedBox: {
    backgroundColor: "#fff",
    border: "2px solid #007bff",
    padding: "15px",
    marginBottom: "15px",
    borderRadius: "4px",
  } as React.CSSProperties,

  totalAmount: {
    fontSize: "16px",
    fontWeight: "bold",
    color: "#007bff",
    marginTop: "10px",
  } as React.CSSProperties,

  submitButton: {
    padding: "10px 20px",
    fontSize: "16px",
    backgroundColor: "#007bff",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
    width: "100%",
  } as React.CSSProperties,

  message: {
    marginTop: "15px",
    padding: "10px",
    borderLeft: "4px solid",
    borderRadius: "4px",
    backgroundColor: "#f0f0f0",
  } as React.CSSProperties,
};
