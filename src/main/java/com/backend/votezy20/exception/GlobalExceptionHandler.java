package com.backend.votezy20.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 404
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
			HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	// 400
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// 409
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex,
			HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	// 401
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	// 403
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	// OTP
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<ErrorResponse> handleInvalidOtp(InvalidOtpException ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

		return ResponseEntity.badRequest().body(response);
	}

	// Election closed
	@ExceptionHandler(ElectionClosedException.class)
	public ResponseEntity<ErrorResponse> handleElectionClosed(ElectionClosedException ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

		return ResponseEntity.badRequest().body(response);
	}

	// Vote already cast
	@ExceptionHandler(VoteAlreadyCastException.class)
	public ResponseEntity<ErrorResponse> handleVoteAlreadyCast(VoteAlreadyCastException ex,
			HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	// Validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {

			String fieldName = ((FieldError) error).getField();

			String errorMessage = error.getDefaultMessage();

			errors.put(fieldName, errorMessage);
		});

		return ResponseEntity.badRequest().body(errors);
	}

	// Spring Security Access Denied
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.FORBIDDEN, "Access denied", request);

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	// Runtime fallback
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

		return ResponseEntity.badRequest().body(response);
	}

	// Global exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {

		ErrorResponse response = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", request);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	private ErrorResponse buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {

		return ErrorResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
				.error(status.getReasonPhrase()).message(message).path(request.getRequestURI()).build();
	}
}