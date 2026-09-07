package com.sms.erp.research.specification;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import com.sms.erp.research.entity.Research;
import com.sms.erp.research.dto.ResearchSearchDto;

public final class ResearchSpecification {
    private ResearchSpecification() {}
    public static Specification<Research> bySearch(ResearchSearchDto search) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (search == null) return p;
            if (search.getCode() != null && !search.getCode().isBlank()) p = cb.and(p, cb.like(cb.lower(root.get("code")), "%" + search.getCode().toLowerCase() + "%"));
            if (search.getStatus() != null && !search.getStatus().isBlank()) p = cb.and(p, cb.equal(root.get("status"), search.getStatus()));
            if (search.getKeyword() != null && !search.getKeyword().isBlank()) {
                String k = "%" + search.getKeyword().toLowerCase() + "%";
                Predicate keyword = cb.disjunction();
                for (String field : new String[] {"code","status","description"}) keyword = cb.or(keyword, cb.like(cb.lower(root.get(field)), k));
                p = cb.and(p, keyword);
            }
            return p;
        };
    }
}
