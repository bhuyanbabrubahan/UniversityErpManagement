package com.sms.erp.transport.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransportResponseDto {
    private Long id;
    private String code;
    private String vehicleNo;
    private String route;
    private String driver;
    private Integer capacity;
    private String fee;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
