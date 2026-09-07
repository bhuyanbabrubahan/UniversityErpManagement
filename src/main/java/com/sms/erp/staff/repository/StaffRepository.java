package com.sms.erp.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.staff.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {
    Optional<Staff> findByCode(String code);
    boolean existsByCode(String code);
}
