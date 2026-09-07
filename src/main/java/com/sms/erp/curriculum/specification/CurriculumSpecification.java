package com.sms.erp.curriculum.specification;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.sms.erp.curriculum.dto.CurriculumSearchDto;
import com.sms.erp.curriculum.entity.Curriculum;
import com.sms.enums.RecordStatus;

public final class CurriculumSpecification {

    private CurriculumSpecification() {
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public static Specification<Curriculum> bySearch(
            CurriculumSearchDto search) {

        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            // =================================================
            // ALWAYS EXCLUDE DELETED
            // =================================================

            predicate = cb.and(
                    predicate,
                    cb.notEqual(
                            root.get("status"),
                            RecordStatus.DELETED
                    )
            );

            // =================================================
            // NULL SEARCH
            // =================================================

            if (search == null) {
                return predicate;
            }

            // =================================================
            // CODE
            // =================================================

            if (search.getCode() != null
                    && !search.getCode().isBlank()) {

                String code =
                        "%"
                        + search.getCode()
                                .trim()
                                .toLowerCase()
                        + "%";

                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(
                                        root.get("code")
                                ),
                                code
                        )
                );
            }

            // =================================================
            // STATUS
            // =================================================

            if (search.getStatus() != null
                    && !search.getStatus().isBlank()) {

                try {

                    RecordStatus status =
                            RecordStatus.valueOf(
                                    search.getStatus()
                                            .trim()
                                            .toUpperCase()
                            );

                    // DELETED normal search mein
                    // kabhi expose nahi hoga.
                    if (status == RecordStatus.DELETED) {

                        return cb.disjunction();
                    }

                    predicate = cb.and(
                            predicate,
                            cb.equal(
                                    root.get("status"),
                                    status
                            )
                    );

                } catch (IllegalArgumentException ex) {

                    // Invalid status = no result
                    return cb.disjunction();
                }
            }

            // =================================================
            // KEYWORD
            // =================================================

            if (search.getKeyword() != null
                    && !search.getKeyword().isBlank()) {

                String keyword =
                        "%"
                        + search.getKeyword()
                                .trim()
                                .toLowerCase()
                        + "%";

                Predicate keywordPredicate =
                        cb.disjunction();

                String[] fields = {
                        "code",
                        "academicYear",
                        "course",
                        "semester",
                        "subject",
                        "status",
                        "description"
                };

                for (String field : fields) {

                    keywordPredicate =
                            cb.or(
                                    keywordPredicate,
                                    cb.like(
                                            cb.lower(
                                                    root.get(field)
                                            ),
                                            keyword
                                    )
                            );
                }

                predicate = cb.and(
                        predicate,
                        keywordPredicate
                );
            }

            return predicate;
        };
    }
}
