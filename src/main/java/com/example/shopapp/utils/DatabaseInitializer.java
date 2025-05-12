package com.example.shopapp.utils;

import com.example.shopapp.entity.Role;
import com.example.shopapp.entity.User;
import com.example.shopapp.entity.Product;
import com.example.shopapp.service.RoleService;
import com.example.shopapp.service.UserService;
import com.example.shopapp.service.ProductService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseInitializer {
    
    private RoleService roleService;
    private UserService userService;
    private ProductService productService;
    
    public DatabaseInitializer() {
        roleService = new RoleService();
        userService = new UserService();
        productService = new ProductService();
    }
    
    
    public void initialize() {
        initializeRoles();
        initializeAdminUser();
        initializeSampleProducts();
    }
    
   
    private void initializeRoles() {
        if (isTableEmpty("Role")) {
            System.out.println("Initializing roles...");
            
            // Create admin role
            Role adminRole = roleService.createRole("admin");
            if (adminRole != null) {
                System.out.println("Created admin role with ID: " + adminRole.getRoleId());
            }
            
            // Create user role
            Role userRole = roleService.createRole("user");
            if (userRole != null) {
                System.out.println("Created user role with ID: " + userRole.getRoleId());
            }
        }
    }
    
   
    private void initializeAdminUser() {
        if (isTableEmpty("User")) {
            System.out.println("Creating admin user...");
            
            // Get admin role (assuming it's role_id = 1)
            Role adminRole = roleService.getRoleById(1);
            if (adminRole != null) {
                // Create admin user
                User adminUser = userService.registerUser(
                        "admin",
                        "admin123", // In real app, use a secure password
                        "admin@example.com",
                        "System Administrator",
                        adminRole.getRoleId());
                
                if (adminUser != null) {
                    System.out.println("Created admin user: " + adminUser.getUsername());
                }
            }
        }
    }
    
    
    private void initializeSampleProducts() {
        if (isTableEmpty("Product")) {
            System.out.println("Adding sample products...");
            
            // Add sample products
            addSampleProduct("Áo Thun Nam Basic", 
                    "Áo thun nam chất liệu cotton 100%, thoáng mát", 
                    new BigDecimal("150000"), "M", "Đen", 50);
            
            addSampleProduct("Áo Sơ Mi Nữ", 
                    "Áo sơ mi nữ dài tay, form rộng thoải mái", 
                    new BigDecimal("250000"), "S", "Trắng", 30);
            
            addSampleProduct("Quần Jeans Nam Slim Fit", 
                    "Quần jeans nam ôm chân, co giãn tốt", 
                    new BigDecimal("450000"), "32", "Xanh đậm", 25);
            
            addSampleProduct("Váy Xòe Nữ", 
                    "Váy xòe nữ dáng ngắn, phù hợp đi chơi, dạo phố", 
                    new BigDecimal("350000"), "M", "Hồng", 20);
            
            addSampleProduct("Áo Khoác Dù Unisex", 
                    "Áo khoác dù chống nước, chống gió", 
                    new BigDecimal("550000"), "L", "Đen", 15);
        }
    }
    
    
    private void addSampleProduct(String name, String description, BigDecimal price, 
                                String size, String color, int quantity) {
        Product product = productService.createProduct(name, description, price, size, color, quantity);
        if (product != null) {
            System.out.println("Added product: " + product.getProductName());
        }
    }
    
    
    private boolean isTableEmpty(String tableName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isEmpty = true;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT TOP 1 * FROM " + tableName);
            rs = pstmt.executeQuery();
            
            isEmpty = !rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking if table is empty: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return isEmpty;
    }
}
