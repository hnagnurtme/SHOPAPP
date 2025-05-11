package com.example.shopapp.service;

import com.example.shopapp.dao.ProductDAO;
import com.example.shopapp.entity.Product;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for Product operations
 */
public class ProductService {
    private ProductDAO productDAO;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
    }
    
    /**
     * Get all products from database
     * @return List of Product objects
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    /**
     * Get product by ID
     * @param productId ID of the product
     * @return Product object, or null if not found
     */
    public Product getProductById(int productId) {
        return productDAO.getById(productId);
    }
    
    /**
     * Search products by keyword
     * @param keyword Keyword to search for in name or description
     * @return List of matching products
     */
    public List<Product> searchProducts(String keyword) {
        return productDAO.searchProducts(keyword);
    }
    
    /**
     * Create a new product
     * @param productName Name of the product
     * @param description Description of the product
     * @param price Price of the product
     * @param size Size of the product
     * @param color Color of the product
     * @param quantity Initial quantity in stock
     * @return Product object if successful, null otherwise
     */
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
    
    /**
     * Update an existing product
     * @param productId ID of the product to update
     * @param productName New name (or null to keep existing)
     * @param description New description (or null to keep existing)
     * @param price New price (or null to keep existing)
     * @param size New size (or null to keep existing)
     * @param color New color (or null to keep existing)
     * @param quantity New quantity (or -1 to keep existing)
     * @return Updated Product object if successful, null otherwise
     */
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
    
    /**
     * Update product quantity
     * @param productId ID of the product
     * @param quantity New quantity
     * @return true if successful, false otherwise
     */
    public boolean updateQuantity(int productId, int quantity) {
        if (quantity < 0) {
            return false;
        }
        return productDAO.updateQuantity(productId, quantity);
    }
    
    /**
     * Delete a product
     * @param productId ID of the product to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        return productDAO.delete(productId);
    }
}
