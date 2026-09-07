package com.sms.security.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;
import com.sms.security.dto.AdminSearchRequestDto;
import com.sms.security.entity.Admin;
import com.sms.security.enums.AdminDesignation;

import jakarta.persistence.criteria.JoinType;

public final class AdminSpecification {

    private AdminSpecification() {
    }

    public static Specification<Admin> search(AdminSearchRequestDto request) {

        Specification<Admin> spec = Specification.where(null);

        if (request == null) {
            return spec;
        }

        // IDENTITY

        if (request.getId() != null) {
            spec = spec.and(hasId(request.getId()));
        }

        if (hasText(request.getEmployeeCode())) {
            spec = spec.and(hasEmployeeCode(request.getEmployeeCode()));
        }

        // PERSONAL INFORMATION

        if (hasText(request.getFirstName())) {
            spec = spec.and(hasFirstName(request.getFirstName()));
        }

        if (hasText(request.getLastName())) {
            spec = spec.and(hasLastName(request.getLastName()));
        }

        if (hasText(request.getFullName())) {
            spec = spec.and(hasFullName(request.getFullName()));
        }

        if (request.getGender() != null) {
            spec = spec.and(hasGender(request.getGender()));
        }

        if (request.getDateOfBirthFrom() != null) {
            spec = spec.and(dateOfBirthFrom(request.getDateOfBirthFrom()));
        }

        if (request.getDateOfBirthTo() != null) {
            spec = spec.and(dateOfBirthTo(request.getDateOfBirthTo()));
        }

        // EMPLOYMENT INFORMATION

        if (request.getDesignation() != null) {
            spec = spec.and(hasDesignation(request.getDesignation()));
        }

        if (request.getJoiningDateFrom() != null) {
            spec = spec.and(joiningDateFrom(request.getJoiningDateFrom()));
        }

        if (request.getJoiningDateTo() != null) {
            spec = spec.and(joiningDateTo(request.getJoiningDateTo()));
        }

        // ACCOUNT INFORMATION

        if (hasText(request.getEmail())) {
            spec = spec.and(hasEmail(request.getEmail()));
        }

        if (hasText(request.getMobileNumber())) {
            spec = spec.and(hasMobileNumber(request.getMobileNumber()));
        }

        // ADDRESS INFORMATION

        if (request.getVillageId() != null) {
            spec = spec.and(hasVillageId(request.getVillageId()));
        }

        if (request.getTehsilId() != null) {
            spec = spec.and(hasTehsilId(request.getTehsilId()));
        }

        if (request.getDistrictId() != null) {
            spec = spec.and(hasDistrictId(request.getDistrictId()));
        }

        if (request.getStateId() != null) {
            spec = spec.and(hasStateId(request.getStateId()));
        }

        if (request.getCountryId() != null) {
            spec = spec.and(hasCountryId(request.getCountryId()));
        }

        if (hasText(request.getVillage())) {
            spec = spec.and(hasVillage(request.getVillage()));
        }

        if (hasText(request.getDistrict())) {
            spec = spec.and(hasDistrict(request.getDistrict()));
        }

        if (hasText(request.getState())) {
            spec = spec.and(hasState(request.getState()));
        }

        if (hasText(request.getCountry())) {
            spec = spec.and(hasCountry(request.getCountry()));
        }

        if (hasText(request.getPincode())) {
            spec = spec.and(hasPincode(request.getPincode()));
        }

        // STATUS

        if (request.getStatus() != null) {
            spec = spec.and(hasStatus(request.getStatus()));
        }

        return spec;
    }

    public static Specification<Admin> hasId(Long id) {
        return (root, query, cb) ->
                cb.equal(root.get("id"), id);
    }

