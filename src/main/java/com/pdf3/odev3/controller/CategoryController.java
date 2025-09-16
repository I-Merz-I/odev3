package com.pdf3.odev3.controller;

import com.pdf3.odev3.model.Category;
import com.pdf3.odev3.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    // GET → tüm kategorileri listele
    @GetMapping
    public List<Category> list() {
        return repo.findAll();
    }

    // POST → yeni kategori ekle
    @PostMapping
    public Category create(@RequestBody Category category) {
        return repo.save(category);
    }
}
