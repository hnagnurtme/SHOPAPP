package com.example.shopapp.ui;

import com.example.shopapp.entity.User;
import com.example.shopapp.service.UserService;
import com.example.shopapp.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;


public class SignupForm extends JFrame {
    private UserService userService;
    
    // UI Components
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signupButton;
    private JButton loginButton;
    private JLabel statusLabel;
    
    // Colors - Using shared colors from UIUtils
    private final Color PRIMARY_COLOR = UIUtils.PRIMARY_COLOR;
    private final Color ACCENT_COLOR = UIUtils.ACCENT_COLOR;
    private final Color BACKGROUND_COLOR = UIUtils.BACKGROUND_COLOR;

    
    public SignupForm() {
        userService = new UserService();
        setupUI();
    }
    
   
    private void setupUI() {
       
        UIUtils.setupLookAndFeel();
    
        setTitle("Shop Management - Sign Up");
        setSize(450, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Logo and Title
        JPanel headerPanel = createHeaderPanel();
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Links panel
        JPanel linksPanel = createLinksPanel();
        
        // Status panel
        JPanel statusPanel = createStatusPanel();
        
        // Add components to main panel
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(linksPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(statusPanel);
        
        // Add main panel to frame
        getContentPane().setBackground(BACKGROUND_COLOR);
        setContentPane(mainPanel);
        
        // Setup action listeners
        setupActionListeners();
    }
    
   
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add Title
        JLabel titleLabel = new JLabel("Shop Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add subtitle
        JLabel subtitleLabel = new JLabel("Create a new account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        return headerPanel;
    }
    
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username field
        JLabel usernameLabel = UIUtils.createStyledLabel("Username *", true);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = UIUtils.createStyledTextField();
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Email field
        JLabel emailLabel = UIUtils.createStyledLabel("Email *", true);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        emailField = UIUtils.createStyledTextField();
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Full Name field
        JLabel fullNameLabel = UIUtils.createStyledLabel("Full Name *", true);
        fullNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fullNameField = UIUtils.createStyledTextField();
        fullNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Password field
        JLabel passwordLabel = UIUtils.createStyledLabel("Password *", true);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = UIUtils.createStyledPasswordField();
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Confirm Password field
        JLabel confirmPasswordLabel = UIUtils.createStyledLabel("Confirm Password *", true);
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        confirmPasswordField = UIUtils.createStyledPasswordField();
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Signup Button
        signupButton = UIUtils.createStyledButton("Sign Up", ACCENT_COLOR);
        signupButton.setFont(new Font("Arial", Font.BOLD, 14)); // Make it slightly larger than default
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        signupButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add components to form panel
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(fullNameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(fullNameField);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(signupButton);
        
        return formPanel;
    }
    
    
    private JPanel createLinksPanel() {
        JPanel linksPanel = new JPanel();
        linksPanel.setLayout(new BoxLayout(linksPanel, BoxLayout.Y_AXIS));
        linksPanel.setBackground(BACKGROUND_COLOR);
        linksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel loginLabel = UIUtils.createStyledLabel("Already have an account?", false);
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Smaller font
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setForeground(PRIMARY_COLOR);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginPanel.add(loginLabel);
        loginPanel.add(loginButton);
        
        linksPanel.add(loginPanel);
        
        return linksPanel;
    }
    
   
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(BACKGROUND_COLOR);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        
        return statusPanel;
    }
    
   
    private void setupActionListeners() {
        // Signup button action
        signupButton.addActionListener(e -> performSignup());
        
        // Login button action
        loginButton.addActionListener(e -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            dispose(); // Close the signup form
        });
    }
    
   
    private void performSignup() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || fullName.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            UIUtils.showStatus(statusLabel, "All fields marked with * are required", false);
            return;
        }
        
        if (!isValidEmail(email)) {
            UIUtils.showStatus(statusLabel, "Invalid email format", false);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            UIUtils.showStatus(statusLabel, "Passwords do not match", false);
            return;
        }
        
        if (password.length() < 6) {
            UIUtils.showStatus(statusLabel, "Password must be at least 6 characters long", false);
            return;
        }
        
        // Default role ID for customers (role_id = 2)
        int roleId = 2;
        
        // Try to register the user
        User user = userService.registerUser(username, password, email, fullName, roleId);
        
        if (user != null) {
            UIUtils.showStatus(statusLabel, "Registration successful! Redirecting to login...", true);
            
            // Short delay before opening the login page
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Go to login page
                    LoginForm loginForm = new LoginForm();
                    loginForm.setVisible(true);
                    dispose(); // Close the signup form
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            UIUtils.showStatus(statusLabel, "Username already exists or registration failed", false);
        }
    }
    
    
    private boolean isValidEmail(String email) {
        // Simple email validation pattern
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
     
}
