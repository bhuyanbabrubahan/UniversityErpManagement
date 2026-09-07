package com.sms.fee.refund.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.refund.dto.RefundHistoryResponseDto;
import com.sms.fee.refund.entity.StudentFeeRefund;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RefundHistoryMapper {


    // =====================================================
    // Entity To Refund History Response
    // =====================================================

    @Mapping(
            target = "refundId",
            source = "id"
    )
    RefundHistoryResponseDto toDto(
            StudentFeeRefund refund
    );

}