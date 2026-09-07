package com.location.country.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CountryRequestDto {

    @NotBlank(message = "Country name is required")
    @Size(min = 2, max = 100)
    private String countryName;

    @NotBlank(message = "Country code is required")
    @Pattern(
        regexp = "^[A-Z]{2,3}$",
        message = "Country code must contain 2 or 3 uppercase letters"
    )
    private String countryCode;

}