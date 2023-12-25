package com.datascrapeapi.company;

import com.datascrapeapi.headquarter.Headquarter;
import com.datascrapeapi.headquarter.HeadquarterDTO;
import com.datascrapeapi.headquarter.HeadquarterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final HeadquarterRepository headquarterRepository;

    public CompanyService(CompanyRepository companyRepository, HeadquarterRepository headquarterRepository) {
        this.companyRepository = companyRepository;
        this.headquarterRepository = headquarterRepository;
    }
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional
    public Company saveOrUpdateCompany(CompanyDTO companyDTO, HeadquarterDTO headquarterDTO) {
        Optional<Company> existingCompanyOpt = companyRepository.findByTicker(companyDTO.getCompanyTicker());

        if (existingCompanyOpt.isPresent()) {
            Company existingCompany = existingCompanyOpt.get();
            Company companyToUpdate = companyDTO.toEntity();
            existingCompany.setCompanyName(companyToUpdate.getCompanyName());
            existingCompany.setMarketCap(companyToUpdate.getMarketCap());
            existingCompany.setYearFounded(companyToUpdate.getYearFounded());
            return companyRepository.save(existingCompany);
        } else {
            Company newCompany = companyDTO.toEntity();
            Optional<Headquarter> headquarter = headquarterRepository.findByCityAndState(headquarterDTO.getCity(), headquarterDTO.getState());
            if(headquarter.isPresent()){
                newCompany.setHeadquarter(headquarter.get());
            }
            return companyRepository.save(newCompany);
        }
    }

}
