package kg.core.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.core.comment.dtos.CommentCreateRequest;
import kg.core.comment.dtos.CommentResponse;
import kg.core.comment.dtos.CommentUpdateRequest;
import kg.core.comment.endpoint.CommentEndpoint;
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
@RequestMapping(PathUtils.COMMENT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Comments",
        description = "Управление комментариями"
)
@SecurityRequirement(name = "bearer-jwt")
public class CommentController {

    CommentEndpoint endpoint;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать коммент",
            description = "Создает комментарии к таску"
    )
    public CommentResponse create(@Valid @RequestBody CommentCreateRequest request){
        return endpoint.create(request);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Обновить коммент",
            description = "Возвращяет информацию о комментарии"
    )
    public CommentResponse update(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request){
        return endpoint.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить комментарий",
            description = "Удаляет коммент по ID"
    )
    public void delete(@PathVariable Long id){
        endpoint.delete(id);
    }

    @GetMapping("/task/{taskId}")
    @Operation(
            summary = "Получить комментарии к задаче",
            description = "Возвращяет список комментариев"
    )
    public List<CommentResponse> findByTaskId(@PathVariable Long taskId){
        return endpoint.findByTaskId(taskId);
    }



}
