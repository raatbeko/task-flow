package kg.core.project.dtos;


import kg.core.project.model.ProjectStatus;

public record ProjectSearchRequest(
        String query,
        ProjectStatus  status
) {
}
