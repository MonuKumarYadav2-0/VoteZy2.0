package com.backend.votezy20.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.votezy20.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Override
	@Async("emailExecutor")
	public void sendOtpEmail(String to, String otp) {

		try {

			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(to);

			helper.setSubject("VoteZy OTP Verification");

			String html = """
					<h2>VoteZy Email Verification</h2>
					<p>Your OTP is:</p>
					<h1>%s</h1>
					<p>Valid for 15 minutes.</p>
					""".formatted(otp);

			helper.setText(html, true);

			mailSender.send(message);

			log.info("OTP mail sent to {}", to);

		} catch (Exception e) {

			log.error("Failed to send OTP email to {}", to, e);

			throw new RuntimeException("Failed to send OTP email");
		}
	}

	@Override
	@Async("emailExecutor")
	public void sendVoterInviteEmail(String to, String name, String token) {

		try {

			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(to);

			helper.setSubject("VoteZy Voter Invitation");

			String setupLink = "http://localhost:5173/setup-password?token=" + token;

			String html = """
					<h2>Welcome to VoteZy</h2>
					<p>Hello %s,</p>
					<p>Set your password:</p>
					<a href="%s">
					    Setup Password
					</a>
					""".formatted(name, setupLink);

			helper.setText(html, true);

			mailSender.send(message);

		} catch (Exception e) {

			log.error("Failed to send voter invite email to {}", to, e);

			throw new RuntimeException("Failed to send voter invite email");
		}
	}

	@Override
	@Async("emailExecutor")
	public void sendPasswordSetupConfirmation(String to, String name) {

		try {

			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(to);

			helper.setSubject("Password Setup Successful");

			String html = """
					<h2>Password Set Successfully</h2>
					<p>Hello %s,</p>
					<p>Your password has been configured.</p>
					""".formatted(name);

			helper.setText(html, true);

			mailSender.send(message);

		} catch (Exception e) {

			log.error("Failed to send confirmation email to {}", to, e);

			throw new RuntimeException("Failed to send confirmation email");
		}
	}

	@Override
	@Async("emailExecutor")
	public void sendElectionNotification(String to, String name, String electionName) {

		try {

			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(to);

			helper.setSubject("New Election Available");

			String html = """
					<h2>New Election</h2>
					<p>Hello %s,</p>
					<p>A new election has been created:</p>
					<h3>%s</h3>
					""".formatted(name, electionName);

			helper.setText(html, true);

			mailSender.send(message);

		} catch (Exception e) {

			log.error("Failed to send election notification to {}", to, e);

			throw new RuntimeException("Failed to send election notification");
		}
	}
}