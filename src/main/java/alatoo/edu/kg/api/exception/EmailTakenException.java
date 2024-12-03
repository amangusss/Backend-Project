package alatoo.edu.kg.api.exception;

import org.springframework.http.HttpStatus;

public class EmailTakenException extends BaseException {
    public EmailTakenException() {
        super("Email is already taken", HttpStatus.CONFLICT);
    }
}
