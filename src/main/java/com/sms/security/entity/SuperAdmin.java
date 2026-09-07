package com.sms.security.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.location.address.entity.Address;
import com.sms.common.entity.BaseEntity;
import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;
import com.sms.security.enums.SuperAdminDesignation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "super_admins")
@Getter
@Setter
@NoArgsConstructor
public class SuperAdmin extends BaseEntity {

    // ==================== PRIMARY KEY ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==================== EMPLOYEE INFORMATION ====================

    @Column(
        name = "employee_code",
        unique = true,
        length = 30
    )
    private String employeeCode;

    @Column(
        name = "first_name",
        nullable = false,
        length = 50
    )
    private String firstName;

    @Column(
        name = "last_name",
        nullable = false,
        length = 50
    )
    private String lastName;

    // ==================== PERSONAL INFORMATION ====================

    @Enumerated(EnumType.STRING)
    @Column(
        name = "gender",
        nullable = false,
        length = 20
    )
    private Gender gender;

    @Column(
        name = "date_of_birth",
        nullable = false
    )
    private LocalDate dateOfBirth;

    // ==================== EMPLOYMENT INFORMATION ====================

    @Enumerated(EnumType.STRING)
    @Column(
        name = "designation",
        nullable = false,
        length = 50
    )
    private SuperAdminDesignation designation;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(
        name = "salary",
        precision = 12,
        scale = 2
    )
    private BigDecimal salary;

    // ==================== PROFILE ====================

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "signature_image")
    private String signatureImage;

    @Column(
        name = "remarks",
        length = 500
    )
    private String remarks;

    // ==================== USER ACCOUNT ====================

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    @ToString.Exclude
    private User user;

    // ==================== ADDRESS ====================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    private Address address;

    // ==================== RECORD STATUS ====================

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 20
    )
    private RecordStatus status;
    
    public String getFullName() {

        return Stream.of(
                firstName,
                lastName
            )
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .collect(Collectors.joining(" "));
    }
}