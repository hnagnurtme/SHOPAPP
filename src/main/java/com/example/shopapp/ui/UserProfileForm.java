package com.example.shopapp.ui;

import com.example.shopapp.entity.User;
import com.example.shopapp.service.UserService;
import com.example.shopapp.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class UserProfileForm extends JDialog {
    private UserService userService;
    private User currentUser;
    private JFrame parentFrame;
    
    // UI Components
    private JTextField usernameField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel statusLabel;
    private JButton saveButton;
    private JButton cancelButton;
    
    // Colors
    private final Color PRIMARY_COLOR = UIUtils.PRIMARY_COLOR;
    private final Color SECONDARY_COLOR = UIUtils.SECONDARY_COLOR;
    private final Color BACKGROUND_COLOR = UIUtils.BACKGROUND_COLOR;
    
    
    public UserProfileForm(JFrame parent, UserService userService, User user) {
        super(parent, "Account Information", true);
        this.parentFrame = parent;
        this.userService = userService;
        this.currentUser = user;
        
        setupUI();
        loadUserData();
    }
    
    
    private void setupUI() {
        // Set dialog properties
        setSize(450, 750);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Create status panel
        JPanel statusPanel = createStatusPanel();
        
        // Add to main panel
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set content pane
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);
    }
    
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Add title
        JLabel titleLabel = new JLabel("Account Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        // Add subtitle
        JLabel subtitleLabel = new JLabel("View and update your account details");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.setBackground(BACKGROUND_COLOR);
        labelPanel.add(titleLabel);
        labelPanel.add(subtitleLabel);
        
        // Add icon
        JLabel iconLabel = new JLabel(UIUtils.createIcon(getClass(), "/icons/user.png", 48, 48));
        
        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(labelPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Profile Information Section
        JLabel sectionLabel = new JLabel("Profile Information");
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionLabel.setForeground(PRIMARY_COLOR);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Username field (read-only)
        JLabel usernameLabel = UIUtils.createStyledLabel("Username (cannot be changed)", true);
        usernameField = UIUtils.createStyledTextField();
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(240, 240, 240));
        
        // Full Name field
        JLabel fullNameLabel = UIUtils.createStyledLabel("Full Name", true);
        fullNameField = UIUtils.createStyledTextField();
        
        // Email field
        JLabel emailLabel = UIUtils.createStyledLabel("Email", true);
        emailField = UIUtils.createStyledTextField();
        
        
        // Current Password field
        JLabel currentPasswordLabel = UIUtils.createStyledLabel("Current Password", true);
        currentPasswordField = UIUtils.createStyledPasswordField();
        
        // New Password field
        JLabel newPasswordLabel = UIUtils.createStyledLabel("New Password", true);
        newPasswordField = UIUtils.createStyledPasswordField();
        
        // Confirm Password field
        JLabel confirmPasswordLabel = UIUtils.createStyledLabel("Confirm New Password", true);
        confirmPasswordField = UIUtils.createStyledPasswordField();
        
        // Add components to form panel
        formPanel.add(sectionLabel);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(fullNameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(fullNameField);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(25));
        
        formPanel.add(currentPasswordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(currentPasswordField);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(newPasswordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(newPasswordField);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(confirmPasswordField);
        
        return formPanel;
    }
    
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        cancelButton = UIUtils.createStyledButton("Cancel", SECONDARY_COLOR);
        saveButton = UIUtils.createStyledButton("Save Changes", PRIMARY_COLOR);
        
        // Add action listeners
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveUserData());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(saveButton);
        
        return buttonPanel;
    }
    
    /**
     * Create status panel for displaying messages
     */
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(PRIMARY_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        return statusPanel;
    }
    
    private void loadUserData() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            fullNameField.setText(currentUser.getFullName());
            emailField.setText(currentUser.getEmail());
            updateStatus("User information loaded");
        } else {
            updateStatus("Error: No user information available");
        }
    }
    
    
    private void saveUserData() {
        // Validate fields
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Full name and email are required fields.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if password change is requested
        boolean changePassword = !currentPassword.isEmpty() || 
                                !newPassword.isEmpty() || 
                                !confirmPassword.isEmpty();
        
        if (changePassword) {
            // Validate current password
            if (currentPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter your current password to change it.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate new password
            if (newPassword.isEmpty() || newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this,
                    "New password must be at least 6 characters long.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate password match
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "New password and confirmation do not match.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verify current password is correct
            if (!userService.verifyPassword(currentUser.getUsername(), currentPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Current password is incorrect.",
                    "Authentication Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        try {
            updateStatus("Saving changes...");
            
            // Update user object
            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            
            // Update user in database
            boolean success = false;
            
            if (changePassword) {
                success = userService.updateUserWithPassword(
                    currentUser.getUserId(), 
                    fullName, 
                    email, 
                    newPassword
                );
            } else {
                success = userService.updateUser(
                    currentUser.getUserId(), 
                    fullName, 
                    email
                );
            }
            
            if (success) {
                updateStatus("Changes saved successfully");
                JOptionPane.showMessageDialog(this,
                    "Your account information has been updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Update the userInfoLabel in HomePage if it's the parent
                if (parentFrame instanceof HomePage) {
                    HomePage homePage = (HomePage) parentFrame;
                    homePage.updateUserInfo(currentUser);
                }
                
                dispose();
            } else {
                updateStatus("Error saving changes");
                JOptionPane.showMessageDialog(this,
                    "Failed to update account information.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            updateStatus("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "An error occurred: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
   
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
}
