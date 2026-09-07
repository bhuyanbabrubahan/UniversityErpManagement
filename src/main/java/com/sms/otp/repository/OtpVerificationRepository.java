package com.sms.otp.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sms.enums.RecordStatus;
import com.sms.otp.entity.OtpVerification;
import com.sms.otp.enums.OtpPurpose;



@Repository
public interface OtpVerificationRepository 
        extends JpaRepository<OtpVerification, Long> {


    /*
     * Latest active OTP for user and purpose.
     *
     * Example:
     * User login OTP
     * Forgot password OTP
     */
    Optional<OtpVerification> 
    findTopByUserIdAndPurposeAndStatusOrderByCreatedAtDesc(
            Long userId,
            OtpPurpose purpose,
            RecordStatus status
    );

    Optional<OtpVerification>
    findTopByMobileNumberAndPurposeOrderByCreatedAtDesc(
            String mobileNumber,
            OtpPurpose purpose
    );


    /*
     * Latest OTP using mobile number.
     *
     * Useful for resend cooldown.
     */
    Optional<OtpVerification>
    findTopByMobileNumberAndPurposeAndStatusOrderByCreatedAtDesc(
            String mobileNumber,
            OtpPurpose purpose,
            RecordStatus status
    );

    
    Optional<OtpVerification>
    findTopByUserIdAndPurposeOrderByCreatedAtDesc(
            Long userId,
            OtpPurpose purpose
    );

    
    Optional<OtpVerification>
    findTopByUserIdOrderByCreatedAtDesc(
            Long userId
    );	
    
    
    List<OtpVerification> findByExpiryTimeBeforeAndStatus(
            LocalDateTime time,
            RecordStatus status
    );
    
    
    /*
     * Cleanup expired OTP.
     *
     * Soft delete approach.
     */
    @Modifying
    @Query("""
            update OtpVerification o
            set o.status = com.sms.enums.RecordStatus.DELETED
            where o.expiryTime < :now
            and o.status = com.sms.enums.RecordStatus.ACTIVE
            """)
    int markExpiredOtpAsDeleted(
            LocalDateTime now
    );



    /*
     * Remove old OTP before generating new one.
     */
    @Modifying
    @Query("""
            update OtpVerification o
            set o.status = com.sms.enums.RecordStatus.INACTIVE
            where o.user.id = :userId
            and o.purpose = :purpose
            and o.status = com.sms.enums.RecordStatus.ACTIVE
            """)
    int deactivatePreviousOtp(
            Long userId,
            OtpPurpose purpose
    );


}