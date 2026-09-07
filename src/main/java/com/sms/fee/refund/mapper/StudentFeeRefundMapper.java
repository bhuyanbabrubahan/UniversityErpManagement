package com.sms.fee.refund.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.refund.dto.StudentFeeRefundRequestDto;
import com.sms.fee.refund.dto.StudentFeeRefundResponseDto;
import com.sms.fee.refund.entity.StudentFeeRefund;



@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentFeeRefundMapper {


    // =====================================================
    // Request DTO -> Entity
    // =====================================================

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "payment",
            ignore = true
    )
    @Mapping(
            target = "receipt",
            ignore = true
    )
    @Mapping(
            target = "refundNumber",
            ignore = true
    )
    @Mapping(
            target = "refundDate",
            ignore = true
    )
    @Mapping(
            target = "studentId",
            ignore = true
    )
    @Mapping(
            target = "studentName",
            ignore = true
    )
    @Mapping(
            target = "rollNumber",
            ignore = true
    )
    @Mapping(
            target = "admissionId",
            ignore = true
    )
    @Mapping(
            target = "admissionNumber",
            ignore = true
    )
    @Mapping(
            target = "receiptNumber",
            ignore = true
    )
    @Mapping(
            target = "receiptDate",
            ignore = true
    )
    @Mapping(
            target = "paymentTransactionReference",
            ignore = true
    )
    @Mapping(
            target = "refundStatus",
            ignore = true
    )
    @Mapping(
            target = "requestedByUserId",
            ignore = true
    )
    @Mapping(
            target = "requestedBy",
            ignore = true
    )
    @Mapping(
            target = "requestedDate",
            ignore = true
    )
    @Mapping(
            target = "approvedByUserId",
            ignore = true
    )
    @Mapping(
            target = "approvedBy",
            ignore = true
    )
    @Mapping(
            target = "approvedDate",
            ignore = true
    )
    @Mapping(
            target = "completedByUserId",
            ignore = true
    )
    @Mapping(
            target = "completedBy",
            ignore = true
    )
    @Mapping(
            target = "completedDate",
            ignore = true
    )
    @Mapping(
            target = "rejectedByUserId",
            ignore = true
    )
    @Mapping(
            target = "rejectedBy",
            ignore = true
    )
    @Mapping(
            target = "rejectedDate",
            ignore = true
    )
    @Mapping(
            target = "rejectionReason",
            ignore = true
    )
    @Mapping(
            target = "cancelledByUserId",
            ignore = true
    )
    @Mapping(
            target = "cancelledBy",
            ignore = true
    )
    @Mapping(
            target = "cancelledDate",
            ignore = true
    )
    @Mapping(
            target = "cancellationReason",
            ignore = true
    )
    @Mapping(
            target = "deletedByUserId",
            ignore = true
    )
    @Mapping(
            target = "deletedBy",
            ignore = true
    )
    @Mapping(
            target = "deletedDate",
            ignore = true
    )
    @Mapping(
            target = "refundTransactionReference",
            ignore = true
    )
    @Mapping(
            target = "bankReference",
            ignore = true
    )
    @Mapping(
            target = "status",
            ignore = true
    )
    StudentFeeRefund toEntity(
            StudentFeeRefundRequestDto dto
    );



    // =====================================================
    // Entity -> Response DTO
    // =====================================================

    @Mapping(
            target = "paymentId",
            source = "payment.id"
    )

    StudentFeeRefundResponseDto toDto(
            StudentFeeRefund entity
    );

}