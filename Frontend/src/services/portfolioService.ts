// Portfolio Service
// This file contains all the logic for managing the user's portfolio
// Portfolio data is stored in browser localStorage for now
// NOTE: Broker fee will be 1% of the total cost of the order for now
//       All sell market orders sell at the average cost price (static prices) for testing and simplicity


import { getStockByTicker } from "./mockData";

// Struct for single stock holding in the portfolio
export interface OwnedStock {
  ticker: string;      // Ticker symbol
  quantity: number;    // Number of shares owned
  averageCost: number; // Average cost per share
}

// Struct for user's portfolio
export interface Portfolio {
  cashBalance: number;  // Available cash in dollars
  stocks: OwnedStock[]; // Array of stocks user owns
}

// Key for storing portfolio in localStorage
const STORAGE_KEY = "trading_portfolio";

// Initial portfolio configuration
const INITIAL_PORTFOLIO: Portfolio = {
  cashBalance: 100000, // Starting with $100,000 in cash
  stocks: [],          // Start with no stocks owned
};

// Load portfolio from localStorage
// Creates initial portfolio if none exists
export function loadPortfolio(): Portfolio {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      return JSON.parse(stored);
    }
    // If first time user, create initial portfolio
    savePortfolio(INITIAL_PORTFOLIO);
    return INITIAL_PORTFOLIO;
  } catch (error) {
    console.error("Error loading portfolio:", error);
    savePortfolio(INITIAL_PORTFOLIO);
    return INITIAL_PORTFOLIO;
  }
}

// Save portfolio to localStorage
export function savePortfolio(portfolio: Portfolio): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(portfolio));
  } catch (error) {
    console.error("Error saving portfolio:", error);
  }
}

// Execute a buy order
// Validates cash, deducts fee, updates portfolio
export function executeBuyOrder(
  portfolio: Portfolio,
  ticker: string,
  quantity: number
): { success: boolean; message: string; portfolio?: Portfolio } {
  if (quantity <= 0) {
    return { success: false, message: "Quantity must be greater than 0" };
  }

  const stock = getStockByTicker(ticker);
  if (!stock) {
    return { success: false, message: "Stock not found" };
  }

  // Calculate cost with 1% broker fee
  const baseCost = stock.currentPrice * quantity;
  const brokerFee = baseCost * 0.01;
  const totalCost = baseCost + brokerFee;

  if (portfolio.cashBalance < totalCost) {
    return {
      success: false,
      message: `Insufficient cash. You need $${totalCost.toFixed(2)}, but only have $${portfolio.cashBalance.toFixed(2)}`,
    };
  }

  const updatedPortfolio = { ...portfolio };
  updatedPortfolio.cashBalance -= totalCost;

  // Add or update stock in portfolio
  const existingStock = updatedPortfolio.stocks.find((s) => s.ticker === ticker);

  if (existingStock) {
    // Update average cost when buying more
    const oldTotal = existingStock.quantity * existingStock.averageCost;
    const newTotal = quantity * stock.currentPrice;
    existingStock.quantity += quantity;
    existingStock.averageCost = (oldTotal + newTotal) / existingStock.quantity;
  } else {
    updatedPortfolio.stocks.push({
      ticker,
      quantity,
      averageCost: stock.currentPrice,
    });
  }

  savePortfolio(updatedPortfolio);

  return {
    success: true,
    message: `Successfully bought ${quantity} shares of ${ticker} for $${totalCost.toFixed(2)} (including 1% fee)`,
    portfolio: updatedPortfolio,
  };
}

// Execute a sell order
// Validates ownership, deducts fee, updates portfolio
export function executeSellOrder(
  portfolio: Portfolio,
  ticker: string,
  quantity: number
): { success: boolean; message: string; portfolio?: Portfolio } {
  if (quantity <= 0) {
    return { success: false, message: "Quantity must be greater than 0" };
  }

  const ownedStock = portfolio.stocks.find((s) => s.ticker === ticker);

  if (!ownedStock) {
    return {
      success: false,
      message: `You don't own any ${ticker} shares`,
    };
  }

  if (ownedStock.quantity < quantity) {
    return {
      success: false,
      message: `You only own ${ownedStock.quantity} shares of ${ticker}, but are trying to sell ${quantity}`,
    };
  }

  const stock = getStockByTicker(ticker);
  if (!stock) {
    return { success: false, message: "Stock not found" };
  }

  // Calculate proceeds with 1% broker fee
  // Note: selling at average cost for now (static prices)
  const baseProceeds = ownedStock.averageCost * quantity;
  const brokerFee = baseProceeds * 0.01;
  const netProceeds = baseProceeds - brokerFee;

  const updatedPortfolio = { ...portfolio };
  updatedPortfolio.cashBalance += netProceeds;

  // Update stock holdings
  ownedStock.quantity -= quantity;

  if (ownedStock.quantity === 0) {
    // Remove stock if sold all shares
    updatedPortfolio.stocks = updatedPortfolio.stocks.filter(
      (s) => s.ticker !== ticker
    );
  }

  savePortfolio(updatedPortfolio);

  return {
    success: true,
    message: `Successfully sold ${quantity} shares of ${ticker} for $${netProceeds.toFixed(2)} (after 1% fee)`,
    portfolio: updatedPortfolio,
  };
}
