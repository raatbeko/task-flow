package kg.core.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.core.board.dtos.BoardCreateRequest;
import kg.core.board.dtos.BoardPositionRequest;
import kg.core.board.dtos.BoardResponse;
import kg.core.board.dtos.BoardUpdateRequest;
import kg.core.board.endpoint.BoardEndpoint;
import kg.core.task.dtos.UpdatePosition;
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
@RequestMapping(PathUtils.BOARD)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Boards",
        description = "Управление досками внутри проектов"
)
@SecurityRequirement(name = "bearer-jwt")
public class BoardController {

    BoardEndpoint endpoint;

    @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(
                summary = "Создать доску",
                description = "Создает новую доску внутри указанного проекта со статусом ACTIVE"
        )
    public BoardResponse create(@Valid @RequestBody BoardCreateRequest request) {
        return endpoint.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить доску по ID",
            description = "Возвращяет информацию о доске"
    )
    public BoardResponse getById(@PathVariable Long id) {

        return endpoint.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить доску",
            description = "Возвращяет информацию о доске"
    )
    public BoardResponse update(@PathVariable Long id, @Valid @RequestBody BoardUpdateRequest request) {
        return endpoint.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить доску",
            description = "Удаляет доску по ID"
    )
    public void delete(@PathVariable Long id) {

        endpoint.delete(id);
    }

    @PatchMapping("/{id}/archive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Архивировать доску",
            description = "Меняет статус доски с ACTIVE на ARCHIVE"
    )
    public void archive(@PathVariable Long id) {

        endpoint.archive(id);
    }

    @PostMapping("/{id}/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Дублировать доску",
            description = "Создает дубликат доски с припиской (клон)"
    )
    public BoardResponse duplicate(@PathVariable Long id) {

        return endpoint.duplicate(id);
    }

    @PatchMapping("/{id}/change-position")
    @Operation(
            summary = "Поменять позицию",
            description = "Меняет позицию доски"
    )
    public BoardResponse changePosition(@PathVariable Long id, @Valid @RequestBody BoardPositionRequest request) {
        return endpoint.changePosition(id, request);
    }

    @GetMapping("/project/{id}")
    @Operation(
            summary = "Получить доски проекта",
            description = "Возвращяет акивные доски проекта"
    )
    public List<BoardResponse> findByProjectId(@PathVariable Long id) {
        return endpoint.findByProjectId(id);
    }

}

