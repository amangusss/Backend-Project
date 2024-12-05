package alatoo.edu.kg.api.exception;


public class TokenRefreshException extends ConflictException {
    public TokenRefreshException(String reason) {
        super("Token refresh failed: " + reason);
    }
}
