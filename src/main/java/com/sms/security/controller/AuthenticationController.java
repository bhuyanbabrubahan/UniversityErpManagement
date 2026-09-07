package com.sms.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.email.dto.ResendVerificationRequestDto;
import com.sms.email.service.EmailVerificationService;
import com.sms.otp.dto.MobileVerificationRequestDto;
import com.sms.otp.dto.VerifyMobileOtpRequestDto;
import com.sms.payload.ApiResponseDto;
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
import com.sms.security.service.AuthenticationService;
import com.sms.security.util.JwtTokenUtil;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final JwtTokenUtil jwtTokenUtil;
	private final EmailVerificationService emailVerificationService;

	// ============================
	// REGISTER API
	// ============================

	@Operation(
	        summary = "Register Student",
	        description = "Registers a new student account in the University Management System."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "201",
	                description = "Student registered successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Validation failed"
	        ),

	        @ApiResponse(
	                responseCode = "409",
	                description = "Email already exists"
	        )
	})
	@PostMapping("/register")
	public ResponseEntity<ApiResponseDto<UserResponseDto>> register(

	        @Valid
	        @RequestBody
	        UserRequestDto request) {
		
		log.info("Student registration request received for email: {}", request.getEmail());

	    UserResponseDto response =
	            authenticationService.register(request);

	    return ResponseBuilder.success(

	            response,

	            "Student registration completed successfully.",

	            HttpStatus.CREATED
	    );
	}

	// ============================
	// LOGIN API
	// ============================

	@Operation(
	        summary = "User Login",
	        description = "Authenticates the user and returns a JWT access token."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Login successful"
	        ),

	        @ApiResponse(
	                responseCode = "401",
	                description = "Invalid email or password"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Validation failed"
	        )
	})
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(

	        @Valid
	        @RequestBody
	        LoginRequestDto request) {
		
		log.info("Login request received for email: {}", request.getEmail());

	    LoginResponseDto response =
	            authenticationService.login(request);

	    return ResponseBuilder.success(
	            response,
	            "Login successful.",
	            HttpStatus.OK
	    );
	}
	
	
	
	// =====================================
	// FORGOT PASSWORD
	// =====================================

	/**
	 * Generates a password reset link for the given email address.
	 * <p>
	 * For security reasons, this API always returns the same success
	 * response even if the email is not registered.
	 * This prevents User Enumeration attacks.
	 */
	@Operation(
	        summary = "Forgot Password",
	        description = "Generates a password reset link for the registered email address. "
	                + "For security reasons, the same response is returned even if the email is not registered."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Password reset email processed successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid request payload"
	        )
	})
	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponseDto<PasswordResetResponseDto>> forgotPassword(

	        @Valid
	        @RequestBody
	        ForgotPasswordRequestDto request) {


	    PasswordResetResponseDto response =
	            authenticationService.forgotPassword(request);


	    return ResponseBuilder.success(

	            response,

	            response.getMessage(),

	            HttpStatus.OK
	    );
	}

	
	// =====================================
		// VERIFY FORGOT PASSWORD OTP
		// =====================================

		@Operation(
		        summary = "Verify Forgot Password OTP",
		        description = "Verifies forgot password OTP and returns a password reset token."
		)
		@ApiResponses({

		        @ApiResponse(
		                responseCode = "200",
		                description = "OTP verified successfully"
		        ),

		        @ApiResponse(
		                responseCode = "400",
		                description = "Invalid or expired OTP"
		        )
		})
		@PostMapping("/verify-forgot-password-otp")
		public ResponseEntity<ApiResponseDto<VerifyForgotPasswordOtpResponseDto>>
		verifyForgotPasswordOtp(

		        @Valid
		        @RequestBody
		        VerifyForgotPasswordOtpRequestDto request

		) {

		    VerifyForgotPasswordOtpResponseDto response =
		            authenticationService.verifyForgotPasswordOtp(
		                    request
		            );

		    return ResponseBuilder.success(

		            response,

		            "OTP verified successfully.",

		            HttpStatus.OK
		    );

		}



	// =====================================
	// RESET PASSWORD
	// =====================================

	/**
	 * Resets the user's password using a valid password reset token.
	 */
	@Operation(
	        summary = "Reset Password",
	        description = "Resets the user's password using a valid, unused and non-expired password reset token."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Password reset successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid request, expired token, used token or password validation failed"
	        )
	})
	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponseDto<PasswordResetResponseDto>> resetPassword(

	        @Valid
	        @RequestBody
	        ResetPasswordRequestDto request) {


	    PasswordResetResponseDto response =
	            authenticationService.resetPassword(request);


	    return ResponseBuilder.success(

	            response,

	            "Password reset successfully.",

	            HttpStatus.OK
	    );
	}
	
	
	
	// =====================================
	// LOGOUT
	// =====================================

	@Operation(
	        summary = "Logout",
	        description = "Logs out the authenticated user by invalidating access token and removing refresh token.",
	        security = {
	                @SecurityRequirement(
	                        name = "bearerAuth"
	                )
	        }
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Logout successful"
	        ),

	        @ApiResponse(
	                responseCode = "401",
	                description = "Unauthorized - Invalid or missing JWT token"
	        )

	})
	@PostMapping("/logout")
	public ResponseEntity<ApiResponseDto<LogoutResponseDto>> logout(

	        HttpServletRequest request

	) {


	    String token =
	            jwtTokenUtil
	            .getTokenFromRequest(request);



	    LogoutResponseDto response =
	            authenticationService.logout(token);



	    return ResponseBuilder.success(

	            response,

	            "Logout successful.",

	            HttpStatus.OK
	    );


	}
	
	
	
	
	
	
	// =====================================
	// REFRESH ACCESS TOKEN
	// =====================================

	@Operation(
	        summary = "Refresh Access Token",
	        description = "Generates a new JWT access token using a valid refresh token."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Access token generated successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid or expired refresh token"
	        ),

	        @ApiResponse(
	                responseCode = "401",
	                description = "Unauthorized"
	        )
	})
	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponseDto<RefreshTokenResponseDto>> refreshToken(

	        @Valid
	        @RequestBody
	        RefreshTokenRequestDto request) {

	    RefreshTokenResponseDto response =
	            authenticationService.refreshToken(request);

	    return ResponseBuilder.success(

	            response,

	            "Access token generated successfully.", 

	            HttpStatus.OK
	    );
	}
	
	
	
	// =====================================
	// VERIFY EMAIL
	// =====================================

	@Operation(
	        summary = "Verify Email",
	        description = "Verifies user email using verification token."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Email verified successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid or expired verification token"
	        )
	})
	@GetMapping("/verify-email")
	public ResponseEntity<ApiResponseDto<String>> verifyEmail(

	        @RequestParam String token

	) {

	    String message =
	            emailVerificationService.verifyEmail(token);


	    return ResponseBuilder.success(

	            message,

	            "Email verified successfully.",

	            HttpStatus.OK
	    );
	}



	// =====================================
	// RESEND VERIFICATION EMAIL
	// =====================================

	@Operation(
	        summary = "Resend Verification Email",
	        description = "Resends email verification link to registered email."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Verification email sent successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid request"
	        ),

	        @ApiResponse(
	                responseCode = "404",
	                description = "User not found"
	        )
	})
	@PostMapping("/resend-verification")
	public ResponseEntity<ApiResponseDto<String>> resendVerification(

	        @Valid
	        @RequestBody
	        ResendVerificationRequestDto request

	) {

	    String message =
	            emailVerificationService.resendVerification(
	                    request.getEmail()
	            );


	    return ResponseBuilder.success(
	            message,
	            "Verification email sent successfully.",
	            HttpStatus.OK
	    );
	}
	
	
	
	// =====================================
	// SEND MOBILE VERIFICATION OTP
	// =====================================

	@Operation(
	        summary = "Send Mobile Verification OTP",
	        description = "Sends OTP to user's registered mobile number for verification."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "OTP sent successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Mobile already verified or invalid request"
	        ),

	        @ApiResponse(
	                responseCode = "404",
	                description = "User not found"
	        )
	})
	@PostMapping("/mobile/send")
	public ResponseEntity<ApiResponseDto<String>> sendMobileVerificationOtp(

	        @Valid
	        @RequestBody
	        MobileVerificationRequestDto request

	) {


	    String response =
	            authenticationService
	            .sendMobileVerificationOtp(
	                    request.getMobileNumber()
	            );


	    return ResponseBuilder.success(

	            response,

	            "Mobile verification OTP sent successfully.",

	            HttpStatus.OK
	    );

	}



	// =====================================
	// VERIFY MOBILE OTP
	// =====================================

	@Operation(
	        summary = "Verify Mobile OTP",
	        description = "Verifies mobile OTP and activates mobile verification."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Mobile verified successfully"
	        ),

	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid or expired OTP"
	        )
	})
	@PostMapping("/mobile/verify")
	public ResponseEntity<ApiResponseDto<String>> verifyMobileOtp(

	        @Valid
	        @RequestBody
	        VerifyMobileOtpRequestDto request

	) {


	    String response =
	            authenticationService
	            .verifyMobileOtp(
	                    request.getMobileNumber(),
	                    request.getOtp()
	            );


	    return ResponseBuilder.success(

	            response,

	            "Mobile verified successfully.",

	            HttpStatus.OK
	    );

	}

	
	
	@GetMapping("/verification-status")
	public ResponseEntity<ApiResponseDto<Object>> verificationStatus(
	        @RequestParam String email
	){

	    return ResponseBuilder.success(
	            authenticationService.getVerificationStatus(email),
	            "Verification status fetched",
	            HttpStatus.OK
	    );

	}

}