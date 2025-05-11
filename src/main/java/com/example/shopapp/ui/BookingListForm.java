package com.example.shopapp.ui;

import com.example.shopapp.entity.Booking;
import com.example.shopapp.entity.BookingDetail;
import com.example.shopapp.service.BookingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Form hiển thị danh sách đơn hàng
 */
public class BookingListForm extends JDialog {
    private BookingService bookingService;
    private JTable bookingTable;
    private DefaultTableModel bookingModel;
    private JTable detailTable;
    private DefaultTableModel detailModel;

    /**
     * Constructor cho BookingListForm
     * @param parent Component cha
     * @param bookingService Service xử lý đơn hàng
     */
    public BookingListForm(JFrame parent, BookingService bookingService) {
        super(parent, "Danh Sách Đơn Hàng", true);
        this.bookingService = bookingService;
        initializeUI();
        loadBookingData();
    }
    
    /**
     * Constructor cho BookingListForm
     * @param parent Component cha
     */
    public BookingListForm(JFrame parent) {
        super(parent, "Danh Sách Đơn Hàng", true);
        this.bookingService = new BookingService();
        initializeUI();
        loadBookingData();
    }

    /**
     * Khởi tạo giao diện người dùng
     */
    private void initializeUI() {
        setSize(800, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getParent());
        
        // Tạo bảng cho danh sách đơn hàng
        String[] columns = {"ID", "Khách Hàng", "Ngày Đặt Hàng", "Tổng Tiền", "Trạng Thái"};
        bookingModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(bookingModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        
        // Tạo panel chi tiết
        JPanel detailPanel = new JPanel(new BorderLayout());
        JLabel detailLabel = new JLabel("Chi tiết đơn hàng:");
        
        // Tạo bảng cho chi tiết đơn hàng
        String[] detailColumns = {"ID Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        detailModel = new DefaultTableModel(detailColumns, 0);
        detailTable = new JTable(detailModel);
        JScrollPane detailScrollPane = new JScrollPane(detailTable);
        
        detailPanel.add(detailLabel, BorderLayout.NORTH);
        detailPanel.add(detailScrollPane, BorderLayout.CENTER);
        
        // Thêm listener cho bảng đơn hàng
        bookingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bookingTable.getSelectedRow();
                if (selectedRow != -1) {
                    int bookingId = (int) bookingModel.getValueAt(selectedRow, 0);
                    displayBookingDetails(bookingId);
                }
            }
        });
        
        // Tạo SplitPane để chia màn hình
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, detailPanel);
        splitPane.setDividerLocation(250);
        
        // Tạo panel cho các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        // Thêm các component vào dialog
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Tải dữ liệu đơn hàng vào bảng
     */
    private void loadBookingData() {
        // Xóa dữ liệu hiện có
        bookingModel.setRowCount(0);
        
        // Lấy danh sách đơn hàng
        List<Booking> bookings = bookingService.getAllBookings();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Thêm đơn hàng vào bảng
        for (Booking booking : bookings) {
            String customerName = booking.getUser() != null ? 
                                  booking.getUser().getFullName() : 
                                  "Khách hàng #" + booking.getUserId();
            
            Object[] row = {
                booking.getBookingId(),
                customerName,
                booking.getBookingDate().format(formatter),
                booking.getTotalPrice() + " VNĐ",
                booking.getStatus()
            };
            bookingModel.addRow(row);
        }
    }

    /**
     * Hiển thị chi tiết đơn hàng
     * @param bookingId ID của đơn hàng cần hiển thị chi tiết
     */
    private void displayBookingDetails(int bookingId) {
        // Xóa dữ liệu chi tiết hiện có
        detailModel.setRowCount(0);
        
        // Lấy thông tin chi tiết đơn hàng
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null || booking.getDetails() == null) {
            return;
        }
        
        // Thêm chi tiết vào bảng
        for (BookingDetail detail : booking.getDetails()) {
            String productName = detail.getProduct() != null ? 
                               detail.getProduct().getProductName() : 
                               "Sản phẩm #" + detail.getProductId();
            
            BigDecimal lineTotal = detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity()));
            
            Object[] row = {
                detail.getProductId(),
                productName,
                detail.getQuantity(),
                detail.getUnitPrice() + " VNĐ",
                lineTotal + " VNĐ"
            };
            detailModel.addRow(row);
        }
    }
}
