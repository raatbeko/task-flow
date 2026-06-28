package kg.core.auth.service;

import kg.core.auth.dtos.AuthResponse;
import kg.core.auth.dtos.LoginRequest;
import kg.core.auth.dtos.RegisterRequest;
import kg.core.security.jwt.JwtService;
import kg.core.user.model.AuthProvider;
import kg.core.user.model.Role;
import kg.core.user.model.User;
import kg.core.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserService userService;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validate(request);

        User user = new User();
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPhoneNumber(request.phoneNumber());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.LOCAL);

        User saved = userService.save(user);
        String accessToken = jwtService.generateToken(saved);
        String refreshToken = jwtService.generateRefreshToken(saved);

        return new AuthResponse(accessToken, refreshToken, saved.getId(), saved.getEmail(), saved.getUsername());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userService.findByEmailOrUsername(request.identifier())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        var authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                request.password()
        );
        authenticationManager.authenticate(authToken);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken, user.getId(), user.getEmail(), user.getUsername());
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(String refreshToken) {
        String identifier = jwtService.extractUsername(refreshToken);
        User user = userService.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponse(newAccessToken, newRefreshToken, user.getId(), user.getEmail(), user.getUsername());
    }

    private void validate(RegisterRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userService.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already in use");
        }
        if (userService.existsByPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already in use");
        }
    }
}
