package com.sms.fee.refund.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "RefundActionRequestDto",
        description = "Request DTO for refund reject or cancel action"
)
public class RefundActionRequestDto {


    @NotBlank(
            message = "Reason is required"
    )
    @Size(
            min = 5,
            max = 500,
            message = "Reason must be between 5 and 500 characters"
    )
    @Schema(
            description = "Reason for rejection or cancellation",
            example = "Duplicate refund request"
    )
    private String reason;


}
