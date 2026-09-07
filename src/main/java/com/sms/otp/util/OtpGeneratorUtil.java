package com.sms.otp.util;

import java.security.SecureRandom;

public final class OtpGeneratorUtil {


    private static final SecureRandom RANDOM =
            new SecureRandom();


    private OtpGeneratorUtil() {

    }


    public static String generateOtp() {

        int otp =
                RANDOM.nextInt(900000) + 100000;

        return String.valueOf(otp);
    }


}