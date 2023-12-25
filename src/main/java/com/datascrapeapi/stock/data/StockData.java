package com.datascrapeapi.stock.data;

import com.datascrapeapi.company.Company;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Entity
@Data
public class StockData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date scrapingDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date targetDate;

    private double previousClosePrice;
    private double openPrice;
}
