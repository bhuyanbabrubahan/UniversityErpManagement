package com.sms.dto;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponseDto {


    @Schema(
            description = "Department Id",
            example = "1"
    )
    private Long id;


    @Schema(
            description = "Department Name",
            example = "Computer Science"
    )
    private String departmentName;


    @Schema(
            description = "Department Code",
            example = "CSE"
    )
    private String departmentCode;


    @Schema(
            description = "Department Description",
            example = "Computer Science Department"
    )
    private String description;


    @Schema(
            description = "Record Status",
            example = "ACTIVE"
    )
    private RecordStatus status;




}