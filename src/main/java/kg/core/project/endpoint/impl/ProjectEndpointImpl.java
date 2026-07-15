package kg.core.project.endpoint.impl;

import kg.core.project.dtos.ProjectDto;
import kg.core.project.endpoint.ProjectEndpoint;
import kg.core.project.mapper.ProjectMapper;
import kg.core.project.model.Project;
import kg.core.project.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectEndpointImpl implements ProjectEndpoint {

    ProjectService service;
    ProjectMapper mapper;

    @Override
    public ProjectDto get(Long id) {
        Project response =  service.find(id);
        return mapper.toDto(response);
    }

    @Override
    public List<ProjectDto> getAll() {
        List<Project> response =  service.findAll();
        return mapper.toDtos(response);
    }

    @Override
    public ProjectDto create(ProjectDto dto) {
        Project project = mapper.toEntity(dto);
        service.save(project);
        return mapper.toDto(project);
    }

    @Override
    public ProjectDto update(Long id, ProjectDto dto) {
        Project project = service.find(id);
        service.save(mapper.update(dto, project));
        return mapper.toDto(project);
    }

    @Override
    public void archive(Long id) {
        service.archive(id);
    }

    @Override
    public void unarchive(Long id) {
        service.unarchive(id);
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }
}
