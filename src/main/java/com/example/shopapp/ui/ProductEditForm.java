package com.example.shopapp.ui;

import com.example.shopapp.entity.Product;
import com.example.shopapp.service.ProductService;
import com.example.shopapp.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Form sửa thông tin sản phẩm
 */
public class ProductEditForm extends JDialog {
    private ProductService productService;
    private Product product;
    
    
    private JTextField nameField;
    private JTextField descField;
    private JTextField priceField;
    private JTextField sizeField;
    private JTextField colorField;
    private JTextField quantityField;
    private JButton saveButton;
    private JButton cancelButton;

    
    public ProductEditForm(JFrame parent, ProductService productService, Product product) {
        super(parent, "Sửa Sản Phẩm", true);
        this.productService = productService;
        this.product = product;
        initializeUI();
        UIUtils.setupLookAndFeel();
    }

   
    private void initializeUI() {
        setSize(400, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getParent());
        
        // Tạo form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tạo các trường nhập liệu với giá trị hiện tại
        nameField = new JTextField(product.getProductName());
        descField = new JTextField(product.getDescription());
        priceField = new JTextField(product.getPrice().toString());
        sizeField = new JTextField(product.getSize());
        colorField = new JTextField(product.getColor());
        quantityField = new JTextField(String.valueOf(product.getQuantity()));
        
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

    
    private void saveProduct() {
        try {
            // Cập nhật thông tin sản phẩm
            product.setProductName(nameField.getText());
            product.setDescription(descField.getText());
            product.setPrice(new BigDecimal(priceField.getText()));
            product.setSize(sizeField.getText());
            product.setColor(colorField.getText());
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            
            // Lưu sản phẩm
            productService.updateProduct(product);
            
            // Đóng dialog
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
