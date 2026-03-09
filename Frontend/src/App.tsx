// ============================================================================
// MAIN APP COMPONENT
// This is the main entrypoint for the stock trading tool application.
// It manages the overall app state and layout.
// ============================================================================

import { useState, useEffect } from "react";
import { PortfolioComponent } from "./components/Portfolio";
import { MarketOrder } from "./components/MarketOrder";
import {
  loadPortfolio,
  Portfolio,
} from "./services/portfolioService";
import "./App.css";

const BACKEND_URL = "http://localhost:8000";

function App() {
  // ========================================================================
  // STATE: Portfolio
  // This holds the current portfolio state (cash balance and stocks)
  // It's similar to a member variable in a C++ class
  // ========================================================================
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);

  // ========================================================================
  // STATE: Backend connection (DB -> Backend -> Electron)
  // Checks if the backend API is reachable (backend connects to PostgreSQL)
  // ========================================================================
  const [backendStatus, setBackendStatus] = useState<"checking" | "connected" | "offline">("checking");

  // ========================================================================
  // EFFECT: Check backend connection on load
  // Fetches /api/symbols - if OK, backend (and its DB connection) is working
  // ========================================================================
  useEffect(() => {
    fetch(`${BACKEND_URL}/api/symbols`)
      .then((res) => {
        setBackendStatus(res.ok ? "connected" : "offline");
      })
      .catch(() => setBackendStatus("offline"));
  }, []);

  // ========================================================================
  // EFFECT: Load Portfolio on App Start
  // This runs once when the app first loads (empty dependency array means run once)
  // Similar to a C++ constructor or initialization function
  // ========================================================================
  useEffect(() => {
    // Load the portfolio from localStorage
    // If no portfolio exists, it will create one with default values
    const loadedPortfolio = loadPortfolio();
    setPortfolio(loadedPortfolio);
  }, []); // Empty array = run only on component mount

  // ========================================================================
  // HANDLER: Order Executed
  // This function is called when a market order is successfully executed
  // It updates the portfolio state in the app
  // ========================================================================
  const handleOrderExecuted = (updatedPortfolio: Portfolio) => {
    // Update the portfolio state with the new data
    // This will trigger a re-render of all components using this data
    // Similar to calling a C++ setter function to update state
    setPortfolio(updatedPortfolio);
  };

  // ========================================================================
  // RENDER
  // Show loading message while portfolio is loading
  // Once loaded, display the MarketOrder and Portfolio components
  // ========================================================================

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
        {/* Market Order Form - On Top */}
        <MarketOrder portfolio={portfolio} onOrderExecuted={handleOrderExecuted} />

        {/* Portfolio Display - Below */}
        <PortfolioComponent portfolio={portfolio} />
      </main>
    </div>
  );
}

// ============================================================================
// STYLING
// These are inline CSS styles for the main app layout
// In the future, these can be moved to separate CSS files
// ============================================================================
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
