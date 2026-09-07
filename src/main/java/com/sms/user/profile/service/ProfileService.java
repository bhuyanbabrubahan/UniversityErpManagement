package com.sms.user.profile.service;

import org.springframework.web.multipart.MultipartFile;

import com.sms.security.dto.ChangePasswordRequestDto;
import com.sms.security.dto.ChangePasswordResponseDto;
import com.sms.user.profile.dto.ProfileResponseDto;
import com.sms.user.profile.dto.UpdateProfileRequestDto;

public interface ProfileService {


    ProfileResponseDto getMyProfile();
    
    ProfileResponseDto updateProfile(UpdateProfileRequestDto request);

    ProfileResponseDto  uploadProfilePhoto(MultipartFile file);

    void removeProfilePhoto();


    ChangePasswordResponseDto changePassword(
            ChangePasswordRequestDto request,
            String token);


}