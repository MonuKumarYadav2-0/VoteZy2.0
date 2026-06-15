package com.backend.votezy20.service;

public interface OtpService {

	String generateOtp();

	String createOtp(String email);

	boolean verifyOtp(String email, String otp);

	void invalidateOtp(String email);
}