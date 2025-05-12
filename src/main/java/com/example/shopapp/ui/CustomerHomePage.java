package com.example.shopapp.ui;

import com.example.shopapp.entity.Booking;
import com.example.shopapp.entity.Product;
import com.example.shopapp.entity.User;
import com.example.shopapp.service.ProductService;
import com.example.shopapp.service.UserService;
import com.example.shopapp.utils.UIUtils;
import com.example.shopapp.service.BookingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class CustomerHomePage extends JFrame {
    private ProductService productService;
    private BookingService bookingService;
    
    private User currentUser;
    
    // UI Components
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton viewOrdersButton;
    private JButton refreshButton;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addToCartButton;
    private JLabel statusLabel;
    private JPanel statusPanel;
    private JLabel userInfoLabel;
    private JButton logoutButton;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color SECONDARY_COLOR = new Color(66, 165, 245);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TABLE_HEADER_BG = new Color(25, 118, 210);
    private final Color TABLE_HEADER_FG = Color.WHITE;
    private final Color TABLE_ALTERNATE_ROW = new Color(240, 248, 255);
    
    
    public CustomerHomePage(User user) {
        this.currentUser = user;
        productService = new ProductService();
        bookingService = new BookingService();
        UIUtils.setupLookAndFeel();
        initializeUI();
        loadProductData();
    }

    private void initializeUI() {
        // Thiết lập thuộc tính cho frame
        setTitle("Shop Management - Customer Portal");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        // Set background color
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Tạo panel chính với border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Tạo header panel với tiêu đề ứng dụng
        JPanel headerPanel = createHeaderPanel();
        
        // Tạo panel tìm kiếm và điều hướng
        JPanel topPanel = createTopPanel();
        
        // Tạo bảng sản phẩm
        JScrollPane scrollPane = createProductTable();
        
        // Tạo panel cho các nút
        JPanel buttonPanel = createButtonPanel();
        
        // Tạo status bar
        statusPanel = createStatusPanel();
        
        // Thêm các component vào panel chính
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add button panel to the center panel
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Put status panel at the bottom
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Thiết lập listener cho các nút
        setupActionListeners();
        
        // Thiết lập panel chính cho frame
        setContentPane(mainPanel);
    }
    
   
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left side - Title and subtitle
        JLabel titleLabel = new JLabel("Customer Shop Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JLabel subtitleLabel = new JLabel("Browse and Purchase Products");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.setBackground(PRIMARY_COLOR);
        labelPanel.add(titleLabel);
        labelPanel.add(subtitleLabel);
        
        headerPanel.add(labelPanel, BorderLayout.WEST);
        
        // Right side - User info and logout button
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(PRIMARY_COLOR);
        
        String userInfo = "Welcome, " + 
                         (currentUser != null ? currentUser.getFullName() : "Guest");
        userInfoLabel = new JLabel(userInfo);
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userInfoLabel.setToolTipText("Click to edit account information");
        
        // Add mouse listener to open account info form when clicked
        userInfoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showUserProfileForm();
            }
            
            // Add hover effect
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                userInfoLabel.setForeground(new Color(255, 255, 150));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                userInfoLabel.setForeground(Color.WHITE);
            }
        });
        
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setForeground(PRIMARY_COLOR);
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(new CompoundBorder(
            new LineBorder(Color.WHITE, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        userPanel.add(userInfoLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(logoutButton);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel searchLabel = new JLabel("Search Products: ");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(new CompoundBorder(
            new LineBorder(SECONDARY_COLOR, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        
        searchButton = UIUtils.createStyledButton("Search", PRIMARY_COLOR);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Add button to view orders
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setBackground(BACKGROUND_COLOR);
        
        viewOrdersButton = UIUtils.createStyledButton("View Orders", SECONDARY_COLOR);
        
        navPanel.add(viewOrdersButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(navPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    
    private JScrollPane createProductTable() {
        // Table model with column names
        String[] columns = {"ID", "Name", "Description", "Price", "Size", "Color", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        productTable.setRowHeight(30);
        productTable.setShowGrid(true);
        productTable.setGridColor(new Color(230, 230, 230));
        
        
        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
        header.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        
        productTable.getColumnModel().getColumn(0).setMaxWidth(60);  // ID Column
        productTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        productTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Description
        productTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Price
        productTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Size
        productTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Color
        productTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Quantity
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        
        
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                                                          boolean isSelected, boolean hasFocus, 
                                                          int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALTERNATE_ROW);
                }
                
                return comp;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        return scrollPane;
    }
    
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        addToCartButton = UIUtils.createStyledButton("Buy Now", PRIMARY_COLOR);
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 14)); // Larger font
        addToCartButton.setPreferredSize(new Dimension(150, 40)); // Make button larger
        
        refreshButton = UIUtils.createStyledButton("Refresh", SECONDARY_COLOR);
        
        buttonPanel.add(addToCartButton);
        buttonPanel.add(Box.createHorizontalStrut(20)); 
        buttonPanel.add(refreshButton);
        
        return buttonPanel;
    }
    
    
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        
        return statusPanel;
    }

    
    private void setupActionListeners() {
        // Logout button
        logoutButton.addActionListener(e -> {
            updateStatus("Logging out...");
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            dispose();
        });
        
        // View orders button
        viewOrdersButton.addActionListener(e -> {
            updateStatus("Opening your orders...");
            BookingListForm bookingListForm = new BookingListForm(this, bookingService);
            bookingListForm.setVisible(true);
            updateStatus("Ready");
        });
        
        // Refresh button
        refreshButton.addActionListener(e -> {
            updateStatus("Refreshing product data...");
            loadProductData();
            updateStatus("Product data refreshed successfully");
        });
        
        // Search button
        searchButton.addActionListener(e -> searchProducts());
        
        // Search field enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts();
                }
            }
        });
        
        // Buy Now button
        addToCartButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a product to purchase", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            String productName = (String) tableModel.getValueAt(selectedRow, 1);
            double price = Double.parseDouble(tableModel.getValueAt(selectedRow, 3).toString());
            
            // Ask for quantity
            String quantityStr = JOptionPane.showInputDialog(this, 
                "Enter quantity for " + productName + ":", 
                "Purchase Quantity", 
                JOptionPane.QUESTION_MESSAGE);
                
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                return; // User cancelled
            }
            
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter a valid quantity (greater than 0)",
                        "Invalid Quantity",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check available stock
                int availableStock = (int) tableModel.getValueAt(selectedRow, 6);
                if (quantity > availableStock) {
                    JOptionPane.showMessageDialog(this,
                        "Not enough stock available. Maximum available: " + availableStock,
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Confirm purchase
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Confirm purchase of " + quantity + " x " + productName + "\n" +
                    "Total price: $" + (price * quantity),
                    "Confirm Purchase",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    // Create direct booking
                    boolean success = createDirectBooking(productId, quantity);
                    
                    if (success) {
                        updateStatus("Purchased " + quantity + " x " + productName);
                        JOptionPane.showMessageDialog(this,
                            "Your purchase was successful!\n" +
                            "Product: " + productName + "\n" +
                            "Quantity: " + quantity + "\n" +
                            "Total: $" + (price * quantity),
                            "Purchase Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                            
                        // Refresh product data to update stock
                        loadProductData();
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for quantity",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    
    private void loadProductData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get products from service
        List<Product> products = productService.getAllProducts();
        
        // Add products to table
        for (Product product : products) {
            Object[] row = {
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getSize(),
                product.getColor(),
                product.getQuantity()
            };
            tableModel.addRow(row);
        }
        
        updateStatus("Loaded " + products.size() + " products");
    }
    
    
    private void searchProducts() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadProductData();
            return;
        }
        
        updateStatus("Searching for products containing '" + searchTerm + "'...");
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get filtered products
        List<Product> products = productService.searchProducts(searchTerm);
        
        // Add products to table
        for (Product product : products) {
            Object[] row = {
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getSize(),
                product.getColor(),
                product.getQuantity()
            };
            tableModel.addRow(row);
        }
        
        updateStatus("Found " + products.size() + " products matching '" + searchTerm + "'");
    }
    
   
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    
    private boolean createDirectBooking(int productId, int quantity) {
        try {
            // Create a booking item for the product
            BookingService.BookingItem item = new BookingService.BookingItem(productId, quantity);
            List<BookingService.BookingItem> items = new ArrayList<>();
            items.add(item);
            
            // Create the booking
            Booking booking = bookingService.createBooking(currentUser.getUserId(), items);
            
            return booking != null;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error creating booking: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Shows user profile form for viewing and editing account information
     */
    private void showUserProfileForm() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, 
                "No user information available", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create and display user profile form
        UserProfileForm profileForm = new UserProfileForm(this, new UserService(), currentUser);
        profileForm.setVisible(true);
    }
    
    /**
     * Update user information displayed in the UI
     * 
     * @param user The updated user object
     */
    public void updateUserInfo(User user) {
        if (user == null) {
            return;
        }
        
        // Update current user
        this.currentUser = user;
        
        // Update user info label
        userInfoLabel.setText("Welcome, " + user.getFullName());
    }
}
