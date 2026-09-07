package com.sms.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.dto.ResultSearchRequest;
import com.sms.entity.Exam;
import com.sms.entity.Result;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class ResultSpecification {

    public static Specification<Result> filter(
            ResultSearchRequest request
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();


            // ==================================================
            // Default Active Result Only
            // ==================================================

            predicates.add(

                    cb.equal(
                            root.get("status"),
                            RecordStatus.ACTIVE
                    )

            );


            // ==================================================
            // Common Joins
            // ==================================================

            Join<Result, Student> studentJoin =
                    root.join("student");

            Join<Result, Exam> examJoin =
                    root.join("exam");


            // ==================================================
            // Student Id Filter
            // ==================================================

            if (request.getStudentId() != null) {

                predicates.add(

                        cb.equal(
                                studentJoin.get("id"),
                                request.getStudentId()
                        )

                );

            }


            // ==================================================
            // Roll Number Search
            // ==================================================

            if (request.getRollNumber() != null
                    && !request.getRollNumber().isBlank()) {

                predicates.add(

                        cb.like(

                                cb.lower(
                                        studentJoin.get("rollNumber")
                                ),

                                "%"
                                        + request.getRollNumber().toLowerCase()
                                        + "%"

                        )

                );

            }


            // ==================================================
            // Student Name Search
            // ==================================================

            if (request.getStudentName() != null
                    && !request.getStudentName().isBlank()) {

                Expression<String> fullName = cb.concat(

                        cb.concat(

                                cb.lower(studentJoin.get("firstName")),

                                " "

                        ),

                        cb.lower(studentJoin.get("lastName"))

                );

                predicates.add(

                        cb.like(

                                fullName,

                                "%" + request.getStudentName().trim().toLowerCase() + "%"

                        )

                );

            }

            // ==================================================
            // Exam Filter
            // ==================================================

            if (request.getExamId() != null) {

                predicates.add(

                        cb.equal(
                                examJoin.get("id"),
                                request.getExamId()
                        )

                );

            }


            // ==================================================
            // Subject Filter
            // ==================================================

            if (request.getSubjectId() != null) {

                predicates.add(

                        cb.equal(

                                examJoin
                                        .get("subject")
                                        .get("id"),

                                request.getSubjectId()

                        )

                );

            }


            // ==================================================
            // Semester Filter
            // ==================================================

            if (request.getSemesterId() != null) {

                predicates.add(

                        cb.equal(

                                examJoin
                                        .get("semester")
                                        .get("id"),

                                request.getSemesterId()

                        )

                );

            }


            // ==================================================
            // Result Status
            // ==================================================

            if (request.getResultStatus() != null) {

                predicates.add(

                        cb.equal(

                                root.get("resultStatus"),

                                request.getResultStatus()

                        )

                );

            }


            // ==================================================
            // Grade
            // ==================================================

            if (request.getGrade() != null
                    && !request.getGrade().isBlank()) {

                predicates.add(

                        cb.equal(

                                root.get("grade"),

                                request.getGrade()

                        )

                );

            }


            // ==================================================
            // Percentage Range 
            // ==================================================

            if (request.getMinPercentage() != null) {

            	predicates.add(

            	        cb.and(

            	            cb.greaterThanOrEqualTo(
            	                    root.get("obtainedMarks"),
            	                    request.getMinObtainedMarks()
            	            ),

            	            cb.lessThanOrEqualTo(
            	                    root.get("obtainedMarks"),
            	                    examJoin.get("maximumMarks")
            	            )

            	        )

            	);

            }

            if (request.getMaxPercentage() != null) {

            	predicates.add(

            	        cb.and(

            	            cb.lessThanOrEqualTo(
            	                    root.get("obtainedMarks"),
            	                    request.getMaxObtainedMarks()
            	            ),

            	            cb.lessThanOrEqualTo(
            	                    root.get("obtainedMarks"),
            	                    examJoin.get("maximumMarks")
            	            )

            	        )

            	);

            }


            // ==================================================
            // Obtained Marks Range
            // ==================================================

            if (request.getMinObtainedMarks() != null) {

                predicates.add(

                        cb.greaterThanOrEqualTo(

                                root.get("obtainedMarks"),

                                request.getMinObtainedMarks()

                        )

                );

            }

            if (request.getMaxObtainedMarks() != null) {

                predicates.add(

                        cb.lessThanOrEqualTo(

                                root.get("obtainedMarks"),

                                request.getMaxObtainedMarks()

                        )

                );

            }


            return cb.and(
                    predicates.toArray(new Predicate[0])
            );

        };

    }

}