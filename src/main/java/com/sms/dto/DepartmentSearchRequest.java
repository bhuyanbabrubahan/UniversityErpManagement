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
public class DepartmentSearchRequest {

	@Schema(
	        description = "Department Name",
	        example = "Computer"
	)
	private String departmentName;
	
	private String departmentCode;
	
	@Schema(
	        description = "Department Status",
	        example = "ACTIVE"
	)
	private RecordStatus status;

}
