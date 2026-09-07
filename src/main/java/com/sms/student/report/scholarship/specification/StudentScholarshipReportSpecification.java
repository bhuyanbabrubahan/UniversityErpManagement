package com.sms.student.report.scholarship.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;
import com.sms.student.report.scholarship.dto.ScholarshipReportRequestDto;

import jakarta.persistence.criteria.Predicate;

public class StudentScholarshipReportSpecification {

    private StudentScholarshipReportSpecification() {
    }

    public static Specification<StudentScholarship> search(ScholarshipReportRequestDto request) {

        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            // =====================================================
            // Student
            // =====================================================

            if (request.getStudentId() != null) {

                predicate = cb.and(predicate,
                        cb.equal(root.get("student").get("id"), request.getStudentId()));
            }

            if (request.getStudentName() != null && !request.getStudentName().isBlank()) {

                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("student").get("fullName")),
                                "%" + request.getStudentName().toLowerCase() + "%"));
            }

            if (request.getRollNumber() != null && !request.getRollNumber().isBlank()) {

                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("student").get("rollNumber")),
                                "%" + request.getRollNumber().toLowerCase() + "%"));
            }

            // =====================================================
            // Admission
            // =====================================================

            if (request.getAdmissionId() != null) {

                predicate = cb.and(predicate,
                        cb.equal(root.get("admission").get("id"), request.getAdmissionId()));
            }

            if (request.getAdmissionNumber() != null && !request.getAdmissionNumber().isBlank()) {

                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("admission").get("admissionNumber")),
                                "%" + request.getAdmissionNumber().toLowerCase() + "%"));
            }

            // =====================================================
            // Academic
            // =====================================================

            if (request.getAcademicYear() != null && !request.getAcademicYear().isBlank()) {

                predicate = cb.and(predicate,
                        cb.equal(root.get("academicYear"), request.getAcademicYear()));
            }

            if (request.getCourseName() != null && !request.getCourseName().isBlank()) {

                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("admission").get("course").get("courseName")),
                                "%" + request.getCourseName().toLowerCase() + "%"));
            }

            if (request.getDepartmentName() != null && !request.getDepartmentName().isBlank()) {

                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("admission").get("department").get("departmentName")),
                                "%" + request.getDepartmentName().toLowerCase() + "%"));
            }

            if (request.getSemesterName() != null && !request.getSemesterName().isBlank()) {

                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("admission").get("semester").get("semesterName")),
                                "%" + request.getSemesterName().toLowerCase() + "%"));
            }

            // =====================================================
            // Scholarship
            // =====================================================

            if (request.getScholarshipName() != null && !request.getScholarshipName().isBlank()) {

                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("scholarshipName")),
                                "%" + request.getScholarshipName().toLowerCase() + "%"));
            }

            ScholarshipType scholarshipType = request.getScholarshipType();
            if (scholarshipType != null) {

                predicate = cb.and(predicate,
                        cb.equal(root.get("scholarshipType"), scholarshipType));
            }

            ScholarshipStatus scholarshipStatus = request.getScholarshipStatus();
            if (scholarshipStatus != null) {

                predicate = cb.and(predicate,
                        cb.equal(root.get("scholarshipStatus"), scholarshipStatus));
            }

            // =====================================================
            // Requested Amount
            // =====================================================

            BigDecimal minRequested = request.getMinRequestedAmount();
            BigDecimal maxRequested = request.getMaxRequestedAmount();

            if (minRequested != null) {

                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("requestedAmount"), minRequested));
            }

            if (maxRequested != null) {

                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("requestedAmount"), maxRequested));
            }

            // =====================================================
            // Approved Amount
            // =====================================================

            BigDecimal minApproved = request.getMinApprovedAmount();
            BigDecimal maxApproved = request.getMaxApprovedAmount();

            if (minApproved != null) {

                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("approvedAmount"), minApproved));
            }

            if (maxApproved != null) {

                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("approvedAmount"), maxApproved));
            }

            // =====================================================
            // Approval Date
            // =====================================================

            LocalDate from = request.getApprovalDateFrom();
            LocalDate to = request.getApprovalDateTo();

            if (from != null) {

                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("approvalDate"),
                                from.atStartOfDay()));
            }

            if (to != null) {

                LocalDateTime endOfDay = to.atTime(23, 59, 59);

                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("approvalDate"), endOfDay));
            }

            // =====================================================
            // Record Status
            // =====================================================

            if (request.getStatus() != null) {

                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), request.getStatus()));

            } else {

                predicate = cb.and(predicate,
                        cb.notEqual(root.get("status"), RecordStatus.DELETED));
            }

            return predicate;
        };
    }

}