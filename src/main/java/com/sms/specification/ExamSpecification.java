package com.sms.specification;


import org.springframework.data.jpa.domain.Specification;

import com.sms.entity.Exam;
import com.sms.enums.ExamType;
import com.sms.enums.RecordStatus;


public class ExamSpecification {


    // ==========================
    // Subject Filter
    // ==========================

    public static Specification<Exam> hasSubject(
            Long subjectId
    ){

        return (root, query, cb) ->

                cb.equal(
                        root.get("subject").get("id"),
                        subjectId
                );
    }





    // ==========================
    // Semester Filter
    // ==========================

    public static Specification<Exam> hasSemester(
            Long semesterId
    ){

        return (root, query, cb) ->

                cb.equal(
                        root.get("subject")
                            .get("semester")
                            .get("id"),
                        semesterId
                );
    }





    // ==========================
    // Exam Type Filter
    // ==========================

    public static Specification<Exam> hasExamType(
            ExamType examType
    ){

        return (root, query, cb) ->

                cb.equal(
                        root.get("examType"),
                        examType
                );

    }





    // ==========================
    // Exam Date Filter
    // ==========================

    public static Specification<Exam> hasExamDate(
            java.time.LocalDate examDate
    ){

        return (root, query, cb) ->

                cb.equal(
                        root.get("examDate"),
                        examDate
                );
    }





    // ==========================
    // Status Filter
    // ==========================

    public static Specification<Exam> hasStatus(
    		RecordStatus status
    ){

        return (root, query, cb) ->

                cb.equal(
                        root.get("status"),
                        status
                );

    }





    // ==========================
    // Exclude Deleted
    // ==========================

    public static Specification<Exam> statusNot(
    		RecordStatus status
    ){

        return (root, query, cb) ->

                cb.notEqual(
                        root.get("status"),
                        status
                );

    }



}