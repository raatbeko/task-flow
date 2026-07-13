package kg.core.project.mapper;

import kg.core.project.dtos.ProjectDto;
import kg.core.project.model.Project;

import java.util.List;

public interface ProjectMapper {

    ProjectDto toDto(Project entity);

    Project toEntity(ProjectDto dto);

    Project update(ProjectDto dto, Project entity);

    List<ProjectDto> toDtos(List<Project> entities);

    List<Project> toEntities(List<ProjectDto> dtos);
}
