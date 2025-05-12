package com.example;

import com.example.shopapp.ui.LoginForm;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting Shop Quan Ao Application...");
        
        try {
            // Initialize default data if needed
            initializeData();
            
            // Launch the application with the login form
            SwingUtilities.invokeLater(() -> {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    private static void initializeData() {
        try {
            // Initialize database with default data
            com.example.shopapp.utils.DatabaseInitializer initializer = new com.example.shopapp.utils.DatabaseInitializer();
            initializer.initialize();
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}