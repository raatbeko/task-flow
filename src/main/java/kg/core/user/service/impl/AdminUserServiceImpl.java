package kg.core.user.service.impl;

import kg.core.base.search.PageMapper;
import kg.core.base.search.PageResponse;
import kg.core.user.dtos.UpdateUserRequest;
import kg.core.user.dtos.UserResponse;
import kg.core.user.mapper.UserMapper;
import kg.core.user.model.User;
import kg.core.user.search.UserSearchRequest;
import kg.core.user.service.AdminUserService;
import kg.core.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserServiceImpl implements AdminUserService {

    UserService userService;
    UserMapper userMapper;
    PageMapper pageMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> search(UserSearchRequest searchRequest) {
        Page<User> result = userService.search(searchRequest);
        return pageMapper.toResponse(result, userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userService.find(id);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse edit(Long id, UpdateUserRequest request) {
        User user = userService.find(id);

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userService.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует");
            }
            user.setEmail(request.email());
        }

        if (request.username() != null && !request.username().equals(user.getUsername())) {
            if (userService.existsByUsername(request.username())) {
                throw new IllegalArgumentException("Пользователь с таким username уже существует");
            }
            user.setUsername(request.username());
        }

        if (request.phoneNumber() != null && !request.phoneNumber().equals(user.getPhoneNumber())) {
            if (userService.existsByPhoneNumber(request.phoneNumber())) {
                throw new IllegalArgumentException("Пользователь с таким номером телефона уже существует");
            }
            user.setPhoneNumber(request.phoneNumber());
        }

        if (request.role() != null) {
            user.setRole(request.role());
        }

        if (request.provider() != null) {
            user.setProvider(request.provider());
        }

        User saved = userService.save(user);
        return userMapper.toResponse(saved);
    }

    private boolean containsIgnoreCase(String source, String value) {
        if (source == null || value == null) {
            return false;
        }
        return source.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT));
    }
}
