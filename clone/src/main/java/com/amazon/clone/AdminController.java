package com.amazon.clone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Show admin panel
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        List<Product> products = productRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "admin";
    }

    // Add new product
    @PostMapping("/admin/add")
    public String addProduct(@RequestParam String name,
                             @RequestParam Double price,
                             @RequestParam String imageUrl,
                             @RequestParam Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setImageUrl(imageUrl);
            product.setCategory(category);
            productRepository.save(product);
        }
        return "redirect:/admin";
    }

    // Delete product
    @GetMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin";
    }
}