package com.sms.audit.specification;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.audit.dto.AuditLogSearchRequestDto;
import com.sms.audit.entity.AuditLog;

import jakarta.persistence.criteria.Predicate;


public class AuditLogSpecification {


    private AuditLogSpecification() {

    }



    public static Specification<AuditLog> search(
            AuditLogSearchRequestDto request
    ) {


        return (root, query, criteriaBuilder) -> {


            List<Predicate> predicates = new ArrayList<>();


            /*
             * Action Filter
             */
            if(request.getAction() != null) {

                predicates.add(
                    criteriaBuilder.equal(
                        root.get("action"),
                        request.getAction()
                    )
                );

            }



            /*
             * Module Name Filter
             */
            if(request.getModuleName() != null 
                    && !request.getModuleName().isBlank()) {


                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(
                            root.get("moduleName")
                        ),
                        "%" +
                        request.getModuleName().toLowerCase()
                        +
                        "%"
                    )
                );

            }




            /*
             * Performed By Filter
             */
            if(request.getPerformedBy() != null
                    && !request.getPerformedBy().isBlank()) {


                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(
                            root.get("performedBy")
                        ),
                        "%" +
                        request.getPerformedBy().toLowerCase()
                        +
                        "%"
                    )
                );

            }




            /*
             * Role Filter
             */
            if(request.getRole() != null
                    && !request.getRole().isBlank()) {


                predicates.add(
                    criteriaBuilder.equal(
                        root.get("role"),
                        request.getRole()
                    )
                );

            }




            /*
             * HTTP Method Filter
             */
            if(request.getRequestMethod() != null
                    && !request.getRequestMethod().isBlank()) {


                predicates.add(
                    criteriaBuilder.equal(
                        root.get("requestMethod"),
                        request.getRequestMethod()
                    )
                );

            }




            /*
             * Request URL Filter
             */
            if(request.getRequestUrl() != null
                    && !request.getRequestUrl().isBlank()) {


                predicates.add(
                    criteriaBuilder.like(
                        root.get("requestUrl"),
                        "%" 
                        + request.getRequestUrl()
                        + "%"
                    )
                );

            }





            /*
             * From Date Filter
             */
            if(request.getFromDate() != null) {


                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("timestamp"),
                        request.getFromDate()
                    )
                );

            }





            /*
             * To Date Filter
             */
            if(request.getToDate() != null) {


                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("timestamp"),
                        request.getToDate()
                    )
                );

            }





            /*
             * Always show ACTIVE records only
             */
            predicates.add(
                criteriaBuilder.equal(
                    root.get("status"),
                    "ACTIVE"
                )
            );



            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );

        };


    }


}