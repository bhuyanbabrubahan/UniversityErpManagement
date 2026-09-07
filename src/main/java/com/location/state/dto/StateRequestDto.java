package com.location.state.dto;

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
public class StateRequestDto {

    @NotBlank(message = "State name is required")
    @Size(min = 2, max = 150, message = "State name must be between 2 and 150 characters")
    private String stateName;

    @NotBlank(message = "State code is required")
    @Size(min = 2, max = 20, message = "State code must be between 2 and 20 characters")
    private String stateCode;

    @NotNull(message = "Country is required")
    private Long countryId;

}
