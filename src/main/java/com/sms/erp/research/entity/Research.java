package com.sms.erp.research.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_research")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Research {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String title;
    private String principalInvestigator;
    private String department;
    private BigDecimal grantAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
