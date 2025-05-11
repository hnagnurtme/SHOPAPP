package com.example.shopapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import com.example.shopapp.entity.Product;
import com.example.shopapp.utils.DatabaseConnection;

/**
 * Data Access Object for Product entity
 */
public class ProductDAO {
    
    /**
     * Get all products from database
     * @return List of Product objects
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT product_id, product_name, description, price, size, color, quantity, created_at FROM Product");
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSize(rs.getString("size"));
                product.setColor(rs.getString("color"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return products;
    }
    
    /**
     * Get product by ID
     * @param productId ID of the product
     * @return Product object, or null if not found
     */
    public Product getById(int productId) {
        Product product = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT product_id, product_name, description, price, size, color, quantity, created_at FROM Product WHERE product_id = ?");
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSize(rs.getString("size"));
                product.setColor(rs.getString("color"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by id: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return product;
    }
    
    /**
     * Search products by name or description
     * @param keyword Keyword to search for
     * @return List of matching products
     */
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(
                    "SELECT product_id, product_name, description, price, size, color, quantity, created_at " +
                    "FROM Product WHERE product_name LIKE ? OR description LIKE ?");
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSize(rs.getString("size"));
                product.setColor(rs.getString("color"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return products;
    }
    
    /**
     * Save a new product to database
     * @param product Product to save
     * @return true if successful, false otherwise
     */
    public boolean save(Product product) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(
                    "INSERT INTO Product (product_name, description, price, size, color, quantity, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)", 
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setString(4, product.getSize());
            pstmt.setString(5, product.getColor());
            pstmt.setInt(6, product.getQuantity());
            pstmt.setTimestamp(7, Timestamp.valueOf(product.getCreatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    product.setProductId(generatedKeys.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving product: " + e.getMessage());
        } finally {
            if (generatedKeys != null) {
                try { generatedKeys.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    /**
     * Update an existing product
     * @param product Product to update
     * @return true if successful, false otherwise
     */
    public boolean update(Product product) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(
                    "UPDATE Product SET product_name = ?, description = ?, price = ?, " +
                    "size = ?, color = ?, quantity = ? WHERE product_id = ?");
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setString(4, product.getSize());
            pstmt.setString(5, product.getColor());
            pstmt.setInt(6, product.getQuantity());
            pstmt.setInt(7, product.getProductId());
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    /**
     * Update product quantity
     * @param productId ID of the product
     * @param quantity New quantity value
     * @return true if successful, false otherwise
     */
    public boolean updateQuantity(int productId, int quantity) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("UPDATE Product SET quantity = ? WHERE product_id = ?");
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating product quantity: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    /**
     * Delete a product
     * @param productId ID of the product to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int productId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM Product WHERE product_id = ?");
            pstmt.setInt(1, productId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    /**
     * Search products by name
     * @param name The name to search for
     * @return List of matching products
     */
    public List<Product> searchProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(
                    "SELECT product_id, product_name, description, price, size, color, quantity, created_at " +
                    "FROM Product WHERE product_name LIKE ?");
            pstmt.setString(1, "%" + name + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSize(rs.getString("size"));
                product.setColor(rs.getString("color"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error searching products by name: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return products;
    }
}
