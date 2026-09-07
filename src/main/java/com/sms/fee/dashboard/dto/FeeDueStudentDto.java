package com.sms.fee.dashboard.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeeDueStudentDto {
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private BigDecimal dueAmount;
}
