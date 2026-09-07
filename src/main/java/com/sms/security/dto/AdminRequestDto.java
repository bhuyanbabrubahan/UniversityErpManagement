package com.sms.security.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.location.address.dto.AddressRequestDto;
import com.sms.enums.Gender;
import com.sms.security.enums.AdminDesignation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AdminRequestDto", description = "Request DTO used to create and update admin information.")
public class AdminRequestDto {

    // PERSONAL INFORMATION

    @Schema(description = "Admin first name", example = "Rahul", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Schema(description = "Admin last name", example = "Sharma", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Schema(description = "Admin gender", example = "MALE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Schema(description = "Admin date of birth", example = "1990-05-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    // EMPLOYMENT INFORMATION

    @Schema(description = "Admin designation", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Designation is required")
    private AdminDesignation designation;

    @Schema(description = "Admin joining date", example = "2024-07-01")
    private LocalDate joiningDate;

    @Schema(description = "Admin monthly salary", example = "65000.00")
    @DecimalMin(value = "0.0", message = "Salary cannot be negative")
    private BigDecimal salary;

    @Schema(description = "Admin remarks", example = "System administrator")
    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    // USER ACCOUNT INFORMATION

    @Schema(description = "Admin email address", example = "admin@university.edu", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Admin account password", example = "Admin@123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @Pattern(
        regexp = "^$|^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
        message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String password;

    @Schema(description = "Admin mobile number", example = "9876543210", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Mobile number must be a valid 10 digit Indian mobile number"
    )
    private String mobileNumber;

    // PROFILE INFORMATION

    @Schema(description = "Admin profile image path", example = "/uploads/admin/admin1.jpg")
    private String profileImage;

    // ADDRESS INFORMATION

    @Schema(description = "Admin address information", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Address is required")
    @Valid
    private AddressRequestDto address;
}
