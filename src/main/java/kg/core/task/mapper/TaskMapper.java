package kg.core.task.mapper;

import jakarta.persistence.EntityNotFoundException;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.tag.model.Tag;
import kg.core.tag.repository.TagRepository;
import kg.core.task.dtos.TaskDto;
import kg.core.task.model.Task;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    protected BoardColumnRepository boardColumnRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Mapping(target = "boardColumn", source = "boardColumnId", qualifiedByName = "boardColumnIdToBoardColumn")
    @Mapping(target = "assignees", source = "assignees", qualifiedByName = "assigneeIdToUsers")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagIdsToTags")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "usersFavorites", ignore = true)
    @Mapping(target = "copiedHistory", ignore = true)
    public abstract Task toEntity(TaskDto dto);

    @Mapping(target = "boardColumnId", source = "boardColumn.id")
    @Mapping(target = "assignees", source = "assignees", qualifiedByName = "usersToAssigneeId")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagsToTagIds")
    public abstract TaskDto toDto(Task entity);

    @Mapping(target = "boardColumn", source = "boardColumnId", qualifiedByName = "boardColumnIdToBoardColumn")
    @Mapping(target = "assignees", source = "assignees", qualifiedByName = "assigneeIdToUsers")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagIdsToTags")
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

    @Named("assigneeIdToUsers")
    protected Collection<User> assigneeIdToUsers(Long userId) {
        if (userId == null) return new HashSet<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        return Set.of(user);
    }

    @Named("usersToAssigneeId")
    protected Long usersToAssigneeId(Collection<User> users) {
        if (users == null || users.isEmpty()) return null;
        return users.iterator().next().getId();
    }

    @Named("tagIdsToTags")
    protected Set<Task> tagIdsToTags(Long[] tagIds) {
        if (tagIds == null || tagIds.length == 0) return new HashSet<>();
        return Arrays.stream(tagIds)
                .map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Тег не найден с id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("tagsToTagIds")
    protected Long[] tagsToTagIds(Collection<Tag> tags) {
        if (tags == null || tags.isEmpty()) return new Long[0];
        return tags.stream().map(Tag::getId).toArray(Long[]::new);
    }
}
