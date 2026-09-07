package com.sms.erp.grievance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_grievance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Grievance {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String ticketNo;
    private String raisedBy;
    private String category;
    private String subject;
    private String assignedTo;
    private String resolution;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
