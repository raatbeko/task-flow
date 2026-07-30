package kg.core.project.endpoint.impl;

import kg.core.project.dtos.ProjectRequest;
import kg.core.project.dtos.ProjectResponse;
import kg.core.project.dtos.ProjectSearchRequest;
import kg.core.project.endpoint.ProjectEndpoint;
import kg.core.project.mapper.ProjectMapper;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectDocument;
import kg.core.project.repository.ProjectSearchRepository;
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
    ProjectSearchRepository searchRepository;

    @Override
    public ProjectResponse get(Long id) {
        Project response =  service.find(id);
        return mapper.toResponse(response);
    }

    @Override
    public List<ProjectResponse> getAll() {
        List<Project> response = service.findAll();
        return mapper.toResponses(response);
    }

    @Override
    public ProjectResponse create(ProjectRequest request) {
        Project project = mapper.toEntity(request);
        service.save(project);

        ProjectDocument projectDocument = mapper.toDocument(project);
        searchRepository.save(projectDocument);

        return mapper.toResponse(project);
    }

    @Override
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = service.find(id);
        service.save(mapper.update(request, project));

        ProjectDocument projectDocument = mapper.toDocument(project);
        searchRepository.save(projectDocument);
    
        return mapper.toResponse(project);
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

    @Override
    public List<ProjectResponse> search(ProjectSearchRequest searchRequest) {
        List<ProjectDocument> documents = service.search(searchRequest.query(), searchRequest.status());
        return mapper.documentsToDtos(documents);
    }
}
