package com.example.shopapp.entity;

import java.math.BigDecimal;

public class BookingDetail {
    private int detailId;
    private int bookingId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    
    // References
    private Product product;

    public BookingDetail() {
    }

    public BookingDetail(int detailId, int bookingId, int productId, int quantity, BigDecimal unitPrice) {
        this.detailId = detailId;
        this.bookingId = bookingId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "BookingDetail{" +
                "detailId=" + detailId +
                ", bookingId=" + bookingId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
