package com.location.tehsil.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.location.tehsil.dto.TehsilSearchDto;
import com.location.tehsil.entity.Tehsil;

import jakarta.persistence.criteria.Predicate;

public final class TehsilSpecification {

    private TehsilSpecification() {
    }

    public static Specification<Tehsil> search(
            TehsilSearchDto dto) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            if (dto.getTehsilName() != null &&
                    !dto.getTehsilName().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("tehsilName")),
                                "%" + dto.getTehsilName().toLowerCase().trim() + "%"));
            }

            if (dto.getDistrictId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("district").get("id"),
                                dto.getDistrictId()));
            }

            if (dto.getStateId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("district").get("state").get("id"),
                                dto.getStateId()));
            }

            if (dto.getCountryId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("district")
                                        .get("state")
                                        .get("country")
                                        .get("id"),
                                dto.getCountryId()));
            }

            if (dto.getStatus() != null) {

                predicates.add(
                        cb.equal(root.get("status"),
                                dto.getStatus()));
            }

            return cb.and(
                    predicates.toArray(new Predicate[0]));

        };

    }

}