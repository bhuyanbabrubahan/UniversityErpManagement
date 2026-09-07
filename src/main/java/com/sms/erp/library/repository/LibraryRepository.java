package com.sms.erp.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.library.entity.Library;

public interface LibraryRepository extends JpaRepository<Library, Long>, JpaSpecificationExecutor<Library> {
    Optional<Library> findByCode(String code);
    boolean existsByCode(String code);
}
