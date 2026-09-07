package com.location.importLocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.location.importLocation.entity.LocationMasterImport;

@Repository
public interface LocationMasterImportRepository
        extends JpaRepository<LocationMasterImport, Long> {

}