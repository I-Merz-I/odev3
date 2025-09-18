package com.pdf3.odev3.controller;

import com.pdf3.odev3.model.Role;
import com.pdf3.odev3.model.User;
import com.pdf3.odev3.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // REGISTER → yeni kullanıcı kaydı
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // Admin mi, yoksa normal user mı?
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            Role adminRole = new Role();
            adminRole.setId(1L); // admin
            user.setRole(adminRole);
        } else {
            Role userRole = new Role();
            userRole.setId(2L); // user
            user.setRole(userRole);
        }

        return userRepository.save(user);
    }

    // LOGIN → kullanıcı adı ve şifre kontrolü
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginUser) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(loginUser.getUsername()) &&
                        u.getPassword().equals(loginUser.getPassword()))
                .findFirst()
                .map(u -> ResponseEntity.ok("Login successful! Role: " + u.getRole().getName()))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
}
