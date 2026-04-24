package com.amazon.clone;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Cart findByUserAndProduct(User user, Product product);
}