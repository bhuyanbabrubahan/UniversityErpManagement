package com.sms.security.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.location.address.dto.AddressRequestDto;
import com.sms.enums.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "RegisterRequestDto",
        description = "Request DTO used for Student Registration."
)
public class UserRequestDto {

    // ==========================================================
    // Basic Details
    // ==========================================================

    @Schema(
            description = "First Name",
            example = "Rahul",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(
            description = "Last Name",
            example = "Sharma",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(
            description = "Email",
            example = "rahul@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(
            description = "Password",
            example = "Rahul@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(
            description = "Confirm Password",
            example = "Rahul@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
    
    @Schema(
            description = "mobile Number",
            example = "9876543210",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "mobile Number  is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10 digit mobile number"
    )
    private String mobileNumber;

    // ==========================================================
    // Student Details
    // ==========================================================

    @Schema(
            description = "Gender",
            example = "MALE",
            allowableValues = {
                    "MALE",
                    "FEMALE",
                    "OTHER"
            },
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Schema(
            description = "Date of Birth",
            example = "2003-05-10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Schema(
            description = "Admission Date",
            example = "2026-07-16",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Admission date is required")
    private LocalDate admissionDate;

    @Schema(
            description = "Admission Fees",
            example = "45000.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Fees is required")
    @Positive(message = "Fees must be greater than zero")
    private BigDecimal fees;

    @Schema(
            description = "Profile Image",
            example = "images/student1.jpg"
    )
    private String profileImage;

    // ==========================================================
    // Department
    // ==========================================================

    @Schema(
            description = "Department Id",
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Department is required")
    private Long departmentId;

    // ==========================================================
    // Address
    // ==========================================================

    @Valid
    @NotNull(message = "Address is required")
    private AddressRequestDto address;

}