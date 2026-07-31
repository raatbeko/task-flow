package kg.core.project.mapper;

import kg.core.project.dtos.ProjectResponse;
import kg.core.project.dtos.projectRequest;
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
    public abstract Project toEntity(projectRequest dto);

    @Mapping(target = "ownerId", source = "owner.id")
    public abstract ProjectResponse toResponse(Project entity);


    @AfterMapping
    protected void setOwnerAndStatus(@MappingTarget Project project) {
        if (project.getId() == null) {
            project.setOwner(userProvider.getCurrentUser());
            project.setStatus(ProjectStatus.ACTIVE);
        }
    }

    public abstract projectRequest toDto(Project entity);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract Project update(projectRequest dto, @MappingTarget Project entity);

    public abstract List<projectRequest> toDtos(List<Project> entities);

    public abstract List<Project> toEntities(List<projectRequest> dtos);
}
