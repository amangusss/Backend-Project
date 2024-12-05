package alatoo.edu.kg.api.exception;

public class EmailTakenException extends ConflictException {
    public EmailTakenException() {
        super("Email is already taken");
    }
}
