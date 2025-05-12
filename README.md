# Ứng Dụng Shop Quản Lý

Đây là một ứng dụng Java Swing để quản lý shop quần áo với chức năng đặt hàng trực tuyến. Ứng dụng có hai vai trò: Admin và User thông thường.

## Tính Năng Chính

- **Quản lý sản phẩm**: Thêm, sửa, xóa và tìm kiếm sản phẩm
- **Đặt hàng**: Người dùng có thể mua sản phẩm trực tiếp
- **Quản lý tài khoản**: Đăng nhập, đăng ký và phân quyền người dùng
- **Thống kê doanh thu**: Xem báo cáo doanh thu và thống kê đơn hàng

## Cấu Trúc Dự Án

```
shopapp
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── shopapp
│   │   │           │   ├── dao         # Data Access Objects
│   │   │           │   ├── entity      # Model classes
│   │   │           │   ├── service     # Business logic
│   │   │           │   ├── ui          # User interface with Java Swing
│   │   │           │   └── utils       # Utility classes
│   │   └── resources
│   │       └── config.properties       # Database và app configuration
│   └── test
│       └── java
├── pom.xml
└── README.md
```

## Yêu Cầu Hệ Thống

- Java 11 trở lên
- Maven 3.6 trở lên
- SQL Server hoặc MySQL

## Biên Dịch và Chạy Dự Án

1. **Biên dịch dự án**:
   ```
   mvn clean install
   ```

2. **Chạy ứng dụng**:
   ```
   mvn exec:java -Dexec.mainClass="com.example.shopapp.ui.LoginForm"
   ```

## Cấu Hình Cơ Sở Dữ Liệu

### Thay đổi kết nối database
Mở tệp `src/main/resources/config.properties` và chỉnh sửa các thông số kết nối:

```properties
# Database Configuration
db.url=jdbc:sqlserver://localhost:1433;databaseName=ShopAppDB
db.username=sa
db.password=YourPassword
```

### Thay đổi mật khẩu SQL
1. Sửa thông số `db.password` trong tệp config.properties
2. Nếu sử dụng SQL Server, đảm bảo cập nhật mật khẩu của tài khoản 'sa' trong SQL Server Management Studio:
   - Mở SQL Server Management Studio
   - Kết nối đến SQL Server
   - Mở Object Explorer > Security > Logins
   - Chuột phải vào 'sa' và chọn Properties
   - Chọn tab General và đặt mật khẩu mới

## Thông Tin Đăng Nhập

### Tài khoản Admin
- **Username**: admin
- **Password**: admin123
- **Quyền hạn**: Quản lý sản phẩm, quản lý người dùng, xem báo cáo doanh thu

### Tài khoản User
- **Username**: user
- **Password**: user123
- **Quyền hạn**: Xem và mua sản phẩm, quản lý đơn hàng cá nhân

## Hướng Dẫn Sử Dụng

### Dành cho Admin
1. Đăng nhập với tài khoản admin
2. Tại Homepage, bạn có thể:
   - Quản lý sản phẩm (thêm, sửa, xóa)
   - Xem danh sách khách hàng
   - Xem danh sách đơn hàng
   - Xem thống kê doanh thu

### Dành cho User
1. Đăng nhập hoặc đăng ký tài khoản mới
2. Tại Customer Homepage, bạn có thể:
   - Tìm kiếm và xem sản phẩm
   - Chọn sản phẩm và mua ngay
   - Xem lịch sử đơn hàng

## Hỗ Trợ

Nếu gặp vấn đề, vui lòng tạo Issue trên GitHub hoặc liên hệ qua email: anhnon01062gmail.com