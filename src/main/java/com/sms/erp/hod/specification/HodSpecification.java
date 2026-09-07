package com.sms.erp.hod.specification;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.sms.erp.hod.dto.HodSearchDto;
import com.sms.erp.hod.entity.Hod;
import com.sms.enums.RecordStatus;

public final class HodSpecification {

    private HodSpecification() {
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public static Specification<Hod> bySearch(
            HodSearchDto search) {

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
            // NO SEARCH FILTER
            // =================================================

            if (search == null) {
                return predicate;
            }

            // =================================================
            // CODE FILTER
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
            // STATUS FILTER
            // =================================================

            if (search.getStatus() != null) {

                /*
                 * DELETED is never allowed in normal search.
                 *
                 * DELETED records are already excluded above,
                 * but this check also prevents a DELETED status
                 * request from accidentally returning anything.
                 */

                if (search.getStatus()
                        != RecordStatus.DELETED) {

                    predicate = cb.and(
                            predicate,
                            cb.equal(
                                    root.get("status"),
                                    search.getStatus()
                            )
                    );

                } else {

                    /*
                     * User explicitly searched DELETED.
                     * Normal API should return no records.
                     */

                    return cb.disjunction();
                }
            }

            // =================================================
            // KEYWORD FILTER
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

                // -------------------------------------------------
                // CODE
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get("code")
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // EMPLOYEE CODE
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "employeeCode"
                                                )
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // FULL NAME
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "fullName"
                                                )
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // DEPARTMENT
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "department"
                                                )
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // EMAIL
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "email"
                                                )
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // PHONE
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "phone"
                                                )
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // DESIGNATION
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "designation"
                                                )
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // DESCRIPTION
                // -------------------------------------------------

                keywordPredicate =
                        cb.or(
                                keywordPredicate,
                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "description"
                                                )
                                        ),
                                        keyword
                                )
                        );

                // -------------------------------------------------
                // APPLY KEYWORD
                // -------------------------------------------------

                predicate = cb.and(
                        predicate,
                        keywordPredicate
                );
            }

            return predicate;
        };
    }
}
