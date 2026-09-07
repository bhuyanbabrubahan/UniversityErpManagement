package com.sms.erp.hostel.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_hostel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Hostel {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String hostelName;
    private String roomNo;
    private Integer capacity;
    private Integer occupied;
    private BigDecimal bedsFee;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
