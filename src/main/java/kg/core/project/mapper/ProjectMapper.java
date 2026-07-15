package kg.core.project.mapper;

import kg.core.project.dtos.ProjectDto;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectStatus;
import kg.core.utils.UserProvider;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProjectMapper {

    @Autowired
    protected UserProvider userProvider;

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract Project toEntity(ProjectDto dto);

    @AfterMapping
    protected void setOwnerAndStatus(@MappingTarget Project project) {
        if (project.getId() == null) {
            project.setOwner(userProvider.getCurrentUser());
            project.setStatus(ProjectStatus.ACTIVE);
        }
    }

    public abstract ProjectDto toDto(Project entity);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract Project update(ProjectDto dto, @MappingTarget Project entity);

    public abstract List<ProjectDto> toDtos(List<Project> entities);

    public abstract List<Project> toEntities(List<ProjectDto> dtos);
}
