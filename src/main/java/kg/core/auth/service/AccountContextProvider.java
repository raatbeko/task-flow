package kg.core.auth.service;

import kg.core.user.model.User;

public interface AccountContextProvider {
  User getCurrentUser();
}
