package kg.core.base.search;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ с пагинацией")
public record PageResponse<T>(
        @Schema(description = "Содержимое страницы")
        List<T> content,
        
        @Schema(description = "Номер текущей страницы (начиная с 0)")
        int page,
        
        @Schema(description = "Размер страницы")
        int size,
        
        @Schema(description = "Общее количество элементов")
        long totalElements,
        
        @Schema(description = "Общее количество страниц")
        int totalPages,
        
        @Schema(description = "Первая ли это страница")
        boolean first,
        
        @Schema(description = "Последняя ли это страница")
        boolean last,
        
        @Schema(description = "Пустая ли страница")
        boolean empty
) {
}
