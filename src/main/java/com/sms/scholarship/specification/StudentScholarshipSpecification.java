package com.sms.scholarship.specification;


import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;


public class StudentScholarshipSpecification {



    public static Specification<StudentScholarship> getSpecification(
            
            Long studentId,
            Long admissionId,
            String academicYear,
            String scholarshipName,
            ScholarshipType scholarshipType,
            BigDecimal minApprovedAmount,
            BigDecimal maxApprovedAmount,
            RecordStatus status,
            ScholarshipStatus scholarshipStatus

    ) {


        return (root, query, cb) -> {


            var predicates =
                    cb.conjunction();



            // =====================================================
            // Student Filter
            // =====================================================

            if(studentId != null){

                predicates =
                cb.and(
                        predicates,
                        cb.equal(
                                root.get("student").get("id"),
                                studentId
                        )
                );
            }





            // =====================================================
            // Admission Filter
            // =====================================================

            if(admissionId != null){

                predicates =
                cb.and(
                        predicates,
                        cb.equal(
                                root.get("admission").get("id"),
                                admissionId
                        )
                );
            }





            // =====================================================
            // Academic Year
            // =====================================================

            if(academicYear != null
                    && !academicYear.isBlank()) {


                predicates =
                cb.and(
                        predicates,
                        cb.equal(
                                root.get("academicYear"),
                                academicYear
                        )
                );

            }





            // =====================================================
            // Scholarship Name
            // =====================================================

            if(scholarshipName != null
                    && !scholarshipName.isBlank()) {


                predicates =
                cb.and(
                        predicates,
                        cb.like(
                                cb.lower(
                                    root.get("scholarshipName")
                                ),
                                "%"
                                + scholarshipName.toLowerCase()
                                + "%"
                        )
                );

            }





            // =====================================================
            // Scholarship Type
            // =====================================================

            if(scholarshipType != null){

                predicates =
                cb.and(
                        predicates,
                        cb.equal(
                                root.get("scholarshipType"),
                                scholarshipType
                        )
                );

            }





            // =====================================================
            // Minimum Approved Amount
            // =====================================================

            if(minApprovedAmount != null){

                predicates =
                cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(
                                root.get("approvedAmount"),
                                minApprovedAmount
                        )
                );
            }





            // =====================================================
            // Maximum Approved Amount
            // =====================================================

            if(maxApprovedAmount != null){

                predicates =
                cb.and(
                        predicates,
                        cb.lessThanOrEqualTo(
                                root.get("approvedAmount"),
                                maxApprovedAmount
                        )
                );
            }





            // =====================================================
            // Status Filter
            // =====================================================

            if(status != null){

                predicates =
                cb.and(
                        predicates,
                        cb.equal(
                                root.get("status"),
                                status
                        )
                );

            }
            else {

                predicates =
                cb.and(
                        predicates,
                        cb.notEqual(
                                root.get("status"),
                                RecordStatus.DELETED
                        )
                );

            }

            
         // =====================================================
         // Scholarship Status Filter
         // =====================================================

         if (scholarshipStatus != null) {

             predicates =
                     cb.and(
                             predicates,
                             cb.equal(
                                     root.get("scholarshipStatus"),
                                     scholarshipStatus
                             )
                     );

         }


            return predicates;
        };

    }

}