package com.sms.scholarship.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipRejectRequestDto {

    @NotBlank(message = "Rejection reason is required")
    private String reason;

}
