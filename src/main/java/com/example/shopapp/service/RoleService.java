package com.example.shopapp.service;

import com.example.shopapp.dao.RoleDAO;
import com.example.shopapp.entity.Role;
import java.util.List;

/**
 * Service class for Role operations
 */
public class RoleService {
    private RoleDAO roleDAO;
    
    public RoleService() {
        this.roleDAO = new RoleDAO();
    }
    
    /**
     * Get all roles from database
     * @return List of Role objects
     */
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }
    
    /**
     * Get role by ID
     * @param roleId ID of the role
     * @return Role object, or null if not found
     */
    public Role getRoleById(int roleId) {
        return roleDAO.getById(roleId);
    }
    
    /**
     * Create a new role
     * @param roleName Name of the role
     * @return Role object if successful, null otherwise
     */
    public Role createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        
        if (roleDAO.save(role)) {
            return role;
        } else {
            return null;
        }
    }
    
    /**
     * Update an existing role
     * @param roleId ID of the role to update
     * @param roleName New role name
     * @return Updated Role object if successful, null otherwise
     */
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
    
    /**
     * Delete a role
     * @param roleId ID of the role to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteRole(int roleId) {
        return roleDAO.delete(roleId);
    }
}
