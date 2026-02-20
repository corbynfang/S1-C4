package com.example.Backend.service;

import com.example.Backend.dto.CsvPriceRow;
import com.example.Backend.entity.PriceHistory;
import com.example.Backend.repository.PriceHistoryRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads CSV files from classpath:datasets/*.csv into the price_history table.
 * Flow: find CSVs -> parse each file -> convert rows to PriceHistory -> saveAll in batches.
 */
@Service
public class CsvPriceRowService {

    private final PriceHistoryRepository repository;

    // Prefer constructor injection over @Autowired on field - it's easier to test
    public CsvPriceRowService(PriceHistoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Load all CSV files from classpath:datasets/*.csv
     * Symbol is extracted from filename: ko_us_d.csv -> KO
     */
    @Transactional
    public void loadAllDatasets() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:datasets/*.csv");

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null) continue;

                // Extract symbol: ko_us_d.csv -> KO (uppercase, before _us_d)
                String symbol = filename.replace("_us_d.csv", "").toUpperCase();
                loadFromResource(resource, symbol);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load CSV datasets", e);
        }
    }

    /**
     * Load one CSV file from a Spring Resource (works with classpath).
     * We use Resource instead of file path so it works in JARs too.
     */
    private void loadFromResource(Resource resource, String symbol) throws Exception {
        List<CsvPriceRow> rows = parseCsv(resource);
        List<PriceHistory> entities = new ArrayList<>();

        for (CsvPriceRow row : rows) {
            PriceHistory entity = convertToEntity(row, symbol);
            entities.add(entity);
        }

        // saveAll is much faster than save() in a loop - one batch per file
        repository.saveAll(entities);
    }

    /**
     * Parse CSV: read line by line, split by comma, skip header.
     * Same idea as C++: open file, read lines, parse fields.
     */
    private List<CsvPriceRow> parseCsv(Resource resource) throws Exception {
        List<CsvPriceRow> rows = new ArrayList<>();

        try (var reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String header = reader.readLine(); // skip "Date,Open,High,Low,Close,Volume"
            if (header == null) return rows;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length != 6) continue;

                CsvPriceRow row = new CsvPriceRow(
                    parts[0].trim(),  // Date
                    parts[1].trim(),  // Open
                    parts[2].trim(),  // High
                    parts[3].trim(),  // Low
                    parts[4].trim(),  // Close
                    parts[5].trim()   // Volume
                );
                rows.add(row);
            }
        }
        return rows;
    }

    /**
     * Convert CsvPriceRow (all Strings) to PriceHistory (proper types).
     * PriceHistory expects LocalDate and BigDecimal - we MUST convert here.
     */
    private PriceHistory convertToEntity(CsvPriceRow row, String symbol) {
        PriceHistory entity = new PriceHistory();
        entity.setSymbol(symbol);
        entity.setTradeDate(LocalDate.parse(row.getDate()));  // "1970-01-02" -> LocalDate
        entity.setOpen(new BigDecimal(row.getOpen()));
        entity.setHigh(new BigDecimal(row.getHigh()));
        entity.setLow(new BigDecimal(row.getLow()));
        entity.setClose(new BigDecimal(row.getClose()));
        entity.setVolume(new BigDecimal(row.getVolume()));
        return entity;
    }
}
