package kg.core.base.search;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PageMapper {

    /**
     * Преобразует Page в PageResponse с маппингом содержимого
     *
     * @param page   исходная страница
     * @param mapper функция для преобразования элементов
     * @param <E>    тип исходной сущности
     * @param <R>    тип результирующего DTO
     * @return PageResponse с преобразованным содержимым
     */
    public <E, R> PageResponse<R> toResponse(Page<E> page, Function<E, R> mapper) {
        List<R> content = page.getContent().stream()
                .map(mapper)
                .toList();

        return toResponse(page, content);
    }

    /**
     * Преобразует Page в PageResponse с уже готовым списком содержимого
     *
     * @param page    исходная страница (для метаданных)
     * @param content уже преобразованное содержимое
     * @param <T>     тип содержимого
     * @return PageResponse
     */
    public <T> PageResponse<T> toResponse(Page<?> page, List<T> content) {
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }

    /**
     * Преобразует Page в PageResponse без изменения типа содержимого
     *
     * @param page исходная страница
     * @param <T>  тип содержимого
     * @return PageResponse
     */
    public <T> PageResponse<T> toResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }
}
