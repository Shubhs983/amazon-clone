package com.amazon.clone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Show register page
    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    // Handle registration
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/login";
    }

    // Show login page
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // Handle login
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("user", user.get().getUsername());
            return "redirect:/products";
        }
        model.addAttribute("error", "Invalid email or password!");
        return "login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
