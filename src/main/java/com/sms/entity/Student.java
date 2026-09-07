package com.sms.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.location.address.entity.Address;
import com.sms.common.entity.BaseEntity;
import com.sms.enums.BloodGroup;
import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;
import com.sms.enums.StudentCategory;
import com.sms.enums.StudentStatus;
import com.sms.enums.StudentType;
import com.sms.security.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    // ==================== PRIMARY KEY ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==================== STUDENT IDENTITY ====================

    @Column(
        name = "student_code",
        nullable = false,
        unique = true,
        length = 30
    )
    private String studentCode;

    @Column(
        name = "roll_number",
        nullable = false,
        unique = true,
        length = 20
    )
    private String rollNumber;

    @Column(
        name = "registration_number",
        unique = true,
        length = 30
    )
    private String registrationNumber;

    @Column(
        name = "admission_number",
        unique = true,
        length = 30
    )
    private String admissionNumber;

    // ==================== PERSONAL INFORMATION ====================

    @Column(
        name = "first_name",
        nullable = false,
        length = 50
    )
    private String firstName;

    @Column(
        name = "middle_name",
        length = 50
    )
    private String middleName;

    @Column(
        name = "last_name",
        nullable = false,
        length = 50
    )
    private String lastName;

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

    @Enumerated(EnumType.STRING)
    @Column(
        name = "blood_group",
        length = 10
    )
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "category",
        length = 30
    )
    private StudentCategory category;

    @Column(
        name = "religion",
        length = 50
    )
    private String religion;

    @Column(
        name = "nationality",
        length = 50
    )
    private String nationality;

    // ==================== CONTACT INFORMATION ====================

    @Column(
        name = "alternate_mobile",
        length = 10
    )
    private String alternateMobile;

    @Column(
        name = "alternate_email",
        length = 150
    )
    private String alternateEmail;

    // ==================== IDENTITY DOCUMENTS ====================

    @Column(
        name = "aadhaar_number",
        length = 12
    )
    private String aadhaarNumber;

    @Column(
        name = "pan_number",
        length = 10
    )
    private String panNumber;

    // ==================== PROFILE ====================

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "signature_image")
    private String signatureImage;

    // ==================== ADMISSION INFORMATION ====================

    @Column(
        name = "admission_date",
        nullable = false
    )
    private LocalDate admissionDate;

    @Column(
        name = "academic_year",
        length = 20
    )
    private String academicYear;

    // ==================== ACADEMIC INFORMATION ====================

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "department_id",
        nullable = false
    )
    private Department department;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_semester_id")
    private Semester currentSemester;

    @Column(
        name = "section",
        length = 20
    )
    private String section;

    @Column(
        name = "batch",
        length = 30
    )
    private String batch;

    @Column(name = "current_year")
    private Integer currentYear;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "student_type",
        length = 30
    )
    private StudentType studentType;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "student_status",
        length = 30
    )
    private StudentStatus studentStatus;

    // ==================== USER ACCOUNT ====================

    @ToString.Exclude
    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        unique = true,
        nullable = false
    )
    private User user;

    // ==================== ADDRESS ====================

    @ToString.Exclude
    @OneToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "address_id")
    private Address address;

    // ==================== RECORD STATUS ====================

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 20
    )
    private RecordStatus status;

    // ==================== DERIVED INFORMATION ====================

    public String getFullName() {

        return Stream.of(
                firstName,
                middleName,
                lastName
            )
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .collect(Collectors.joining(" "));
    }
}