package com.location.state.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.location.country.entity.Country;
import com.location.enums.MasterStatus;
import com.location.state.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long>,
        JpaSpecificationExecutor<State> {

    Optional<State> findByIdAndStatus(Long id, MasterStatus status);

    Optional<State> findByCountryAndStateNameIgnoreCase(
            Country country,
            String stateName);

    boolean existsByCountryIdAndStateNameIgnoreCase(
            Long countryId,
            String stateName);

    boolean existsByCountryIdAndStateNameIgnoreCaseAndIdNot(
            Long countryId,
            String stateName,
            Long id);

    List<State> findByCountryIdAndStatusOrderByStateNameAsc(
            Long countryId,
            MasterStatus status);

    boolean existsByStateCodeIgnoreCase(String stateCode);

    boolean existsByStateCodeIgnoreCaseAndIdNot(
            String stateCode,
            Long id);
    
    Optional<State> findByCountryIdAndStateNameIgnoreCase(
            Long countryId,
            String stateName);
}
