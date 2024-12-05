package alatoo.edu.kg.api.exception;

public class UsernameTakenException extends ConflictException {
    public UsernameTakenException() {
        super("Username is already taken");
    }
}