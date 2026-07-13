package kg.core.project.service;

import kg.core.base.service.CrudService;
import kg.core.user.model.User;
import kg.core.user.search.UserSearchRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProjectService extends CrudService<User, Long> {


}
