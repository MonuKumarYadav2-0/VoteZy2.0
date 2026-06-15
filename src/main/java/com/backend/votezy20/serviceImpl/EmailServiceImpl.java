package com.backend.votezy20.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.votezy20.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

	private final SendGrid sendGrid;

	@Value("${app.mail.from}")
	private String fromEmail;

	@Override
	@Async("taskExecutor")
	public void sendOtpEmail(String to, String otp) {

		String html = """
				<h2>VoteZy Email Verification</h2>
				<p>Your OTP is:</p>
				<h1>%s</h1>
				<p>Valid for 15 minutes.</p>
				""".formatted(otp);

		sendEmail(to, "VoteZy OTP Verification", html);
	}

	@Override
	@Async("taskExecutor")
	public void sendVoterInviteEmail(String to, String name, String token) {

		String setupLink = "http://localhost:5173/setup-password?token=" + token;

		String html = """
				<h2>Welcome to VoteZy</h2>
				<p>Hello %s,</p>
				<p>Set your password:</p>
				<a href="%s">
				    Setup Password
				</a>
				""".formatted(name, setupLink);

		sendEmail(to, "VoteZy Voter Invitation", html);
	}

	@Override
	@Async("taskExecutor")
	public void sendPasswordSetupConfirmation(String to, String name) {

		String html = """
				<h2>Password Set Successfully</h2>
				<p>Hello %s,</p>
				<p>Your password has been configured.</p>
				""".formatted(name);

		sendEmail(to, "Password Setup Successful", html);
	}

	@Override
	@Async("taskExecutor")
	public void sendElectionNotification(String to, String name, String electionName) {

		String html = """
				<h2>New Election</h2>
				<p>Hello %s,</p>
				<p>A new election has been created:</p>
				<h3>%s</h3>
				""".formatted(name, electionName);

		sendEmail(to, "New Election Available", html);
	}

	private void sendEmail(String to, String subject, String html) {

		try {

			Email from = new Email(fromEmail);

			Email receiver = new Email(to);

			Content content = new Content("text/html", html);

			Mail mail = new Mail(from, subject, receiver, content);

			Request request = new Request();

			request.setMethod(Method.POST);

			request.setEndpoint("mail/send");

			request.setBody(mail.build());

			Response response = sendGrid.api(request);

			log.info("Mail sent to {} with status {}", to, response.getStatusCode());

		} catch (IOException e) {

			log.error("Failed to send email to {}", to, e);

			throw new RuntimeException("Failed to send email");
		}
	}
}