package com.location.district.dto;



import com.location.enums.MasterStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistrictSearchDto {

    private String districtName;

    private String districtCode;

    private Long stateId;

    private Long countryId;

    private MasterStatus status;

}