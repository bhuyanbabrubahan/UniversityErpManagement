package com.sms.fee.refund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.fee.refund.enums.RefundStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "RefundHistoryResponse",
        description = "Refund history details"
)
public class RefundHistoryResponseDto {


    private Long refundId;


    private String refundNumber;


    private BigDecimal refundAmount;


    private RefundStatus refundStatus;


    private LocalDate refundDate;


    private String refundReason;


    private String approvedBy;


    private LocalDateTime approvedDate;


    private String completedBy;


    private LocalDateTime completedDate;


    private String rejectedBy;


    private LocalDateTime rejectedDate;


    private String cancelledBy;


    private LocalDateTime cancelledDate;
    
    private String rejectionReason;

    private String cancellationReason;

}