package dev.lucavassos.recruiter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);
    }
}
