package com.location.village.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.common.service.LocationValidationService;
import com.location.enums.MasterStatus;
import com.location.tehsil.entity.Tehsil;
import com.location.village.dto.VillageRequestDto;
import com.location.village.dto.VillageResponseDto;
import com.location.village.dto.VillageSearchDto;
import com.location.village.entity.Village;
import com.location.village.mapper.VillageMapper;
import com.location.village.repository.VillageRepository;
import com.location.village.service.VillageService;
import com.location.village.specification.VillageSpecification;
import com.sms.exception.DuplicateResourceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VillageServiceImpl implements VillageService {

    private final VillageRepository villageRepository;
    private final VillageMapper villageMapper;
    private final LocationValidationService validationService;

    @Override
    public VillageResponseDto createVillage(VillageRequestDto dto) {

        Tehsil tehsil = validationService.validateTehsil(dto.getTehsilId());

        if (villageRepository.existsByTehsilIdAndVillageNameIgnoreCaseAndPincode(
                tehsil.getId(),
                dto.getVillageName().trim(),
                dto.getPincode().trim())) {

            throw new DuplicateResourceException("Village already exists.");
        }

        Village village = villageMapper.toEntity(dto);

        village.setTehsil(tehsil);
        village.setStatus(MasterStatus.ACTIVE);

        Village saved = villageRepository.save(village);

        return villageMapper.toDto(saved);
    }

    @Override
    public VillageResponseDto updateVillage(Long id, VillageRequestDto dto) {

        Village village = validationService.validateVillage(id);

        Tehsil tehsil = validationService.validateTehsil(dto.getTehsilId());

        if (villageRepository.existsByTehsilIdAndVillageNameIgnoreCaseAndPincodeAndIdNot(
                tehsil.getId(),
                dto.getVillageName().trim(),
                dto.getPincode().trim(),
                id)) {

            throw new DuplicateResourceException("Village already exists.");
        }

        villageMapper.updateEntity(village, dto);

        village.setTehsil(tehsil);

        Village updated = villageRepository.save(village);

        return villageMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public VillageResponseDto getVillageById(Long id) {

        Village village = validationService.validateVillage(id);

        return villageMapper.toDto(village);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VillageResponseDto> getAllVillages(Pageable pageable) {

        return villageRepository
                .findAll(VillageSpecification.search(new VillageSearchDto()), pageable)
                .map(villageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VillageResponseDto> searchVillages(
            VillageSearchDto dto,
            Pageable pageable) {

        return villageRepository
                .findAll(VillageSpecification.search(dto), pageable)
                .map(villageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VillageResponseDto> getVillagesByTehsil(Long tehsilId) {

        validationService.validateTehsil(tehsilId);

        return villageRepository
                .findByTehsilIdAndStatusOrderByVillageNameAsc(
                        tehsilId,
                        MasterStatus.ACTIVE)
                .stream()
                .map(villageMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VillageResponseDto> getVillagesByPincode(String pincode) {

        return villageRepository
                .findByPincodeAndStatusOrderByVillageNameAsc(
                        pincode,
                        MasterStatus.ACTIVE)
                .stream()
                .map(villageMapper::toDto)
                .toList();
    }

    @Override
    public void deleteVillage(Long id) {

        Village village = validationService.validateVillage(id);

        village.setStatus(MasterStatus.DELETED);

        villageRepository.save(village);
    }
}