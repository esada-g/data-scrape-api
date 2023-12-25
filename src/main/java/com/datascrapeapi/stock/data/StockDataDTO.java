package com.datascrapeapi.stock.data;

import com.datascrapeapi.company.Company;
import com.datascrapeapi.utils.DateUtils;
import lombok.Data;

import java.util.Date;

@Data
public class StockDataDTO {
    private Date scrapingDate;
    private String targetDate;
    private String previousClosePrice;
    private String openPrice;

    public StockData toEntity(Company company) {
        StockData entity = new StockData();
        entity.setCompany(company);

        Date targetDate = DateUtils.stringToDate(this.targetDate);

        entity.setScrapingDate(scrapingDate);
        entity.setTargetDate(targetDate);

        double parsedPreviousClosePrice = Double.parseDouble(this.previousClosePrice);

        double parsedOpenPrice = Double.parseDouble(this.openPrice);

        entity.setPreviousClosePrice(parsedPreviousClosePrice);
        entity.setOpenPrice(parsedOpenPrice);

        return entity;
    }
}
