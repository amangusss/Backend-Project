package alatoo.edu.kg.api.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends BaseException{
    public AuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
