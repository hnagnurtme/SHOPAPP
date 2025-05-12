package com.example.shopapp.service;

import com.example.shopapp.dao.UserDAO;
import com.example.shopapp.entity.User;
import java.util.List;


public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    
    public User getUserById(int userId) {
        return userDAO.getById(userId);
    }
    
    
    public User getUserByUsername(String username) {
        return userDAO.getByUsername(username);
    }
    
    
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
        user.setPassword(password);
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
    
    
    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }
    
    
    public User login(String username, String password) {
        User user = userDAO.getByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        
        return null;
    }
    
    public int countUsers() {
        List<User> users = getAllUsers();
        return users != null ? users.size() : 0;
    }
    
    
    public boolean verifyPassword(String username, String password) {
        User user = userDAO.getByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }
    
    
    public boolean updateUser(int userId, String fullName, String email) {
        User user = userDAO.getById(userId);
        if (user == null) {
            return false;
        }
        
        user.setFullName(fullName);
        user.setEmail(email);
        
        return userDAO.update(user);
    }
    
    
    public boolean updateUserWithPassword(int userId, String fullName, String email, String newPassword) {
        User user = userDAO.getById(userId);
        if (user == null) {
            return false;
        }
        
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(newPassword);
        
        return userDAO.update(user);
    }
}
