package alatoo.edu.kg.api.exception;

import org.springframework.http.HttpStatus;

public class FileOperationException extends BaseException {
    public FileOperationException(String message, Throwable cause) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        initCause(cause);
    }
}
