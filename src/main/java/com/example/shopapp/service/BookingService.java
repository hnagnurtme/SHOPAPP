package com.example.shopapp.service;

import com.example.shopapp.dao.BookingDAO;
import com.example.shopapp.dao.ProductDAO;
import com.example.shopapp.entity.Booking;
import com.example.shopapp.entity.BookingDetail;
import com.example.shopapp.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class BookingService {
    private BookingDAO bookingDAO;
    private ProductDAO productDAO;
    
    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.productDAO = new ProductDAO();
    }
    
    
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
    
    
    public Booking getBookingById(int bookingId) {
        return bookingDAO.getById(bookingId);
    }
    
    
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingDAO.getByUserId(userId);
    }
    
    
    public Booking createBooking(int userId, List<BookingItem> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("No items in booking");
            return null;
        }
        
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("pending");
        
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<BookingDetail> details = new ArrayList<>();
        
        for (BookingItem item : items) {
            Product product = productDAO.getById(item.getProductId());
            if (product == null) {
                System.out.println("Product not found: " + item.getProductId());
                continue;
            }
            
            if (product.getQuantity() < item.getQuantity()) {
                System.out.println("Not enough quantity for product: " + product.getProductName());
                return null;
            }
            
            BookingDetail detail = new BookingDetail();
            detail.setProductId(item.getProductId());
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(product.getPrice());
            detail.setProduct(product);
            
            // Calculate line total
            BigDecimal lineTotal = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalPrice = totalPrice.add(lineTotal);
            
            details.add(detail);
        }
        
        booking.setTotalPrice(totalPrice);
        booking.setDetails(details);
        
        if (bookingDAO.save(booking)) {
            return booking;
        } else {
            return null;
        }
    }
    
    
    public boolean updateBookingStatus(int bookingId, String status) {
        if (status == null || status.isEmpty()) {
            return false;
        }
        
        // Check if status is valid
        if (!isValidStatus(status)) {
            System.out.println("Invalid status: " + status);
            return false;
        }
        
        return bookingDAO.updateStatus(bookingId, status);
    }
    
    
    private boolean isValidStatus(String status) {
        return status.equals("pending") || status.equals("confirmed") || 
               status.equals("shipped") || status.equals("delivered") ||
               status.equals("cancelled");
    }
    
    
    public static class BookingItem {
        private int productId;
        private int quantity;
        
        public BookingItem(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
        
        public int getProductId() {
            return productId;
        }
        
        public int getQuantity() {
            return quantity;
        }
    }
    
    public BigDecimal calculateTotalRevenue() {
        List<Booking> bookings = getAllBookings();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        
        if (bookings != null) {
            for (Booking booking : bookings) {
                if (booking.getTotalPrice() != null) {
                    totalRevenue = totalRevenue.add(booking.getTotalPrice());
                }
            }
        }
        
        return totalRevenue;
    }
}
