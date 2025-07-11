package com.example.TravelHub.Controllers;

import com.example.TravelHub.Services.SessionService;
import com.example.TravelHub.Services.UserService;
import com.example.TravelHub.Entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {
    private final SessionService sessionService;
    private final UserService userService;

    public UserController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User request) {
        String userId = request.getId();
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
        }
        String token = UUID.randomUUID().toString();
        long expiresIn = 900L;
        sessionService.saveSession(token, userId, expiresIn);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "expires_in", expiresIn));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User request) {
        String username = request.getUsername();
        String password = request.getPassword();
        if (username == null  || password == null  || username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password are required"));
        }
        try {
            User user = userService.registerUser(username, password);
            return ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
