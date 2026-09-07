package com.location.village.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.location.enums.MasterStatus;
import com.location.village.entity.Village;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long>,
        JpaSpecificationExecutor<Village> {

	Optional<Village> findByIdAndStatus(
            Long id,
            MasterStatus status);

    boolean existsByTehsilIdAndVillageNameIgnoreCaseAndPincode(
            Long tehsilId,
            String villageName,
            String pincode);

    boolean existsByTehsilIdAndVillageNameIgnoreCaseAndPincodeAndIdNot(
            Long tehsilId,
            String villageName,
            String pincode,
            Long id);

    List<Village> findByTehsilIdAndStatusOrderByVillageNameAsc(
            Long tehsilId,
            MasterStatus status);

    List<Village> findByPincodeAndStatusOrderByVillageNameAsc(
            String pincode,
            MasterStatus status);

    Optional<Village> findByVillageNameIgnoreCaseAndPincode(
            String villageName,
            String pincode);

    boolean existsByPincode(String pincode);
    
    Optional<Village>
    findByTehsilIdAndVillageNameIgnoreCaseAndPincode(
            Long tehsilId,
            String villageName,
            String pincode);

}
