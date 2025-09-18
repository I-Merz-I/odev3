package com.pdf3.odev3.controller;

import com.pdf3.odev3.model.Category;
import com.pdf3.odev3.repository.CategoryRepository;
import com.pdf3.odev3.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repo;
    private final UserService userService;

    public CategoryController(CategoryRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @GetMapping
    public List<Category> list() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category,
                                    @RequestHeader("username") String username) {
        if (!userService.isAdmin(username)) {
            return ResponseEntity.status(403).body("Forbidden: Only admin can add categories");
        }
        return ResponseEntity.ok(repo.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Category category,
                                    @RequestHeader("username") String username) {
        if (!userService.isAdmin(username)) {
            return ResponseEntity.status(403).body("Forbidden: Only admin can update categories");
        }
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(category.getName());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("username") String username) {
        if (!userService.isAdmin(username)) {
            return ResponseEntity.status(403).body("Forbidden: Only admin can delete categories");
        }
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok("Category deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
