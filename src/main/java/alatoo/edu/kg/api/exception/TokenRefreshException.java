package alatoo.edu.kg.api.exception;


import org.springframework.http.HttpStatus;

public class TokenRefreshException extends BaseException{
    public TokenRefreshException(String token, String message) {
        super("Phone number is already taken", HttpStatus.CONFLICT);
    }
}
