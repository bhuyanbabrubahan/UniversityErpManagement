package com.sms.erp.installment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_installment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Installment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String student;
    private String feeHead;
    private Integer installmentNo;
    private LocalDate dueDate;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
