package com.sms.erp.grievance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.grievance.entity.Grievance;

public interface GrievanceRepository extends JpaRepository<Grievance, Long>, JpaSpecificationExecutor<Grievance> {
    Optional<Grievance> findByCode(String code);
    boolean existsByCode(String code);
}
