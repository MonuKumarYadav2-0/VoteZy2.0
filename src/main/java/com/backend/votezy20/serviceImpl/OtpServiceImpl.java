package com.backend.votezy20.serviceImpl;

import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.backend.votezy20.exception.InvalidOtpException;
import com.backend.votezy20.service.OtpService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

	private static final String OTP_PREFIX = "otp:";

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public String generateOtp() {

		Random random = new Random();

		int otpNumber = 100000 + random.nextInt(900000);

		return String.valueOf(otpNumber);
	}

	@Override
	public String createOtp(String email) {

		String otp = generateOtp();

		String key = OTP_PREFIX + email;

		// Save OTP in Redis
		redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(5));

		return otp;
	}

	@Override
	public boolean verifyOtp(String email, String otp) {

		String key = OTP_PREFIX + email;

		Object storedOtp = redisTemplate.opsForValue().get(key);

		// OTP expired or missing
		if (storedOtp == null) {

			throw new InvalidOtpException("OTP expired");
		}

		// Invalid OTP
		if (!storedOtp.toString().equals(otp)) {

			throw new InvalidOtpException("Invalid OTP");
		}

		// Remove after successful verification
		redisTemplate.delete(key);

		return true;
	}

	@Override
	public void invalidateOtp(String email) {

		redisTemplate.delete(OTP_PREFIX + email);
	}
}