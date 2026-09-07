package com.sms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.location.address.dto.AddressRequestDto;
import com.sms.enums.Designation;
import com.sms.enums.Gender;
import com.sms.enums.Qualification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "FacultyRequestDto",
    description = "Request DTO used to create or update a Faculty."
)
public class FacultyRequestDto {

    @Schema(
        description = "Faculty first name",
        example = "Rahul",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @Schema(
            description = "Faculty Midlle name",
            example = "Kumar",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
    private String middleName;

    @Schema(
        description = "Faculty last name",
        example = "Sharma",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(
        description = "Gender of the faculty",
        example = "MALE",
        allowableValues = { "MALE", "FEMALE", "OTHER" },
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Enter valid mobile number"
    )
    private String mobileNumber;
    
    @Schema(
        description = "Faculty date of birth (yyyy-MM-dd)",
        example = "1990-05-15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Schema(
        description = "Faculty joining date (yyyy-MM-dd)",
        example = "2024-07-01",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @Schema(
        description = "Faculty designation",
        example = "ASSISTANT_PROFESSOR",
        allowableValues = {
            "PROFESSOR",
            "ASSOCIATE_PROFESSOR",
            "ASSISTANT_PROFESSOR",
            "LECTURER",
            "HOD"
        },
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Designation is required")
    private Designation designation;

    @Schema(
        description = "Faculty qualification",
        example = "MTECH",
        allowableValues = {
            "BTECH",
            "MTECH",
            "MSC",
            "PHD",
            "MBA",
            "MCA"
        },
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Qualification is required")
    private Qualification qualification;

    @Schema(
        description = "Monthly salary of the faculty",
        example = "65000.00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Salary is required")
    @DecimalMin(
        value = "0.0",
        message = "Salary must be positive"
    )
    private BigDecimal salary;

    @Schema(
        description = "Faculty profile image URL or file path",
        example = "/uploads/faculty/rahul-sharma.jpg"
    )
    private String profileImage;

    @Schema(
        description = "Department ID to which faculty belongs",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Department is required")
    private Long departmentId;

    @Schema(
        description = "Faculty address details",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Valid
    @NotNull(message = "Address is required")
    private AddressRequestDto address;

    @Schema(
        description = "Faculty email address",
        example = "rahul.sharma@university.edu",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(
        description = "Login password for faculty account",
        example = "Rahul@123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;
}