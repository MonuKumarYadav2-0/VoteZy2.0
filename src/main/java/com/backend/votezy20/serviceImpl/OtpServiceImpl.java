package com.backend.votezy20.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.votezy20.entitiy.OtpToken;
import com.backend.votezy20.repositories.OtpTokenRepository;
import com.backend.votezy20.service.OtpService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OtpServiceImpl implements OtpService {

	private final OtpTokenRepository otpTokenRepository;

	@Override
	public String generateOtp() {

		Random random = new Random();

		int otpNumber = 100000 + random.nextInt(900000);

		return String.valueOf(otpNumber);
	}

	@Override
	public OtpToken createOtp(String email) {

		String otp = generateOtp();

		OtpToken otpToken = OtpToken.builder().email(email).otp(otp).expiresAt(LocalDateTime.now().plusMinutes(15))
				.used(false).build();

		return otpTokenRepository.save(otpToken);
	}

	@Override
	public boolean verifyOtp(String email, String otp) {

		OtpToken otpToken = otpTokenRepository.findTopByEmailAndOtpAndUsedFalseOrderByCreatedAtDesc(email, otp)
				.orElse(null);

		if (otpToken == null) {
			return false;
		}

		if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			return false;
		}

		otpToken.setUsed(true);

		otpTokenRepository.save(otpToken);

		return true;
	}

	@Override
	public void invalidateOtp(OtpToken otpToken) {

		otpToken.setUsed(true);

		otpTokenRepository.save(otpToken);
	}
}