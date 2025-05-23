package com.example.shopapp.ui;

import com.example.shopapp.entity.User;
import com.example.shopapp.service.UserService;
import com.example.shopapp.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class CustomerListForm extends JDialog {
    private UserService userService;
    private JTable customerTable;
    private DefaultTableModel customerModel;

   
    public CustomerListForm(JFrame parent, UserService userService) {
        super(parent, "Danh Sách Khách Hàng", true);
        this.userService = userService;
        initializeUI();
        UIUtils.setupLookAndFeel();
        loadCustomerData();
    }
    
    
    public CustomerListForm(JFrame parent) {
        super(parent, "Danh Sách Khách Hàng", true);
        this.userService = new UserService();
        initializeUI();
        loadCustomerData();
    }

    
    private void initializeUI() {
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getParent());
        
        // Tạo bảng danh sách khách hàng
        String[] columns = {"ID", "Username", "Email", "Họ và Tên", "Vai Trò", "Ngày Tạo"};
        customerModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(customerModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        
        // Tạo panel cho các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        // Thêm các component vào dialog
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    
    private void loadCustomerData() {
        // Xóa dữ liệu hiện có
        customerModel.setRowCount(0);
        
        // Lấy danh sách khách hàng
        List<User> users = userService.getAllUsers();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Thêm khách hàng vào bảng
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole() != null ? user.getRole().getRoleName() : "Chưa xác định",
                user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : ""
            };
            customerModel.addRow(row);
        }
    }
}
