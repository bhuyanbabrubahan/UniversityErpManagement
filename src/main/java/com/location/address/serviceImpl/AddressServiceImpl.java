package com.location.address.serviceImpl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.address.dto.AddressRequestDto;
import com.location.address.dto.AddressResponseDto;
import com.location.address.entity.Address;
import com.location.address.mapper.AddressMapper;
import com.location.address.repository.AddressRepository;
import com.location.address.service.AddressService;
import com.location.common.service.LocationValidationService;
import com.location.village.entity.Village;
import com.sms.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final LocationValidationService validationService;

    // ==========================================================
    // CREATE ADDRESS
    // ==========================================================

    @Override
    public AddressResponseDto createAddress(AddressRequestDto dto) {

        Address address = saveAddress(
                Objects.requireNonNull(dto)
        );

        return addressMapper.toDto(address);
    }

    // ==========================================================
    // GET ADDRESS BY ID
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDto getAddressById(Long id) {

        Address address =
                addressRepository.findById(
                        Objects.requireNonNull(id)
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id : " + id
                        )
                );

        return addressMapper.toDto(address);
    }

    // ==========================================================
    // GET ALL ADDRESSES
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAllAddresses() {

        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    // ==========================================================
    // UPDATE ADDRESS
    // ==========================================================

    @Override
    public AddressResponseDto updateAddress(
            Long id,
            AddressRequestDto dto) {

        Address address =
                addressRepository.findById(
                        Objects.requireNonNull(id)
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id : " + id
                        )
                );

        Address updatedAddress =
                updateAddress(
                        address,
                        Objects.requireNonNull(dto)
                );

        return addressMapper.toDto(updatedAddress);
    }

    // ==========================================================
    // DELETE ADDRESS
    // ==========================================================

    public void deleteAddress(Long id) {

        Address address =
                addressRepository.findById(
                        Objects.requireNonNull(id)
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id : " + id
                        )
                );

        addressRepository.delete(
                Objects.requireNonNull(address)
        );
    }

    // ==========================================================
    // INTERNAL SAVE
    // Used by Student, Faculty, Employee etc.
    // ==========================================================

    @Override
    public Address saveAddress(AddressRequestDto dto) {

        Objects.requireNonNull(dto);

        Village village =
                validationService.validateVillage(
                        dto.getVillageId()
                );

        Address address =
                addressMapper.toEntity(dto);

        address.setVillage(village);

        return addressRepository.save(address);
    }

    // ==========================================================
    // INTERNAL UPDATE
    // Used by Student, Faculty, Employee etc.
    // ==========================================================

    @Override
    public Address updateAddress(
            Address address,
            AddressRequestDto dto) {

        Objects.requireNonNull(address);
        Objects.requireNonNull(dto);

        Village village =
                validationService.validateVillage(
                        dto.getVillageId()
                );

        addressMapper.updateEntity(
                address,
                dto
        );

        address.setVillage(village);

        return addressRepository.save(address);
    }
}