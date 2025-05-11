package com.example.shopapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.shopapp.entity.User;
import com.example.shopapp.utils.DatabaseConnection;

/**
 * Data Access Object for User entity
 */
public class UserDAO {
    
    private RoleDAO roleDAO;
    
    public UserDAO() {
        this.roleDAO = new RoleDAO();
    }
    
    /**
     * Get all users from database
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT user_id, username, password, email, full_name, role_id, created_at FROM [User]");
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                // Set role if role_id is not null
                if (rs.getObject("role_id") != null) {
                    user.setRole(roleDAO.getById(rs.getInt("role_id")));
                }
                
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting users: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return users;
    }
    
    /**
     * Get user by ID
     * @param userId ID of the user
     * @return User object, or null if not found
     */
    public User getById(int userId) {
        User user = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT user_id, username, password, email, full_name, role_id, created_at FROM [User] WHERE user_id = ?");
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                // Set role if role_id is not null
                if (rs.getObject("role_id") != null) {
                    user.setRole(roleDAO.getById(rs.getInt("role_id")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by id: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return user;
    }
    
    /**
     * Get user by username
     * @param username Username to search for
     * @return User object, or null if not found
     */
    public User getByUsername(String username) {
        User user = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT user_id, username, password, email, full_name, role_id, created_at FROM [User] WHERE username = ?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                // Set role if role_id is not null
                if (rs.getObject("role_id") != null) {
                    user.setRole(roleDAO.getById(rs.getInt("role_id")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return user;
    }
    
    /**
     * Save a new user to database
     * @param user User to save
     * @return true if successful, false otherwise
     */
    public boolean save(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(
                    "INSERT INTO [User] (username, password, email, full_name, role_id, created_at) VALUES (?, ?, ?, ?, ?, ?)", 
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFullName());
            if (user.getRoleId() > 0) {
                pstmt.setInt(5, user.getRoleId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            pstmt.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
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
     * Update an existing user
     * @param user User to update
     * @return true if successful, false otherwise
     */
    public boolean update(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(
                    "UPDATE [User] SET username = ?, password = ?, email = ?, full_name = ?, role_id = ? WHERE user_id = ?");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFullName());
            if (user.getRoleId() > 0) {
                pstmt.setInt(5, user.getRoleId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            pstmt.setInt(6, user.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    /**
     * Delete a user
     * @param userId ID of the user to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM [User] WHERE user_id = ?");
            pstmt.setInt(1, userId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
}
