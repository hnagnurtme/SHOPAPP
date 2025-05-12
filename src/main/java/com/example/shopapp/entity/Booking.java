package com.example.shopapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;


public class Booking {
    private int bookingId;
    private int userId;
    private LocalDateTime bookingDate;
    private String status;
    private BigDecimal totalPrice;
    
    // References
    private User user;
    private List<BookingDetail> details;

    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.status = "pending";
        this.details = new ArrayList<>();
    }

    public Booking(int bookingId, int userId, BigDecimal totalPrice) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.bookingDate = LocalDateTime.now();
        this.status = "pending";
        this.totalPrice = totalPrice;
        this.details = new ArrayList<>();
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<BookingDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BookingDetail> details) {
        this.details = details;
    }
    
    public void addDetail(BookingDetail detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        this.details.add(detail);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", bookingDate=" + bookingDate +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
