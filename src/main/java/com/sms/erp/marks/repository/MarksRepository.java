package com.sms.erp.marks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.marks.entity.Marks;

public interface MarksRepository extends JpaRepository<Marks, Long>, JpaSpecificationExecutor<Marks> {
    Optional<Marks> findByCode(String code);
    boolean existsByCode(String code);
}
