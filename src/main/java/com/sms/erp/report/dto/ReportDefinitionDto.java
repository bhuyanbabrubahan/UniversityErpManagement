package com.sms.erp.report.dto;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportDefinitionDto { private String code; private String name; private String category; private String format; private boolean pdfAvailable; }
