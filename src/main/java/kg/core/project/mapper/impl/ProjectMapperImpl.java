package kg.core.project.mapper.impl;

import kg.core.project.dtos.ProjectDto;
import kg.core.project.mapper.ProjectMapper;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectStatus;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectMapperImpl implements ProjectMapper {


    UserProvider currentlyUser;

    public ProjectMapperImpl(UserProvider currentlyUser) {
        this.currentlyUser = currentlyUser;
    }

    @Override
    public ProjectDto toDto(Project entity) {
        return new ProjectDto(
                entity.getName(),
                entity.getDescription()
        );
    }

    @Override
    public Project toEntity(ProjectDto dto) {
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setOwner(currentlyUser.getCurrentUser());
        project.setStatus(ProjectStatus.ACTIVE);
        return project;
    }

    @Override
    public Project update(ProjectDto dto, Project entity) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        return entity;
    }

    @Override
    public List<ProjectDto> toDtos(List<Project> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> toEntities(List<ProjectDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
