package com.pdf3.odev3.repository;

import com.pdf3.odev3.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
