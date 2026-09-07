package com.sms.erp.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.hostel.entity.Hostel;

public interface HostelRepository extends JpaRepository<Hostel, Long>, JpaSpecificationExecutor<Hostel> {
    Optional<Hostel> findByCode(String code);
    boolean existsByCode(String code);
}
