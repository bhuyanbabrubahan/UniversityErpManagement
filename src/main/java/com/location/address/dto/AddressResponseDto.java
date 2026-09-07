package com.location.address.dto;

import java.time.LocalDateTime;

import com.location.address.enums.AddressType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {

    private Long id;
    private String houseNumber;
    private String street;
    private String landmark;
    private AddressType addressType;
    private Long villageId;
    private String village;
    private Long tehsilId;
    private String tehsil;
    private Long districtId;
    private String district;
    private Long stateId;
    private String state;
    private Long countryId;
    private String country;
    private String pincode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}