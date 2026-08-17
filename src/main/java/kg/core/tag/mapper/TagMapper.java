package kg.core.tag.mapper;

import kg.core.base.exception.NotFoundException;
import kg.core.project.model.Project;
import kg.core.project.repository.ProjectRepository;
import kg.core.tag.dtos.TagDto;
import kg.core.tag.model.Tag;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TagMapper {

    @Autowired
    protected ProjectRepository projectRepository;

    @Mapping(target = "project", source = "projectId", qualifiedByName = "projectIdToProject")
    public abstract Tag toEntity(TagDto dto);

    @Mapping(target = "projectId", source = "project.id")
    public abstract TagDto toDto(Tag entity);

    @Mapping(target = "project", source = "projectId", qualifiedByName = "projectIdToProject")
    public abstract Tag update(TagDto dto, @MappingTarget Tag entity);

    public abstract List<TagDto> toDtos(List<Tag> entities);

    @Named("projectIdToProject")
    protected Project projectIdToProject(Long projectId) {
        if (projectId == null) return null;
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект не найден"));
    }

}