    public static Specification<Admin> hasEmployeeCode(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(root.get("employeeCode")),
                    contains(value)
                );
    }

    public static Specification<Admin> hasFirstName(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(root.get("firstName")),
                    contains(value)
                );
    }

    public static Specification<Admin> hasLastName(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(root.get("lastName")),
                    contains(value)
                );
    }

    public static Specification<Admin> hasFullName(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(
                        cb.concat(
                            cb.concat(
                                root.get("firstName"),
                                " "
                            ),
                            root.get("lastName")
                        )
                    ),
                    contains(value)
                );
    }

    public static Specification<Admin> hasGender(Gender value) {
        return (root, query, cb) ->
                cb.equal(root.get("gender"), value);
    }

    public static Specification<Admin> dateOfBirthFrom(LocalDate value) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                    root.get("dateOfBirth"),
                    value
                );
    }

    public static Specification<Admin> dateOfBirthTo(LocalDate value) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                    root.get("dateOfBirth"),
                    value
                );
    }

    public static Specification<Admin> hasDesignation(AdminDesignation value) {
        return (root, query, cb) ->
                cb.equal(root.get("designation"), value);
    }

    public static Specification<Admin> joiningDateFrom(LocalDate value) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                    root.get("joiningDate"),
                    value
                );
    }

    public static Specification<Admin> joiningDateTo(LocalDate value) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                    root.get("joiningDate"),
                    value
                );
    }

    public static Specification<Admin> hasEmail(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(
                        root.join("user", JoinType.INNER)
                            .get("email")
                    ),
                    contains(value)
                );
    }

    public static Specification<Admin> hasMobileNumber(String value) {
        return (root, query, cb) ->
                cb.equal(
                    root.join("user", JoinType.INNER)
                        .get("mobileNumber"),
                    value.trim()
                );
    }

    public static Specification<Admin> hasVillageId(Long value) {
        return (root, query, cb) ->
                cb.equal(
                    root.join("address", JoinType.INNER)
                        .join("village", JoinType.INNER)
                        .get("id"),
                    value
                );
    }

    public static Specification<Admin> hasTehsilId(Long value) {
        return (root, query, cb) ->
                cb.equal(
                    root.join("address", JoinType.INNER)
                        .join("village", JoinType.INNER)
                        .join("tehsil", JoinType.INNER)
                        .get("id"),
                    value
                );
    }

    public static Specification<Admin> hasDistrictId(Long value) {
        return (root, query, cb) ->
                cb.equal(
                    root.join("address", JoinType.INNER)
                        .join("village", JoinType.INNER)
                        .join("tehsil", JoinType.INNER)
                        .join("district", JoinType.INNER)
                        .get("id"),
                    value
                );
    }

    public static Specification<Admin> hasStateId(Long value) {
        return (root, query, cb) ->
                cb.equal(
                    root.join("address", JoinType.INNER)
                        .join("village", JoinType.INNER)
                        .join("tehsil", JoinType.INNER)
                        .join("district", JoinType.INNER)
                        .join("state", JoinType.INNER)
                        .get("id"),
                    value
                );
    }

    public static Specification<Admin> hasCountryId(Long value) {
        return (root, query, cb) ->
                cb.equal(
                    root.join("address", JoinType.INNER)
                        .join("village", JoinType.INNER)
                        .join("tehsil", JoinType.INNER)
                        .join("district", JoinType.INNER)
                        .join("state", JoinType.INNER)
                        .join("country", JoinType.INNER)
                        .get("id"),
                    value
                );
    }

    public static Specification<Admin> hasVillage(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(
                        root.join("address", JoinType.INNER)
                            .join("village", JoinType.INNER)
                            .get("villageName")
                    ),
                    contains(value)
                );
    }

    public static Specification<Admin> hasDistrict(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(
                        root.join("address", JoinType.INNER)
                            .join("village", JoinType.INNER)
                            .join("tehsil", JoinType.INNER)
                            .join("district", JoinType.INNER)
                            .get("districtName")
                    ),
                    contains(value)
                );
    }

    public static Specification<Admin> hasState(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(
                        root.join("address", JoinType.INNER)
                            .join("village", JoinType.INNER)
                            .join("tehsil", JoinType.INNER)
                            .join("district", JoinType.INNER)
                            .join("state", JoinType.INNER)
                            .get("stateName")
                    ),
                    contains(value)
                );
    }

    public static Specification<Admin> hasCountry(String value) {
        return (root, query, cb) ->
                cb.like(
                    cb.lower(
                        root.join("address", JoinType.INNER)
                            .join("village", JoinType.INNER)
                            .join("tehsil", JoinType.INNER)
                            .join("district", JoinType.INNER)
                            .join("state", JoinType.INNER)
                            .join("country", JoinType.INNER)
                            .get("countryName")
                    ),
                    contains(value)
                );
    }

    public static Specification<Admin> hasPincode(String value) {
        return (root, query, cb) ->
                cb.equal(
                    root.join("address", JoinType.INNER)
                        .join("village", JoinType.INNER)
                        .get("pincode"),
                    value.trim()
                );
    }

    public static Specification<Admin> hasStatus(RecordStatus value) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), value);
    }

    private static String contains(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
