package com.location.importLocation.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationImportRowDto {

    private String country;

    private String countryCode;

    private String state;

    private String stateCode;

    private String district;

    private String districtCode;

    private String tehsil;

    private String village;

    private String pincode;

}