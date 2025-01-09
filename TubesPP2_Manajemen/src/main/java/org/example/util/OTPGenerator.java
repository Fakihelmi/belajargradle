package org.example.util;

import java.util.Random;

public class OTPGenerator {
    private static final int OTP_LENGTH = 6;

    public static String generateOTP() {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return otp.toString();
    }
}