package com.sms.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sms.entity.FacultySubjectMapping;
import com.sms.enums.RecordStatus;

public class FacultySubjectSpecification {

    // ==========================
    // STATUS
    // ==========================
    public static Specification<FacultySubjectMapping> hasStatus(
    		RecordStatus status) {

        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    // ==========================
    // FACULTY
    // ==========================
    public static Specification<FacultySubjectMapping> hasFaculty(
            Long facultyId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("faculty").get("id"),
                        facultyId
                );
    }

    // ==========================
    // SUBJECT
    // ==========================
    public static Specification<FacultySubjectMapping> hasSubject(
            Long subjectId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("subject").get("id"),
                        subjectId
                );
    }

    // ==========================
    // ACADEMIC YEAR
    // ==========================
    public static Specification<FacultySubjectMapping> hasAcademicYear(
            String academicYear) {

        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("academicYear")),
                        "%" + academicYear.toLowerCase() + "%"
                );
    }
    
    public static Specification<FacultySubjectMapping> statusNot(
    		RecordStatus status){

    return (root,query,cb)->

    cb.notEqual(
    root.get("status"),
    status);

    }

}