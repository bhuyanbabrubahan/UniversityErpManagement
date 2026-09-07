package com.sms.erp.staff.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_staff")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Staff {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String employeeCode;
    private String fullName;
    private String department;
    private String email;
    private String phone;
    private String designation;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
