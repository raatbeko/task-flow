package kg.core.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.core.task.dtos.*;
import kg.core.task.endpoint.TaskEndpoint;
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
@RequestMapping(PathUtils.TASK)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Задачи",
        description = "Взаимодействие с задачами"
)
public class TaskController {

    TaskEndpoint endpoint;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать задачу",
            description = "Создает новую задачу"
    )
    public TaskDto create(@Valid @RequestBody TaskDto request) {
        return endpoint.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
                summary = "Получить задачу по ID",
            description = "Возвращяет информацию о задаче"
    )
    public TaskDto getById(@PathVariable Long id) {
        return endpoint.get(id);
    }

    @GetMapping
    @Operation(
            summary = "Получить все задачи",
            description = "Возвращяет все задачи по колонке борда"
    )
    public List<TaskDto> getAll(@RequestParam Long boardColumnId) {
        return endpoint.getAll(boardColumnId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить задачу",
            description = "Возвращяет информацию о задаче"
    )
    public TaskDto update(@PathVariable Long id, @Valid @RequestBody TaskDto request) {
        return endpoint.update(id, request);
    }

    @PutMapping("/{id}/change-position")
    @Operation(
            summary = "Поменять позицию",
            description = "Меняет позицию задачи"
    )
    public UpdatePositionDto changePosition(@PathVariable Long id, @Valid @RequestBody UpdatePositionDto request) {
        return endpoint.changePosition(id, request);
    }

    @PutMapping("/{id}/purpose-tags")
    @Operation(
            summary = "Добавить теги задачи",
            description = "Добавление тегов к задаче"
    )
    public UpdateTagsDto purposeTags(@PathVariable Long id, @Valid @RequestBody UpdateTagsDto request) {
        return endpoint.purposeTags(id, request);
    }

    @PutMapping("/{id}/users")
    @Operation(
            summary = "Добавить пользователя к задаче",
            description = "Назначение пользователя к задаче"
    )
    public UpdateUsersDto purposeUsers(@PathVariable Long id, @Valid @RequestBody UpdateUsersDto request) {
        return endpoint.purposeUsers(id, request);
    }

    @PutMapping("/{id}/replace-purpose-tags")
    @Operation(
            summary = "Заменить теги задачи",
            description = "Очищает текущие теги задачи и назначает переданные"
    )
    public UpdateTagsDto replacePurposeTags(@PathVariable Long id, @Valid @RequestBody UpdateTagsDto request) {
        return endpoint.replacePurposeTags(id, request);
    }

        @PutMapping("/{id}/replace-users")
    @Operation(
            summary = "Заменить пользователей задачи",
            description = "Очищает текущих исполнителей задачи и назначает переданных"
    )
    public UpdateUsersDto  replacePurposeUsers(@PathVariable Long id, @Valid @RequestBody UpdateUsersDto request) {
        return endpoint.replacePurposeUsers(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по ID")
    public void delete(@PathVariable Long id) {
        endpoint.delete(id);
    }
}