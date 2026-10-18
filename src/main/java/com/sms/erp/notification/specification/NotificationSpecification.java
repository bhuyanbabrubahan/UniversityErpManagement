
package com.sms.erp.notification.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import com.sms.erp.notification.entity.Notification;
import com.sms.erp.notification.dto.NotificationSearchDto;

public final class NotificationSpecification {

    private NotificationSpecification() {
    }

    public static Specification<Notification> bySearch(
            NotificationSearchDto search) {

        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (search == null) {
                return p;
            }

            if (search.getCode() != null
                    && !search.getCode().isBlank()) {
                p = cb.and(
                        p,
                        cb.like(
                                cb.lower(root.get("code")),
                                "%" + search.getCode().toLowerCase() + "%"
                        )
                );
            }

            if (search.getStatus() != null
                    && !search.getStatus().isBlank()) {
                p = cb.and(
                        p,
                        cb.equal(
                                root.get("status"),
                                search.getStatus()
                        )
                );
            }

            if (search.getKeyword() != null
                    && !search.getKeyword().isBlank()) {

                String k = "%"
                        + search.getKeyword().toLowerCase()
                        + "%";

                Predicate keyword = cb.disjunction();

                for (String field : new String[]{
                        "code", "status", "description"
                }) {
                    keyword = cb.or(
                            keyword,
                            cb.like(
                                    cb.lower(root.get(field)),
                                    k
                            )
                    );
                }

                p = cb.and(p, keyword);
            }

            return p;
        };
    }

    public static Specification<Notification> visibleToRole(
            String role) {

        return (root, query, cb) -> {

            // Target role condition
            Predicate roleCondition = cb.or(
                    cb.isNull(root.get("targetRole")),
                    cb.equal(
                            cb.upper(root.get("targetRole")),
                            "ALL"
                    )
            );

            if (role != null && !role.isBlank()) {
                roleCondition = cb.or(
                        roleCondition,
                        cb.equal(
                                cb.upper(root.get("targetRole")),
                                role.toUpperCase()
                        )
                );
            }

            // Published or active notifications only
            Predicate statusCondition = cb.or(
                    cb.equal(
                            cb.upper(root.get("status")),
                            "PUBLISHED"
                    ),
                    cb.equal(
                            cb.upper(root.get("status")),
                            "ACTIVE"
                    )
            );

            // Published time must be reached
            Predicate publishedCondition = cb.or(
                    cb.isNull(root.get("publishedAt")),
                    cb.lessThanOrEqualTo(
                            root.get("publishedAt"),
                            java.time.LocalDateTime.now()
                    )
            );

            return cb.and(
                    roleCondition,
                    statusCondition,
                    publishedCondition
            );
        };
    }
}