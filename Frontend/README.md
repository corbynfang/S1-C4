# Frontend — Electron + React + TypeScript + Vite

Stock trading tool UI. Components: CandlestickChart (real backend data), MarketOrder, Portfolio.

## CandlestickChart Component

`src/components/CandlestickChart.tsx` fetches OHLCV data from the backend and renders a candlestick + volume chart using [lightweight-charts](https://tradingview.github.io/lightweight-charts/).

**Concepts used:**
- `useState` — selected symbol, symbols list, prices, loading, error
- `useEffect` — fetch `/api/symbols` on mount, fetch `/api/prices` when symbol changes
- `useRef` — DOM container for the chart, chart/series instances for updates
- TypeScript `interface OHLCV` — matches backend `PriceHistory` entity

**Backend endpoints:**
- `GET /api/symbols` → `string[]`
- `GET /api/prices?symbol=AAPL&start=2020-01-01&end=2021-01-01` → OHLCV array

## Replacing mockData with Real API Calls

`MarketOrder` and `Portfolio` currently use `mockData.ts` for tickers and prices. To use the backend instead:

1. **Symbols:** Replace `getAllTickers()` and `getStockByTicker()` with a fetch to `GET /api/symbols` (plus optional metadata from another endpoint if you add one).
2. **Current price:** The backend has historical OHLCV, not “current” price. Options: use latest close from `/api/prices`, or add a `GET /api/quote?symbol=X` endpoint.
3. **Same pattern as CandlestickChart:** `useState` for data, `useEffect` to fetch on mount or when inputs change, handle loading/error.

---

# React + TypeScript + Vite (Template Notes)

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type aware lint rules:

- Configure the top-level `parserOptions` property like this:

```js
export default {
  // other rules...
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
    project: ['./tsconfig.json', './tsconfig.node.json'],
    tsconfigRootDir: __dirname,
  },
}
```

- Replace `plugin:@typescript-eslint/recommended` to `plugin:@typescript-eslint/recommended-type-checked` or `plugin:@typescript-eslint/strict-type-checked`
- Optionally add `plugin:@typescript-eslint/stylistic-type-checked`
- Install [eslint-plugin-react](https://github.com/jsx-eslint/eslint-plugin-react) and add `plugin:react/recommended` & `plugin:react/jsx-runtime` to the `extends` list
