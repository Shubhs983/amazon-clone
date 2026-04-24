package com.amazon.clone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/products")
    public String getProducts(@RequestParam(required = false) String search,
                              HttpSession session, Model model) {
        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(search);
            model.addAttribute("search", search);
        } else {
            products = productRepository.findAll();
        }

        // Count cart items
        String username = (String) session.getAttribute("user");
        if (username != null) {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                List<Cart> cartItems = cartRepository.findByUser(user);
                model.addAttribute("cartCount", cartItems.size());
            }
        }

        model.addAttribute("products", products);
        return "products";
    }
}