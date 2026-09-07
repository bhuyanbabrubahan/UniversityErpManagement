package com.sms.security.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class EmployeeBase extends BaseEntity {

    @Column(name = "employee_code", nullable = false, unique = true, length = 30)
    private String employeeCode;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "middle_name", nullable = false, length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "salary", precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "signature_image")
    private String signatureImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecordStatus status;

    public String getFullName() {
        String first = firstName == null ? "" : firstName.trim();
        String last = lastName == null ? "" : lastName.trim();

        return (first + " " + last).trim();
    }
}