package com.application.urbanRide.advices;

import com.application.urbanRide.custExceptions.BadRequestException;
import com.application.urbanRide.custExceptions.ResourceNotFoundException;
import com.application.urbanRide.custExceptions.RuntimeConflictException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class RestControlAdvices {

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiError> handleRuntimeConflictException(RuntimeConflictException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.CONFLICT,ex.getLocalizedMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(NoSuchElementException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.UNAUTHORIZED,ex.getLocalizedMessage()),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.UNAUTHORIZED,ex.getLocalizedMessage()),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex)
    {
        return new ResponseEntity<>(new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage()),HttpStatus.FORBIDDEN);
    }

}


