import { useState, useEffect, useRef } from "react";
import {
  createChart,
  CandlestickSeries,
  HistogramSeries,
  ColorType,
  type IChartApi,
} from "lightweight-charts";

const BACKEND_URL = "http://localhost:8000";

// OHLCV data from backend (matches Java PriceHistory entity)
interface OHLCV {
  tradeDate: string; // "2020-01-15"
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
}

function CandlestickChart() {
  // State: selected symbol (default AAPL, fallback when symbols load)
  const [symbol, setSymbol] = useState("AAPL");

  // State: list of symbols from GET /api/symbols
  const [symbols, setSymbols] = useState<string[]>([]);

  // State: OHLCV price data from GET /api/prices
  const [prices, setPrices] = useState<OHLCV[]>([]);

  // State: loading and error for UX
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Ref: DOM element for the chart (lightweight-charts needs a real DOM node)
  const chartContainerRef = useRef<HTMLDivElement>(null);
  const chartRef = useRef<IChartApi | null>(null);

  // Effect: fetch symbols on mount
  useEffect(() => {
    fetch(`${BACKEND_URL}/api/symbols`)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch symbols");
        return res.json();
      })
      .then((data: string[]) => {
        setSymbols(data);
        if (data.length > 0 && !data.includes(symbol)) {
          setSymbol(data[0]);
        }
      })
      .catch((err) => setError(err instanceof Error ? err.message : "Unknown error"));
  }, []);

  // Effect: fetch prices when symbol changes
  useEffect(() => {
    if (!symbol) return;
    setLoading(true);
    setError(null);
    const start = "2020-01-01";
    const end = "2021-01-01";
    fetch(`${BACKEND_URL}/api/prices?symbol=${encodeURIComponent(symbol)}&start=${start}&end=${end}`)
      .then((res) => {
        if (!res.ok) throw new Error(`Failed to fetch prices for ${symbol}`);
        return res.json();
      })
      .then((data: OHLCV[]) => {
        setPrices(data);
      })
      .catch((err) => {
        setError(err instanceof Error ? err.message : "Unknown error");
        setPrices([]);
      })
      .finally(() => setLoading(false));
  }, [symbol]);

  // Chart + series refs (keep series so we can update data without recreating)
  const candleSeriesRef = useRef<ReturnType<IChartApi["addSeries"]> | null>(null);
  const volSeriesRef = useRef<ReturnType<IChartApi["addSeries"]> | null>(null);

  // Effect: create chart once on mount, cleanup on unmount
  useEffect(() => {
    if (!chartContainerRef.current) return;
    const chart = createChart(chartContainerRef.current, {
      layout: {
        textColor: "#333",
        background: { type: ColorType.Solid, color: "#fff" },
      },
      width: chartContainerRef.current.clientWidth,
      height: 400,
      rightPriceScale: { borderVisible: false },
      timeScale: { borderVisible: true },
    });

    const candlestickSeries = chart.addSeries(CandlestickSeries, {
      upColor: "#26a69a",
      downColor: "#ef5350",
    });
    candlestickSeries.priceScale().applyOptions({
      scaleMargins: { top: 0.1, bottom: 0.4 },
    });

    const volumeSeries = chart.addSeries(HistogramSeries, {
      priceFormat: { type: "volume" },
      priceScaleId: "",
    });
    volumeSeries.priceScale().applyOptions({
      scaleMargins: { top: 0.7, bottom: 0 },
    });

    chartRef.current = chart;
    candleSeriesRef.current = candlestickSeries;
    volSeriesRef.current = volumeSeries;

    return () => {
      chart.remove();
      chartRef.current = null;
      candleSeriesRef.current = null;
      volSeriesRef.current = null;
    };
  }, []);

  // Effect: update chart data when prices change
  useEffect(() => {
    if (prices.length === 0 || !candleSeriesRef.current || !volSeriesRef.current) return;

    const candleData = prices.map((p) => ({
      time: p.tradeDate,
      open: p.open,
      high: p.high,
      low: p.low,
      close: p.close,
    }));
    const volData = prices.map((p) => ({
      time: p.tradeDate,
      value: p.volume,
      color: p.close >= p.open ? "#26a69a" : "#ef5350",
    }));

    candleSeriesRef.current.setData(candleData);
    volSeriesRef.current.setData(volData);
    chartRef.current?.timeScale().fitContent();
  }, [prices]);

  return (
    <div style={styles.container}>
      <h2>Candlestick Chart</h2>
      <div style={styles.controls}>
        <label>
          Symbol:{" "}
          <select
            value={symbol}
            onChange={(e) => setSymbol(e.target.value)}
            disabled={symbols.length === 0}
            style={styles.select}
          >
            {symbols.length === 0 && <option value="AAPL">Loading...</option>}
            {symbols.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </label>
      </div>
      {error && <p style={styles.error}>{error}</p>}
      {loading && <p>Loading prices...</p>}
      <div ref={chartContainerRef} style={styles.chart} />
    </div>
  );
}

const styles: Record<string, React.CSSProperties> = {
  container: {
    marginTop: "20px",
    padding: "16px",
    border: "1px solid #ddd",
    borderRadius: "8px",
    backgroundColor: "#fafafa",
  },
  controls: { marginBottom: "12px" },
  select: {
    padding: "6px 10px",
    fontSize: "14px",
  },
  error: { color: "#dc3545", marginBottom: "8px" },
  chart: { minHeight: "400px" },
};

export default CandlestickChart;
