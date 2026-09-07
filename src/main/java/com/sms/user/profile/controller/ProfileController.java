package com.sms.user.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sms.payload.ApiResponseDto;
import com.sms.security.dto.ChangePasswordRequestDto;
import com.sms.security.dto.ChangePasswordResponseDto;
import com.sms.security.util.JwtTokenUtil;
import com.sms.user.profile.dto.ProfileResponseDto;
import com.sms.user.profile.dto.UpdateProfileRequestDto;
import com.sms.user.profile.service.ProfileService;
import com.sms.util.ResponseBuilder;
import com.sms.service.FileStorageService;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(
		name = "Profile API",
		description = "APIs for logged-in user profile management"
)
public class ProfileController {


	private final ProfileService profileService;

	private final JwtTokenUtil jwtTokenUtil;
	private final FileStorageService fileStorageService;



	// =====================================
	// GET PROFILE
	// =====================================


	@GetMapping("/me")
	@Operation(
			summary = "Get My Profile",
			description = "Fetch currently authenticated user profile details"
	)
	public ResponseEntity<ApiResponseDto<ProfileResponseDto>> getMyProfile() {


		ProfileResponseDto response =
				profileService.getMyProfile();


		return ResponseBuilder.success(
				response,
				"Profile fetched successfully",
				HttpStatus.OK
		);
	}



	// =====================================
	// UPDATE PROFILE
	// =====================================


	@PutMapping
	@Operation(
			summary = "Update My Profile",
			description = "Update logged in user profile information"
	)
	public ResponseEntity<ApiResponseDto<ProfileResponseDto>> updateProfile(

			@Valid @RequestBody UpdateProfileRequestDto request

	) {


		ProfileResponseDto response =
				profileService.updateProfile(request);


		return ResponseBuilder.success(
				response,
				"Profile updated successfully",
				HttpStatus.OK
		);
	}



	// =====================================
	// UPLOAD PROFILE PHOTO
	// =====================================

	@PostMapping(
	        value = "/photo",
	        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	@Operation(
	        summary = "Upload Profile Photo",
	        description = "Upload or replace logged in user profile image"
	)
	public ResponseEntity<ApiResponseDto<ProfileResponseDto>> uploadProfilePhoto(

	        @RequestPart("file")
	        @io.swagger.v3.oas.annotations.Parameter(
	                description = "Profile image file",
	                required = true,
	                content = @Content(
	                        mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
	                        schema = @Schema(
	                                type = "string",
	                                format = "binary"
	                        )
	                )
	        )
	        MultipartFile file

	) {

	    ProfileResponseDto response =
	    		profileService.uploadProfilePhoto(file);

	    return ResponseBuilder.success(
	            response,
	            "Profile photo uploaded successfully",
	            HttpStatus.OK
	    );
	}



	// =====================================
	// REMOVE PROFILE PHOTO
	// =====================================


	@DeleteMapping("/photo")
	@Operation(
			summary = "Remove Profile Photo",
			description = "Remove logged in user profile image"
	)
	public ResponseEntity<ApiResponseDto<Void>> removeProfilePhoto() {


		profileService.removeProfilePhoto();



		return ResponseBuilder.success(
				null,
				"Profile photo removed successfully",
				HttpStatus.OK
		);
	}




	// =====================================
	// CHANGE PASSWORD
	// =====================================


	@PutMapping("/change-password")
	@Operation(
			summary = "Change Password",
			description = "Changes password and invalidates existing sessions"
	)
	public ResponseEntity<ApiResponseDto<ChangePasswordResponseDto>> changePassword(

			@Valid @RequestBody ChangePasswordRequestDto request,

			HttpServletRequest httpRequest

	) {


		String token =
				jwtTokenUtil.getTokenFromRequest(httpRequest);



		ChangePasswordResponseDto response =
				profileService.changePassword(
						request,
						token
				);



		return ResponseBuilder.success(
				response,
				"Password changed successfully",
				HttpStatus.OK
		);
	}
	
	
	
	
	
	// =====================================
	// VIEW PROFILE PHOTO
	// =====================================

	@SuppressWarnings("null")
	@GetMapping("/photo/{fileName}")
	@Operation(
	        summary = "View Profile Photo",
	        description = "Loads profile image"
	)
	public ResponseEntity<Resource> viewProfilePhoto(

	        @PathVariable String fileName

	) {


	    Resource resource =
	            fileStorageService.loadFile(fileName);


	    return ResponseEntity.ok()
	            .contentType(
	                    MediaTypeFactory
	                    .getMediaType(resource)
	                    .orElse(
	                        MediaType.APPLICATION_OCTET_STREAM
	                    )
	            )
	            .body(resource);

	}
	
	
	

}