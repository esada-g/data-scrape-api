package com.datascrapeapi.headquarter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HeadquarterService {

    private final HeadquarterRepository headquarterRepository;

    public HeadquarterService(HeadquarterRepository headquarterRepository) {
        this.headquarterRepository = headquarterRepository;
    }

    @Transactional
    public Headquarter saveHeadquarterIfNotExists(HeadquarterDTO headquarterDTO) {
        Optional<Headquarter> existingHeadquarter = headquarterRepository.findByCityAndState(headquarterDTO.getCity(), headquarterDTO.getState());

        if (existingHeadquarter.isEmpty()) {
            Headquarter headquarter = headquarterDTO.toEntity();
            return headquarterRepository.save(headquarter);
        }
        return existingHeadquarter.get();
    }
}
