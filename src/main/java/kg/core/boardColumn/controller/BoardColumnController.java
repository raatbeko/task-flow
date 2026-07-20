package kg.core.boardColumn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.core.board.dtos.BoardResponse;
import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnPositionRequest;
import kg.core.boardColumn.dtos.BoardColumnResponse;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;
import kg.core.boardColumn.endpoint.BoardColumnEndpoint;
import kg.core.task.dtos.UpdatePosition;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(PathUtils.BOARD_COLUMN)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Board Columns",
        description = "Управление колонками внутри досок"
)
@SecurityRequirement(name = "bearer-jwt")
public class BoardColumnController {

    BoardColumnEndpoint endpoint;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать колонку",
            description = "Управление колонками доски"
    )
    public BoardColumnResponse create(@Valid @RequestBody BoardColumnCreateRequest request) {
        return endpoint.create(request);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить колонку",
            description = "Возвращяет информацию о колонке"
    )
    public BoardColumnResponse update(@PathVariable Long id, @Valid @RequestBody BoardColumnUpdateRequest request) {
            return endpoint.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить колонку",
            description = "Удалить колонку по ID"
    )
    public void delete(@PathVariable Long id) {
        endpoint.delete(id);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить по колонку по ID",
            description = "Возвращяет информацию о колонке"
    )
    public BoardColumnResponse getById(@PathVariable Long id) {
        return endpoint.getById(id);
    }

    @PatchMapping("/{id}/change-position")
    @Operation(
            summary = "Поменять позицию",
            description = "Меняет позицию колонки"
    )
    public BoardColumnResponse changePosition(@PathVariable Long id, @Valid @RequestBody BoardColumnPositionRequest request) {
        return endpoint.changePosition(id, request);
    }

}
