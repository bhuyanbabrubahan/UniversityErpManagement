package com.sms.erp.timetable.specification;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.erp.timetable.dto.TimetableSearchDto;
import com.sms.erp.timetable.entity.Timetable;

public final class TimetableSpecification {

    private TimetableSpecification() {
    }

    public static Specification<Timetable> bySearch(
            TimetableSearchDto search) {

        return (root, query, cb) -> {

            Predicate predicate =
                    cb.conjunction();

            // =================================================
            // ALWAYS EXCLUDE SOFT-DELETED RECORDS
            // =================================================

            predicate = cb.and(
                    predicate,
                    cb.notEqual(
                            root.get("status"),
                            RecordStatus.DELETED
                    )
            );

            // =================================================
            // NO SEARCH
            // =================================================

            if (search == null) {
                return predicate;
            }

            // =================================================
            // CODE
            // =================================================

            if (isNotBlank(search.getCode())) {

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

            if (isNotBlank(search.getStatus())) {

                try {

                    RecordStatus status =
                            RecordStatus.valueOf(
                                    search.getStatus()
                                            .trim()
                                            .toUpperCase()
                            );

                    // Deleted is never exposed
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

                    return cb.disjunction();
                }
            }

            // =================================================
            // KEYWORD
            // =================================================

            if (isNotBlank(search.getKeyword())) {

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
                        "semester",
                        "course",
                        "section",
                        "subject",
                        "faculty",
                        "room",
                        "dayOfWeek",
                        "startTime",
                        "endTime",
                        "period",
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

    private static boolean isNotBlank(
            String value) {

        return value != null
                && !value.trim().isEmpty();
    }
}