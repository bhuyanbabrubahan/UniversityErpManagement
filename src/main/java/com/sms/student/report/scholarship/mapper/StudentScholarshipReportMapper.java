package com.sms.student.report.scholarship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.scholarship.entity.StudentScholarship;
import com.sms.student.report.scholarship.dto.ScholarshipReportResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentScholarshipReportMapper {

	// Entity To Scholarship Report DTO

	@Mapping(target = "scholarshipId", source = "id")

	@Mapping(target = "studentId", source = "student.id")

	@Mapping(target = "studentName", source = "student.fullName")

	@Mapping(target = "rollNumber", source = "student.rollNumber")
	
	@Mapping(target = "courseName", source = "admission.course.courseName")

	@Mapping(target = "departmentName", source = "admission.department.departmentName")

	@Mapping(target = "semesterName", source = "admission.semester.semesterName")

	@Mapping(target = "admissionId", source = "admission.id")

	@Mapping(target = "admissionNumber", source = "admission.admissionNumber")

	@Mapping(target = "approvedBy", source = "approvedBy")

	@Mapping(target = "approvalDate", source = "approvalDate")

	ScholarshipReportResponseDto toDto(StudentScholarship entity);

}