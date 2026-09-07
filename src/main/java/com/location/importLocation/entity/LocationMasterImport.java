package com.location.importLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location_master_import_test")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationMasterImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(name = "state", nullable = false, length = 150)
    private String state;

    @Column(name = "state_code", nullable = false, length = 20)
    private String stateCode;

    @Column(name = "district", nullable = false, length = 150)
    private String district;

    @Column(name = "district_code", nullable = false, length = 20)
    private String districtCode;

    @Column(name = "tehsil", nullable = false, length = 150)
    private String tehsil;

    @Column(name = "village", nullable = false, length = 200)
    private String village;

    @Column(name = "pincode", nullable = false, length = 6)
    private String pincode;
    
    @Column(name = "import_status", length = 20)
    private String importStatus;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

}