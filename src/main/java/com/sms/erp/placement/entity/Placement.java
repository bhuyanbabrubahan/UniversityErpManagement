package com.sms.erp.placement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_placement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Placement {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String companyName;
    private LocalDate driveDate;
    private String jobTitle;
    private BigDecimal packageAmount;
    private String eligibility;
    private Integer selectedStudents;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
