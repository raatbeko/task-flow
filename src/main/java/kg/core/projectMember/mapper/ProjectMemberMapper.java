package kg.core.projectMember.mapper;

import kg.core.projectMember.dtos.ProjectMemberResponse;
import kg.core.projectMember.model.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "email", source = "user.email")
    ProjectMemberResponse toResponse(ProjectMember projectMember);

}
