package com.example.shopapp.service;

import com.example.shopapp.dao.UserDAO;
import com.example.shopapp.entity.User;
import java.util.List;

/**
 * Service class for User operations
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Get all users from database
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * Get user by ID
     * @param userId ID of the user
     * @return User object, or null if not found
     */
    public User getUserById(int userId) {
        return userDAO.getById(userId);
    }
    
    /**
     * Get user by username
     * @param username Username to search for
     * @return User object, or null if not found
     */
    public User getUserByUsername(String username) {
        return userDAO.getByUsername(username);
    }
    
    /**
     * Register a new user
     * @param username Username for the new user
     * @param password Password for the new user
     * @param email Email for the new user
     * @param fullName Full name of the user
     * @param roleId Role ID for the user (optional, can be 0)
     * @return User object if successful, null otherwise
     */
    public User registerUser(String username, String password, String email, String fullName, int roleId) {
        // Check if username or email already exists
        User existingUser = userDAO.getByUsername(username);
        if (existingUser != null) {
            System.out.println("Username already exists");
            return null;
        }
        
        existingUser = userDAO.getByEmail(email);
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In real app, store hashed password
        user.setEmail(email);
        user.setFullName(fullName);
        if (roleId > 0) {
            user.setRoleId(roleId);
        }
        
        if (userDAO.save(user)) {
            return user;
        } else {
            return null;
        }
    }
    
    /**
     * Update user information
     * @param userId ID of the user to update
     * @param fullName New full name (or null to keep existing)
     * @param email New email (or null to keep existing)
     * @param password New password (or null to keep existing)
     * @return Updated User object if successful, null otherwise
     */
    public User updateUser(int userId, String fullName, String email, String password) {
        User user = userDAO.getById(userId);
        if (user != null) {
            if (fullName != null && !fullName.isEmpty()) {
                user.setFullName(fullName);
            }
            
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }
            
            if (password != null && !password.isEmpty()) {
                
                user.setPassword(password);
            }
            
            if (userDAO.update(user)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Delete a user
     * @param userId ID of the user to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }
    
    /**
     * Authenticate a user
     * @param username Username to authenticate
     * @param password Password to check
     * @return User object if authentication successful, null otherwise
     */
    public User login(String username, String password) {
        User user = userDAO.getByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            // In a real application, use password hashing
            return user;
        }
        
        return null;
    }
}
