package com.sms.security.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.security.dto.SuperAdminSearchRequestDto;
import com.sms.security.entity.SuperAdmin;

import jakarta.persistence.criteria.Predicate;

public class SuperAdminSpecification {


    public static Specification<SuperAdmin> search(
            SuperAdminSearchRequestDto request
    ) {

        return (root, query, criteriaBuilder) -> {


            Predicate predicate =
                    criteriaBuilder.conjunction();



            // Keyword Search

            if (request.getKeyword() != null
                    && !request.getKeyword().isBlank()) {


                String keyword =
                        "%" + request.getKeyword().toLowerCase() + "%";


                Predicate keywordPredicate =
                        criteriaBuilder.or(

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("firstName")
                                        ),
                                        keyword
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("lastName")
                                        ),
                                        keyword
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("employeeCode")
                                        ),
                                        keyword
                                )

                        );


                predicate =
                        criteriaBuilder.and(
                                predicate,
                                keywordPredicate
                        );

            }



            // Designation Filter

            if (request.getDesignation() != null) {


                predicate =
                        criteriaBuilder.and(

                                predicate,

                                criteriaBuilder.equal(
                                        root.get("designation"),
                                        request.getDesignation()
                                )

                        );

            }



            // Status Filter

            if (request.getStatus() != null) {


                predicate =
                        criteriaBuilder.and(

                                predicate,

                                criteriaBuilder.equal(
                                        root.get("status"),
                                        request.getStatus()
                                )

                        );

            }
            else {


                predicate =
                        criteriaBuilder.and(

                                predicate,

                                criteriaBuilder.notEqual(
                                        root.get("status"),
                                        RecordStatus.DELETED
                                )

                        );

            }



            return predicate;

        };

    }

}