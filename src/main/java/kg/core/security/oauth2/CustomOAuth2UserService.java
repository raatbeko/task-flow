package kg.core.security.oauth2;

import kg.core.user.model.AuthProvider;
import kg.core.user.model.Role;
import kg.core.user.model.User;
import kg.core.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    UserService userService;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String sub = (String) attributes.get("sub");
        String name = (String) attributes.getOrDefault("name", email);

        User user = userService.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setUsername(name);
                    u.setPhoneNumber(null); // user will fill this later via profile completion
                    u.setRole(Role.USER);
                    u.setProvider(AuthProvider.GOOGLE);
                    u.setProviderId(sub);
                    return userService.save(u);
                });

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                attributes,
                "sub"
        );
    }
}
