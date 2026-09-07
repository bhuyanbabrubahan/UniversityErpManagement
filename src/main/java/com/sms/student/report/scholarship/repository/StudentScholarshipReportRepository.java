package com.sms.student.report.scholarship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sms.scholarship.entity.StudentScholarship;


@Repository
public interface StudentScholarshipReportRepository
        extends JpaRepository<StudentScholarship, Long>,
                JpaSpecificationExecutor<StudentScholarship> {

}