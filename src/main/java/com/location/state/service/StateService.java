package com.location.state.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.location.state.dto.StateRequestDto;
import com.location.state.dto.StateResponseDto;
import com.location.state.dto.StateSearchDto;

public interface StateService {

    StateResponseDto createState(StateRequestDto dto);

    StateResponseDto updateState(Long id, StateRequestDto dto);

    StateResponseDto getStateById(Long id);

    Page<StateResponseDto> getAllStates(Pageable pageable);

    Page<StateResponseDto> searchStates(StateSearchDto dto,
                                        Pageable pageable);

    void deleteState(Long id);
    List<StateResponseDto> getStatesByCountry(Long countryId);
    

}
