package com.location.address.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.location.address.entity.Address;


public interface AddressRepository 
        extends JpaRepository<Address, Long>{

}