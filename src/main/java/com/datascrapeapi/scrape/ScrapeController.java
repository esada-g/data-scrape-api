package com.datascrapeapi.scrape;

import com.datascrapeapi.exceptions.StockDataException;
import com.datascrapeapi.exceptions.TickerDoesNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/scrape")
public class ScrapeController {
    private final ScrapeService scrapeService;

    public ScrapeController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    @PostMapping("/")
    public ResponseEntity<List<ScrapeDataResponse>> scrapeData(@RequestBody ScrapeDataRequestBody scrapeDataRequestBody) {
        List<ScrapeDataResponse> responses = scrapeService.scrapeData(scrapeDataRequestBody.getDate(), scrapeDataRequestBody.getTickers());
        boolean hasError = responses.stream().anyMatch(response -> !response.isOk());

        if (hasError) {
            return ResponseEntity.badRequest().body(responses);
        } else {
            return ResponseEntity.ok(responses);
        }
    }
}
