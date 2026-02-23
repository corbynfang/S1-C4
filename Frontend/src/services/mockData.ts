// ============================================================================
// MOCK DATA SERVICE
// This file contains hardcoded mock data that simulates what will eventually
// be pulled from our backend database in the future.
// ============================================================================

// Define the structure for a stock's company information
// This is similar to a C++ struct that holds related data together
export interface StockInfo {
  ticker: string;           // Ticker symbol (e.g., "AAPL")
  companyName: string;      // Full company name
  sector: string;           // Industry sector (e.g., "Technology")
  description: string;      // Brief description of the company
  currentPrice: number;     // Current price per share (STATIC FOR NOW)
}

// This is a mock database of 10 well-known stocks
// In the future, this will be fetched from our backend API
// Like a C++ std::vector<StockInfo> but as a hardcoded constant
export const MOCK_STOCKS: StockInfo[] = [
  {
    ticker: "AAPL",
    companyName: "Apple Inc.",
    sector: "Technology",
    description: "Consumer electronics and software company.",
    currentPrice: 150.25,
  },
  {
    ticker: "MSFT",
    companyName: "Microsoft Corporation",
    sector: "Technology",
    description: "Cloud computing and software company.",
    currentPrice: 380.50,
  },
  {
    ticker: "GOOGL",
    companyName: "Alphabet Inc.",
    sector: "Technology",
    description: "Search engine and advertising company.",
    currentPrice: 145.30,
  },
  {
    ticker: "AMZN",
    companyName: "Amazon.com Inc.",
    sector: "Consumer",
    description: "E-commerce and cloud services company.",
    currentPrice: 178.75,
  },
  {
    ticker: "NVDA",
    companyName: "NVIDIA Corporation",
    sector: "Technology",
    description: "GPU and AI chip manufacturer.",
    currentPrice: 875.43,
  },
  {
    ticker: "TSLA",
    companyName: "Tesla Inc.",
    sector: "Automotive",
    description: "Electric vehicles and energy storage.",
    currentPrice: 245.60,
  },
  {
    ticker: "META",
    companyName: "Meta Platforms Inc.",
    sector: "Technology",
    description: "Social media and metaverse company.",
    currentPrice: 520.15,
  },
  {
    ticker: "JPM",
    companyName: "JPMorgan Chase & Co.",
    sector: "Finance",
    description: "Banking and financial services company.",
    currentPrice: 195.80,
  },
  {
    ticker: "JNJ",
    companyName: "Johnson & Johnson",
    sector: "Healthcare",
    description: "Pharmaceutical and healthcare products.",
    currentPrice: 155.40,
  },
  {
    ticker: "XOM",
    companyName: "Exxon Mobil Corporation",
    sector: "Energy",
    description: "Oil and gas exploration company.",
    currentPrice: 115.25,
  },
];

// Helper function to get stock info by ticker symbol
// This is like a C++ map lookup or binary search function
// It searches through MOCK_STOCKS and returns the matching stock or undefined
export function getStockByTicker(ticker: string): StockInfo | undefined {
  return MOCK_STOCKS.find((stock) => stock.ticker === ticker);
}

// Helper function to get all ticker symbols as an array
// Useful for populating dropdown menus
// Similar to C++ std::vector::data() or just creating a list of keys
export function getAllTickers(): string[] {
  return MOCK_STOCKS.map((stock) => stock.ticker);
}
