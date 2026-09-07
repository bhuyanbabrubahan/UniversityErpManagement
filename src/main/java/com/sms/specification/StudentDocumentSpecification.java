package com.sms.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.dto.studentdocument.StudentDocumentSearchRequestDto;
import com.sms.entity.StudentDocument;
import com.sms.enums.RecordStatus;

import jakarta.persistence.criteria.Predicate;

public class StudentDocumentSpecification {

    private StudentDocumentSpecification() {

    }

    public static Specification<StudentDocument> search(

            StudentDocumentSearchRequestDto request

    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();


            // =====================================
            // ACTIVE RECORDS ONLY
            // =====================================

            predicates.add(

                    cb.equal(

                            root.get("status"),

                            RecordStatus.ACTIVE

                    )

            );


            // =====================================
            // DOCUMENT ID
            // =====================================

            if (request.getId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("id"),

                                request.getId()

                        )

                );

            }


            // =====================================
            // STUDENT ID
            // =====================================

            if (request.getStudentId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("student").get("id"),

                                request.getStudentId()

                        )

                );

            }


            // =====================================
            // DOCUMENT TYPE
            // =====================================

            if (request.getDocumentType() != null) {

                predicates.add(

                        cb.equal(

                                root.get("documentType"),

                                request.getDocumentType()

                        )

                );

            }


            // =====================================
            // ORIGINAL FILE NAME
            // =====================================

            if (request.getOriginalFileName() != null
                    && !request.getOriginalFileName().trim().isEmpty()) {

                predicates.add(

                        cb.like(

                                cb.lower(

                                        root.get("originalFileName")

                                ),

                                "%" +

                                request.getOriginalFileName()
                                        .trim()
                                        .toLowerCase()

                                + "%"

                        )

                );

            }


            // =====================================
            // STORED FILE NAME
            // =====================================

            if (request.getStoredFileName() != null
                    && !request.getStoredFileName().trim().isEmpty()) {

                predicates.add(

                        cb.like(

                                cb.lower(

                                        root.get("storedFileName")

                                ),

                                "%" +

                                request.getStoredFileName()
                                        .trim()
                                        .toLowerCase()

                                + "%"

                        )

                );

            }


            // =====================================
            // CONTENT TYPE
            // =====================================

            if (request.getContentType() != null
                    && !request.getContentType().trim().isEmpty()) {

                predicates.add(

                        cb.like(

                                cb.lower(

                                        root.get("contentType")

                                ),

                                "%" +

                                request.getContentType()
                                        .trim()
                                        .toLowerCase()

                                + "%"

                        )

                );

            }


            // =====================================
            // MIN FILE SIZE
            // =====================================

            if (request.getMinFileSize() != null) {

                predicates.add(

                        cb.greaterThanOrEqualTo(

                                root.get("fileSize"),

                                request.getMinFileSize()

                        )

                );

            }


            // =====================================
            // MAX FILE SIZE
            // =====================================

            if (request.getMaxFileSize() != null) {

                predicates.add(

                        cb.lessThanOrEqualTo(

                                root.get("fileSize"),

                                request.getMaxFileSize()

                        )

                );

            }


            // =====================================
            // UPLOADED FROM
            // =====================================

            if (request.getUploadedFrom() != null) {

                predicates.add(

                        cb.greaterThanOrEqualTo(

                                root.get("uploadedAt"),

                                request.getUploadedFrom()

                        )

                );

            }


            // =====================================
            // UPLOADED TO
            // =====================================

            if (request.getUploadedTo() != null) {

                predicates.add(

                        cb.lessThanOrEqualTo(

                                root.get("uploadedAt"),

                                request.getUploadedTo()

                        )

                );

            }


            return cb.and(

                    predicates.toArray(

                            new Predicate[0]

                    )

            );

        };

    }

}