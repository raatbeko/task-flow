package kg.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        
        // Подробное логирование в консоль
        log.error("═══════════════════════════════════════════════════════════");
        log.error("🔒 AUTHENTICATION ERROR");
        log.error("═══════════════════════════════════════════════════════════");
        log.error("📍 URI: {} {}", request.getMethod(), request.getRequestURI());
        log.error("🔗 Full URL: {}", request.getRequestURL());
        log.error("❓ Query String: {}", request.getQueryString());
        log.error("🌐 Remote Address: {}", request.getRemoteAddr());
        log.error("📋 Authorization Header: {}", maskToken(request.getHeader("Authorization")));
        log.error("🔑 Exception Type: {}", authException.getClass().getSimpleName());
        log.error("💬 Exception Message: {}", authException.getMessage());
        log.error("═══════════════════════════════════════════════════════════");
        
        // Формируем JSON ответ
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", authException.getMessage() != null 
                ? authException.getMessage() 
                : "Authentication required");
        errorResponse.put("path", request.getRequestURI());
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
    
    private String maskToken(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return "[NOT PROVIDED]";
        }
        if (authHeader.startsWith("Bearer ") && authHeader.length() > 20) {
            return authHeader.substring(0, 15) + "..." + authHeader.substring(authHeader.length() - 5);
        }
        return "[INVALID FORMAT]";
    }
}
