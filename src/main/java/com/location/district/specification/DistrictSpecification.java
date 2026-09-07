package com.location.district.specification;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.location.district.dto.DistrictSearchDto;
import com.location.district.entity.District;

import jakarta.persistence.criteria.Predicate;

public class DistrictSpecification {

    private DistrictSpecification() {
    }

    public static Specification<District> search(DistrictSearchDto dto) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (dto.getDistrictName() != null &&
                    !dto.getDistrictName().isBlank()) {

                predicates.add(cb.like(
                        cb.lower(root.get("districtName")),
                        "%" + dto.getDistrictName().trim().toLowerCase() + "%"));
            }

            if (dto.getDistrictCode() != null &&
                    !dto.getDistrictCode().isBlank()) {

                predicates.add(cb.like(
                        cb.lower(root.get("districtCode")),
                        "%" + dto.getDistrictCode().trim().toLowerCase() + "%"));
            }

            if (dto.getStateId() != null) {

                predicates.add(cb.equal(
                        root.get("state").get("id"),
                        dto.getStateId()));
            }

            if (dto.getCountryId() != null) {

                predicates.add(cb.equal(
                        root.get("state").get("country").get("id"),
                        dto.getCountryId()));
            }

            if (dto.getStatus() != null) {

                predicates.add(cb.equal(
                        root.get("status"),
                        dto.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };

    }

}