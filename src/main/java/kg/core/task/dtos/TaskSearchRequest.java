package kg.core.task.dtos;

import kg.core.task.model.Priority;

import java.time.LocalDateTime;

public record TaskSearchRequest(
        String query
) {
}
