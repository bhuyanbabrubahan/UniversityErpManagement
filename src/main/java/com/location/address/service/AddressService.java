package com.location.address.service;

import java.util.List;

import com.location.address.dto.AddressRequestDto;
import com.location.address.dto.AddressResponseDto;
import com.location.address.entity.Address;

public interface AddressService {

    // ==========================================================
    // REST APIs
    // ==========================================================

    AddressResponseDto createAddress(AddressRequestDto request);

    AddressResponseDto getAddressById(Long id);

    List<AddressResponseDto> getAllAddresses();

    AddressResponseDto updateAddress(
            Long id,
            AddressRequestDto request
    );

    void deleteAddress(Long id);

    // ==========================================================
    // INTERNAL REUSABLE METHODS
    // Used by Student, Faculty, Employee etc.
    // ==========================================================

    Address saveAddress(AddressRequestDto dto);

    Address updateAddress(
            Address address,
            AddressRequestDto dto
    );

}