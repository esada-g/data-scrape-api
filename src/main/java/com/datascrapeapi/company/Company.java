package com.datascrapeapi.company;

import com.datascrapeapi.headquarter.Headquarter;
import com.datascrapeapi.stock.data.StockData;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;

    @Column(nullable = true)
    private BigDecimal marketCap;

    @Column(nullable = true)
    private Long fullTimeEmployees;

    @Column(nullable = true)
    private Integer yearFounded;

    @Column(nullable = false, length = 255)
    private String companyName;

    @OneToOne
    @JoinColumn(name = "headquarter_id", nullable = true)
    private Headquarter headquarter;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockData> stocks = new ArrayList<>();
}
