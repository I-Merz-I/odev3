package com.pdf3.odev3.repository;

import com.pdf3.odev3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
