package com.sms.user.profile.dto;

import java.time.LocalDate;

import com.location.address.enums.AddressType;
import com.sms.enums.BloodGroup;
import com.sms.enums.Designation;
import com.sms.enums.Gender;
import com.sms.enums.Qualification;
import com.sms.enums.StudentCategory;
import com.sms.enums.StudentStatus;
import com.sms.enums.StudentType;
import com.sms.security.enums.AdminDesignation;
import com.sms.security.enums.SuperAdminDesignation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(
    name = "UpdateProfileRequestDto",
    description = "Request used by authenticated users to update their own profile"
)
public class UpdateProfileRequestDto {

    // ==================== COMMON PERSONAL INFORMATION ====================

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Middle name must not exceed 100 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;
    
    private String fullName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Invalid mobile number"
    )
    private String mobileNumber;

    private LocalDate dateOfBirth;


    // ==================== EXTRA PERSONAL INFORMATION ====================

    private BloodGroup bloodGroup;

    private StudentCategory category;

    @Size(max = 50, message = "Religion must not exceed 50 characters")
    private String religion;

    @Size(max = 50, message = "Nationality must not exceed 50 characters")
    private String nationality;

    @Pattern(
        regexp = "^$|^[6-9]\\d{9}$",
        message = "Invalid alternate mobile number"
    )
    private String alternateMobile;

    @Email(message = "Invalid alternate email")
    @Size(max = 150, message = "Alternate email must not exceed 150 characters")
    private String alternateEmail;

    @Pattern(
        regexp = "^$|^\\d{12}$",
        message = "Aadhaar number must contain exactly 12 digits"
    )
    private String aadhaarNumber;

    @Pattern(
        regexp = "^$|^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
        message = "Invalid PAN number"
    )
    private String panNumber;


    // ==================== ACADEMIC INFORMATION ====================

    @Size(max = 20, message = "Academic year must not exceed 20 characters")
    private String academicYear;

    @Size(max = 20, message = "Section must not exceed 20 characters")
    private String section;

    @Size(max = 30, message = "Batch must not exceed 30 characters")
    private String batch;

    private Integer currentYear;

    private StudentType studentType;

    private StudentStatus studentStatus;


    // ==================== FACULTY INFORMATION ====================

    @Size(max = 30, message = "Employee code must not exceed 30 characters")
    private String employeeCode;

    private Designation facultyDesignation;

    
    public Designation getFacultyDesignation() {
        return facultyDesignation;
    }

    public void setFacultyDesignation(Designation facultyDesignation) {
        this.facultyDesignation = facultyDesignation;
    }

    private Qualification qualification;

    private LocalDate joiningDate;


    // ==================== ADMIN INFORMATION ====================

    private AdminDesignation adminDesignation;
    
    


    // ==================== SUPER ADMIN INFORMATION ====================

    private SuperAdminDesignation superAdminDesignation;


    // ==================== ADDRESS INFORMATION ====================

    @NotBlank(message = "House number is required")
    @Size(max = 100, message = "House number must not exceed 100 characters")
    private String houseNumber;

    @NotBlank(message = "Street is required")
    @Size(max = 150, message = "Street must not exceed 150 characters")
    private String street;

    @Size(max = 150, message = "Landmark must not exceed 150 characters")
    private String landmark;

    @NotNull(message = "Address type is required")
    private AddressType addressType;

    @NotNull(message = "Village is required")
    private Long villageId;
}