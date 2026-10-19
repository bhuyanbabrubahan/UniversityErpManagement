package com.sms.security.serviceImpl;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.address.entity.Address;
import com.location.address.repository.AddressRepository;
import com.location.village.entity.Village;
import com.location.village.repository.VillageRepository;
import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.email.entity.EmailVerificationToken;
import com.sms.email.repository.EmailVerificationTokenRepository;
import com.sms.email.service.EmailService;
import com.sms.entity.Department;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.exception.AccountVerificationException;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceAlreadyExistsException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.StudentMapper;
import com.sms.otp.enums.OtpPurpose;
import com.sms.otp.service.OtpService;
import com.sms.otp.service.PasswordResetTokenService;
import com.sms.repository.DepartmentRepository;
import com.sms.repository.StudentRepository;
import com.sms.security.dto.AccountVerificationResponseDto;
import com.sms.security.dto.ForgotPasswordRequestDto;
import com.sms.security.dto.LoginRequestDto;
import com.sms.security.dto.LoginResponseDto;
import com.sms.security.dto.LogoutResponseDto;
import com.sms.security.dto.PasswordResetResponseDto;
import com.sms.security.dto.RefreshTokenRequestDto;
import com.sms.security.dto.RefreshTokenResponseDto;
import com.sms.security.dto.ResetPasswordRequestDto;
import com.sms.security.dto.UserRequestDto;
import com.sms.security.dto.UserResponseDto;
import com.sms.security.dto.VerifyForgotPasswordOtpRequestDto;
import com.sms.security.dto.VerifyForgotPasswordOtpResponseDto;
import com.sms.security.entity.RefreshToken;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;
import com.sms.security.repository.UserRepository;
import com.sms.security.service.AuthenticationService;
import com.sms.security.service.JwtBlacklistService;
import com.sms.security.service.JwtService;
import com.sms.security.service.RefreshTokenService;
import com.sms.sequence.enums.ModuleType;
import com.sms.sequence.service.CodeGeneratorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;

	private final StudentRepository studentRepository;

	private final DepartmentRepository departmentRepository;

	private final VillageRepository villageRepository;

	private final AddressRepository addressRepository;

	private final EmailVerificationTokenRepository emailVerificationTokenRepository;
	
	private final PasswordEncoder passwordEncoder;

	private final StudentMapper studentMapper;

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;
	
	private final OtpService otpService;

	private final CodeGeneratorService codeGeneratorService;

	private final EmailService emailService;

	private final RefreshTokenService refreshTokenService;

	private final JwtBlacklistService jwtBlacklistService;
	
	private final PasswordResetTokenService passwordResetTokenService;
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	// =====================================
	// REGISTER STUDENT
	// =====================================

	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.CREATE,
	        module = "AUTH",
	        description = "Student registration"
	)
	public UserResponseDto register(UserRequestDto request) {

		// 1. Duplicate Email Check

		if (userRepository.existsByEmail(request.getEmail())) {

			throw new ResourceAlreadyExistsException("Email already exists : " + request.getEmail());

		}
		
		
		if(request.getMobileNumber() != null &&
		        userRepository.existsByMobileNumber(
		                request.getMobileNumber()
		        )) {

		    throw new ResourceAlreadyExistsException(
		            "Mobile number already exists."
		    );
		}

		// 2. Department Validation

		Department department =

				departmentRepository.findByIdAndStatus(request.getDepartmentId(), RecordStatus.ACTIVE)

						.orElseThrow(() -> new ResourceNotFoundException("Department not found or inactive"));
		
		
		if (!request.getPassword().equals(request.getConfirmPassword())) {

		    throw new BusinessException(
		            "Password and Confirm Password do not match.");

		}
		

		// 3. Create User

		User user = new User();

		user.setEmail(request.getEmail());
		
		user.setMobileNumber(
		        request.getMobileNumber()
		);

		user.setEmailVerified(false);

		user.setMobileVerified(false);

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole(Role.STUDENT);

		// Email aur Mobile verify hone tak account pending rahega
		user.setStatus(UserStatus.PENDING_VERIFICATION);

		// 4. Create Address

		@SuppressWarnings("null")
		Village village =

				villageRepository.findById(request.getAddress().getVillageId())

						.orElseThrow(() -> new ResourceNotFoundException("Village not found"));

		Address address = new Address();

		address.setHouseNumber(request.getAddress().getHouseNumber());

		address.setStreet(request.getAddress().getStreet());

		address.setLandmark(request.getAddress().getLandmark());

		address.setAddressType(request.getAddress().getAddressType());

		address.setVillage(village);

		address = addressRepository.save(address);

		// 5. Create Student

		Student student =

				studentMapper.convertToEntity(request);

		student.setDepartment(department);

		student.setUser(user);

		student.setAddress(address);

		student.setRollNumber(codeGeneratorService.generateCode(ModuleType.STUDENT, "STU"));

		student.setStatus(RecordStatus.ACTIVE);

		// User pehle save hona chahiye
		user = userRepository.save(user);

		// Ab Student save karo
		student = studentRepository.save(student);
		
		if(user.getMobileNumber() != null) {

		    otpService.sendOtp(
		            user.getMobileNumber(),
		            OtpPurpose.MOBILE_VERIFICATION
		    );

		}
		
		String token = UUID.randomUUID().toString();

		EmailVerificationToken verificationToken =
		        new EmailVerificationToken();

		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(
		        LocalDateTime.now().plusHours(24)
		);
		verificationToken.setVerified(false);

		emailVerificationTokenRepository.save(
		        verificationToken
		);

		try {
		    emailService.sendVerificationEmail(
		        user.getEmail(),
		        token
		    );
		} catch (org.springframework.mail.MailException e) {
		    log.error(
		        "Verification email failed for registered user",
		        e
		    );
		}

		
		// 6. Response

		return new UserResponseDto(

		);

	}

	// =====================================
	// LOGIN USER
	// =====================================

	@Override
	@AuditLog(
	        action = AuditAction.LOGIN,
	        module = "AUTH",
	        description = "User login"
	)
	public LoginResponseDto login(LoginRequestDto request) {

	    log.info("LOGIN API CALLED : {}", request.getEmail());


	    User user =
	            userRepository.findByEmail(request.getEmail())
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException("User not found")
	                    );


	    log.info("LOGIN USER FOUND : {}", user.getEmail());

	    log.info(
	            "CURRENT FAILED ATTEMPTS : {}",
	            user.getFailedLoginAttempts()
	    );



	    // ================================
	    // Account lock check
	    // ================================

	    if(user.getAccountLockedUntil() != null
	            &&
	       user.getAccountLockedUntil()
	           .isAfter(LocalDateTime.now())) {


	        throw new BusinessException(
	                "Account temporarily locked. Try again later."
	        );

	    }



	    // ================================
	    // Reset expired account lock
	    // ================================

	    if(user.getAccountLockedUntil() != null
	            &&
	       !user.getAccountLockedUntil()
	            .isAfter(LocalDateTime.now())) {


	        user.setAccountLockedUntil(null);

	        user.setFailedLoginAttempts(0);

	        userRepository.save(user);


	        log.info(
	                "Account lock expired. Failed attempts reset for : {}",
	                user.getEmail()
	        );

	    }




	 // ================================
	 // Account Verification Check
	 // ================================

	    if(!Boolean.TRUE.equals(user.getEmailVerified())
	            ||
	       !Boolean.TRUE.equals(user.getMobileVerified())) {


	        AccountVerificationResponseDto verificationData =
	                new AccountVerificationResponseDto(

	                        user.getEmail(),

	                        user.getMobileNumber(),

	                        user.getEmailVerified(),

	                        user.getMobileVerified(),

	                        user.getStatus().name()

	                );


	        throw new AccountVerificationException(
	                "ACCOUNT_VERIFICATION_PENDING",
	                verificationData
	        );

	    }

	 // ================================
	 // Account Status Check
	 // ================================

	 switch (user.getStatus()) {


	     case ACTIVE:
	         break;


	     case PENDING_VERIFICATION:

	         throw new BusinessException(
	                 "Account verification pending."
	         );


	     case BLOCKED:

	         throw new BusinessException(
	                 "Your account has been blocked."
	         );


	     case INACTIVE:

	         throw new BusinessException(
	                 "Your account is inactive."
	         );


	     default:

	         throw new BusinessException(
	                 "Account unavailable."
	         );

	 }





	    // ================================
	    // Authenticate password
	    // ================================

	    try {


	        log.info(
	                "AUTHENTICATION START FOR : {}",
	                request.getEmail()
	        );


	        authenticationManager.authenticate(

	                new UsernamePasswordAuthenticationToken(
	                        request.getEmail(),
	                        request.getPassword()
	                )

	        );



	        // Login success

	        user.setFailedLoginAttempts(0);

	        user.setAccountLockedUntil(null);


	        userRepository.save(user);



	        log.info(
	                "LOGIN SUCCESS. RESET FAILED ATTEMPTS FOR : {}",
	                user.getEmail()
	        );


	    }
	    catch(Exception ex) {


	        log.error(
	                "LOGIN FAILED FOR USER : {}",
	                user.getEmail()
	        );


	        log.error(
	                "AUTH ERROR : {}",
	                ex.getMessage()
	        );



	        int attempts =
	                user.getFailedLoginAttempts() == null
	                ? 0
	                : user.getFailedLoginAttempts();



	        attempts++;


	        log.info(
	                "FAILED LOGIN COUNT NOW : {}",
	                attempts
	        );

	        user.setFailedLoginAttempts(attempts);

	        if(attempts >= 5) {
	            log.warn(
	                    "ACCOUNT LOCKING USER : {}",
	                    user.getEmail()
	            );

	            user.setAccountLockedUntil(
	                    LocalDateTime.now()
	                    .plusMinutes(15)
	            );

	        }

	        userRepository.save(user);

	        throw new BusinessException(
	                "Invalid email or password"
	        );

	    }

	    // Generate JWT Access Token

	    String accessToken =
	            jwtService.generateToken(user);

	    // Generate Refresh Token

	    RefreshToken refreshToken =
	            refreshTokenService.createRefreshToken(user);

	    // Response

	    return new LoginResponseDto(

	            accessToken,
	            refreshToken.getToken(),
	            "Bearer",
	            jwtService.getAccessTokenExpirationInSeconds(),
	            user.getEmail(),
	            user.getMobileNumber(),
	            user.getRole().name(),
	            user.getStatus(),
	            Boolean.TRUE.equals(user.getEmailVerified()),
	            Boolean.TRUE.equals(user.getMobileVerified())
	    );
	}

	
	
	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.FORGOT_PASSWORD,
	        module = "AUTH",
	        description = "Forgot password request"
	)
	public PasswordResetResponseDto forgotPassword(
	        ForgotPasswordRequestDto request
	) {


	    LocalDateTime now =
	            LocalDateTime.now();



	    /*
	     * Find user using mobile number.
	     *
	     * Security:
	     * Do not expose whether user exists.
	     */

	    User user =
	            userRepository
	            .findByMobileNumber(
	                    request.getMobileNumber()
	            )
	            .orElse(null);



	    if(user == null) {


	    	return new PasswordResetResponseDto(
	    			 "If the mobile number is registered, a password reset OTP has been sent.",
	    	        now
	    	);
	    }

	    if(!Boolean.TRUE.equals(user.getMobileVerified())) {

	        throw new BusinessException(
	            "Mobile number is not verified."
	        );

	    }

	     // Generate OTP for password reset

	    otpService.sendOtp(

	            user.getMobileNumber(),

	            OtpPurpose.FORGOT_PASSWORD

	    );


	    return new PasswordResetResponseDto(

	    		"Password reset OTP has been sent to your registered mobile number.",

	            now
	    );

	}

	
	
	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.RESET_PASSWORD,
	        module = "AUTH",
	        description = "Password reset"
	)
	public PasswordResetResponseDto resetPassword(
	        ResetPasswordRequestDto request
	) {


	    LocalDateTime now =
	            LocalDateTime.now();



	    /*
	     * Password confirmation check
	     */
	    if(!request.getNewPassword()
	            .equals(request.getConfirmPassword())) {


	        throw new BusinessException(
	                "New password and confirm password do not match."
	        );

	    }



	    /*
	     * Validate reset token.
	     *
	     * Checks:
	     * - Token exists
	     * - Token not expired
	     * - Token not already used
	     */
	    passwordResetTokenService
	            .validateToken(
	                    request.getToken()
	            );



	    /*
	     * Get user from reset token.
	     *
	     * User identity comes from token.
	     * Client cannot decide which user password to reset.
	     */
	    User user =
	            passwordResetTokenService
	            .getUserByToken(
	                    request.getToken()
	            );



	    /*
	     * Prevent old password reuse.
	     */
	    if(passwordEncoder.matches(
	            request.getNewPassword(),
	            user.getPassword()
	    )) {


	        throw new BusinessException(
	                "New password cannot be same as old password."
	        );

	    }



	    /*
	     * Update password.
	     */
	    user.setPassword(
	            passwordEncoder.encode(
	                    request.getNewPassword()
	            )
	    );


	    userRepository.save(user);



	    /*
	     * Invalidate reset token after successful password change.
	     *
	     * Token becomes one-time usable.
	     */
	    passwordResetTokenService
	            .markTokenUsed(
	                    request.getToken()
	            );



	    return new PasswordResetResponseDto(

	            "Password reset successfully.",

	            now
	    );

	}
	
	
	

	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.LOGOUT,
	        module = "AUTH",
	        description = "User logout"
	)
	public LogoutResponseDto logout(String token) {

		if (token == null) {

			throw new BusinessException("JWT token missing");

		}

		String email = jwtService.extractUsername(token);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// Add access token into blacklist
		jwtBlacklistService.blacklistToken(token);

		// Remove refresh token
		refreshTokenService.deleteByUser(user);

		log.info("User logout successful : {}", email);

		return LogoutResponseDto.builder()

				.message("Logout successful.")

				.timestamp(LocalDateTime.now())

				.build();

	}

	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.LOGIN,
	        module = "AUTH",
	        description = "Refresh access token"
	)
	public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request) {

		log.info("Refresh token request received.");

		// =====================================
		// STEP 1 : Find Refresh Token
		// =====================================

		RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());

		// =====================================
		// STEP 2 : Check Revoked
		// =====================================

		if (refreshToken.isRevoked()) {

			log.warn("Refresh token is revoked.");

			throw new BusinessException("Refresh token has been revoked.");

		}

		// =====================================
		// STEP 3 : Check Expiry
		// =====================================

		if (refreshTokenService.isExpired(refreshToken)) {

			log.warn("Refresh token expired.");

			throw new BusinessException("Refresh token has expired.");

		}

		// =====================================
		// STEP 4 : User Validation
		// =====================================

		User user = refreshToken.getUser();

		switch (user.getStatus()) {


		    case ACTIVE:
		        break;
	
	
		    case PENDING_VERIFICATION:
	
		        throw new BusinessException(
		                "Account verification pending."
		        );
	
	
		    case BLOCKED:
	
		        throw new BusinessException(
		                "Your account has been blocked."
		        );
	
	
		    case INACTIVE:
	
		        throw new BusinessException(
		                "Your account is inactive."
		        );
	
	
		    default:
	
		        throw new BusinessException(
		                "Account unavailable."
		        );
	
		}
		// =====================================
		// STEP 5 : ROTATE REFRESH TOKEN
		// =====================================

		refreshTokenService.deleteByUser(user);

		RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

		// =====================================
		// STEP 6 : Generate New Access Token
		// =====================================

		String accessToken = jwtService.generateToken(user);

		log.info("Access token rotated successfully for user : {}", user.getEmail());

		// =====================================
		// STEP 7 : Return Response
		// =====================================

		return RefreshTokenResponseDto.builder()

				.accessToken(accessToken)

				.refreshToken(newRefreshToken.getToken())

				.tokenType("Bearer")

				.expiresIn(jwtService.getAccessTokenExpirationInSeconds())

				.email(user.getEmail())

				.role(user.getRole().name())

				.timestamp(LocalDateTime.now())

				.build();

	}





	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.CREATE,
	        module = "MOBILE_VERIFICATION",
	        description = "Send mobile verification OTP"
	)
	public String sendMobileVerificationOtp(
	        String mobileNumber
	) {


	    User user =
	            userRepository
	            .findByMobileNumber(mobileNumber)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User not found"
	                    )
	            );



	    if(Boolean.TRUE.equals(
	            user.getMobileVerified()
	    )) {


	        throw new BusinessException(
	                "Mobile number already verified."
	        );

	    }



	    return otpService.sendOtp(
	            mobileNumber,
	            OtpPurpose.MOBILE_VERIFICATION
	    );

	}





	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "MOBILE_VERIFICATION",
	        description = "Verify mobile OTP"
	)
	public String verifyMobileOtp(
	        String mobileNumber,
	        String otp
	) {


	    String response =
	            otpService.verifyOtp(
	                    mobileNumber,
	                    otp,
	                    OtpPurpose.MOBILE_VERIFICATION
	            );


	    User user =
	        userRepository.findByMobileNumber(mobileNumber)
	        .orElseThrow(() ->
	            new ResourceNotFoundException("User not found")
	        );


	    user.setMobileVerified(true);

		 // Email + Mobile दोनों verify होने पर ही ACTIVE
	
		 if(Boolean.TRUE.equals(user.getEmailVerified())
		         &&
		    Boolean.TRUE.equals(user.getMobileVerified())) {
	
		     user.setStatus(
		             UserStatus.ACTIVE
		     );
	
		 }
		 else {
	
		     user.setStatus(
		             UserStatus.PENDING_VERIFICATION
		     );
		 }
		 userRepository.save(user);

	    return response;

	}

	
	
	
	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.FORGOT_PASSWORD,
	        module = "AUTH",
	        description = "Verify forgot password OTP"
	)
	public VerifyForgotPasswordOtpResponseDto verifyForgotPasswordOtp(
	        VerifyForgotPasswordOtpRequestDto request
	) {

	    /*
	     * Verify OTP
	     */
	    otpService.verifyOtp(

	            request.getMobileNumber(),

	            request.getOtp(),

	            OtpPurpose.FORGOT_PASSWORD

	    );

	    /*
	     * Find user
	     */
	    User user =
	            userRepository
	                    .findByMobileNumber(
	                            request.getMobileNumber()
	                    )
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "User not found."
	                            )
	                    );

	    /*
	     * Generate password reset token
	     */
	    String token =
	            passwordResetTokenService
	                    .createToken(user);

	    /*
	     * Return response
	     */
	    return new VerifyForgotPasswordOtpResponseDto(

	            token,

	            "OTP verified successfully.",

	            LocalDateTime.now()

	    );

	}

	
	public Map<String,Object> getVerificationStatus(String email){

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(
	              () -> new RuntimeException("User not found")
	            );

	    Map<String,Object> response = new HashMap<>();

	    response.put(
	        "email",
	        user.getEmail()
	    );

	    response.put(
	        "emailVerified",
	        user.getEmailVerified()
	    );

	    response.put(
	        "mobileVerified",
	        user.getMobileVerified()
	    );

	    response.put(
	        "mobileNumber",
	        user.getMobileNumber()
	    );

	    response.put(
	        "status",
	        user.getStatus()
	    );

	    return response;
	}

}