package com.pdf3.odev3.controller;

import com.pdf3.odev3.model.Role;
import com.pdf3.odev3.model.User;
import com.pdf3.odev3.repository.RoleRepository;
import com.pdf3.odev3.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // REGISTER → yeni kullanıcı kaydı
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        Role role;
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            role = roleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Admin role not found"));
        } else {
            role = roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("User role not found"));
        }
        user.setRole(role);
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
