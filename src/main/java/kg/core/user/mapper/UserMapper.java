package kg.core.user.mapper;

import kg.core.user.dtos.UserResponse;
import kg.core.user.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    List<UserResponse> toResponses(List<User> users);
}
