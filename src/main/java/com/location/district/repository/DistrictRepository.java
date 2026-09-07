package com.location.district.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.location.district.entity.District;
import com.location.enums.MasterStatus;
import com.location.state.entity.State;


@Repository
public interface DistrictRepository extends JpaRepository<District, Long>,
        JpaSpecificationExecutor<District> {

    Optional<District> findByIdAndStatus(Long id,
                                         MasterStatus status);

    Optional<District> findByStateAndDistrictNameIgnoreCase(
            State state,
            String districtName);

    boolean existsByStateIdAndDistrictNameIgnoreCase(
            Long stateId,
            String districtName);

    boolean existsByStateIdAndDistrictNameIgnoreCaseAndIdNot(
            Long stateId,
            String districtName,
            Long id);

    List<District> findByStateIdAndStatusOrderByDistrictNameAsc(
            Long stateId,
            MasterStatus status);
    
    boolean existsByDistrictCodeIgnoreCase(String districtCode);

    boolean existsByDistrictCodeIgnoreCaseAndIdNot(
            String districtCode,
            Long id);
    
    Optional<District> findByStateIdAndDistrictNameIgnoreCase(
            Long stateId,
            String districtName);

}