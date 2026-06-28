package kg.core.user.service.impl;

import com.querydsl.core.BooleanBuilder;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
import kg.core.user.search.UserSearchRequest;
import kg.core.user.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static kg.core.user.model.QUser.user;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl extends DefaultCrudService<User, Long> implements UserService {

    UserRepository repository;

    protected UserServiceImpl(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> search(UserSearchRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();

        search(request, predicate);

        return repository.findAll(predicate, request);
    }

    private static void search(UserSearchRequest request, BooleanBuilder predicate) {
        if (request.getId() != null) predicate.and(user.id.eq(request.getId()));
        if (request.getEmail() != null) predicate.and(user.email.containsIgnoreCase(request.getEmail()));
        if (request.getUsername() != null) predicate.and(user.username.containsIgnoreCase(request.getUsername()));
        if (request.getPhoneNumber() != null)
            predicate.and(user.phoneNumber.containsIgnoreCase(request.getPhoneNumber()));
        if (request.getRole() != null) predicate.and(user.role.eq(request.getRole()));
        if (request.getProvider() != null) predicate.and(user.provider.eq(request.getProvider()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmailOrUsername(String identifier) {
        return repository.findByEmailOrUsername(identifier, identifier);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }
}
