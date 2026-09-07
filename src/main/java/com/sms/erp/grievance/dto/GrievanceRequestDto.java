package com.sms.erp.grievance.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GrievanceRequestDto {
    private String code;
    private String ticketNo;
    private String raisedBy;
    private String category;
    private String subject;
    private String assignedTo;
    private String resolution;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
