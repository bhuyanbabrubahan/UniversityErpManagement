package com.sms.dto;


import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(
        name="Subject Search Request",
        description="Search filters for Subject"
)
@Getter
@Setter
public class SubjectSearchRequest {

	@Schema(example="SUB001")
    private String subjectCode;

	@Schema(example="Java")
    private String subjectName;

	@Schema(example="4")
    private Integer credits;

	@Schema(example="1")
    private Long semesterId;

	@Schema(example = "1")
	private Integer semesterNumber;
	
	@Schema(
			example="ACTIVE"
			)
    private RecordStatus status;

}