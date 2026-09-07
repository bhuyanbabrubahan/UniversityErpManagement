package com.sms.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
        name = "ResetPasswordRequestDto",
        description = "Request DTO used to reset password after OTP verification."
)
public class ResetPasswordRequestDto {


	@Schema(
	        description = "Password reset token generated after OTP verification",
	        example = "8835d635-ade1-43ad-a323-9e6b5dd57901",
	        requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotBlank(
	        message = "Reset token is required"
	)
	private String token;



    @Schema(
            description = "New password",
            example = "Amit@789",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "New password is required"
    )
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must be 8 to 20 characters long and contain uppercase, lowercase, digit and special character."
    )
    private String newPassword;



    @Schema(
            description = "Confirm new password",
            example = "Amit@789",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Confirm password is required"
    )
    private String confirmPassword;

}