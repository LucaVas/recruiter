package dev.lucavassos.recruiter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UserNotApprovedException extends RuntimeException {

    public UserNotApprovedException(String message) {
        super(message);
    }
}
