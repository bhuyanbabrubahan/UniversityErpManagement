package com.sms.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.location.address.entity.Address;
import com.location.country.entity.Country;
import com.location.district.entity.District;
import com.location.state.entity.State;
import com.location.tehsil.entity.Tehsil;
import com.location.village.entity.Village;
import com.sms.entity.Student;
import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public final class StudentSpecification {

    private StudentSpecification() {
    }

    // ==========================================================
    // Student ID
    // ==========================================================

    public static Specification<Student> hasId(Long id) {

        return (root, query, cb) ->
                id == null
                        ? null
                        : cb.equal(root.get("id"), id);
    }

    // ==========================================================
    // Roll Number
    // ==========================================================

    public static Specification<Student> hasRollNumber(String rollNumber) {

        return (root, query, cb) ->
                isBlank(rollNumber)
                        ? null
                        : cb.like(
                                cb.lower(root.get("rollNumber")),
                                "%" + rollNumber.trim().toLowerCase() + "%"
                        );
    }

    // ==========================================================
    // First Name
    // ==========================================================

    public static Specification<Student> hasFirstName(String firstName) {

        return (root, query, cb) ->
                isBlank(firstName)
                        ? null
                        : cb.like(
                                cb.lower(root.get("firstName")),
                                "%" + firstName.trim().toLowerCase() + "%"
                        );
    }

    // ==========================================================
    // Last Name
    // ==========================================================

    public static Specification<Student> hasLastName(String lastName) {

        return (root, query, cb) ->
                isBlank(lastName)
                        ? null
                        : cb.like(
                                cb.lower(root.get("lastName")),
                                "%" + lastName.trim().toLowerCase() + "%"
                        );
    }

    // ==========================================================
    // Full Name
    // ==========================================================

    public static Specification<Student> hasFullName(String fullName) {

        return (root, query, cb) -> {

            if (isBlank(fullName)) {
                return null;
            }

            return cb.like(
                    cb.lower(
                            cb.concat(
                                    cb.concat(root.get("firstName"), " "),
                                    root.get("lastName")
                            )
                    ),
                    "%" + fullName.trim().toLowerCase() + "%"
            );
        };
    }

   

    // ==========================================================
    // Department
    // ==========================================================

    public static Specification<Student> hasDepartmentId(Long departmentId) {

        return (root, query, cb) ->
                departmentId == null
                        ? null
                        : cb.equal(root.get("department").get("id"), departmentId);
    }

    // ==========================================================
    // Gender
    // ==========================================================

    public static Specification<Student> hasGender(Gender gender) {

        return (root, query, cb) ->
                gender == null
                        ? null
                        : cb.equal(root.get("gender"), gender);
    }

    // ==========================================================
    // Admission Date Range
    // ==========================================================

    public static Specification<Student> admissionDateFrom(LocalDate from) {

        return (root, query, cb) ->
                from == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("admissionDate"), from);
    }

    public static Specification<Student> admissionDateTo(LocalDate to) {

        return (root, query, cb) ->
                to == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("admissionDate"), to);
    }

    // ==========================================================
    // DOB Range
    // ==========================================================

    public static Specification<Student> dateOfBirthFrom(LocalDate from) {

        return (root, query, cb) ->
                from == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("dateOfBirth"), from);
    }

    public static Specification<Student> dateOfBirthTo(LocalDate to) {

        return (root, query, cb) ->
                to == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("dateOfBirth"), to);
    }

    

    // ==========================================================
    // Village
    // ==========================================================

    public static Specification<Student> hasVillage(String villageName) {

        return (root, query, cb) -> {

            if (isBlank(villageName)) {
                return null;
            }

            Join<Student, Address> address = root.join("address", JoinType.LEFT);
            Join<Address, Village> village = address.join("village", JoinType.LEFT);

            return cb.like(
                    cb.lower(village.get("villageName")),
                    "%" + villageName.trim().toLowerCase() + "%"
            );
        };
    }

    // ==========================================================
    // Tehsil
    // ==========================================================

    public static Specification<Student> hasTehsil(String tehsilName) {

        return (root, query, cb) -> {

            if (isBlank(tehsilName)) {
                return null;
            }

            Join<Student, Address> address = root.join("address");
            Join<Address, Village> village = address.join("village");
            Join<Village, Tehsil> tehsil = village.join("tehsil");

            return cb.like(
                    cb.lower(tehsil.get("tehsilName")),
                    "%" + tehsilName.trim().toLowerCase() + "%"
            );
        };
    }

    // ==========================================================
    // District
    // ==========================================================

    public static Specification<Student> hasDistrict(String districtName) {

        return (root, query, cb) -> {

            if (isBlank(districtName)) {
                return null;
            }

            Join<Student, Address> address = root.join("address");
            Join<Address, Village> village = address.join("village");
            Join<Village, Tehsil> tehsil = village.join("tehsil");
            Join<Tehsil, District> district = tehsil.join("district");

            return cb.like(
                    cb.lower(district.get("districtName")),
                    "%" + districtName.trim().toLowerCase() + "%"
            );
        };
    }

    // ==========================================================
    // State
    // ==========================================================

    public static Specification<Student> hasState(String stateName) {

        return (root, query, cb) -> {

            if (isBlank(stateName)) {
                return null;
            }

            Join<Student, Address> address = root.join("address");
            Join<Address, Village> village = address.join("village");
            Join<Village, Tehsil> tehsil = village.join("tehsil");
            Join<Tehsil, District> district = tehsil.join("district");
            Join<District, State> state = district.join("state");

            return cb.like(
                    cb.lower(state.get("stateName")),
                    "%" + stateName.trim().toLowerCase() + "%"
            );
        };
    }

    // ==========================================================
    // Country
    // ==========================================================

    public static Specification<Student> hasCountry(String countryName) {

        return (root, query, cb) -> {

            if (isBlank(countryName)) {
                return null;
            }

            Join<Student, Address> address = root.join("address");
            Join<Address, Village> village = address.join("village");
            Join<Village, Tehsil> tehsil = village.join("tehsil");
            Join<Tehsil, District> district = tehsil.join("district");
            Join<District, State> state = district.join("state");
            Join<State, Country> country = state.join("country");

            return cb.like(
                    cb.lower(country.get("countryName")),
                    "%" + countryName.trim().toLowerCase() + "%"
            );
        };
    }

    // ==========================================================
    // Pincode
    // ==========================================================

    public static Specification<Student> hasPincode(String pincode) {

        return (root, query, cb) -> {

            if (isBlank(pincode)) {
                return null;
            }

            Join<Student, Address> address = root.join("address");
            Join<Address, Village> village = address.join("village");

            return cb.equal(village.get("pincode"), pincode.trim());
        };
    }

    // ==========================================================
    // Status
    // ==========================================================

    public static Specification<Student> hasStatus(RecordStatus status) {

        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }

    // ==========================================================
    // Utility
    // ==========================================================

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}