package com.sms.erp.scholarship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.scholarship.entity.Scholarship;

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long>, JpaSpecificationExecutor<Scholarship> {
    Optional<Scholarship> findByCode(String code);
    boolean existsByCode(String code);
}
