package com.example.shopapp.ui;

import com.example.shopapp.entity.User;
import com.example.shopapp.service.UserService;
import com.example.shopapp.utils.UIUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Login form for the Shop Application
 */
public class LoginForm extends JFrame {
    private UserService userService;
    
    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JLabel statusLabel;
    
    // Colors - Using shared colors from UIUtils
    private final Color PRIMARY_COLOR = UIUtils.PRIMARY_COLOR;
    private final Color SECONDARY_COLOR = UIUtils.SECONDARY_COLOR;
    private final Color ACCENT_COLOR = UIUtils.ACCENT_COLOR;
    private final Color BACKGROUND_COLOR = UIUtils.BACKGROUND_COLOR;
    private final Color ERROR_COLOR = UIUtils.ERROR_COLOR;
    private final Color SUCCESS_COLOR = UIUtils.SUCCESS_COLOR;

    /**
     * Constructor for the login form
     */
    public LoginForm() {
        userService = new UserService();
        setupUI();
    }
    
    /**
     * Set up the user interface
     */
    private void setupUI() {
        // Setup look and feel
        setupLookAndFeel();
        
        // Setup the JFrame
        setTitle("Shop Management - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(createIcon("/icons/shop.png", 16, 16).getImage());
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
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
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(linksPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(statusPanel);
        
        // Add main panel to frame
        getContentPane().setBackground(BACKGROUND_COLOR);
        setContentPane(mainPanel);
        
        // Setup action listeners
        setupActionListeners();
    }
    
    /**
     * Create the header panel with logo and title
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(createIcon("/icons/shop.png", 80, 80));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add Title
        JLabel titleLabel = new JLabel("Shop Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add subtitle
        JLabel subtitleLabel = new JLabel("Login to continue");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        return headerPanel;
    }
    
    /**
     * Create the form panel with input fields
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username field
        JLabel usernameLabel = UIUtils.createStyledLabel("Username", true);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = UIUtils.createStyledTextField();
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Password field
        JLabel passwordLabel = UIUtils.createStyledLabel("Password", true);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = UIUtils.createStyledPasswordField();
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Login Button
        loginButton = UIUtils.createStyledButton("Login", PRIMARY_COLOR);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14)); // Keep the slightly larger font
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add components to form panel
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(loginButton);
        
        return formPanel;
    }
    
    /**
     * Create the links panel with additional options
     */
    private JPanel createLinksPanel() {
        JPanel linksPanel = new JPanel();
        linksPanel.setLayout(new BoxLayout(linksPanel, BoxLayout.Y_AXIS));
        linksPanel.setBackground(BACKGROUND_COLOR);
        linksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Signup link
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 12));
        signupButton.setForeground(ACCENT_COLOR);
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setFocusPainted(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        signupPanel.add(signupLabel);
        signupPanel.add(signupButton);
        
        linksPanel.add(signupPanel);
        
        return linksPanel;
    }
    
    /**
     * Create the status panel for displaying messages
     */
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(BACKGROUND_COLOR);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        
        return statusPanel;
    }
    
    /**
     * Setup action listeners for interactive components
     */
    private void setupActionListeners() {
        // Login button action
        loginButton.addActionListener(e -> performLogin());
        
        // Enter key in password field triggers login
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        
        // Signup button action
        signupButton.addActionListener(e -> {
            SignupForm signupForm = new SignupForm();
            signupForm.setVisible(true);
            dispose(); // Close the login form
        });
    }
    
    /**
     * Perform the login operation
     */
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", false);
            return;
        }
        
        // Try to login
        User user = userService.login(username, password);
        
        if (user != null) {
            showStatus("Login successful!", true);
            
            // Short delay before opening the appropriate page based on user role (for UI feedback)
            Timer timer = new Timer(800, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Check user role and open appropriate form
                    if (user.getRole() != null && "admin".equalsIgnoreCase(user.getRole().getRoleName())) {
                        // If admin role, open the home page (admin dashboard)
                        HomePage homePage = new HomePage(user);
                        homePage.setVisible(true);
                    } else {
                        // If regular user role, open the customer home page
                        CustomerHomePage customerHomePage = new CustomerHomePage(user);
                        customerHomePage.setVisible(true);
                    }
                    
                    // Close the login form
                    dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showStatus("Invalid username or password", false);
        }
    }
    
    /**
     * Display a status message
     * @param message The message to display
     * @param isSuccess Whether it's a success message
     */
    private void showStatus(String message, boolean isSuccess) {
        UIUtils.showStatus(statusLabel, message, isSuccess);
    }
    
    /**
     * Setup look and feel for the application
     */
    private void setupLookAndFeel() {
        UIUtils.setupLookAndFeel();
    }
    
    /**
     * Create an icon from a resource path
     * @param path Resource path
     * @param width Width of the icon
     * @param height Height of the icon
     * @return ImageIcon object
     */
    private ImageIcon createIcon(String path, int width, int height) {
        return UIUtils.createIcon(getClass(), path, width, height);
    }
}
