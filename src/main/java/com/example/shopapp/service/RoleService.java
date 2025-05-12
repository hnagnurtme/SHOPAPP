package com.example.shopapp.service;

import com.example.shopapp.dao.RoleDAO;
import com.example.shopapp.entity.Role;
import java.util.List;


public class RoleService {
    private RoleDAO roleDAO;
    
    public RoleService() {
        this.roleDAO = new RoleDAO();
    }
    
    
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }
    
    
    public Role getRoleById(int roleId) {
        return roleDAO.getById(roleId);
    }
    
    
    public Role createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        
        if (roleDAO.save(role)) {
            return role;
        } else {
            return null;
        }
    }
    
    
    public Role updateRole(int roleId, String roleName) {
        Role role = roleDAO.getById(roleId);
        if (role != null) {
            role.setRoleName(roleName);
            if (roleDAO.update(role)) {
                return role;
            }
        }
        return null;
    }
    
    
    public boolean deleteRole(int roleId) {
        return roleDAO.delete(roleId);
    }
}
