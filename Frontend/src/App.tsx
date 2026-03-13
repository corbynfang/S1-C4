import { useState, useEffect } from "react";
import { PortfolioComponent } from "./components/Portfolio";
import { MarketOrder } from "./components/MarketOrder";
import CandlestickChart from "./components/CandlestickChart";
import {
  loadPortfolio,
  Portfolio,
} from "./services/portfolioService";
import "./App.css";

const BACKEND_URL = "http://localhost:8000";

function App() {
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);
  // Check if the backend API is reachable (backend connects to PostgreSQL)
  const [backendStatus, setBackendStatus] = useState<"checking" | "connected" | "offline">("checking");

  useEffect(() => {
    fetch(`${BACKEND_URL}/api/symbols`)
      .then((res) => {
        setBackendStatus(res.ok ? "connected" : "offline");
      })
      .catch(() => setBackendStatus("offline"));
  }, []);

  useEffect(() => {
    // Load the portfolio from localStorage
    // If no portfolio exists, it will create one with default values
    const loadedPortfolio = loadPortfolio();
    setPortfolio(loadedPortfolio);
  }, []); // Empty array = run only on component mount

  const handleOrderExecuted = (updatedPortfolio: Portfolio) => {
    // Update the portfolio state with the new data
    // This will trigger a re-render of all components using this data
    // Similar to calling a C++ setter function to update state
    setPortfolio(updatedPortfolio);
  };

  if (portfolio === null) {
    return <div>Loading portfolio...</div>;
  }

  return (
    <div style={styles.appContainer}>
      {/* Header */}
      <header style={styles.header}>
        <h1>Stock Trading Tool</h1>
        <div style={styles.status}>
          Backend:{" "}
          {backendStatus === "checking" && "Checking..."}
          {backendStatus === "connected" && <span style={styles.connected}>Connected (DB ok)</span>}
          {backendStatus === "offline" && <span style={styles.offline}>Offline – start Backend on port 8000</span>}
        </div>
      </header>

      {/* Main Content */}
      <main style={styles.main}>
        {/* Candlestick Chart - Price history from backend */}
        <CandlestickChart />

        {/* Market Order Form */}
        <MarketOrder portfolio={portfolio} onOrderExecuted={handleOrderExecuted} />

        {/* Portfolio Display */}
        <PortfolioComponent portfolio={portfolio} />
      </main>
    </div>
  );
}

/* CSS styles for the main app layout */
const styles = {
  appContainer: {
    maxWidth: "1200px",
    margin: "0 auto",
    padding: "20px",
    fontFamily: "Arial, sans-serif",
  } as React.CSSProperties,

  header: {
    textAlign: "center" as const,
    marginBottom: "30px",
    borderBottom: "2px solid #007bff",
    paddingBottom: "20px",
  } as React.CSSProperties,

  status: {
    marginTop: "8px",
    fontSize: "14px",
  } as React.CSSProperties,

  connected: {
    color: "#28a745",
    fontWeight: "bold",
  } as React.CSSProperties,

  offline: {
    color: "#dc3545",
  } as React.CSSProperties,

  main: {
    display: "flex",
    flexDirection: "column" as const,
  } as React.CSSProperties,
};

export default App;
