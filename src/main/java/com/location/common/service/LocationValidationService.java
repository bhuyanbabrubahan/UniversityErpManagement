package com.location.common.service;

import com.location.country.entity.Country;
import com.location.district.entity.District;
import com.location.state.entity.State;
import com.location.tehsil.entity.Tehsil;
import com.location.village.entity.Village;

public interface LocationValidationService {

    Country validateCountry(Long countryId);

    State validateState(Long stateId);

    District validateDistrict(Long districtId);

    Tehsil validateTehsil(Long tehsilId);

    Village validateVillage(Long villageId);
    
    

}