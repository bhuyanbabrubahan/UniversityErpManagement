package com.location.tehsil.dto;

import com.location.district.dto.DistrictResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "TehsilResponseDto",
        description = "Tehsil information."
)
public class TehsilResponseDto {

    private Long id;

    private String tehsilName;

    private DistrictResponseDto district;
}