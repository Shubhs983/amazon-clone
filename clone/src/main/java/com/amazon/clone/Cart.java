package com.amazon.clone;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}