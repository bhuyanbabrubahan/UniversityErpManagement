package com.sms.fee.installment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.installment.dto.StudentFeeInstallmentRequestDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentResponseDto;
import com.sms.fee.installment.entity.StudentFeeInstallment;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentFeeInstallmentMapper {


    // =====================================================
    // Request DTO To Entity
    // =====================================================

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "installmentNumber", ignore = true)

    @Mapping(target = "student", ignore = true)

    @Mapping(target = "admission", ignore = true)

    @Mapping(target = "payment", ignore = true)


    // Snapshot

    @Mapping(target = "studentSnapshotId", ignore = true)

    @Mapping(target = "studentName", ignore = true)

    @Mapping(target = "rollNumber", ignore = true)

    @Mapping(target = "admissionSnapshotId", ignore = true)

    @Mapping(target = "admissionSnapshotNumber", ignore = true)


    // Payment Information

    @Mapping(target = "paidAmount", ignore = true)

    @Mapping(target = "remainingAmount", ignore = true)

    @Mapping(target = "paymentDate", ignore = true)

    @Mapping(target = "paymentMode", ignore = true)

    @Mapping(target = "transactionReference", ignore = true)


    // Status

    @Mapping(target = "installmentStatus", ignore = true)

    @Mapping(target = "status", ignore = true)


    // Audit

    @Mapping(target = "createdByUserId", ignore = true)

    @Mapping(target = "createdBy", ignore = true)


    @Mapping(target = "updatedByUserId", ignore = true)

    @Mapping(target = "updatedBy", ignore = true)



    // Completion Audit

    @Mapping(target = "completedByUserId", ignore = true)

    @Mapping(target = "completedBy", ignore = true)

    @Mapping(target = "completedDate", ignore = true)


    // Delete Audit

    @Mapping(target = "deletedByUserId", ignore = true)

    @Mapping(target = "deletedBy", ignore = true)

    @Mapping(target = "deletedDate", ignore = true)
    
    @Mapping(target = "createdAt", ignore = true)

    @Mapping(target = "updatedAt", ignore = true)

    @Mapping(target = "academicYear", ignore = true)


    StudentFeeInstallment toEntity(
            StudentFeeInstallmentRequestDto dto
    );




    // =====================================================
    // Entity To Response DTO
    // =====================================================


    @Mapping(
            target = "paymentId",
            source = "payment.id"
    )

    @Mapping(
            target = "deletedByUserId",
            source = "deletedByUserId"
    )

    @Mapping(
            target = "deletedBy",
            source = "deletedBy"
    )

    @Mapping(
            target = "deletedDate",
            source = "deletedDate"
    )


    StudentFeeInstallmentResponseDto toResponseDto(
            StudentFeeInstallment entity
    );




    // =====================================================
    // Update Entity From DTO
    // =====================================================


    @Mapping(target = "id", ignore = true)


    @Mapping(target = "installmentNumber", ignore = true)


    @Mapping(target = "student", ignore = true)

    @Mapping(target = "admission", ignore = true)

    @Mapping(target = "payment", ignore = true)



    // Snapshot

    @Mapping(target = "studentSnapshotId", ignore = true)

    @Mapping(target = "studentName", ignore = true)

    @Mapping(target = "rollNumber", ignore = true)

    @Mapping(target = "admissionSnapshotId", ignore = true)

    @Mapping(target = "admissionSnapshotNumber", ignore = true)



    // Payment Information

    @Mapping(target = "paidAmount", ignore = true)

    @Mapping(target = "remainingAmount", ignore = true)

    @Mapping(target = "paymentDate", ignore = true)

    @Mapping(target = "paymentMode", ignore = true)

    @Mapping(target = "transactionReference", ignore = true)



    // Status

    @Mapping(target = "installmentStatus", ignore = true)

    @Mapping(target = "status", ignore = true)



    // Audit

    @Mapping(target = "createdByUserId", ignore = true)

    @Mapping(target = "createdBy", ignore = true)


    @Mapping(target = "updatedByUserId", ignore = true)

    @Mapping(target = "updatedBy", ignore = true)




    // Completion Audit

    @Mapping(target = "completedByUserId", ignore = true)

    @Mapping(target = "completedBy", ignore = true)

    @Mapping(target = "completedDate", ignore = true)



    // Delete Audit

    @Mapping(target = "deletedByUserId", ignore = true)

    @Mapping(target = "deletedBy", ignore = true)

    @Mapping(target = "deletedDate", ignore = true)



    void updateEntityFromDto(
            StudentFeeInstallmentRequestDto dto,
            @MappingTarget StudentFeeInstallment entity
    );

}