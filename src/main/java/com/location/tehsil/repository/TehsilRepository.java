package com.location.tehsil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.location.district.entity.District;
import com.location.enums.MasterStatus;
import com.location.tehsil.entity.Tehsil;

@Repository
public interface TehsilRepository extends JpaRepository<Tehsil, Long>,
        JpaSpecificationExecutor<Tehsil> {

    Optional<Tehsil> findByIdAndStatus(Long id,
                                       MasterStatus status);

    Optional<Tehsil> findByDistrictAndTehsilNameIgnoreCase(
            District district,
            String tehsilName);

    boolean existsByDistrictIdAndTehsilNameIgnoreCase(
            Long districtId,
            String tehsilName);

    boolean existsByDistrictIdAndTehsilNameIgnoreCaseAndIdNot(
            Long districtId,
            String tehsilName,
            Long id);

    List<Tehsil> findByDistrictIdAndStatusOrderByTehsilNameAsc(
            Long districtId,
            MasterStatus status);
    
    Optional<Tehsil> findByDistrictIdAndTehsilNameIgnoreCase(
            Long districtId,
            String tehsilName);
    
    

}
