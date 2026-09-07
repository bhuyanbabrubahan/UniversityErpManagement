package com.sms.erp.hod.entity;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "erp_hod",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_erp_hod_code",
            columnNames = "code"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(name = "employee_code", length = 100)
    private String employeeCode;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(length = 150)
    private String department;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RecordStatus status;

    @Column(nullable = false, length = 500)
    private String description;
}
