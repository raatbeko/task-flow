package kg.core.auth.service.impl;


import kg.core.auth.service.AccountContextProvider;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultAccountContextProvider implements AccountContextProvider {

  private final UserRepository userRepository;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
  public User getCurrentUser() {
    CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.getOne(user.getUser().getId());
  }
}
