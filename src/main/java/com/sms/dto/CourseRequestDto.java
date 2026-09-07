package com.sms.dto;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {

	@Schema(
	        description = "Course Code",
	        example = "CSE101"
	)
    @NotBlank(message = "Course code is required")
    @Size(max = 20)
    private String courseCode;

    
    @Schema(
            description = "Course Name",
            example = "Java Full Stack"
    )
    @NotBlank(message = "Course name is required")
    @Size(max = 100)
    private String courseName;

    
    
    
    @Schema(
            description = "Duration in Months",
            example = "48"
    )
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 month")
    private Integer durationInMonths;

    
    
    
    @Schema(
            description = "Course Description",
            example = "Java Spring Boot Course"
    )
    @Size(max = 500)
    private String description;

    
    
    @Schema(
            description = "Department Id",
            example = "2"
    )
    @NotNull(message = "Department is required")
    private Long departmentId;

    private RecordStatus status;
}