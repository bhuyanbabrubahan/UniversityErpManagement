package com.location.importLocation.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.common.util.LocationUtils;
import com.location.country.entity.Country;
import com.location.country.repository.CountryRepository;
import com.location.district.entity.District;
import com.location.district.repository.DistrictRepository;
import com.location.enums.MasterStatus;
import com.location.importLocation.dto.ImportResultDto;
import com.location.importLocation.entity.LocationMasterImport;
import com.location.importLocation.repository.LocationMasterImportRepository;
import com.location.importLocation.service.LocationImportService;
import com.location.state.entity.State;
import com.location.state.repository.StateRepository;
import com.location.tehsil.entity.Tehsil;
import com.location.tehsil.repository.TehsilRepository;
import com.location.village.entity.Village;
import com.location.village.repository.VillageRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LocationImportServiceImpl implements LocationImportService {

	private final LocationMasterImportRepository importRepository;

	private final CountryRepository countryRepository;

	private final StateRepository stateRepository;

	private final DistrictRepository districtRepository;

	private final TehsilRepository tehsilRepository;

	private final VillageRepository villageRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private final Map<String, Country> countryCache = new HashMap<>();

	private final Map<String, State> stateCache = new HashMap<>();

	private final Map<String, District> districtCache = new HashMap<>();

	private final Map<String, Tehsil> tehsilCache = new HashMap<>();

	private final Map<String, Village> villageCache = new HashMap<>();

	@Override
	public ImportResultDto importLocationMaster() {

	    ImportResultDto result = new ImportResultDto();
	    result.setStartTime(System.currentTimeMillis());

	    final int batchSize = 500;

	    int page = 0;

	    Page<LocationMasterImport> pageData;

	    log.info("============== IMPORT STARTED ==============");

	    do {

	    	pageData = importRepository.findAll(

	    			PageRequest.of(

	    			page,

	    			batchSize,

	    			Sort.by("id").ascending()

	    			)

	    			);

	        log.info(
	                "Processing Page : {} | Records : {}",
	                page + 1,
	                pageData.getNumberOfElements());
	        
	        log.info(
	        	    "Processing Page : {} | Records : {}",
	        	    page + 1,
	        	    pageData.getNumberOfElements());

	        result.setTotalRows(
	                result.getTotalRows()
	                        + pageData.getNumberOfElements());

	        for (LocationMasterImport row : pageData.getContent()) {

	            try {

	                Country country =
	                        getOrCreateCountry(row);

	                State state =
	                        getOrCreateState(row, country);

	                District district =
	                        getOrCreateDistrict(row, state);

	                Tehsil tehsil =
	                        getOrCreateTehsil(row, district);

	                getOrCreateVillage(row, tehsil);

	                row.setImportStatus("SUCCESS");
	                row.setErrorMessage(null);
	                
	                result.setSuccessRows(
	                        result.getSuccessRows() + 1);

	            }

	            catch (Exception ex) {

	                row.setImportStatus("FAILED");

	                row.setErrorMessage(ex.getMessage());

	                result.setFailedRows(
	                        result.getFailedRows()+1);

	                result.getErrors().add(
	                        "Row "
	                        +row.getId()
	                        +" : "
	                        +ex.getMessage());

	                log.error(
	                        "Failed Row {}",
	                        row.getId(),
	                        ex);

	            }

	        }

	        importRepository.saveAll(pageData.getContent());
	        
	        entityManager.flush();

	        entityManager.clear();


	        log.info(
	                "Page {} Completed | Success : {} | Failed : {}",
	                page + 1,
	                result.getSuccessRows(),
	                result.getFailedRows());

	        page++; 
	    }

	    while (pageData.hasNext());

	    try{

	    	   // import

	    	}
	    	finally{

	    	    countryCache.clear();

	    	    stateCache.clear();

	    	    districtCache.clear();

	    	    tehsilCache.clear();

	    	    villageCache.clear();

	    	}
	    result.setEndTime(System.currentTimeMillis());

	    result.setDurationInSeconds(
	            (result.getEndTime() - result.getStartTime()) / 1000);

	    log.info("======================================");

	    log.info("IMPORT FINISHED");

	    log.info("Total Rows : {}", result.getTotalRows());

	    log.info("Success : {}", result.getSuccessRows());

	    log.info("Failed : {}", result.getFailedRows());

	    log.info("Time Taken : {} Seconds",
	            result.getDurationInSeconds());

	    log.info("======================================");

	    return result;

	}
	
	private Country getOrCreateCountry(LocationMasterImport row) {

	    String countryCode =
	            LocationUtils.normalizeCode(row.getCountryCode());

	    // 1. Cache Check
	    Country cacheCountry = countryCache.get(countryCode);

	    if (cacheCountry != null) {
	        return cacheCountry;
	    }

	    // 2. Database Check
	    Country country = countryRepository
	            .findByCountryCodeIgnoreCase(countryCode)
	            .orElseGet(() -> {

	                Country newCountry = new Country();

	                newCountry.setCountryName(
	                        LocationUtils.normalizeName(row.getCountry()));

	                newCountry.setCountryCode(countryCode);

	                newCountry.setStatus(MasterStatus.ACTIVE);

	                return countryRepository.save(newCountry);

	            });

	    // 3. Put into Cache
	    countryCache.put(countryCode, country);

	    return country;
	}
	
	
	
	private State getOrCreateState(LocationMasterImport row, Country country) {

		String stateName = LocationUtils.normalizeName(row.getState());

		String cacheKey = country.getId() + "_" + stateName;

		// Cache
		State cacheState = stateCache.get(cacheKey);

		if (cacheState != null) {
			return cacheState;
		}

		// Database
		State state = stateRepository

				.findByCountryIdAndStateNameIgnoreCase(country.getId(), stateName)

				.orElseGet(() -> {

					State newState = new State();

					newState.setCountry(country);

					newState.setStateName(stateName);

					newState.setStateCode(LocationUtils.normalizeCode(row.getStateCode()));

					newState.setStatus(MasterStatus.ACTIVE);

					return stateRepository.save(newState);

				});

		// Cache
		stateCache.put(cacheKey, state);

		return state;
	}
	
	
	
	private District getOrCreateDistrict(LocationMasterImport row, State state) {

		String districtName = LocationUtils.normalizeName(row.getDistrict());

		String cacheKey = state.getId() + "_" + districtName;

		// Cache Check
		District cacheDistrict = districtCache.get(cacheKey);

		if (cacheDistrict != null) {
			return cacheDistrict;
		}

		// Database Check
		District district = districtRepository.findByStateIdAndDistrictNameIgnoreCase(state.getId(), districtName)
				.orElseGet(() -> {

					District newDistrict = new District();

					newDistrict.setState(state);

					newDistrict.setDistrictName(districtName);

					newDistrict.setDistrictCode(LocationUtils.normalizeCode(row.getDistrictCode()));

					newDistrict.setStatus(MasterStatus.ACTIVE);

					return districtRepository.save(newDistrict);

				});

		districtCache.put(cacheKey, district);

		return district;
	}

	
	
	private Tehsil getOrCreateTehsil(LocationMasterImport row, District district) {

		String tehsilName = LocationUtils.normalizeName(row.getTehsil());

		String cacheKey = district.getId() + "_" + tehsilName;

		// Cache Check
		Tehsil cacheTehsil = tehsilCache.get(cacheKey);

		if (cacheTehsil != null) {
			return cacheTehsil;
		}

		// Database Check
		Tehsil tehsil = tehsilRepository.findByDistrictIdAndTehsilNameIgnoreCase(district.getId(), tehsilName)
				.orElseGet(() -> {

					Tehsil newTehsil = new Tehsil();

					newTehsil.setDistrict(district);

					newTehsil.setTehsilName(tehsilName);

					newTehsil.setStatus(MasterStatus.ACTIVE);

					return tehsilRepository.save(newTehsil);

				});

		tehsilCache.put(cacheKey, tehsil);

		return tehsil;
	}
	
	
	
	

	private Village getOrCreateVillage(LocationMasterImport row, Tehsil tehsil) {

		String villageName = LocationUtils.normalizeName(row.getVillage());

		String pincode = LocationUtils.normalizePincode(row.getPincode());

		String cacheKey = tehsil.getId() + "_" + villageName + "_" + pincode;

		// Cache Check
		Village cacheVillage = villageCache.get(cacheKey);

		if (cacheVillage != null) {
			return cacheVillage;
		}

		// Database Check
		Village village = villageRepository
				.findByTehsilIdAndVillageNameIgnoreCaseAndPincode(tehsil.getId(), villageName, pincode)
				.orElseGet(() -> {

					Village newVillage = new Village();

					newVillage.setTehsil(tehsil);

					newVillage.setVillageName(villageName);

					newVillage.setPincode(pincode);

					newVillage.setStatus(MasterStatus.ACTIVE);

					return villageRepository.save(newVillage);

				});

		villageCache.put(cacheKey, village);

		return village;
	}

}