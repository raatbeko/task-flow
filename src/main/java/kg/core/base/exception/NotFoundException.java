package kg.core.base.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение для ситуаций, когда ресурс не найден (HTTP 404).
 */
public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }

    public NotFoundException(String code, String message) {
        super(HttpStatus.NOT_FOUND, code, message);
    }
}
