package com.location.address.dto;

import com.location.address.enums.AddressType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {

    private String houseNumber;

    private String street;

    private String landmark;

    @NotNull(message = "Address type is required")
    private AddressType addressType;

    @NotNull(message = "Village is required")
    private Long villageId;
    
    
    private String pincode;
}