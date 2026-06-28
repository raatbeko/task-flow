package kg.core.base.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение для ситуаций некорректного запроса (HTTP 400).
 */
public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message);
    }

    public BadRequestException(String code, String message) {
        super(HttpStatus.BAD_REQUEST, code, message);
    }
}
