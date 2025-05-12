package com.example.shopapp.ui;

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
import java.math.BigDecimal;
import java.util.List;

public class HomePage extends JFrame {
    private ProductService productService;
    private UserService userService;
    private BookingService bookingService;
    
    private User currentUser;
    
    // UI Components
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton customersButton;
    private JButton bookingsButton;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel statusLabel;
    private JPanel statusPanel;
    private JLabel userInfoLabel;
    private JButton logoutButton;
    
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color SECONDARY_COLOR = new Color(66, 165, 245);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TABLE_HEADER_BG = new Color(25, 118, 210);
    private final Color TABLE_HEADER_FG = Color.WHITE;
    private final Color TABLE_ALTERNATE_ROW = new Color(240, 248, 255);
    
    
    public HomePage(User user) {
        this.currentUser = user;
        productService = new ProductService();
        userService = new UserService();
        bookingService = new BookingService();
        UIUtils.setupLookAndFeel();
        initializeUI();
        loadProductData();
    }
    
    
    public HomePage() {
        productService = new ProductService();
        userService = new UserService();
        bookingService = new BookingService();
        UIUtils.setupLookAndFeel();
        initializeUI();
        loadProductData();
    }
    
    
    
    private void initializeUI() {
        setTitle("Shop Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(UIUtils.createIcon(getClass(), getName(), 32, 32).getImage());
        
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
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Thêm panel chính và status bar vào frame
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        // Set up action listeners
        setupActionListeners();
    }
    
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left side - Title and subtitle
        JLabel titleLabel = new JLabel("Shop Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JLabel subtitleLabel = new JLabel("Products Management");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.setBackground(PRIMARY_COLOR);
        labelPanel.add(titleLabel);
        labelPanel.add(subtitleLabel);
        
        headerPanel.add(labelPanel, BorderLayout.WEST);
        
        // Center - Statistics
        JPanel statsPanel = createStatisticsPanel();
        headerPanel.add(statsPanel, BorderLayout.CENTER);
        
        // Right side - User info and logout button
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(PRIMARY_COLOR);
        
        String username = "Guest";
        boolean isAdmin = false;
        
        if (currentUser != null) {
            username = currentUser.getUsername();
            isAdmin = currentUser.getRole() != null && 
                     currentUser.getRole().getRoleName().equalsIgnoreCase("admin");
        }
        
        // User info
        userInfoLabel = new JLabel(isAdmin ? "Admin: " + username : "User: " + username);
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setIcon(UIUtils.createIcon(getClass(), username, WIDTH, HEIGHT));
        
        // Logout button
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
        logoutButton.addActionListener(e -> logout());
        
        userPanel.add(userInfoLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(logoutButton);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(new CompoundBorder(
            new LineBorder(SECONDARY_COLOR, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        searchButton = UIUtils.createStyledButton("Search", SECONDARY_COLOR);
        
        JPanel searchInputPanel = new JPanel(new BorderLayout(5, 0));
        searchInputPanel.setBackground(BACKGROUND_COLOR);
        searchInputPanel.add(searchLabel, BorderLayout.WEST);
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);
        
        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setBackground(BACKGROUND_COLOR);
        
        customersButton = UIUtils.createStyledButton("Customers", PRIMARY_COLOR);
        bookingsButton = UIUtils.createStyledButton("Orders", PRIMARY_COLOR);
        
        
        navPanel.add(customersButton);
        navPanel.add(bookingsButton);
        
        // Kết hợp panel tìm kiếm và điều hướng
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(navPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    
    private JScrollPane createProductTable() {
        String[] columns = {"ID", "Product Name", "Description", "Price", "Size", "Color", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 6) { // ID and Quantity columns
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        productTable.setRowHeight(30);
        productTable.setShowGrid(true);
        productTable.setGridColor(new Color(230, 230, 230));
        
        // Set table header style
        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
        header.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        // Set column widths
        productTable.getColumnModel().getColumn(0).setMaxWidth(60);  // ID Column
        productTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        productTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Description
        productTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Price
        productTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Size
        productTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Color
        productTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Quantity
        
        // Center align ID and quantity columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        
        // Create striped rows effect
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        addButton = UIUtils.createStyledButton("Add Product", SECONDARY_COLOR);
        editButton = UIUtils.createStyledButton("Edit Product", SECONDARY_COLOR);
        deleteButton = UIUtils.createStyledButton("Delete Product", SECONDARY_COLOR);
        refreshButton = UIUtils.createStyledButton("Refresh", SECONDARY_COLOR);
       
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        return buttonPanel;
    }
    
   
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.setBackground(PRIMARY_COLOR);
        
        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(versionLabel, BorderLayout.EAST);
        
        return statusPanel;
    }
    
   
    private void setupActionListeners() {
        // Add product button
        addButton.addActionListener(e -> {
            updateStatus("Opening add product form...");
            ProductAddForm addForm = new ProductAddForm(this, productService);
            addForm.setVisible(true);
            loadProductData();
            updateStatus("Product data refreshed");
        });
        
        // Edit product button
        editButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a product to edit", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            updateStatus("Opening edit form for selected product...");
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            Product product = productService.getProductById(productId);
            
            if (product == null) {
                JOptionPane.showMessageDialog(this, 
                    "Product not found", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ProductEditForm editForm = new ProductEditForm(this, productService, product);
            editForm.setVisible(true);
            loadProductData();
            updateStatus("Product data refreshed");
        });
        
        // Delete product button
        deleteButton.addActionListener(e -> deleteProduct());
        
        // Refresh button
        refreshButton.addActionListener(e -> {
            updateStatus("Refreshing product data...");
            loadProductData();
            updateStatus("Product data refreshed successfully");
        });
        
        // Search button
        searchButton.addActionListener(e -> searchProducts());
        
        // Search on Enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts();
                }
            }
        });
        
        // Customers button
        customersButton.addActionListener(e -> {
            updateStatus("Opening customer list...");
            CustomerListForm customerListForm = new CustomerListForm(this, userService);
            customerListForm.setVisible(true);
            updateStatus("Ready");
        });
        
        // Orders button
        bookingsButton.addActionListener(e -> {
            updateStatus("Opening orders list...");
            BookingListForm bookingListForm = new BookingListForm(this, bookingService);
            bookingListForm.setVisible(true);
            updateStatus("Ready");
        });
    }
    
    
    private void loadProductData() {
        // Xóa dữ liệu hiện có
        tableModel.setRowCount(0);
        
        // Lấy tất cả sản phẩm
        List<Product> products = productService.getAllProducts();
        
        // Thêm sản phẩm vào bảng
        for (Product product : products) {
            Object[] row = {
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice().toString(),
                product.getSize(),
                product.getColor(),
                product.getQuantity()
            };
            tableModel.addRow(row);
        }
        
        updateStatus("Loaded " + products.size() + " products");
    }
    
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a product to delete", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        String productName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete product: " + productName + "?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            try {
                updateStatus("Deleting product...");
                productService.deleteProduct(productId);
                loadProductData();
                updateStatus("Product deleted successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting product: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                updateStatus("Error deleting product");
            }
        }
    }
    
    
    private void searchProducts() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadProductData();
            return;
        }
        
        updateStatus("Searching for products containing '" + searchTerm + "'...");
        
        // Xóa dữ liệu hiện có
        tableModel.setRowCount(0);
        
        // Tìm kiếm sản phẩm
        List<Product> products = productService.searchProductsByName(searchTerm);
        
        // Thêm sản phẩm vào bảng
        for (Product product : products) {
            Object[] row = {
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice().toString(),
                product.getSize(),
                product.getColor(),
                product.getQuantity()
            };
            tableModel.addRow(row);
        }
        
        updateStatus("Found " + products.size() + " matching products");
    }
    
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to log out?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            updateStatus("Logging out...");
            this.dispose();
            
            // Show the login form
            SwingUtilities.invokeLater(() -> {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            });
        }
    }
    
    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        statsPanel.setBackground(PRIMARY_COLOR);
        
        // Get statistics
        int totalCustomers = userService.countUsers();
        int totalProducts = productService.countProducts();
        BigDecimal totalRevenue = bookingService.calculateTotalRevenue();
        
        // Format the revenue with two decimal places
        String formattedRevenue = String.format("%,.2f", totalRevenue);
        
        // Create stat boxes with icons (using text since emoji support varies)
        JPanel customersBox = createStatBox("Total Customers", String.valueOf(totalCustomers));
        JPanel productsBox = createStatBox("Total Products", String.valueOf(totalProducts));
        JPanel revenueBox = createStatBox("Total Revenue", "$" + formattedRevenue);
        
        // Add to panel
        statsPanel.add(customersBox);
        statsPanel.add(productsBox);
        statsPanel.add(revenueBox);
        
        return statsPanel;
    }
    
    private JPanel createStatBox(String title, String value) {
        JPanel boxPanel = new JPanel(new BorderLayout(5, 0));
        boxPanel.setBackground(new Color(255, 255, 255, 40)); 
        boxPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Icon and value
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(""); 
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(Color.WHITE);
        
        topPanel.add(iconLabel);
        topPanel.add(valueLabel);
        

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(220, 220, 220));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        boxPanel.add(topPanel, BorderLayout.CENTER);
        boxPanel.add(titleLabel, BorderLayout.SOUTH);
        
        return boxPanel;
    }
}
