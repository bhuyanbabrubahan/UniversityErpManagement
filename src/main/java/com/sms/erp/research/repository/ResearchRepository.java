package com.sms.erp.research.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.research.entity.Research;

public interface ResearchRepository extends JpaRepository<Research, Long>, JpaSpecificationExecutor<Research> {
    Optional<Research> findByCode(String code);
    boolean existsByCode(String code);
}
