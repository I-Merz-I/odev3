package com.pdf3.odev3.service;

import com.pdf3.odev3.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public boolean isAdmin(String username) {
        return repo.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(username) &&
                        u.getRole() != null &&
                        "admin".equalsIgnoreCase(u.getRole().getName()));
    }
}
