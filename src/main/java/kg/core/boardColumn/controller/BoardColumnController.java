package kg.core.boardColumn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.core.board.mapper.BoardMapper;
import kg.core.boardColumn.dtos.BoardColumnCreateRequest;
import kg.core.boardColumn.dtos.BoardColumnResponse;
import kg.core.boardColumn.dtos.BoardColumnUpdateRequest;
import kg.core.boardColumn.mapper.BoardColumnMapper;
import kg.core.boardColumn.service.BoardColumnService;
import kg.core.utils.PathUtils;
import lombok.AccessLevel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(PathUtils.BOARDS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Board Columns",
        description = "Управление колонками внутри досок"
)
@SecurityRequirement(name = "bearer-jwt")
public class BoardColumnController {

    BoardColumnService boardColumnService;
    BoardColumnMapper boardColumnMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать колонку",
            description = "Управление колонками доски"
    )
    public BoardColumnResponse create(@Valid @RequestBody BoardColumnCreateRequest request) {
        return boardColumnMapper.toResponse(boardColumnService.create(request));
    }

    @PutMapping("/{columnId}")
    @Operation(
            summary = "Обновить колонку",
            description = "Возвращяет информацию о колонке"
    )
    public BoardColumnResponse update(@PathVariable Long columnId, @Valid @RequestBody BoardColumnUpdateRequest request) {
        return boardColumnMapper.toResponse(boardColumnService.update(columnId, request));
    }

    @DeleteMapping("/{columnId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить доску",
            description = "Удалить доску по ID"
    )
    public void delete(@PathVariable Long columnId) {
        boardColumnService.delete(columnId);
    }

}
