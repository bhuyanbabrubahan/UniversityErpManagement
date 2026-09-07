package com.sms.user.profile.serviceImpl;


import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.admission.entity.Admission;
import com.admission.repository.AdmissionRepository;
import com.location.address.entity.Address;
import com.location.village.entity.Village;
import com.location.village.repository.VillageRepository;
import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.common.entity.BaseEntity;
import com.sms.entity.Faculty;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.FacultyRepository;
import com.sms.repository.StudentRepository;
import com.sms.security.dto.ChangePasswordRequestDto;
import com.sms.security.dto.ChangePasswordResponseDto;
import com.sms.security.entity.Admin;
import com.sms.security.entity.SuperAdmin;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.repository.AdminRepository;
import com.sms.security.repository.SuperAdminRepository;
import com.sms.security.repository.UserRepository;
import com.sms.security.service.JwtBlacklistService;
import com.sms.security.service.RefreshTokenService;
import com.sms.service.FileStorageService;
import com.sms.user.profile.dto.ProfileResponseDto;
import com.sms.user.profile.dto.UpdateProfileRequestDto;
import com.sms.user.profile.mapper.ProfileMapper;
import com.sms.user.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {


	private final UserRepository userRepository;
	private final StudentRepository studentRepository;
	private final FacultyRepository facultyRepository;
	private final ProfileMapper profileMapper;
	private final PasswordEncoder passwordEncoder;
	private final AdminRepository adminRepository;
	private final SuperAdminRepository superAdminRepository;
	private final RefreshTokenService refreshTokenService;
	private final JwtBlacklistService jwtBlacklistService;
	private final VillageRepository villageRepository;
	private final AdmissionRepository admissionRepository;
	private final FileStorageService fileStorageService;
	
	@Override
	@AuditLog(
	        action = AuditAction.VIEW,
	        module = "PROFILE",
	        description = "User viewed profile"
	)
    public ProfileResponseDto getMyProfile() {

		User user = getLoggedInUser();

        // 3. Check Role

        Role role = user.getRole();

        // =====================================
        // STUDENT PROFILE
        // =====================================


        if(role == Role.STUDENT) {

            Student student =

                    studentRepository
                    .findByUserIdAndStatus(
                            user.getId(),
                            RecordStatus.ACTIVE
                    )

                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Student profile not found"
                            )
                    );

            ProfileResponseDto dto = profileMapper.convertToDto(student);

            Admission admission =
                    admissionRepository
                            .findByStudentIdAndStatus(
                                    student.getId(),
                                    RecordStatus.ACTIVE)
                            .orElse(null);
            

            if (admission != null) {

                dto.setAdmissionNumber(admission.getAdmissionNumber());
                dto.setAcademicYear(admission.getAcademicYear());
                dto.setCourseName(admission.getCourse().getCourseName());
                dto.setSemesterName(admission.getSemester().getSemesterName());
            }

            return dto;

        }


        // =====================================
        // FACULTY PROFILE
        // =====================================


        if(role == Role.FACULTY) {

            Faculty faculty =

                    facultyRepository
                    .findByUserIdAndStatus(
                            user.getId(),
                            RecordStatus.ACTIVE
                    )

                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Faculty profile not found"
                            )
                    );


            return profileMapper
                    .convertFacultyToDto(faculty);

        }


        if(role == Role.ADMIN) {

            Admin admin =
                    adminRepository
                    .findByUserIdAndStatus(
                            user.getId(),
                            RecordStatus.ACTIVE
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Admin profile not found"
                            ));

            return profileMapper.convertAdminToDto(admin);

        }



        if(role == Role.SUPER_ADMIN) {

            SuperAdmin superAdmin =
                    superAdminRepository
                    .findByUserIdAndStatus(
                            user.getId(),
                            RecordStatus.ACTIVE
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Super Admin profile not found"
                            ));

            return profileMapper.convertSuperAdminToDto(superAdmin);

        }

        throw new ResourceNotFoundException(
                "Profile not found for role : " + role
        );
    }

    
    
	 // =====================================
	 // CHANGE PASSWORD
	 // =====================================
	
	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "PROFILE",
	        description = "User changed password"
	)
    public ChangePasswordResponseDto changePassword(

            ChangePasswordRequestDto request,
            String token

    ) {

    	User user = getLoggedInUser();

        // =====================================
        // VERIFY OLD PASSWORD
        // =====================================

        boolean currentPasswordMatch =

                passwordEncoder.matches(

                        request.getCurrentPassword(),

                        user.getPassword()

                );



        if(!currentPasswordMatch) {

            throw new BusinessException(
                    "Current password is incorrect."
            );
        }




        // =====================================
        // CHECK SAME PASSWORD
        // =====================================

        boolean samePassword =

                passwordEncoder.matches(

                        request.getNewPassword(),

                        user.getPassword()

                );



        if(samePassword) {

            throw new BusinessException(
                    "New password cannot be same as old password."
            );

        }




        // =====================================
        // CONFIRM PASSWORD CHECK
        // =====================================

        if(!request.getNewPassword()
                .equals(request.getConfirmPassword())) {


            throw new BusinessException(
                    "New password and confirm password do not match."
            );

        }




        // =====================================
        // UPDATE PASSWORD
        // =====================================


        user.setPassword(

                passwordEncoder.encode(
                        request.getNewPassword()
                )

        );


        userRepository.save(user);





        // =====================================
        // SECURITY INVALIDATION
        // =====================================


        // Remove refresh token

        refreshTokenService
                .deleteByUser(user);


        // Blacklist current access token

        if(token != null && !token.isBlank()) {

            jwtBlacklistService
                    .blacklistToken(token);

        }



        return new ChangePasswordResponseDto(

                "Password changed successfully. Please login again.",

                LocalDateTime.now()

        );

    }



	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "PROFILE",
	        description = "User updated profile information"
	)
	public ProfileResponseDto updateProfile(UpdateProfileRequestDto request) {

	    User user = getLoggedInUser();

	    switch (user.getRole()) {

	        case STUDENT:
	            updateStudentProfile(user, request);
	            break;

	        case FACULTY:
	            updateFacultyProfile(user, request);
	            break;

	        case ADMIN:
	            updateAdminProfile(user, request);
	            break;

	        case SUPER_ADMIN:
	            updateSuperAdminProfile(user, request);
	            break;

	        default:
	            throw new BusinessException("Unsupported role");
	    }

	    return getMyProfile();
	}

	

	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "PROFILE",
	        description = "User uploaded profile photo"
	)
    public ProfileResponseDto  uploadProfilePhoto(MultipartFile file) {

    	User user = getLoggedInUser();

     // Validate Profile Image
     validateProfileImage(file);


       String imagePath =
                fileStorageService.storeFile(file);
       
       String contentType = file.getContentType();

       if (contentType == null || !contentType.startsWith("image/")) {

           throw new BusinessException(
                   "Only image files are allowed"
           );
       }

		// Update Profile Image

		String oldImage = null;

		switch (user.getRole()) {

            case STUDENT:

                Student student =
                        studentRepository
                        .findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found"
                                ));


                oldImage = student.getProfileImage();

                student.setProfileImage(imagePath);
                
                student.setUpdatedByUserId(user.getId());
                student.setUpdatedBy(user.getEmail());

                studentRepository.save(student);

                break;



            case FACULTY:

                Faculty faculty =
                        facultyRepository
                        .findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Faculty not found"
                                ));


                oldImage = faculty.getProfileImage();

                faculty.setProfileImage(imagePath);
                
                faculty.setUpdatedByUserId(user.getId());
                faculty.setUpdatedBy(user.getEmail());

                facultyRepository.save(faculty);

                break;



            case ADMIN:

                Admin admin =
                		adminRepository
                        .findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admin not found"
                                ));


                oldImage = admin.getProfileImage();

                admin.setProfileImage(imagePath);
                
                admin.setUpdatedByUserId(user.getId());
                admin.setUpdatedBy(user.getEmail());

                adminRepository.save(admin);

                break;
                
            case SUPER_ADMIN:

                SuperAdmin superAdmin =
                		superAdminRepository
                        .findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admin not found"
                                ));

                oldImage = superAdmin.getProfileImage();

                superAdmin.setProfileImage(imagePath);

                superAdmin.setUpdatedByUserId(user.getId());
                superAdmin.setUpdatedBy(user.getEmail());

                superAdminRepository.save(superAdmin);

                break;



            default:

                throw new BusinessException(
                        "Unsupported role"
                );
        }



        // =====================================================
        // Delete Old Image
        // =====================================================

		if (oldImage != null && !oldImage.isBlank()) {

		    fileStorageService.deleteFile(oldImage);

		}


		return getMyProfile();
    }


	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.DELETE,
	        module = "PROFILE",
	        description = "User removed profile photo"
	)
    public void removeProfilePhoto() {

    	User user = getLoggedInUser();


        String oldImage = null;


        switch (user.getRole()) {

            case STUDENT:

                Student student =
                        studentRepository.findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Student not found"));

                oldImage = student.getProfileImage();

                student.setProfileImage(null);

                student.setUpdatedByUserId(user.getId());
                student.setUpdatedBy(user.getEmail());

                studentRepository.save(student);

                break;


            case FACULTY:

                Faculty faculty =
                        facultyRepository.findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Faculty not found"));

                oldImage = faculty.getProfileImage();

                faculty.setProfileImage(null);

                faculty.setUpdatedByUserId(user.getId());
                faculty.setUpdatedBy(user.getEmail());

                facultyRepository.save(faculty);

                break;


            case ADMIN:

                Admin admin =
                		adminRepository.findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Admin not found"));

                oldImage = admin.getProfileImage();

                admin.setProfileImage(null);

                admin.setUpdatedByUserId(user.getId());

                admin.setUpdatedBy(user.getEmail());


                adminRepository.save(admin);

                break;

                
            case SUPER_ADMIN:

                SuperAdmin superAdmin =
                		superAdminRepository.findByUserIdAndStatus(
                                user.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Admin not found"));

                oldImage = superAdmin.getProfileImage();

                superAdmin.setProfileImage(null);

                superAdmin.setUpdatedByUserId(user.getId());

                superAdmin.setUpdatedBy(user.getEmail());


                superAdminRepository.save(superAdmin);

                break;

            default:
                throw new BusinessException("Unsupported role");
        }


        if (oldImage != null && !oldImage.isBlank()) {

            fileStorageService.deleteFile(oldImage);

        }
    }
	
	
	
	
	
	
	private void updateFacultyProfile(
	        User user,
	        UpdateProfileRequestDto request
	) {

	    Faculty faculty = facultyRepository
	            .findByUserIdAndStatus(
	                    user.getId(),
	                    RecordStatus.ACTIVE
	            )
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Faculty profile not found"
	                    ));

	    // =====================================================
	    // PERSONAL INFORMATION
	    // =====================================================

	    faculty.setFirstName(request.getFirstName());
	    faculty.setMiddleName(request.getMiddleName());
	    faculty.setLastName(request.getLastName());
	    faculty.setGender(request.getGender());
	    faculty.setDateOfBirth(request.getDateOfBirth());

	    // =====================================================
	    // FACULTY INFORMATION
	    // =====================================================

	    faculty.setDesignation(
	            request.getFacultyDesignation()
	    );

	    faculty.setQualification(
	            request.getQualification()
	    );

	    faculty.setJoiningDate(
	            request.getJoiningDate()
	    );

	    // employeeCode intentionally not editable
	    // salary intentionally not editable
	    // department intentionally not editable

	    // =====================================================
	    // ADDRESS
	    // =====================================================

	    updateAddress(
	            faculty.getAddress(),
	            request
	    );

	    // =====================================================
	    // COMMON USER INFORMATION
	    // =====================================================

	    updateUserInformation(
	            user,
	            request
	    );

	    // =====================================================
	    // AUDIT
	    // =====================================================

	    updateAuditInformation(
	            faculty,
	            user
	    );

	    facultyRepository.save(faculty);
	}
	
	
	private void updateAdminProfile(
	        User user,
	        UpdateProfileRequestDto request
	) {

	    Admin admin = adminRepository
	            .findByUserIdAndStatus(
	                    user.getId(),
	                    RecordStatus.ACTIVE
	            )
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Admin profile not found"
	                    ));

	    // =====================================================
	    // PERSONAL INFORMATION
	    // =====================================================

	    admin.setFirstName(request.getFirstName());
	    admin.setLastName(request.getLastName());
	    admin.setGender(request.getGender());
	    admin.setDateOfBirth(request.getDateOfBirth());

	    // =====================================================
	    // ADMIN INFORMATION
	    // =====================================================

	    admin.setDesignation(
	            request.getAdminDesignation()
	    );

	    admin.setJoiningDate(
	            request.getJoiningDate()
	    );

	    // employeeCode intentionally not editable
	    // salary intentionally not editable

	    // =====================================================
	    // ADDRESS
	    // =====================================================

	    updateAddress(
	            admin.getAddress(),
	            request
	    );

	    // =====================================================
	    // COMMON USER INFORMATION
	    // =====================================================

	    updateUserInformation(
	            user,
	            request
	    );

	    // =====================================================
	    // AUDIT
	    // =====================================================

	    updateAuditInformation(
	            admin,
	            user
	    );

	    adminRepository.save(admin);
	}
	
	
	
	
	// SUPER ADMIN PROFILE UPDATE
	private void updateSuperAdminProfile(
	        User user,
	        UpdateProfileRequestDto request
	) {

	    SuperAdmin superAdmin = superAdminRepository
	            .findByUserIdAndStatus(
	                    user.getId(),
	                    RecordStatus.ACTIVE
	            )
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Super Admin profile not found"
	                    ));

	    // =====================================================
	    // PERSONAL INFORMATION
	    // =====================================================

	    superAdmin.setFirstName(request.getFirstName());
	    superAdmin.setLastName(request.getLastName());
	    superAdmin.setGender(request.getGender());
	    superAdmin.setDateOfBirth(request.getDateOfBirth());

	    // =====================================================
	    // SUPER ADMIN INFORMATION
	    // =====================================================

	    superAdmin.setDesignation(
	            request.getSuperAdminDesignation()
	    );

	    superAdmin.setJoiningDate(
	            request.getJoiningDate()
	    );

	    // employeeCode intentionally not editable
	    // salary intentionally not editable

	    // =====================================================
	    // ADDRESS
	    // =====================================================

	    updateAddress(
	            superAdmin.getAddress(),
	            request
	    );

	    // =====================================================
	    // COMMON USER INFORMATION
	    // =====================================================

	    updateUserInformation(
	            user,
	            request
	    );

	    // =====================================================
	    // AUDIT
	    // =====================================================

	    updateAuditInformation(
	            superAdmin,
	            user
	    );

	    superAdminRepository.save(superAdmin);
	}
	
	
	private void updateStudentProfile(
	        User user,
	        UpdateProfileRequestDto request
	) {

	    Student student = studentRepository
	            .findByUserIdAndStatus(
	                    user.getId(),
	                    RecordStatus.ACTIVE
	            )
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Student profile not found"
	                    ));

	    // =====================================================
	    // PERSONAL INFORMATION
	    // =====================================================

	    student.setFirstName(request.getFirstName());
	    student.setMiddleName(request.getMiddleName());
	    student.setLastName(request.getLastName());
	    student.setGender(request.getGender());
	    student.setDateOfBirth(request.getDateOfBirth());

	    // =====================================================
	    // EXTRA PERSONAL INFORMATION
	    // =====================================================

	    student.setBloodGroup(request.getBloodGroup());
	    student.setCategory(request.getCategory());
	    student.setReligion(request.getReligion());
	    student.setNationality(request.getNationality());

	    // =====================================================
	    // CONTACT INFORMATION
	    // =====================================================

	    student.setAlternateMobile(request.getAlternateMobile());
	    student.setAlternateEmail(request.getAlternateEmail());

	    // =====================================================
	    // IDENTITY DOCUMENTS
	    // =====================================================

	    student.setAadhaarNumber(request.getAadhaarNumber());
	    student.setPanNumber(request.getPanNumber());

	    // =====================================================
	    // ACADEMIC INFORMATION
	    // =====================================================

	    student.setSection(request.getSection());
	    student.setBatch(request.getBatch());
	    student.setCurrentYear(request.getCurrentYear());
	    student.setStudentType(request.getStudentType());
	    student.setStudentStatus(request.getStudentStatus());

	    // =====================================================
	    // ADDRESS
	    // =====================================================

	    updateAddress(
	            student.getAddress(),
	            request
	    );

	    // =====================================================
	    // COMMON USER INFORMATION
	    // =====================================================

	    updateUserInformation(
	            user,
	            request
	    );

	    // =====================================================
	    // AUDIT
	    // =====================================================

	    updateAuditInformation(
	            student,
	            user
	    );

	    studentRepository.save(student);
	}
	
	
	
	// =====================================
	// PROFILE IMAGE VALIDATION
	// =====================================

	private void validateProfileImage(MultipartFile file) {


	    // 1. Empty Check

	    if (file == null || file.isEmpty()) {

	        throw new BusinessException(
	                "Profile image file is required"
	        );

	    }



	    // 2. Content Type Check

	    String contentType = file.getContentType();


	    if (contentType == null ||

	            !(contentType.equals("image/jpeg")
	            || contentType.equals("image/png")
	            || contentType.equals("image/jpg"))) {


	        throw new BusinessException(
	                "Only JPG, JPEG and PNG images are allowed"
	        );

	    }




	    // 3. File Name + Extension Check

	    String fileName = file.getOriginalFilename();


	    if(fileName == null || fileName.isBlank()) {

	        throw new BusinessException(
	                "Invalid image file name"
	        );

	    }


	    int lastDotIndex =
	            fileName.lastIndexOf(".");


	    if(lastDotIndex == -1) {

	        throw new BusinessException(
	                "Image extension missing"
	        );

	    }



	    String extension =
	            fileName.substring(lastDotIndex + 1)
	            .toLowerCase();



	    if(!extension.equals("jpg")
	            && !extension.equals("jpeg")
	            && !extension.equals("png")) {


	        throw new BusinessException(
	                "Only jpg, jpeg and png files are allowed"
	        );

	    }




	    // 4. Size Check

	    long maxSize =
	            2 * 1024 * 1024; // 2 MB


	    if(file.getSize() > maxSize) {

	        throw new BusinessException(
	                "Profile image size must be less than 2 MB"
	        );

	    }

	}
	
	

	
	
	private User getLoggedInUser() {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    return userRepository.findByEmail(authentication.getName())
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User not found"));
	}
	
	
	
	private void updateAddress(
	        Address address,
	        UpdateProfileRequestDto request
	) {

	    if (address == null) {
	        throw new ResourceNotFoundException(
	                "Address not found"
	        );
	    }

	    @SuppressWarnings("null")
		Village village = villageRepository
	            .findById(request.getVillageId())
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Village not found"
	                    ));

	    address.setHouseNumber(
	            request.getHouseNumber()
	    );

	    address.setStreet(
	            request.getStreet()
	    );

	    address.setLandmark(
	            request.getLandmark()
	    );

	    address.setAddressType(
	            request.getAddressType()
	    );

	    address.setVillage(village);
	}
	
	
	private void updateUserInformation(
	        User user,
	        UpdateProfileRequestDto request
	) {

	    user.setMobileNumber(
	            request.getMobileNumber()
	    );

	    userRepository.save(user);
	}
	
	private void updateAuditInformation(
	        BaseEntity entity,
	        User user
	) {

	    entity.setUpdatedByUserId(
	            user.getId()
	    );

	    entity.setUpdatedBy(
	            user.getEmail()
	    );
	}
	
}