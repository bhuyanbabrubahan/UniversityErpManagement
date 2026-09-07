package com.location.village.specification;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.location.village.dto.VillageSearchDto;
import com.location.village.entity.Village;

import jakarta.persistence.criteria.Predicate;

public final class VillageSpecification {

    private VillageSpecification() {
    }

    public static Specification<Village> search(
            VillageSearchDto dto) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (dto.getVillageName() != null &&
                    !dto.getVillageName().isBlank()) {

                predicates.add(

                        cb.like(

                                cb.lower(root.get("villageName")),

                                "%" + dto.getVillageName()
                                        .trim()
                                        .toLowerCase() + "%")

                );

            }

            if (dto.getPincode() != null &&
                    !dto.getPincode().isBlank()) {

                predicates.add(

                        cb.equal(
                                root.get("pincode"),
                                dto.getPincode().trim())

                );

            }

            if (dto.getTehsilId() != null) {

                predicates.add(

                        cb.equal(
                                root.get("tehsil").get("id"),
                                dto.getTehsilId())

                );

            }

            if (dto.getDistrictId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("tehsil")
                                        .get("district")
                                        .get("id"),

                                dto.getDistrictId())

                );

            }

            if (dto.getStateId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("tehsil")
                                        .get("district")
                                        .get("state")
                                        .get("id"),

                                dto.getStateId())

                );

            }

            if (dto.getCountryId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("tehsil")
                                        .get("district")
                                        .get("state")
                                        .get("country")
                                        .get("id"),

                                dto.getCountryId())

                );

            }

            if (dto.getStatus() != null) {

                predicates.add(

                        cb.equal(
                                root.get("status"),
                                dto.getStatus())

                );

            }

            return cb.and(
                    predicates.toArray(new Predicate[0]));

        };

    }

}