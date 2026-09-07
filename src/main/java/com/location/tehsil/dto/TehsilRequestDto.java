package com.location.tehsil.dto;

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
public class TehsilRequestDto {

    @NotBlank(message = "Tehsil name is required")
    @Size(min = 2, max = 150)
    private String tehsilName;

    @NotNull(message = "District is required")
    private Long districtId;

}