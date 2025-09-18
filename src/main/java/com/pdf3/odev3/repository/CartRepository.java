package com.pdf3.odev3.repository;

import com.pdf3.odev3.model.Cart;
import com.pdf3.odev3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
}
