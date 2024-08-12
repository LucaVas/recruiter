package dev.lucavassos.recruiter.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    ProblemDetail handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-401-unauthorized"));
        errorDetail.setProperty("description", "The credentials entered are incorrect");

        return errorDetail;
    }

    @ExceptionHandler(AccountStatusException.class)
    ProblemDetail handleAccountStatusException(AccountStatusException e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-403-forbidden"));
        errorDetail.setProperty("description", "The account is locked");

        return errorDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-404-not-found"));
        errorDetail.setProperty("description", "The resource was not found");

        return errorDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-403-forbidden"));
        errorDetail.setProperty("description", "You are not authorized to access this resource");

        return errorDetail;
    }

    @ExceptionHandler(SignatureException.class)
    ProblemDetail handleSignatureException(SignatureException e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-403-forbidden"));
        errorDetail.setProperty("description", "The JWT signature is invalid");

        return errorDetail;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    ProblemDetail handleExpiredJwtException(ExpiredJwtException e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-403-forbidden"));
        errorDetail.setProperty("description", "The JWT token has expired");

        return errorDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("One of more fields are invalid:\n");
        errors.forEach((key, value) -> sb.append("- ").append(value).append("\n"));

        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, sb.toString());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request"));
        errorDetail.setProperty("description", "One of more fields are invalid");

        return errorDetail;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    ProblemDetail handleDuplicateResourceException(DuplicateResourceException e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request"));
        errorDetail.setProperty("description", "The resource already exists");

        return errorDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());

        StringBuilder errorMessage = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> {
            errorMessage.append(violation.getPropertyPath())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
        });

        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, errorMessage.toString());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request"));
        errorDetail.setProperty("description", "One of more fields are invalid");

        return errorDetail;
    }

    @ExceptionHandler(RequestValidationException.class)
    ProblemDetail handleRequestValidationException(RequestValidationException e) {
        log.error(e.getMessage());

        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request"));
        errorDetail.setProperty("description", "One of more fields are invalid");

        return errorDetail;
    }

    @ExceptionHandler({Exception.class, DatabaseException.class})
    ProblemDetail handlException(Exception e) {
        log.error(e.getMessage());
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        errorDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-500-internal-server-error"));
        errorDetail.setProperty("description", "Unknown internal server error.");

        return errorDetail;
    }
}
