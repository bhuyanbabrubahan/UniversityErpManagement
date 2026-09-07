package com.sms.erp.certificate.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_certificate")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Certificate {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String student;
    private String certificateType;
    private LocalDate requestDate;
    private LocalDate issuedDate;
    private String certificateNo;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
