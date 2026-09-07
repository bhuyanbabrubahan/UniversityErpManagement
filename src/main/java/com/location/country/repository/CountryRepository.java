package com.location.country.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.location.country.entity.Country;
import com.location.enums.MasterStatus;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>,
        JpaSpecificationExecutor<Country> {

    Optional<Country> findByIdAndStatus(Long id, MasterStatus status);

    boolean existsByCountryNameIgnoreCase(String countryName);

    boolean existsByCountryCodeIgnoreCase(String countryCode);

    boolean existsByCountryNameIgnoreCaseAndIdNot(String countryName, Long id);

    boolean existsByCountryCodeIgnoreCaseAndIdNot(String countryCode, Long id);
    
    Optional<Country> findByCountryCodeIgnoreCase(String code);

}