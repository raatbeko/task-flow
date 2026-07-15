package kg.core.task.mapper;

import jakarta.persistence.EntityNotFoundException;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.task.dtos.TaskDto;
import kg.core.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    protected BoardColumnRepository boardColumnRepository;

    @Mapping(target = "boardColumn", source = "boardColumnId", qualifiedByName = "boardColumnIdToBoardColumn")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "usersFavorites", ignore = true)
    @Mapping(target = "copiedHistory", ignore = true)
    public abstract Task toEntity(TaskDto dto);

    @Mapping(target = "boardColumnId", source = "boardColumn.id")
    public abstract TaskDto toDto(Task entity);

    @Mapping(target = "boardColumn", source = "boardColumnId", qualifiedByName = "boardColumnIdToBoardColumn")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "usersFavorites", ignore = true)
    @Mapping(target = "copiedHistory", ignore = true)
    public abstract Task update(TaskDto dto, @MappingTarget Task entity);

    public abstract List<TaskDto> toDtos(List<Task> entities);

    public abstract List<Task> toEntities(List<TaskDto> dtos);

    @Named("boardColumnIdToBoardColumn")
    protected BoardColumn boardColumnIdToBoardColumn(Long boardColumnId) {
        if (boardColumnId == null) return null;
        return boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new EntityNotFoundException("Колонка не найдена"));
    }
}
