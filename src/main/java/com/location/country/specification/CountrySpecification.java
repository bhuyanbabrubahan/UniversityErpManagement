package com.location.country.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.location.country.dto.CountrySearchDto;
import com.location.country.entity.Country;

import jakarta.persistence.criteria.Predicate;

public class CountrySpecification {

    public static Specification<Country> search(CountrySearchDto dto){

        return (root,query,cb)->{

            List<Predicate> predicates=new ArrayList<>();

            if(dto.getCountryName()!=null){

                predicates.add(cb.like(
                        cb.lower(root.get("countryName")),
                        "%" + dto.getCountryName().toLowerCase() + "%"));
            }

            if(dto.getCountryCode()!=null){

                predicates.add(cb.equal(
                        cb.lower(root.get("countryCode")),
                        dto.getCountryCode().toLowerCase()));
            }

            if(dto.getStatus()!=null){

                predicates.add(cb.equal(root.get("status"),
                        dto.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }

}
