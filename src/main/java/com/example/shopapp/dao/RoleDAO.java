package com.example.shopapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.shopapp.entity.Role;
import com.example.shopapp.utils.DatabaseConnection;


public class RoleDAO {
    
    
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT role_id, role_name FROM Role");
            
            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("role_id"));
                role.setRoleName(rs.getString("role_name"));
                roles.add(role);
            }
        } catch (SQLException e) {
            System.err.println("Error getting roles: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return roles;
    }
    
    
    public Role getById(int roleId) {
        Role role = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT role_id, role_name FROM Role WHERE role_id = ?");
            pstmt.setInt(1, roleId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                role = new Role();
                role.setRoleId(rs.getInt("role_id"));
                role.setRoleName(rs.getString("role_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting role by id: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return role;
    }
    
    
    public boolean save(Role role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO Role (role_name) VALUES (?)", 
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, role.getRoleName());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    role.setRoleId(generatedKeys.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving role: " + e.getMessage());
        } finally {
            if (generatedKeys != null) {
                try { generatedKeys.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
    
   
    public boolean update(Role role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("UPDATE Role SET role_name = ? WHERE role_id = ?");
            pstmt.setString(1, role.getRoleName());
            pstmt.setInt(2, role.getRoleId());
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating role: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    
    public boolean delete(int roleId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM Role WHERE role_id = ?");
            pstmt.setInt(1, roleId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting role: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
}
