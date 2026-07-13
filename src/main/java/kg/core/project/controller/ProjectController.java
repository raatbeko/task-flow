package kg.core.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.core.project.dtos.ProjectDto;
import kg.core.project.endpoint.ProjectEndpoint;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(PathUtils.USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Проект",
        description = "Взаимодействие с проектом"
)
public class ProjectController {

    ProjectEndpoint endpoint;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать проект",
            description = "Создает новый проект со статусом ACTIVE"
    )
    public ProjectDto create(@Valid @RequestBody ProjectDto request) {
        return endpoint.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить проект по ID",
            description = "Возвращяет информацию о проекте"
    )
    public ProjectDto getById(@PathVariable Long id) {
        return endpoint.get(id);
    }

    @GetMapping
    @Operation(
            summary = "Получить все проекты",
            description = "Возвращяет все проекты"
    )
    public List<ProjectDto> getAll() {
        return endpoint.getAll();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обнововить проект",
            description = "Возвращяет информацию о проекте"
    )
    public ProjectDto update(@PathVariable Long id, @Valid @RequestBody ProjectDto request) {
        return endpoint.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить проект",
            description = "Удаляет проект по ID"
    )
    public void delete(@PathVariable Long id) {
        endpoint.delete(id);
    }

    @PatchMapping("/{id}/archive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Архивировать проект",
            description = "Меняет статус проект с ACTIVE на ARCHIVE"
    )
    public void archive(@PathVariable Long id) {
        endpoint.archive(id);
    }
}