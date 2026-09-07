package com.sms.user.profile.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.location.address.enums.AddressType;
import com.sms.enums.BloodGroup;
import com.sms.enums.Designation;
import com.sms.enums.Gender;
import com.sms.enums.Qualification;
import com.sms.enums.RecordStatus;
import com.sms.enums.StudentCategory;
import com.sms.enums.StudentStatus;
import com.sms.enums.StudentType;
import com.sms.security.enums.AdminDesignation;
import com.sms.security.enums.SuperAdminDesignation;
import com.sms.security.enums.UserStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDto {

    // =====================================================
    // COMMON USER INFORMATION
    // =====================================================

    private Long userId;

    private String email;

    private String role;

    private String mobileNumber;

    private Boolean mobileVerified;

    private Boolean emailVerified;

    private UserStatus status;

    // =====================================================
    // COMMON PERSONAL INFORMATION
    // =====================================================

    private String firstName;

    private String middleName;

    private String lastName;

    private String fullName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String profileImage;

    private String signatureImage;

    // =====================================================
    // STUDENT INFORMATION
    // =====================================================

    private Long studentId;

    private String studentCode;

    private String rollNumber;

    private String registrationNumber;

    private String admissionNumber;

    private LocalDate admissionDate;

    // =====================================================
    // STUDENT EXTRA PERSONAL INFORMATION
    // =====================================================

    private BloodGroup bloodGroup;

    private StudentCategory category;

    private String religion;

    private String nationality;

    private String alternateMobile;

    private String alternateEmail;

    private String aadhaarNumber;

    private String panNumber;

    // =====================================================
    // STUDENT ACADEMIC INFORMATION
    // =====================================================

    private String academicYear;

    private String section;

    private String batch;

    private Integer currentYear;

    private StudentType studentType;

    private StudentStatus studentStatus;

    private BigDecimal fees;

    private Long courseId;

    private String courseName;

    private Long currentSemesterId;

    private String semesterName;

    private Long departmentId;

    private String departmentName;

    // =====================================================
    // FACULTY INFORMATION
    // =====================================================

    private Long facultyId;

    private String employeeCode;

    private Designation facultyDesignation;

    private Qualification qualification;

    private LocalDate joiningDate;

    private BigDecimal salary;

    // =====================================================
    // ADMIN INFORMATION
    // =====================================================

    private Long adminId;

    private AdminDesignation adminDesignation;

    // =====================================================
    // SUPER ADMIN INFORMATION
    // =====================================================

    private Long superAdminId;

    private SuperAdminDesignation superAdminDesignation;

    // =====================================================
    // ADDRESS INFORMATION
    // =====================================================

    private Long addressId;

    private String houseNumber;

    private String street;

    private String landmark;

    private AddressType addressType;

    // =====================================================
    // LOCATION INFORMATION
    // =====================================================

    private Long countryId;

    private String countryName;

    private Long stateId;

    private String stateName;

    private Long districtId;

    private String districtName;

    private Long tehsilId;

    private String tehsilName;

    private Long villageId;

    private String villageName;

    private String pincode;

    // =====================================================
    // RECORD INFORMATION
    // =====================================================

    private RecordStatus recordStatus;

    // =====================================================
    // AUDIT INFORMATION
    // =====================================================

    private Long createdByUserId;

    private String createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime createdAt;

    private Long updatedByUserId;

    private String updatedBy;

    private LocalDateTime updatedDate;

    private LocalDateTime updatedAt;
}
