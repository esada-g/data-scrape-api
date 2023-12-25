package com.datascrapeapi.company;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyDTO {
    private String companyName;
    private String companyTicker;
    private String yearFounded;
    private String marketCap;
    private String fullTimeEmployees;

    public static long parseFullTimeEmployees(String fullTimeEmployees) {
        String fullTimeEmployeesNumberFormat = fullTimeEmployees.replace(",", "");
        return Long.parseLong(fullTimeEmployeesNumberFormat);
    }

    public Company toEntity() {
        Company company = new Company();

        company.setCompanyName(this.companyName);
        company.setTicker(this.companyTicker.toUpperCase());

        if (!this.marketCap.isEmpty()) {
            company.setMarketCap(parseMarketCap(this.marketCap));
        }
        if (!this.fullTimeEmployees.isEmpty()) {
            company.setFullTimeEmployees(parseFullTimeEmployees(this.fullTimeEmployees));
        }

        if (!this.yearFounded.isEmpty()) {
            company.setYearFounded(Integer.parseInt(this.yearFounded));
        }

        return company;
    }

    private BigDecimal parseMarketCap(String marketCapString) {
        String value = marketCapString.substring(0, marketCapString.length() - 1);
        String mark = marketCapString.substring(marketCapString.length() - 1).toUpperCase();

        return switch (mark) {
            case "T" -> new BigDecimal(value).multiply(new BigDecimal("1000000000000"));
            case "B" -> new BigDecimal(value).multiply(new BigDecimal("1000000000"));
            case "M" -> new BigDecimal(value).multiply(new BigDecimal("1000000"));
            case "K" -> new BigDecimal(value).multiply(new BigDecimal("1000"));
            default -> new BigDecimal(value);
        };
    }

}
