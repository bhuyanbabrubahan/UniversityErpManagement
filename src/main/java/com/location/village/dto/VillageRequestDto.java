package com.location.village.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VillageRequestDto {

    @NotBlank(message = "Village name is required")
    @Size(min = 2, max = 200,
            message = "Village name must be between 2 and 200 characters")
    private String villageName;

    @NotBlank(message = "Pincode is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Pincode must be exactly 6 digits")
    private String pincode;

    @NotNull(message = "Tehsil is required")
    private Long tehsilId;

}
