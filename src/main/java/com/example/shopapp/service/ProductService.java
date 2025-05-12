package com.example.shopapp.service;

import com.example.shopapp.dao.ProductDAO;
import com.example.shopapp.entity.Product;
import java.math.BigDecimal;
import java.util.List;


public class ProductService {
    private ProductDAO productDAO;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
    }
    
    
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    
    public Product getProductById(int productId) {
        return productDAO.getById(productId);
    }
    
    
    public List<Product> searchProducts(String keyword) {
        return productDAO.searchProducts(keyword);
    }
    
    
    public Product createProduct(String productName, String description, BigDecimal price,
                                String size, String color, int quantity) {
        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(price);
        product.setSize(size);
        product.setColor(color);
        product.setQuantity(quantity);
        
        if (productDAO.save(product)) {
            return product;
        } else {
            return null;
        }
    }
    
    
    public Product updateProduct(int productId, String productName, String description, 
                                BigDecimal price, String size, String color, int quantity) {
        Product product = productDAO.getById(productId);
        if (product != null) {
            if (productName != null && !productName.isEmpty()) {
                product.setProductName(productName);
            }
            
            if (description != null) {
                product.setDescription(description);
            }
            
            if (price != null) {
                product.setPrice(price);
            }
            
            if (size != null) {
                product.setSize(size);
            }
            
            if (color != null) {
                product.setColor(color);
            }
            
            if (quantity >= 0) {
                product.setQuantity(quantity);
            }
            
            if (productDAO.update(product)) {
                return product;
            }
        }
        return null;
    }
    
    
    public boolean updateQuantity(int productId, int quantity) {
        if (quantity < 0) {
            return false;
        }
        return productDAO.updateQuantity(productId, quantity);
    }
    
    
    public boolean deleteProduct(int productId) {
        return productDAO.delete(productId);
    }
    
    
    public boolean addProduct(Product product) {
        return productDAO.save(product);
    }
    
    
    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }
    
    
    public List<Product> searchProductsByName(String name) {
        return productDAO.searchProductsByName(name);
    }
    
    public int countProducts() {
        List<Product> products = getAllProducts();
        return products != null ? products.size() : 0;
    }
}
