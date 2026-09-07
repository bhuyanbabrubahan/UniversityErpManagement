package com.sms.dto;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDto {

	@Schema(
	        description = "Department Name",
	        example = "Computer Science"
	)
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name cannot exceed 100 characters")
    private String departmentName;

	
	@Schema(
	        description = "Department Code",
	        example = "CSE"
	)
    @NotBlank(message = "Department code is required")
    @Size(max = 20, message = "Department code cannot exceed 20 characters")
    private String departmentCode;

	
	@Schema(
	        description = "Department Description",
	        example = "Department of Computer Science Engineering"
	)
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
    
	
	
	private RecordStatus status;
	


}