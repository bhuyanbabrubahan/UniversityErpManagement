package com.sms.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "UserSummaryResponseDto",
    description = "User information included in student profile response."
)
public class UserSummaryResponseDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "rahul@gmail.com")
    private String email;

    @Schema(example = "9876543210")
    private String mobileNumber;

    @Schema(example = "true")
    private Boolean emailVerified;

    @Schema(example = "true")
    private Boolean mobileVerified;
}