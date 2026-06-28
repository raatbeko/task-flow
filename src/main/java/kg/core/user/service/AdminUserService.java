package kg.core.user.service;

import kg.core.base.search.PageResponse;
import kg.core.user.dtos.UpdateUserRequest;
import kg.core.user.dtos.UserResponse;
import kg.core.user.search.UserSearchRequest;

public interface AdminUserService {

    PageResponse<UserResponse> search(UserSearchRequest searchRequest);

    UserResponse getById(Long id);

    UserResponse edit(Long id, UpdateUserRequest request);
}
