package com.example;

import com.example.shopapp.ui.HomePage;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting Shop Quan Ao Application...");
        
        try {
            // Initialize default data if needed
            initializeData();
            
            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize default data in the database if needed
     */
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