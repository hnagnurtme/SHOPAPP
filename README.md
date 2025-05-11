# Dự Án Java

Đây là một ứng dụng Java đơn giản minh họa việc sử dụng Maven để quản lý dự án và bao gồm cấu trúc cơ bản để tổ chức mã nguồn và tài nguyên.

## Cấu Trúc Dự Án

```
java-project
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── App.java
│   │   │           └── utils
│   │   │               └── Helper.java
│   │   └── resources
│   │       └── config.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── AppTest.java
├── pom.xml
└── README.md
```

## Yêu Cầu Hệ Thống

- Java 11 trở lên
- Maven 3.6 trở lên

## Biên Dịch Dự Án

Để biên dịch dự án, hãy điều hướng đến thư mục dự án và chạy lệnh sau:

```
mvn clean install
```

## Chạy Ứng Dụng

Sau khi biên dịch dự án, bạn có thể chạy ứng dụng bằng lệnh sau:

```
mvn exec:java -Dexec.mainClass="com.example.App"
```

## Chạy Kiểm Thử

Để chạy các bài kiểm thử, sử dụng lệnh sau:

```
mvn test
```

## Cấu Hình

Các thuộc tính cấu hình có thể được tìm thấy trong tệp `src/main/resources/config.properties`. Sửa đổi tệp này để thay đổi các tham số ứng dụng khi cần thiết.

## Giấy Phép

Dự án này được cấp phép theo Giấy phép MIT. Xem tệp LICENSE để biết thêm chi tiết.