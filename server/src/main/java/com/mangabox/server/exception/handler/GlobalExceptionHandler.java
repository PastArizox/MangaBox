package com.mangabox.server.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mangabox.server.exception.UnauthorizedActionException;
import com.mangabox.server.exception.UserAlreadyExistsException;
import com.mangabox.server.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ApiError errorResponse = new ApiError(HttpStatus.CONFLICT, ex.getMessage());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, errorMessage);

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex) {
        ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError errorResponse = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ApiError> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

}
