package kg.core.user.service;

import kg.core.base.service.CrudService;
import kg.core.user.model.User;
import kg.core.user.search.UserSearchRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService extends CrudService<User, Long> {

    Page<User> search(UserSearchRequest request);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailOrUsername(String identifier);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);
}
