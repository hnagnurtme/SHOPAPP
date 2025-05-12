package com.example.shopapp.utils;

import javax.swing.*;
import java.awt.*;


public class UIUtils {
    // Common UI colors
    public static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    public static final Color SECONDARY_COLOR = new Color(66, 165, 245);
    public static final Color ACCENT_COLOR = new Color(255, 111, 0);
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    public static final Color ERROR_COLOR = new Color(211, 47, 47);
    public static final Color SUCCESS_COLOR = new Color(56, 142, 60);
    public static final Color TABLE_HEADER_BG = new Color(25, 118, 210);
    public static final Color TABLE_HEADER_FG = Color.WHITE;
    public static final Color TABLE_ALTERNATE_ROW = new Color(240, 248, 255);
    
    public static void setupLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Set colors for Nimbus
        UIManager.put("control", BACKGROUND_COLOR);
        UIManager.put("nimbusBase", PRIMARY_COLOR);
        UIManager.put("nimbusFocus", ACCENT_COLOR);
        UIManager.put("nimbusLinkVisited", new Color(102, 14, 122));
        UIManager.put("nimbusBlueGrey", new Color(169, 176, 190));
    }
    
    public static ImageIcon createIcon(Class<?> clazz, String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(clazz.getResource(path));
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            // Icon not found, return empty icon
        }
        return new ImageIcon();
    }
    
    
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    
    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return field;
    }
    
    
    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return field;
    }
    
    
    public static JLabel createStyledLabel(String text, boolean isBold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, 14));
        return label;
    }
    
    
    public static void showStatus(JLabel label, String message, boolean isSuccess) {
        label.setText(message);
        label.setForeground(isSuccess ? SUCCESS_COLOR : ERROR_COLOR);
    }
}
