package com.location.country.dto;

import com.location.enums.MasterStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountrySearchDto {

    private String countryName;

    private String countryCode;

    private MasterStatus status;

}