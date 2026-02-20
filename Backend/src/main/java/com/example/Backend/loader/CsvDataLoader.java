package com.example.Backend.loader;

import com.example.Backend.service.CsvPriceRowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


// Runs once when the app starts. Only active when app.load-csv=true in application.properties.
// Delegates to CsvPriceRowService to load all datasets/*.csv into the database.
@Component
@ConditionalOnProperty(name = "app.load-csv", havingValue = "true")
public class CsvDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);
    private final CsvPriceRowService csvService;

    public CsvDataLoader(CsvPriceRowService csvService) {
        this.csvService = csvService;
    }

    @Override
    public void run(String... args) {
        log.info("Loading CSV datasets into database...");
        csvService.loadAllDatasets();
        log.info("CSV load complete.");
    }
}
