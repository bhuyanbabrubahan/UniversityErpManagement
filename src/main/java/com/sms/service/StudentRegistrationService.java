package com.sms.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.security.dto.UserRequestDto;
import com.sms.security.dto.UserResponseDto;


@Service
public class StudentRegistrationService {


    @Transactional
    public UserResponseDto register(
            UserRequestDto request
    ){


        return null;

    }


}