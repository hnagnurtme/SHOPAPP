package com.example.shopapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.shopapp.entity.Booking;
import com.example.shopapp.entity.BookingDetail;
import com.example.shopapp.utils.DatabaseConnection;


public class BookingDAO {
    private UserDAO userDAO;
    private ProductDAO productDAO;
    
    public BookingDAO() {
        this.userDAO = new UserDAO();
        this.productDAO = new ProductDAO();
    }
    
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT booking_id, user_id, booking_date, status, total_price FROM Booking");
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setStatus(rs.getString("status"));
                booking.setTotalPrice(rs.getBigDecimal("total_price"));
                
                // Set user if user_id is not null
                if (rs.getObject("user_id") != null) {
                    booking.setUser(userDAO.getById(rs.getInt("user_id")));
                }
                
                // Set booking details
                booking.setDetails(getBookingDetailsByBookingId(booking.getBookingId()));
                
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return bookings;
    }
    
    public Booking getById(int bookingId) {
        Booking booking = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT booking_id, user_id, booking_date, status, total_price FROM Booking WHERE booking_id = ?");
            pstmt.setInt(1, bookingId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setStatus(rs.getString("status"));
                booking.setTotalPrice(rs.getBigDecimal("total_price"));
                
                // Set user if user_id is not null
                if (rs.getObject("user_id") != null) {
                    booking.setUser(userDAO.getById(rs.getInt("user_id")));
                }
                
                // Set booking details
                booking.setDetails(getBookingDetailsByBookingId(booking.getBookingId()));
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking by id: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return booking;
    }
    
    
    public List<Booking> getByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT booking_id, user_id, booking_date, status, total_price FROM Booking WHERE user_id = ?");
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setStatus(rs.getString("status"));
                booking.setTotalPrice(rs.getBigDecimal("total_price"));
                
                // Set user
                booking.setUser(userDAO.getById(userId));
                
                // Set booking details
                booking.setDetails(getBookingDetailsByBookingId(booking.getBookingId()));
                
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by user id: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return bookings;
    }
    
    
    public List<BookingDetail> getBookingDetailsByBookingId(int bookingId) {
        List<BookingDetail> details = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(
                    "SELECT detail_id, booking_id, product_id, quantity, unit_price " +
                    "FROM Booking_Detail WHERE booking_id = ?");
            pstmt.setInt(1, bookingId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setDetailId(rs.getInt("detail_id"));
                detail.setBookingId(rs.getInt("booking_id"));
                detail.setProductId(rs.getInt("product_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitPrice(rs.getBigDecimal("unit_price"));
                
                // Set product
                detail.setProduct(productDAO.getById(detail.getProductId()));
                
                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking details: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return details;
    }
    
    
    public boolean save(Booking booking) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        boolean success = false;
        
        try {
            // Begin transaction
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert booking
            pstmt = conn.prepareStatement(
                    "INSERT INTO Booking (user_id, booking_date, status, total_price) VALUES (?, ?, ?, ?)", 
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, booking.getUserId());
            pstmt.setTimestamp(2, Timestamp.valueOf(booking.getBookingDate()));
            pstmt.setString(3, booking.getStatus());
            pstmt.setBigDecimal(4, booking.getTotalPrice());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookingId = generatedKeys.getInt(1);
                    booking.setBookingId(bookingId);
                    
                    // Insert booking details
                    if (booking.getDetails() != null && !booking.getDetails().isEmpty()) {
                        for (BookingDetail detail : booking.getDetails()) {
                            detail.setBookingId(bookingId);
                            if (!saveBookingDetail(conn, detail)) {
                                // Roll back if any detail fails to save
                                conn.rollback();
                                return false;
                            }
                        }
                    }
                    
                    // Commit transaction
                    conn.commit();
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving booking: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
        } finally {
            if (generatedKeys != null) {
                try { generatedKeys.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true);
                    conn.close(); 
                } catch (SQLException e) { /* ignore */ }
            }
        }
        
        return success;
    }
    
    
    private boolean saveBookingDetail(Connection conn, BookingDetail detail) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        boolean success = false;
        
        try {
            pstmt = conn.prepareStatement(
                    "INSERT INTO Booking_Detail (booking_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)", 
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, detail.getBookingId());
            pstmt.setInt(2, detail.getProductId());
            pstmt.setInt(3, detail.getQuantity());
            pstmt.setBigDecimal(4, detail.getUnitPrice());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    detail.setDetailId(generatedKeys.getInt(1));
                    
                    // Update product quantity
                    updateProductQuantity(conn, detail.getProductId(), detail.getQuantity());
                    
                    success = true;
                }
            }
        } finally {
            if (generatedKeys != null) {
                generatedKeys.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        
        return success;
    }
    
    
    private void updateProductQuantity(Connection conn, int productId, int quantity) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Get current quantity
            pstmt = conn.prepareStatement("SELECT quantity FROM Product WHERE product_id = ?");
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = Math.max(0, currentQuantity - quantity);
                
                // Update quantity
                pstmt.close();
                pstmt = conn.prepareStatement("UPDATE Product SET quantity = ? WHERE product_id = ?");
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, productId);
                pstmt.executeUpdate();
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    
    
    public boolean updateStatus(int bookingId, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("UPDATE Booking SET status = ? WHERE booking_id = ?");
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { /* ignore */ }
            }
            DatabaseConnection.closeConnection(conn);
        }
        
        return success;
    }
}
