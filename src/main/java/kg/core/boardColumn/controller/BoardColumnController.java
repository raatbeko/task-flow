package kg.core.boardColumn.controller;

import io.swagger.v3.oas.annotations.Operation;
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
public class BoardColumnController {

    BoardColumnService boardColumnService;
    BoardColumnMapper boardColumnMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать колонку",
            description = "Управление колонками доски"
    )
    public BoardColumnResponse create(@Valid @RequestBody BoardColumnCreateRequest boardColumnCreateRequest) {
        return boardColumnMapper.toResponse(boardColumnService.create(boardColumnCreateRequest));
    }

    @PutMapping("/{id}")
    public BoardColumnResponse update(@PathVariable Long id, @Valid @RequestBody BoardColumnUpdateRequest boardColumnUpdateRequest) {
        return boardColumnMapper.toResponse(boardColumnService.update(id, boardColumnUpdateRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        boardColumnService.delete(id);
    }

}
