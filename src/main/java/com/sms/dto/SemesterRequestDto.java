package com.sms.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "Semester Request",
        description = "Request body for creating or updating a semester"
)
public class SemesterRequestDto {

    @Schema(
            description = "Semester Name",
            example = "Semester 1"
    )
    @NotBlank(message = "Semester name is required.")
    private String semesterName;
    
    

    @Schema(
            description = "Semester Number",
            example = "1"
    )
    @NotNull(message = "Semester number is required.")
    @Min(value = 1, message = "Semester number must be at least 1.")
    @Max(value = 12, message = "Semester number cannot be greater than 12.")
    private Integer semesterNumber;
    
    

    @Schema(
            description = "Semester Start Date",
            example = "2026-01-01"
    )
    @NotNull(message = "Semester start date is required.")
    private LocalDate semesterStartDate;
    
    

    @Schema(
            description = "Semester End Date",
            example = "2026-06-30"
    )
    @NotNull(message = "Semester end date is required.")
    private LocalDate semesterEndDate;
    
    

    @Schema(
            description = "Automatically calculated total working days in the semester",
            example = "154"
    )
    private Integer totalWorkingDays;
    
    

    @Schema(
            description = "Course Id",
            example = "1"
    )
    @NotNull(message = "Course id is required.")
    private Long courseId;

}