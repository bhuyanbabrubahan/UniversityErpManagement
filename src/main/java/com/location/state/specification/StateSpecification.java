package com.location.state.specification;



import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.location.state.dto.StateSearchDto;
import com.location.state.entity.State;

import jakarta.persistence.criteria.Predicate;

public class StateSpecification {

    private StateSpecification() {
    }

    public static Specification<State> search(StateSearchDto dto) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (dto.getStateName() != null && !dto.getStateName().trim().isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("stateName")),
                                "%" + dto.getStateName().trim().toLowerCase() + "%"));
            }

            if (dto.getStateCode() != null && !dto.getStateCode().trim().isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("stateCode")),
                                "%" + dto.getStateCode().trim().toLowerCase() + "%"));
            }

            if (dto.getCountryId() != null) {
                predicates.add(
                        cb.equal(root.get("country").get("id"), dto.getCountryId()));
            }

            if (dto.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), dto.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }

}
