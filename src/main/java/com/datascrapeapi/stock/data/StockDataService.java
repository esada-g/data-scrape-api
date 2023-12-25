package com.datascrapeapi.stock.data;

import com.datascrapeapi.company.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class StockDataService {

    private final StockDataRepository stockDataRepository;

    public StockDataService(StockDataRepository stockDataRepository) {
        this.stockDataRepository = stockDataRepository;
    }

    public Optional<StockData> getStockDataByCompanyIdAndTargetDate(Long companyId, Date targetDate) {
        return stockDataRepository.findByCompanyIdAndTargetDate(companyId, targetDate);
    }

    @Transactional
    public StockData saveStockData(StockDataDTO stockDataDTO, Company company) {
        StockData stockData = stockDataDTO.toEntity(company);

        return stockDataRepository.save(stockData);
    }
}
