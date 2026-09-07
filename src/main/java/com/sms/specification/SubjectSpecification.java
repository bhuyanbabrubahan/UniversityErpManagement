package com.sms.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sms.entity.Subject;
import com.sms.enums.RecordStatus;

public class SubjectSpecification {

    // ==========================
    // STATUS
    // ==========================
    public static Specification<Subject> hasStatus(
            RecordStatus status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }


    // ==========================
    // SUBJECT NAME
    // ==========================
    public static Specification<Subject> hasSubjectName(
            String subjectName) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("subjectName")
                        ),
                        "%" + subjectName.toLowerCase() + "%"
                );
    }


    // ==========================
    // SUBJECT CODE
    // ==========================
    public static Specification<Subject> hasSubjectCode(
            String subjectCode) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("subjectCode")
                        ),
                        "%" + subjectCode.toLowerCase() + "%"
                );
    }


    // ==========================
    // SEMESTER ID
    // ==========================
    public static Specification<Subject> hasSemester(
            Long semesterId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("semester").get("id"),
                        semesterId
                );
    }
    
    


    // ==========================
    // SEMESTER NUMBER
    // ==========================
    public static Specification<Subject> hasSemesterNumber(
            Integer semesterNumber) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("semester")
                            .get("semesterNumber"),
                        semesterNumber
                );
    }


    // ==========================
    // CREDITS
    // ==========================
    public static Specification<Subject> hasCredits(
            Integer credits) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("credits"),
                        credits
                );
    }

}