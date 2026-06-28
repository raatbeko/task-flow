package kg.core.base.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение для ситуаций, когда доступ запрещён (HTTP 403).
 */
public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, "FORBIDDEN", message);
    }

    public ForbiddenException(String code, String message) {
        super(HttpStatus.FORBIDDEN, code, message);
    }
}
