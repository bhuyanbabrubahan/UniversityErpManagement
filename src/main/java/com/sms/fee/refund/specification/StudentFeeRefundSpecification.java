package com.sms.fee.refund.specification;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.refund.dto.StudentFeeRefundSearchRequest;
import com.sms.fee.refund.entity.StudentFeeRefund;



import jakarta.persistence.criteria.Predicate;



public class StudentFeeRefundSpecification {



    private StudentFeeRefundSpecification() {

    }



    public static Specification<StudentFeeRefund> search(
            StudentFeeRefundSearchRequest request
    ){

        return (root, query, criteriaBuilder) -> {


            List<Predicate> predicates =
                    new ArrayList<>();


            // =====================================================
            // Soft Delete Filter
            // =====================================================


            predicates.add(
                    criteriaBuilder.notEqual(
                            root.get("status"),
                            RecordStatus.DELETED
                    )
            );



            // =====================================================
            // Refund Number Search
            // =====================================================


            if(request.getRefundNumber()!=null
                    && !request.getRefundNumber().isBlank()){


                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("refundNumber")
                                ),
                                "%"
                                + request.getRefundNumber().toLowerCase()
                                + "%"
                        )
                );

            }





            // =====================================================
            // Student Name Search
            // =====================================================


            if(request.getStudentName()!=null
                    && !request.getStudentName().isBlank()){


                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("studentName")
                                ),
                                "%"
                                + request.getStudentName().toLowerCase()
                                + "%"
                        )
                );

            }





            // =====================================================
            // Student Id Filter
            // =====================================================


            if(request.getStudentId()!=null){


                predicates.add(
                        criteriaBuilder.equal(
                                root.get("studentId"),
                                request.getStudentId()
                        )
                );

            }





            // =====================================================
            // Payment Id Filter
            // =====================================================


            if(request.getPaymentId()!=null){

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("payment").get("id"),
                                request.getPaymentId()
                        )
                );

            }





            // =====================================================
            // Refund Status Filter
            // =====================================================


            if(request.getRefundStatus()!=null){


                predicates.add(
                        criteriaBuilder.equal(
                                root.get("refundStatus"),
                                request.getRefundStatus()
                        )
                );

            }





            // =====================================================
            // Refund Mode Filter
            // =====================================================


            if(request.getRefundMode()!=null){


                predicates.add(
                        criteriaBuilder.equal(
                                root.get("refundMode"),
                                request.getRefundMode()
                        )
                );

            }





            // =====================================================
            // Date Range Filter
            // =====================================================


            if(request.getFromDate()!=null){


                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("refundDate"),
                                request.getFromDate()
                        )
                );

            }




            if(request.getToDate()!=null){


                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("refundDate"),
                                request.getToDate()
                        )
                );

            }





            return criteriaBuilder.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );


        };

    }


}