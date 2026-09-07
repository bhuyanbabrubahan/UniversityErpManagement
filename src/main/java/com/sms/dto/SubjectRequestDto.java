package com.sms.dto;

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
	    description = "Unique subject code. Leave empty during creation if auto-generated.",
	    example = "SUB001",
	    accessMode = Schema.AccessMode.READ_ONLY
	)
public class SubjectRequestDto {

	@Schema(
		    description = "Unique subject code. Leave empty during creation if auto-generated.",
		    example = "SUB001",
		    accessMode = Schema.AccessMode.READ_ONLY
		)
		private String subjectCode;

    
	@Schema(
		    description = "Subject name",
		    example = "Advanced Java Programming",
		    requiredMode = Schema.RequiredMode.REQUIRED
		)
		@NotBlank(message = "Subject name is required")
    private String subjectName;

    
    
	@Schema(
		    description = "Number of credits",
		    example = "4",
		    minimum = "1",
		    maximum = "10",
		    requiredMode = Schema.RequiredMode.REQUIRED
		)
		@NotNull(message = "Credits are required")
		@Min(value = 1, message = "Credits must be at least 1")
		@Max(value = 10, message = "Credits cannot exceed 10")
    	private Integer credits;
    
    

    @Schema(
    	    description = "Maximum theory marks",
    	    example = "70",
    	    minimum = "0",
    	    maximum = "100",
    	    requiredMode = Schema.RequiredMode.REQUIRED
    	)
    private Integer theoryMarks;
    
    

    @Schema(
    	    description = "Maximum practical marks",
    	    example = "30",
    	    minimum = "0",
    	    maximum = "100",
    	    requiredMode = Schema.RequiredMode.REQUIRED
    	)
    private Integer practicalMarks;
    
    
    

    @Schema(
    	    description = "Semester Id",
    	    example = "1",
    	    requiredMode = Schema.RequiredMode.REQUIRED
    	)
    	@NotNull(message = "Semester id is required")
    	private Long semesterId;
    

}