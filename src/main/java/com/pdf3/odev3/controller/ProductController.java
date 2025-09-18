package com.pdf3.odev3.controller;

import com.pdf3.odev3.model.Product;
import com.pdf3.odev3.repository.ProductRepository;
import com.pdf3.odev3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repo;
    private final UserService userService;

    public ProductController(ProductRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Operation(summary = "Tüm ürünleri listele", description = "Veritabanındaki tüm ürünleri döner.")
    @GetMapping
    public List<Product> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> detail(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product,
                                    @RequestHeader("username") String username) {
        if (!userService.isAdmin(username)) {
            return ResponseEntity.status(403).body("Forbidden: Only admin can add products");
        }
        return ResponseEntity.ok(repo.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Product product,
                                    @RequestHeader("username") String username) {
        if (!userService.isAdmin(username)) {
            return ResponseEntity.status(403).body("Forbidden: Only admin can update products");
        }
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setPrice(product.getPrice());
                    existing.setDescription(product.getDescription());
                    existing.setStock(product.getStock());
                    existing.setCategory(product.getCategory());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("username") String username) {
        if (!userService.isAdmin(username)) {
            return ResponseEntity.status(403).body("Forbidden: Only admin can delete products");
        }
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok("Product deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
