package com.sms.fee.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.dto.FeeStructureSearchRequest;
import com.sms.fee.entity.FeeStructure;

import jakarta.persistence.criteria.Predicate;

public class FeeStructureSpecification {

    private FeeStructureSpecification() {
    }

    public static Specification<FeeStructure> search(
            FeeStructureSearchRequest request
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // ==========================================================
            // Exclude Deleted Records
            // ==========================================================

            predicates.add(

                    cb.notEqual(

                            root.get("status"),

                            RecordStatus.DELETED

                    )

            );

            // ==========================================================
            // Course
            // ==========================================================

            if (request.getCourseId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("course").get("id"),

                                request.getCourseId()

                        )

                );

            }

            // ==========================================================
            // Semester
            // ==========================================================

            if (request.getSemesterId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("semester").get("id"),

                                request.getSemesterId()

                        )

                );

            }

            // ==========================================================
            // Fee Type
            // ==========================================================

            if (request.getFeeType() != null) {

                predicates.add(

                        cb.equal(

                                root.get("feeType"),

                                request.getFeeType()

                        )

                );

            }

            // ==========================================================
            // Academic Year
            // ==========================================================

            if (request.getAcademicYear() != null
                    && !request.getAcademicYear().isBlank()) {

                predicates.add(

                        cb.like(

                                cb.lower(root.get("academicYear")),

                                "%" + request.getAcademicYear()
                                        .toLowerCase()
                                        .trim() + "%"

                        )

                );

            }

            // ==========================================================
            // Status
            // ==========================================================

            if (request.getStatus() != null) {

                predicates.add(

                        cb.equal(

                                root.get("status"),

                                request.getStatus()

                        )

                );

            }

            return cb.and(

                    predicates.toArray(new Predicate[0])

            );

        };

    }

}