package kg.core.project.service.impl;

import kg.core.base.service.impl.DefaultCrudService;
import kg.core.project.service.ProjectService;
import kg.core.user.model.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectServiceImpl extends DefaultCrudService<User, Long> implements ProjectService {


    protected ProjectServiceImpl(JpaRepository<User, Long> repository) {
        super(repository);
    }
}
