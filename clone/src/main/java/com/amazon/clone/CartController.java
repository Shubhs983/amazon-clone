package com.amazon.clone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(username).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (user == null || product == null) {
            return "redirect:/products";
        }

        Cart existing = cartRepository.findByUserAndProduct(user, product);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
            cartRepository.save(existing);
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setProduct(product);
            newCart.setQuantity(1);
            cartRepository.save(newCart);
        }
        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(username).orElse(null);
        List<Cart> cartItems = cartRepository.findByUser(user);

        double total = 0;
        for (Cart item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", String.format("%.2f", total));
        return "cart";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartRepository.deleteById(id);
        return "redirect:/cart";
    }
}