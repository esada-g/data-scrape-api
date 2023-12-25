package com.datascrapeapi.scrape;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ScrapeDataRequestBody {
    private String date;
    private List<String> tickers;
}
