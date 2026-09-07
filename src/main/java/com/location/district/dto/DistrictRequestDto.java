package com.location.district.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistrictRequestDto {

    @NotBlank(message = "District name is required")
    @Size(min = 2, max = 150, message = "District name must be between 2 and 150 characters")
    private String districtName;

    @NotBlank(message = "District code is required")
    @Size(min = 2, max = 20, message = "District code must be between 2 and 20 characters")
    private String districtCode;

    @NotNull(message = "State is required")
    private Long stateId;

}