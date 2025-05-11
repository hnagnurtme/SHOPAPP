package com.example.shopapp.ui;

import com.example.shopapp.entity.Product;
import com.example.shopapp.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Form thêm sản phẩm mới
 */
public class ProductAddForm extends JDialog {
    private ProductService productService;
    
    // UI Components
    private JTextField nameField;
    private JTextField descField;
    private JTextField priceField;
    private JTextField sizeField;
    private JTextField colorField;
    private JTextField quantityField;
    private JButton saveButton;
    private JButton cancelButton;

    /**
     * Constructor cho ProductAddForm
     * @param parent Component cha
     * @param productService Service xử lý sản phẩm
     */
    public ProductAddForm(JFrame parent, ProductService productService) {
        super(parent, "Thêm Sản Phẩm Mới", true);
        this.productService = productService;
        initializeUI();
    }

    /**
     * Khởi tạo giao diện người dùng
     */
    private void initializeUI() {
        setSize(400, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getParent());
        
        // Tạo form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tạo các trường nhập liệu
        nameField = new JTextField();
        descField = new JTextField();
        priceField = new JTextField();
        sizeField = new JTextField();
        colorField = new JTextField();
        quantityField = new JTextField();
        
        // Thêm các trường vào form
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
        
        // Tạo panel cho các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Thêm action listeners
        saveButton.addActionListener(e -> saveProduct());
        cancelButton.addActionListener(e -> dispose());
        
        // Thêm các panel vào dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Lưu sản phẩm mới
     */
    private void saveProduct() {
        try {
            // Tạo sản phẩm mới
            Product product = new Product();
            product.setProductName(nameField.getText());
            product.setDescription(descField.getText());
            product.setPrice(new BigDecimal(priceField.getText()));
            product.setSize(sizeField.getText());
            product.setColor(colorField.getText());
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            
            // Lưu sản phẩm
            productService.addProduct(product);
            
            // Đóng dialog
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
