package com.location.state.dto;

import com.location.country.dto.CountryResponseDto;

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
        name = "StateResponseDto",
        description = "State information."
)
public class StateResponseDto {

    private Long id;

    private String stateName;

    private CountryResponseDto country;
}