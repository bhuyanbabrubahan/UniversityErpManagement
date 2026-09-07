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
        name = "StudentUserResponseDto",
        description = "User account information associated with a student."
)
public class StudentUserResponseDto {

    @Schema(
            description = "User ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Email address",
            example = "rahul@gmail.com"
    )
    private String email;

    @Schema(
            description = "Mobile number",
            example = "9876543210"
    )
    private String mobileNumber;

    @Schema(
            description = "Email verification status",
            example = "true"
    )
    private Boolean emailVerified;

    @Schema(
            description = "Mobile verification status",
            example = "true"
    )
    private Boolean mobileVerified;
}