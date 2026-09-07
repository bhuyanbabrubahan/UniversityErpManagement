package com.sms.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.sms.entity.Semester;
import com.sms.enums.RecordStatus;

public class SemesterSpecification {

    // ==================================================
    // Semester Id
    // ==================================================

    public static Specification<Semester> hasSemesterId(
            Long semesterId
    ) {

        return (root, query, cb) ->

                cb.equal(
                        root.get("id"),
                        semesterId
                );
    }



    // ==================================================
    // Record Status
    // ==================================================

    public static Specification<Semester> hasStatus(
            RecordStatus status
    ) {

        return (root, query, cb) ->

                cb.equal(
                        root.get("status"),
                        status
                );
    }



    // ==================================================
    // Semester Name
    // ==================================================

    public static Specification<Semester> hasSemesterName(
            String semesterName
    ) {

        return (root, query, cb) ->

                cb.like(

                        cb.lower(
                                root.get("semesterName")
                        ),

                        "%"

                        + semesterName
                        .trim()
                        .toLowerCase()

                        + "%"

                );

    }



    // ==================================================
    // Semester Number
    // ==================================================

    public static Specification<Semester> hasSemesterNumber(
            Integer semesterNumber
    ) {

        return (root, query, cb) ->

                cb.equal(
                        root.get("semesterNumber"),
                        semesterNumber
                );
    }



    // ==================================================
    // Course Id
    // ==================================================

    public static Specification<Semester> hasCourse(
            Long courseId
    ) {

        return (root, query, cb) ->

                cb.equal(

                        root
                        .get("course")
                        .get("id"),

                        courseId

                );

    }



    // ==================================================
    // Course Code
    // ==================================================

    public static Specification<Semester> hasCourseCode(
            String courseCode
    ) {

        return (root, query, cb) ->

                cb.like(

                        cb.lower(

                                root
                                .get("course")
                                .get("courseCode")

                        ),

                        "%"

                        + courseCode
                        .trim()
                        .toLowerCase()

                        + "%"

                );

    }



    // ==================================================
    // Course Name
    // ==================================================

    public static Specification<Semester> hasCourseName(
            String courseName
    ) {

        return (root, query, cb) ->

                cb.like(

                        cb.lower(

                                root
                                .get("course")
                                .get("courseName")

                        ),

                        "%"

                        + courseName
                        .trim()
                        .toLowerCase()

                        + "%"

                );

    }



    // ==================================================
    // Minimum Working Days
    // ==================================================

    public static Specification<Semester> hasMinWorkingDays(
            Integer minWorkingDays
    ) {

        return (root, query, cb) ->

                cb.greaterThanOrEqualTo(

                        root.get("totalWorkingDays"),

                        minWorkingDays

                );

    }



    // ==================================================
    // Maximum Working Days
    // ==================================================

    public static Specification<Semester> hasMaxWorkingDays(
            Integer maxWorkingDays
    ) {

        return (root, query, cb) ->

                cb.lessThanOrEqualTo(

                        root.get("totalWorkingDays"),

                        maxWorkingDays

                );

    }



    // ==================================================
    // Semester Start Date From
    // ==================================================

    public static Specification<Semester> hasStartDateFrom(
            LocalDate startDate
    ) {

        return (root, query, cb) ->

                cb.greaterThanOrEqualTo(

                        root.get("semesterStartDate"),

                        startDate

                );

    }



    // ==================================================
    // Semester Start Date To
    // ==================================================

    public static Specification<Semester> hasStartDateTo(
            LocalDate endDate
    ) {

        return (root, query, cb) ->

                cb.lessThanOrEqualTo(

                        root.get("semesterStartDate"),

                        endDate

                );

    }



    // ==================================================
    // Semester End Date From
    // ==================================================

    public static Specification<Semester> hasEndDateFrom(
            LocalDate startDate
    ) {

        return (root, query, cb) ->

                cb.greaterThanOrEqualTo(

                        root.get("semesterEndDate"),

                        startDate

                );

    }



    // ==================================================
    // Semester End Date To
    // ==================================================

    public static Specification<Semester> hasEndDateTo(
            LocalDate endDate
    ) {

        return (root, query, cb) ->

                cb.lessThanOrEqualTo(

                        root.get("semesterEndDate"),

                        endDate

                );

    }

}