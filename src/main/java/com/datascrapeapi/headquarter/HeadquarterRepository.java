package com.datascrapeapi.headquarter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {
    Optional<Headquarter> findByCityAndState(String city, String state);
}
