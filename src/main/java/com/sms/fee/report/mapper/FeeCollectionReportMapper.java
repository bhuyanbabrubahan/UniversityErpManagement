package com.sms.fee.report.mapper;


import org.springframework.stereotype.Component;

import com.sms.fee.dto.report.FeeCollectionReportResponseDto;
import com.sms.fee.entity.StudentFeeReceipt;


@Component
public class FeeCollectionReportMapper {


    public FeeCollectionReportResponseDto toDto(
            StudentFeeReceipt receipt
    ) {


        if (receipt == null) {
            return null;
        }


        return FeeCollectionReportResponseDto.builder()

                .receiptNumber(
                        receipt.getReceiptNumber()
                )

                .receiptId(
                        receipt.getId()
                )

                .studentId(
                        receipt.getStudentId()
                )

                .studentName(
                        receipt.getStudentName()
                )

                .rollNumber(
                        receipt.getRollNumber()
                )

                .admissionNumber(
                        receipt.getAdmissionNumber()
                )

                .departmentName(
                        receipt.getDepartmentName()
                )

                .courseName(
                        receipt.getCourseName()
                )

                .academicYear(
                        receipt.getAcademicYear()
                )

                .paymentDate(
                        receipt.getPaymentDate()
                )

                .receiptDate(
                        receipt.getReceiptDate()
                )

                .paymentMode(
                        receipt.getPaymentMode()
                )

                .paymentStatus(
                        receipt.getPaymentStatus()
                )

                .transactionReference(
                        receipt.getTransactionReference()
                )

                .totalFee(
                        receipt.getTotalFee()
                )

                .scholarshipAmount(
                        receipt.getScholarshipAmount()
                )

                .finalPayableAmount(
                        receipt.getFinalPayableAmount()
                )

                .scholarshipName(
                		receipt.getScholarshipName()
                )
                .scholarshipType(
                		receipt.getScholarshipType()
                )
                
                .paidAmount(
                        receipt.getPaidAmount()
                )

                .dueAmount(
                        receipt.getDueAmount()
                )

                .createdBy(
                        receipt.getCreatedBy()
                )
                
                .build();

    }

}