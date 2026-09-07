package com.sms.erp.placement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.placement.entity.Placement;

public interface PlacementRepository extends JpaRepository<Placement, Long>, JpaSpecificationExecutor<Placement> {
    Optional<Placement> findByCode(String code);
    boolean existsByCode(String code);
}
