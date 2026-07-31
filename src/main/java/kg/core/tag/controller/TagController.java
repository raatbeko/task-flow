package kg.core.tag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import kg.core.tag.dtos.TagDto;
import kg.core.tag.endpoint.TagEndpoint;
import kg.core.utils.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Validated
@RestController
@RequestMapping(PathUtils.TAG)
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Tag(
        name = "Теги",
        description = "Взаимодействие с тегами"
)
public class TagController {

    TagEndpoint endpoint;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать тег",
            description = "Создает новый тег"
    )
    public TagDto create(@Valid @RequestBody TagDto request) {
        return endpoint.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить тег по ID",
            description = "Возвращает информацию о теге"
    )
    public TagDto getById(@PathVariable Long id) {
        return endpoint.get(id);
    }

    @GetMapping
    @Operation(
            summary = "Получить все теги",
            description = "Возвращает все теги по проекту"
    )
    public List<TagDto> getAll(@RequestParam Long projectId) {
        return endpoint.getAll(projectId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить тег",
            description = "Обновляет информацию о теге"
    )
    public TagDto update(@PathVariable Long id, @Valid @RequestBody TagDto request) {
        return endpoint.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить тег",
            description = "Удаляет тег по ID"
    )
    public void delete(@PathVariable Long id) {
        endpoint.delete(id);
    }
}