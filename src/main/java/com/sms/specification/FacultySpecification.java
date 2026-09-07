package com.sms.specification;

import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import com.sms.dto.FacultySearchRequest;
import com.sms.entity.Faculty;
import com.sms.enums.Designation;
import com.sms.enums.Qualification;
import com.sms.enums.RecordStatus;

public final class FacultySpecification {
	private FacultySpecification() {
	}

	public static Specification<Faculty> search(FacultySearchRequest request) {
		Specification<Faculty> spec = Specification.where(null);
		if (request == null)
			return status(RecordStatus.ACTIVE);
		if (request.getId() != null)
			spec = spec.and(hasId(request.getId()));
		if (hasText(request.getEmployeeCode()))
			spec = spec.and(hasEmployeeCode(request.getEmployeeCode()));
		if (hasText(request.getFirstName()))
			spec = spec.and(hasFirstName(request.getFirstName()));
		if (hasText(request.getLastName()))
			spec = spec.and(hasLastName(request.getLastName()));
		if (hasText(request.getFullName()))
			spec = spec.and(hasFullName(request.getFullName()));
		if (request.getDepartmentId() != null)
			spec = spec.and(hasDepartmentId(request.getDepartmentId()));
		if (request.getDesignation() != null)
			spec = spec.and(hasDesignation(request.getDesignation()));
		if (request.getQualification() != null)
			spec = spec.and(hasQualification(request.getQualification()));
		if (request.getStatus() != null)
			spec = spec.and(status(request.getStatus()));
		else
			spec = spec.and(status(RecordStatus.ACTIVE));
		return spec;
	}

	public static Specification<Faculty> hasId(Long id) {
		return (root, query, cb) -> cb.equal(root.get("id"), id);
	}

	public static Specification<Faculty> hasEmployeeCode(String employeeCode) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("employeeCode")), contains(employeeCode));
	}

	public static Specification<Faculty> hasFirstName(String firstName) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("firstName")), contains(firstName));
	}

	public static Specification<Faculty> hasLastName(String lastName) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("lastName")), contains(lastName));
	}

	public static Specification<Faculty> hasFullName(String fullName) {
		return (root, query, cb) -> cb.like(
				cb.lower(cb.concat(cb.concat(root.get("firstName"), " "), root.get("lastName"))), contains(fullName));
	}

	public static Specification<Faculty> hasDepartmentId(Long departmentId) {
		return (root, query, cb) -> cb.equal(root.get("department").get("id"), departmentId);
	}

	public static Specification<Faculty> hasDesignation(Designation designation) {
		return (root, query, cb) -> cb.equal(root.get("designation"), designation);
	}

	public static Specification<Faculty> hasQualification(Qualification qualification) {
		return (root, query, cb) -> cb.equal(root.get("qualification"), qualification);
	}

	public static Specification<Faculty> status(RecordStatus status) {
		return (root, query, cb) -> cb.equal(root.get("status"), status);
	}

	private static String contains(String value) {
		return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
	}

	private static boolean hasText(String value) {
		return value != null && !value.trim().isEmpty();
	}
}
