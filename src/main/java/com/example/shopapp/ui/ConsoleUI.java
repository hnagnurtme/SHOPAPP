package com.example.shopapp.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.shopapp.entity.Booking;
import com.example.shopapp.entity.BookingDetail;
import com.example.shopapp.entity.Product;
import com.example.shopapp.entity.Role;
import com.example.shopapp.entity.User;
import com.example.shopapp.service.BookingService;
import com.example.shopapp.service.BookingService.BookingItem;
import com.example.shopapp.service.ProductService;
import com.example.shopapp.service.RoleService;
import com.example.shopapp.service.UserService;

/**
 * Console UI for the Shop Application
 */
public class ConsoleUI {
    private Scanner scanner;
    private UserService userService;
    private ProductService productService;
    private BookingService bookingService;
    private RoleService roleService;
    private User currentUser = null;
    
    public ConsoleUI() {
        scanner = new Scanner(System.in);
        userService = new UserService();
        productService = new ProductService();
        bookingService = new BookingService();
        roleService = new RoleService();
    }
    
    /**
     * Start the UI
     */
    public void start() {
        System.out.println("Welcome to Shop Quan Ao!");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    /**
     * Show login menu
     */
    private void showLoginMenu() {
        System.out.println("\n===== Login Menu =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 0:
                System.out.println("Thank you for using Shop Quan Ao. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Show main menu based on user role
     */
    private void showMainMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("Welcome, " + currentUser.getUsername() + "!");
        
        // Check if user has admin role
        boolean isAdmin = currentUser.getRole() != null && 
                         currentUser.getRole().getRoleName().equalsIgnoreCase("admin");
        
        System.out.println("1. View Products");
        System.out.println("2. Search Products");
        System.out.println("3. View My Bookings");
        System.out.println("4. Create a Booking");
        
        // Admin options
        if (isAdmin) {
            System.out.println("5. Manage Products");
            System.out.println("6. Manage Users");
            System.out.println("7. Manage Roles");
            System.out.println("8. Manage Bookings");
        }
        
        System.out.println("9. Logout");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                searchProducts();
                break;
            case 3:
                viewMyBookings();
                break;
            case 4:
                createBooking();
                break;
            case 5:
                if (isAdmin) {
                    manageProducts();
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
                break;
            case 6:
                if (isAdmin) {
                    manageUsers();
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
                break;
            case 7:
                if (isAdmin) {
                    manageRoles();
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
                break;
            case 8:
                if (isAdmin) {
                    manageBookings();
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
                break;
            case 9:
                logout();
                break;
            case 0:
                System.out.println("Thank you for using Shop Quan Ao. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Login process
     */
    private void login() {
        System.out.println("\n===== Login =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        User user = userService.login(username, password);
        
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
    
    /**
     * Logout process
     */
    private void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
    
    /**
     * Register a new user
     */
    private void register() {
        System.out.println("\n===== Register =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        
        // Default to regular user role (role id 2 - assuming admin is 1, user is 2)
        int roleId = 2;
        
        User user = userService.registerUser(username, password, email, fullName, roleId);
        
        if (user != null) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }
    
    /**
     * View all products
     */
    private void viewProducts() {
        System.out.println("\n===== Products =====");
        List<Product> products = productService.getAllProducts();
        
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        
        displayProducts(products);
    }
    
    /**
     * Search for products
     */
    private void searchProducts() {
        System.out.println("\n===== Search Products =====");
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        
        List<Product> products = productService.searchProducts(keyword);
        
        if (products.isEmpty()) {
            System.out.println("No products found matching '" + keyword + "'.");
            return;
        }
        
        System.out.println("Found " + products.size() + " products:");
        displayProducts(products);
    }
    
    /**
     * Display a list of products
     */
    private void displayProducts(List<Product> products) {
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s | %-30s | %-10s | %-5s | %-10s\n", 
                "ID", "Name", "Price", "Size", "Quantity");
        System.out.println("-----------------------------------------------------------------");
        
        for (Product product : products) {
            System.out.printf("%-5d | %-30s | $%-9.2f | %-5s | %-10d\n", 
                    product.getProductId(), 
                    truncateString(product.getProductName(), 30),
                    product.getPrice(), 
                    product.getSize(), 
                    product.getQuantity());
        }
        
        System.out.println("-----------------------------------------------------------------");
    }
    
    /**
     * View bookings for current user
     */
    private void viewMyBookings() {
        if (currentUser == null) {
            System.out.println("You need to be logged in to view bookings.");
            return;
        }
        
        System.out.println("\n===== My Bookings =====");
        List<Booking> bookings = bookingService.getBookingsByUserId(currentUser.getUserId());
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s | %-15s | %-10s | %-10s\n", 
                "ID", "Date", "Status", "Total");
        System.out.println("-----------------------------------------------------------------");
        
        for (Booking booking : bookings) {
            System.out.printf("%-5d | %-15s | %-10s | $%-9.2f\n", 
                    booking.getBookingId(), 
                    booking.getBookingDate().toLocalDate(),
                    booking.getStatus(), 
                    booking.getTotalPrice());
        }
        
        System.out.println("-----------------------------------------------------------------");
        
        // Ask if user wants to view details of a booking
        System.out.print("Enter booking ID to view details (or 0 to go back): ");
        int bookingId = getIntInput();
        
        if (bookingId > 0) {
            Booking selectedBooking = null;
            for (Booking b : bookings) {
                if (b.getBookingId() == bookingId) {
                    selectedBooking = b;
                    break;
                }
            }
            
            if (selectedBooking != null) {
                displayBookingDetails(selectedBooking);
            } else {
                System.out.println("Invalid booking ID.");
            }
        }
    }
    
    /**
     * Display details of a booking
     */
    private void displayBookingDetails(Booking booking) {
        System.out.println("\n===== Booking Details =====");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Date: " + booking.getBookingDate());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("Total: $" + booking.getTotalPrice());
        
        System.out.println("\n--- Items ---");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s | %-30s | %-10s | %-5s | %-10s\n", 
                "ID", "Product", "Unit Price", "Qty", "Total");
        System.out.println("-----------------------------------------------------------------");
        
        if (booking.getDetails() != null) {
            for (BookingDetail detail : booking.getDetails()) {
                String productName = detail.getProduct() != null ? 
                        detail.getProduct().getProductName() : "Unknown";
                
                System.out.printf("%-5d | %-30s | $%-9.2f | %-5d | $%-9.2f\n", 
                        detail.getProductId(),
                        truncateString(productName, 30),
                        detail.getUnitPrice(),
                        detail.getQuantity(),
                        detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity())));
            }
        }
        
        System.out.println("-----------------------------------------------------------------");
    }
    
    /**
     * Create a new booking
     */
    private void createBooking() {
        if (currentUser == null) {
            System.out.println("You need to be logged in to create a booking.");
            return;
        }
        
        System.out.println("\n===== Create Booking =====");
        List<Product> products = productService.getAllProducts();
        
        if (products.isEmpty()) {
            System.out.println("No products available to book.");
            return;
        }
        
        List<BookingItem> items = new ArrayList<>();
        boolean done = false;
        
        while (!done) {
            displayProducts(products);
            
            System.out.print("Enter product ID to add to booking (or 0 to finish): ");
            int productId = getIntInput();
            
            if (productId == 0) {
                done = true;
                continue;
            }
            
            // Find the product
            Product selectedProduct = null;
            for (Product p : products) {
                if (p.getProductId() == productId) {
                    selectedProduct = p;
                    break;
                }
            }
            
            if (selectedProduct == null) {
                System.out.println("Invalid product ID. Please try again.");
                continue;
            }
            
            if (selectedProduct.getQuantity() <= 0) {
                System.out.println("Product is out of stock.");
                continue;
            }
            
            System.out.print("Enter quantity (available: " + selectedProduct.getQuantity() + "): ");
            int quantity = getIntInput();
            
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0.");
                continue;
            }
            
            if (quantity > selectedProduct.getQuantity()) {
                System.out.println("Not enough quantity available.");
                continue;
            }
            
            // Add to booking
            items.add(new BookingItem(productId, quantity));
            System.out.println("Added " + quantity + " x " + selectedProduct.getProductName() + " to booking.");
            
            System.out.print("Add another product? (y/n): ");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("y")) {
                done = true;
            }
        }
        
        if (items.isEmpty()) {
            System.out.println("Booking cancelled - no items added.");
            return;
        }
        
        // Create booking
        Booking booking = bookingService.createBooking(currentUser.getUserId(), items);
        
        if (booking != null) {
            System.out.println("Booking created successfully with ID: " + booking.getBookingId());
            displayBookingDetails(booking);
        } else {
            System.out.println("Failed to create booking.");
        }
    }
    
    /**
     * Manage products (admin only)
     */
    private void manageProducts() {
        System.out.println("\n===== Manage Products =====");
        System.out.println("1. List All Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                addProduct();
                break;
            case 3:
                updateProduct();
                break;
            case 4:
                deleteProduct();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Add a new product
     */
    private void addProduct() {
        System.out.println("\n===== Add New Product =====");
        
        System.out.print("Product Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Description: ");
        String description = scanner.nextLine();
        
        System.out.print("Price: ");
        BigDecimal price = getBigDecimalInput();
        
        System.out.print("Size: ");
        String size = scanner.nextLine();
        
        System.out.print("Color: ");
        String color = scanner.nextLine();
        
        System.out.print("Initial Quantity: ");
        int quantity = getIntInput();
        
        Product product = productService.createProduct(name, description, price, size, color, quantity);
        
        if (product != null) {
            System.out.println("Product added successfully with ID: " + product.getProductId());
        } else {
            System.out.println("Failed to add product.");
        }
    }
    
    /**
     * Update an existing product
     */
    private void updateProduct() {
        System.out.println("\n===== Update Product =====");
        
        System.out.print("Enter product ID to update: ");
        int productId = getIntInput();
        
        Product product = productService.getProductById(productId);
        
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        
        System.out.println("Current product details:");
        System.out.println("- Name: " + product.getProductName());
        System.out.println("- Description: " + product.getDescription());
        System.out.println("- Price: $" + product.getPrice());
        System.out.println("- Size: " + product.getSize());
        System.out.println("- Color: " + product.getColor());
        System.out.println("- Quantity: " + product.getQuantity());
        
        System.out.println("\nEnter new values (leave blank to keep current value):");
        
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        name = name.isEmpty() ? null : name;
        
        System.out.print("New Description: ");
        String description = scanner.nextLine();
        description = description.isEmpty() ? null : description;
        
        System.out.print("New Price: ");
        String priceStr = scanner.nextLine();
        BigDecimal price = priceStr.isEmpty() ? null : new BigDecimal(priceStr);
        
        System.out.print("New Size: ");
        String size = scanner.nextLine();
        size = size.isEmpty() ? null : size;
        
        System.out.print("New Color: ");
        String color = scanner.nextLine();
        color = color.isEmpty() ? null : color;
        
        System.out.print("New Quantity: ");
        String quantityStr = scanner.nextLine();
        int quantity = quantityStr.isEmpty() ? -1 : Integer.parseInt(quantityStr);
        
        product = productService.updateProduct(productId, name, description, price, size, color, quantity);
        
        if (product != null) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Failed to update product.");
        }
    }
    
    /**
     * Delete a product
     */
    private void deleteProduct() {
        System.out.println("\n===== Delete Product =====");
        
        System.out.print("Enter product ID to delete: ");
        int productId = getIntInput();
        
        Product product = productService.getProductById(productId);
        
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        
        System.out.println("Are you sure you want to delete this product?");
        System.out.println("- Name: " + product.getProductName());
        System.out.println("- Price: $" + product.getPrice());
        System.out.print("Confirm deletion (y/n): ");
        
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            if (productService.deleteProduct(productId)) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Failed to delete product.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    /**
     * Manage users (admin only)
     */
    private void manageUsers() {
        System.out.println("\n===== Manage Users =====");
        System.out.println("1. List All Users");
        System.out.println("2. Add New User");
        System.out.println("3. Update User");
        System.out.println("4. Delete User");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                listUsers();
                break;
            case 2:
                addUser();
                break;
            case 3:
                updateUser();
                break;
            case 4:
                deleteUser();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * List all users
     */
    private void listUsers() {
        System.out.println("\n===== Users =====");
        List<User> users = userService.getAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s | %-20s | %-30s | %-15s\n", 
                "ID", "Username", "Email", "Role");
        System.out.println("-----------------------------------------------------------------");
        
        for (User user : users) {
            String roleName = user.getRole() != null ? user.getRole().getRoleName() : "No Role";
            System.out.printf("%-5d | %-20s | %-30s | %-15s\n", 
                    user.getUserId(), 
                    user.getUsername(), 
                    user.getEmail(),
                    roleName);
        }
        
        System.out.println("-----------------------------------------------------------------");
    }
    
    /**
     * Add a new user
     */
    private void addUser() {
        System.out.println("\n===== Add New User =====");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        
        // Show available roles
        List<Role> roles = roleService.getAllRoles();
        System.out.println("Available Roles:");
        for (Role role : roles) {
            System.out.println(role.getRoleId() + ". " + role.getRoleName());
        }
        
        System.out.print("Select Role ID: ");
        int roleId = getIntInput();
        
        User user = userService.registerUser(username, password, email, fullName, roleId);
        
        if (user != null) {
            System.out.println("User added successfully with ID: " + user.getUserId());
        } else {
            System.out.println("Failed to add user.");
        }
    }
    
    /**
     * Update an existing user
     */
    private void updateUser() {
        System.out.println("\n===== Update User =====");
        
        System.out.print("Enter user ID to update: ");
        int userId = getIntInput();
        
        User user = userService.getUserById(userId);
        
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        System.out.println("Current user details:");
        System.out.println("- Username: " + user.getUsername());
        System.out.println("- Email: " + user.getEmail());
        System.out.println("- Full Name: " + user.getFullName());
        
        System.out.println("\nEnter new values (leave blank to keep current value):");
        
        System.out.print("New Full Name: ");
        String fullName = scanner.nextLine();
        fullName = fullName.isEmpty() ? null : fullName;
        
        System.out.print("New Email: ");
        String email = scanner.nextLine();
        email = email.isEmpty() ? null : email;
        
        System.out.print("New Password: ");
        String password = scanner.nextLine();
        password = password.isEmpty() ? null : password;
        
        user = userService.updateUser(userId, fullName, email, password);
        
        if (user != null) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("Failed to update user.");
        }
    }
    
    /**
     * Delete a user
     */
    private void deleteUser() {
        System.out.println("\n===== Delete User =====");
        
        System.out.print("Enter user ID to delete: ");
        int userId = getIntInput();
        
        User user = userService.getUserById(userId);
        
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        System.out.println("Are you sure you want to delete this user?");
        System.out.println("- Username: " + user.getUsername());
        System.out.println("- Email: " + user.getEmail());
        System.out.print("Confirm deletion (y/n): ");
        
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            if (userService.deleteUser(userId)) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Failed to delete user.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    /**
     * Manage roles (admin only)
     */
    private void manageRoles() {
        System.out.println("\n===== Manage Roles =====");
        System.out.println("1. List All Roles");
        System.out.println("2. Add New Role");
        System.out.println("3. Update Role");
        System.out.println("4. Delete Role");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                listRoles();
                break;
            case 2:
                addRole();
                break;
            case 3:
                updateRole();
                break;
            case 4:
                deleteRole();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * List all roles
     */
    private void listRoles() {
        System.out.println("\n===== Roles =====");
        List<Role> roles = roleService.getAllRoles();
        
        if (roles.isEmpty()) {
            System.out.println("No roles found.");
            return;
        }
        
        System.out.println("-------------------------");
        System.out.printf("%-5s | %-20s\n", "ID", "Role Name");
        System.out.println("-------------------------");
        
        for (Role role : roles) {
            System.out.printf("%-5d | %-20s\n", role.getRoleId(), role.getRoleName());
        }
        
        System.out.println("-------------------------");
    }
    
    /**
     * Add a new role
     */
    private void addRole() {
        System.out.println("\n===== Add New Role =====");
        
        System.out.print("Role Name: ");
        String roleName = scanner.nextLine();
        
        Role role = roleService.createRole(roleName);
        
        if (role != null) {
            System.out.println("Role added successfully with ID: " + role.getRoleId());
        } else {
            System.out.println("Failed to add role.");
        }
    }
    
    /**
     * Update an existing role
     */
    private void updateRole() {
        System.out.println("\n===== Update Role =====");
        
        System.out.print("Enter role ID to update: ");
        int roleId = getIntInput();
        
        Role role = roleService.getRoleById(roleId);
        
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }
        
        System.out.println("Current role name: " + role.getRoleName());
        System.out.print("New Role Name: ");
        String roleName = scanner.nextLine();
        
        role = roleService.updateRole(roleId, roleName);
        
        if (role != null) {
            System.out.println("Role updated successfully.");
        } else {
            System.out.println("Failed to update role.");
        }
    }
    
    /**
     * Delete a role
     */
    private void deleteRole() {
        System.out.println("\n===== Delete Role =====");
        
        System.out.print("Enter role ID to delete: ");
        int roleId = getIntInput();
        
        Role role = roleService.getRoleById(roleId);
        
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }
        
        System.out.println("Are you sure you want to delete this role?");
        System.out.println("- Role Name: " + role.getRoleName());
        System.out.print("Confirm deletion (y/n): ");
        
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            if (roleService.deleteRole(roleId)) {
                System.out.println("Role deleted successfully.");
            } else {
                System.out.println("Failed to delete role. It might be in use by users.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    /**
     * Manage bookings (admin only)
     */
    private void manageBookings() {
        System.out.println("\n===== Manage Bookings =====");
        System.out.println("1. List All Bookings");
        System.out.println("2. Update Booking Status");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                listAllBookings();
                break;
            case 2:
                updateBookingStatus();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * List all bookings (admin)
     */
    private void listAllBookings() {
        System.out.println("\n===== All Bookings =====");
        List<Booking> bookings = bookingService.getAllBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-5s | %-15s | %-20s | %-10s | %-10s\n", 
                "ID", "Date", "User", "Status", "Total");
        System.out.println("-------------------------------------------------------------------------");
        
        for (Booking booking : bookings) {
            String username = booking.getUser() != null ? booking.getUser().getUsername() : "Unknown";
            
            System.out.printf("%-5d | %-15s | %-20s | %-10s | $%-9.2f\n", 
                    booking.getBookingId(), 
                    booking.getBookingDate().toLocalDate(),
                    username,
                    booking.getStatus(), 
                    booking.getTotalPrice());
        }
        
        System.out.println("-------------------------------------------------------------------------");
        
        // Ask if admin wants to view details of a booking
        System.out.print("Enter booking ID to view details (or 0 to go back): ");
        int bookingId = getIntInput();
        
        if (bookingId > 0) {
            Booking selectedBooking = null;
            for (Booking b : bookings) {
                if (b.getBookingId() == bookingId) {
                    selectedBooking = b;
                    break;
                }
            }
            
            if (selectedBooking != null) {
                displayBookingDetails(selectedBooking);
            } else {
                System.out.println("Invalid booking ID.");
            }
        }
    }
    
    /**
     * Update booking status (admin)
     */
    private void updateBookingStatus() {
        System.out.println("\n===== Update Booking Status =====");
        
        System.out.print("Enter booking ID: ");
        int bookingId = getIntInput();
        
        Booking booking = bookingService.getBookingById(bookingId);
        
        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }
        
        System.out.println("Current status: " + booking.getStatus());
        System.out.println("Available statuses: pending, confirmed, shipped, delivered, cancelled");
        System.out.print("New status: ");
        String status = scanner.nextLine();
        
        if (bookingService.updateBookingStatus(bookingId, status)) {
            System.out.println("Booking status updated successfully.");
        } else {
            System.out.println("Failed to update booking status.");
        }
    }
    
    /**
     * Get integer input from user
     */
    private int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Get BigDecimal input from user
     */
    private BigDecimal getBigDecimalInput() {
        try {
            String input = scanner.nextLine();
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Truncate a string to a certain length
     */
    private String truncateString(String str, int length) {
        if (str == null) return "";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }
}
