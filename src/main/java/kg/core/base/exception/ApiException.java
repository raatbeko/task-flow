package kg.core.base.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * Базовое исключение для бизнес-ошибок API.
 *
 * Позволяет задать HTTP-статус и машинный код ошибки,
 * чтобы {@link kg.core.common.GlobalExceptionHandler} мог корректно
 * сформировать ответ клиенту.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class ApiException extends RuntimeException {

    HttpStatus status;
    String code;

    protected ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    protected ApiException(HttpStatus status, String code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
