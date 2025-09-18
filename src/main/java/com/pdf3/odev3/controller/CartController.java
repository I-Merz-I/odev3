package com.pdf3.odev3.controller;

import com.pdf3.odev3.model.Cart;
import com.pdf3.odev3.model.Product;
import com.pdf3.odev3.model.User;
import com.pdf3.odev3.repository.CartRepository;
import com.pdf3.odev3.repository.ProductRepository;
import com.pdf3.odev3.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartController(CartRepository cartRepository,
                          UserRepository userRepository,
                          ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Kullanıcının sepetini listele
    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("username") String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        List<Cart> carts = cartRepository.findByUser(user);
        return ResponseEntity.ok(carts);
    }

    // Sepete ürün ekle
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestHeader("username") String username,
                                       @RequestParam Long productId,
                                       @RequestParam(defaultValue = "1") Integer quantity) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.status(404).body("Product not found");
        }

        Cart cartItem = new Cart(user, product, quantity);
        return ResponseEntity.ok(cartRepository.save(cartItem));
    }

    // Sepetten ürün sil
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id,
                                            @RequestHeader("username") String username) {
        if (!cartRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cartRepository.deleteById(id);
        return ResponseEntity.ok("Removed from cart");
    }
}
