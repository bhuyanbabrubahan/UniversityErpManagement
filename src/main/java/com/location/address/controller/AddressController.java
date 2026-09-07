package com.location.address.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.location.address.dto.AddressRequestDto;
import com.location.address.dto.AddressResponseDto;
import com.location.address.service.AddressService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/addresses")
public class AddressController {


	private final AddressService addressService;

	public AddressController(AddressService addressService) {

		this.addressService = addressService;

	}


    // CREATE ADDRESS

    @PostMapping
	public ResponseEntity<ApiResponseDto<AddressResponseDto>> createAddress(
			@Valid @RequestBody AddressRequestDto request) {

        AddressResponseDto response =
                addressService.createAddress(request);


        return ResponseBuilder.success(
                response,
                "Address Created Successfully",
                HttpStatus.CREATED
        );

    }






    // GET ADDRESS BY ID

    @GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<AddressResponseDto>> getAddressById
	(@PathVariable Long id) {


        AddressResponseDto response =
                addressService.getAddressById(id);


        return ResponseBuilder.success(
                response,
                "Address Fetch Successfully",
                HttpStatus.OK
        );

    }






    // GET ALL ADDRESS

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<AddressResponseDto>>> getAllAddresses()
    {


        List<AddressResponseDto> response =
                addressService.getAllAddresses();



        return ResponseBuilder.success(
                response,
                "All Addresses Fetch Successfully",
                HttpStatus.OK
        );

    }






    // UPDATE ADDRESS

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDto request)
    {


        AddressResponseDto response =
                addressService.updateAddress(
                        id,
                        request
                );


        return ResponseBuilder.success(
                response,
                "Address Updated Successfully",
                HttpStatus.OK
        );


    }







    // DELETE ADDRESS

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteAddress(
            @PathVariable Long id)
    {


        addressService.deleteAddress(id);


        return ResponseBuilder.success(
                null,
                "Address Deleted Successfully",
                HttpStatus.OK
        );


    }


}