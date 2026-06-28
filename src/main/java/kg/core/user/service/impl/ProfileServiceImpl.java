package kg.core.user.service.impl;

import kg.core.auth.dtos.CompleteProfileRequest;
import kg.core.user.model.User;
import kg.core.user.service.ProfileService;
import kg.core.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfileService {

    UserService userService;

    @Override
    @Transactional
    public void updatePhone(User currentUser, CompleteProfileRequest request) {
        if (userService.existsByPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already in use");
        }

        currentUser.setPhoneNumber(request.phoneNumber());
        userService.save(currentUser);
    }
}
