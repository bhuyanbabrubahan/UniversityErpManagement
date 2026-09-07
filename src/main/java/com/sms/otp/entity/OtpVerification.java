package com.sms.otp.entity;


import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;
import com.sms.otp.enums.OtpPurpose;
import com.sms.security.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "otp_verifications")
@Getter
@Setter
public class OtpVerification extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * OTP belongs to authentication user.
     *
     * Student/Faculty/Admin independent.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;



    /*
     * Mobile snapshot.
     *
     * Reason:
     * If user changes mobile later,
     * old OTP history remains traceable.
     */
    @Column(
            name = "mobile_number",
            nullable = false,
            length = 15
    )
    private String mobileNumber;



    /*
     * Never store plain OTP in production.
     *
     * Store hashed OTP.
     */
    @Column(
            name = "otp_hash",
            nullable = false
    )
    private String otpHash;



    /*
     * Purpose of OTP.
     *
     * LOGIN
     * MOBILE_VERIFY
     * FORGOT_PASSWORD
     */
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private OtpPurpose purpose;



    /*
     * OTP expiry time.
     */
    @Column(
            nullable = false
    )
    private LocalDateTime expiryTime;



    /*
     * Maximum retry protection.
     */
    @Column(
            nullable = false
    )
    private Integer attemptCount = 0;



    /*
     * OTP verification status.
     */
    @Column(
            nullable = false
    )
    private Boolean verified = false;



    /*
     * Soft delete support.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordStatus status = RecordStatus.ACTIVE;

}