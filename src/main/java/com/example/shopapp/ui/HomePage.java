package com.example.shopapp.ui;

import com.example.shopapp.entity.Product;
import com.example.shopapp.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * HomePage class implements the main user interface using JFrame
 */
public class HomePage extends JFrame {
    private ProductService productService;
    
    // UI Components
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTextField searchField;
    private JButton searchButton;
    
    /**
     * Constructor for HomePage
     */
    public HomePage() {
        productService = new ProductService();
        initializeUI();
        loadProductData();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        // Set frame properties
        setTitle("Cửa Hàng - Quản Lý Sản Phẩm");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Tìm Kiếm");
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Create table
        String[] columns = {"ID", "Tên Sản Phẩm", "Mô Tả", "Giá", "Kích Thước", "Màu Sắc", "Số Lượng"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productTable);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        addButton = new JButton("Thêm Sản Phẩm");
        editButton = new JButton("Sửa Sản Phẩm");
        deleteButton = new JButton("Xóa Sản Phẩm");
        refreshButton = new JButton("Làm Mới");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Add components to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        refreshButton.addActionListener(e -> loadProductData());
        searchButton.addActionListener(e -> searchProducts());
    }
    
    /**
     * Load product data into table
     */
    private void loadProductData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get all products
        List<Product> products = productService.getAllProducts();
        
        // Add products to table
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
    }
    
    /**
     * Add a new product
     */
    private void addProduct() {
        // Create a simple dialog for adding a product
        JDialog dialog = new JDialog(this, "Thêm Sản Phẩm Mới", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create form fields
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField quantityField = new JTextField();
        
        // Add fields to form
        formPanel.add(new JLabel("Tên Sản Phẩm:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Mô Tả:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Kích Thước:"));
        formPanel.add(sizeField);
        formPanel.add(new JLabel("Màu Sắc:"));
        formPanel.add(colorField);
        formPanel.add(new JLabel("Số Lượng:"));
        formPanel.add(quantityField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            try {
                // Create new product
                Product product = new Product();
                product.setProductName(nameField.getText());
                product.setDescription(descField.getText());
                product.setPrice(new BigDecimal(priceField.getText()));
                product.setSize(sizeField.getText());
                product.setColor(colorField.getText());
                product.setQuantity(Integer.parseInt(quantityField.getText()));
                
                // Save product
                productService.addProduct(product);
                
                // Refresh table
                loadProductData();
                
                // Close dialog
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        // Add panels to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    /**
     * Edit selected product
     */
    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        Product product = productService.getProductById(productId);
        
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create a dialog for editing the product
        JDialog dialog = new JDialog(this, "Sửa Sản Phẩm", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create form fields with existing values
        JTextField nameField = new JTextField(product.getProductName());
        JTextField descField = new JTextField(product.getDescription());
        JTextField priceField = new JTextField(product.getPrice().toString());
        JTextField sizeField = new JTextField(product.getSize());
        JTextField colorField = new JTextField(product.getColor());
        JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()));
        
        // Add fields to form
        formPanel.add(new JLabel("Tên Sản Phẩm:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Mô Tả:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Kích Thước:"));
        formPanel.add(sizeField);
        formPanel.add(new JLabel("Màu Sắc:"));
        formPanel.add(colorField);
        formPanel.add(new JLabel("Số Lượng:"));
        formPanel.add(quantityField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            try {
                // Update product
                product.setProductName(nameField.getText());
                product.setDescription(descField.getText());
                product.setPrice(new BigDecimal(priceField.getText()));
                product.setSize(sizeField.getText());
                product.setColor(colorField.getText());
                product.setQuantity(Integer.parseInt(quantityField.getText()));
                
                // Save product
                productService.updateProduct(product);
                
                // Refresh table
                loadProductData();
                
                // Close dialog
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        // Add panels to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    /**
     * Delete selected product
     */
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            try {
                productService.deleteProduct(productId);
                loadProductData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa sản phẩm: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Search products by name
     */
    private void searchProducts() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadProductData();
            return;
        }
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Search products
        List<Product> products = productService.searchProductsByName(searchTerm);
        
        // Add products to table
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
    }
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the homepage
        SwingUtilities.invokeLater(() -> {
            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        });
    }
}
