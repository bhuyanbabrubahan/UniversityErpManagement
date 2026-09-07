package com.sms.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ChangePasswordRequestDto",
        description = "Request DTO used to change the password of the logged-in user."
)
public class ChangePasswordRequestDto {

    @Schema(
            description = "Current login password",
            example = "Amit@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Current password is required")
    private String currentPassword;


    @Schema(
            description = "New password",
            example = "Amit@456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "New password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must be 8 to 20 characters long and contain at least one uppercase letter, one lowercase letter, one digit and one special character."
    )
    private String newPassword;


    @Schema(
            description = "Confirm new password",
            example = "Amit@456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

}