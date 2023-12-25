package com.datascrapeapi.headquarter;

import com.datascrapeapi.company.Company;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Headquarter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String state;
}
