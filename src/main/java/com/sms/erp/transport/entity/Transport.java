package com.sms.erp.transport.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_transport")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transport {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String vehicleNo;
    private String route;
    private String driver;
    private Integer capacity;
    private String fee;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
