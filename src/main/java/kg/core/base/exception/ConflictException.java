package kg.core.base.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение для конфликтов состояния (HTTP 409).
 */
public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, "CONFLICT", message);
    }

    public ConflictException(String code, String message) {
        super(HttpStatus.CONFLICT, code, message);
    }
}
