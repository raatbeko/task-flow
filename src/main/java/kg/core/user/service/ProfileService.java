package kg.core.user.service;

import kg.core.auth.dtos.CompleteProfileRequest;
import kg.core.user.model.User;

public interface ProfileService {

    void updatePhone(User currentUser, CompleteProfileRequest request);
}
