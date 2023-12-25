package com.datascrapeapi.stock.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface StockDataRepository extends JpaRepository<StockData, Long> {

    Optional<StockData> findByCompanyIdAndTargetDate(Long company_id, Date targetDate);
}
