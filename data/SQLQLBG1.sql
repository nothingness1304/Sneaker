CREATE DATABASE [QLBG]
USE QLBG


CREATE TABLE Account (
    MaQuanTriVien INT IDENTITY(1,1) PRIMARY KEY,
    UserName VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    NgayTao DATETIME DEFAULT GETDATE()
);

CREATE TABLE TempUser (
    MaQuanTriVien INT IDENTITY(1,1) PRIMARY KEY,
    UserName VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
	VerificationCode VARCHAR(255) NOT NULL,
    NgayTao DATETIME DEFAULT GETDATE()
);

CREATE TABLE KhachHang (
    MaKhachHang INT IDENTITY(1,1) PRIMARY KEY,
    Ho NVARCHAR(50) NOT NULL,
    Ten NVARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(20),
    DiaChi NVARCHAR(255),
    NgayTao DATETIME DEFAULT GETDATE()
);

CREATE TABLE NhaCungCap (
    MaNhaCungCap INT IDENTITY(1,1) PRIMARY KEY,
    TenNhaCungCap NVARCHAR(100) NOT NULL UNIQUE,
    TenNguoiLienHe NVARCHAR(50),
    EmailLienHe VARCHAR(100),
    SoDienThoaiLienHe VARCHAR(20),
    DiaChi NVARCHAR(255),
    NgayTao DATETIME DEFAULT GETDATE()
);

CREATE TABLE SanPham (
    MaSanPham INT IDENTITY(1,1) PRIMARY KEY,
    TenSanPham NVARCHAR(100) NOT NULL,
    MaNhaCungCap INT NOT NULL,
    TenNhaCungCap NVARCHAR(100) NOT NULL,
    KichThuoc NVARCHAR(10),
    MauSac NVARCHAR(20),
    Gia DECIMAL(10, 2) NOT NULL,
    SoLuong INT NOT NULL,
    MoTa NVARCHAR(MAX),
    HinhAnhURL VARCHAR(255),
    NgayTao DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_SanPham_NhaCungCap FOREIGN KEY (MaNhaCungCap)
        REFERENCES NhaCungCap(MaNhaCungCap)
);

CREATE TABLE DonHang (
    MaDonHang INT IDENTITY(1,1) PRIMARY KEY,
    MaKhachHang INT,
    NgayDatHang DATETIME DEFAULT GETDATE(),
    TongTien DECIMAL(10, 2) NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT 'Chờ xử lý',
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang),
    CHECK (TrangThai IN ('Chờ xử lý', 'Đang giao', 'Đã giao', 'Đã hủy'))
);

CREATE TABLE ChiTietDonHang (
    MaChiTietDonHang INT IDENTITY(1,1) PRIMARY KEY,
    MaDonHang INT,
    MaSanPham INT,
    SoLuong INT NOT NULL,
    GiaDonVi DECIMAL(10, 2) NOT NULL,
    TongTien AS (SoLuong * GiaDonVi) PERSISTED,
    FOREIGN KEY (MaDonHang) REFERENCES DonHang(MaDonHang),
    FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham)
);

CREATE TABLE SanPhamNhaCungCap (
    MaSanPhamNhaCungCap INT IDENTITY(1,1) PRIMARY KEY,
    MaSanPham INT,
    MaNhaCungCap INT,
    FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham),
    FOREIGN KEY (MaNhaCungCap) REFERENCES NhaCungCap(MaNhaCungCap)
);

CREATE TABLE BaoCao (
    MaBaoCao INT IDENTITY(1,1) PRIMARY KEY,
    LoaiBaoCao VARCHAR(50),
    DuLieu NVARCHAR(MAX),
    NgayTao DATETIME DEFAULT GETDATE()
);

CREATE TRIGGER trg_InsertSanPham
ON SanPham
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaNhaCungCap INT;
    DECLARE @TenNhaCungCap NVARCHAR(100);
    DECLARE @TenSanPham NVARCHAR(100);
    DECLARE @KichThuoc NVARCHAR(10);
    DECLARE @MauSac NVARCHAR(20);
    DECLARE @Gia DECIMAL(10, 2);
    DECLARE @SoLuong INT;
    DECLARE @MoTa NVARCHAR(MAX);
    DECLARE @HinhAnhURL VARCHAR(255);

    SELECT
        @TenNhaCungCap = TenNhaCungCap,
        @TenSanPham = TenSanPham,
        @KichThuoc = KichThuoc,
        @MauSac = MauSac,
        @Gia = Gia,
        @SoLuong = SoLuong,
        @MoTa = MoTa,
        @HinhAnhURL = HinhAnhURL
    FROM inserted;

    -- Tìm MaNhaCungCap từ TenNhaCungCap
    SELECT @MaNhaCungCap = MaNhaCungCap
    FROM NhaCungCap
    WHERE TenNhaCungCap = @TenNhaCungCap;

    -- Nếu nhà cung cấp không tồn tại, trả về lỗi
    IF @MaNhaCungCap IS NULL
    BEGIN
        RAISERROR('Nhà cung cấp không tồn tại', 16, 1);
        RETURN;
    END

    -- Thêm sản phẩm vào bảng SanPham với MaNhaCungCap
    INSERT INTO SanPham (TenSanPham, MaNhaCungCap, TenNhaCungCap, KichThuoc, MauSac, Gia, SoLuong, MoTa, HinhAnhURL, NgayTao)
    VALUES (@TenSanPham, @MaNhaCungCap, @TenNhaCungCap, @KichThuoc, @MauSac, @Gia, @SoLuong, @MoTa, @HinhAnhURL, GETDATE());

    -- Lấy MaSanPham vừa được thêm
    DECLARE @MaSanPham INT;
    SET @MaSanPham = SCOPE_IDENTITY();

    -- Thêm bản ghi vào bảng SanPhamNhaCungCap
    INSERT INTO SanPhamNhaCungCap (MaSanPham, MaNhaCungCap)
    VALUES (@MaSanPham, @MaNhaCungCap);
END;

INSERT INTO NhaCungCap (TenNhaCungCap, TenNguoiLienHe, EmailLienHe, SoDienThoaiLienHe, DiaChi)
VALUES ('NhaCungCap2', 'NguoiLienHe2', 'email2@example.com', '0123456789', 'DiaChi2');

INSERT INTO SanPham (TenSanPham, TenNhaCungCap, KichThuoc, MauSac, Gia, SoLuong, MoTa, HinhAnhURL)
VALUES ('SanPham3', 'NhaCungCap2', '10x10', 'Do', 100.00, 10, 'MoTa SanPham3', 'http://example3.com/image.jpg');

CREATE TRIGGER trg_AddOrderWithCustomerCheck
ON DonHang
INSTEAD OF INSERT
AS
BEGIN
    DECLARE @Ho NVARCHAR(50);
    DECLARE @Ten NVARCHAR(50);
    DECLARE @Email VARCHAR(100);
    DECLARE @SoDienThoai VARCHAR(20);
    DECLARE @DiaChi NVARCHAR(255);
    DECLARE @TenSanPham VARCHAR(100);
    DECLARE @SoLuong INT;
    DECLARE @GiaDonVi DECIMAL(10, 2);
    DECLARE @TrangThai VARCHAR(20);

    DECLARE @MaKhachHang INT;
    DECLARE @MaSanPham INT;
    DECLARE @TongTien DECIMAL(10, 2);
    DECLARE @MaDonHang INT;

    SELECT 
        @Ho = KhachHang.Ho,
        @Ten = KhachHang.Ten,
        @Email = KhachHang.Email,
        @SoDienThoai = KhachHang.SoDienThoai,
        @DiaChi = KhachHang.DiaChi,
        @TenSanPham = SanPham.TenSanPham,
        @SoLuong = inserted.SoLuong,
        @GiaDonVi = inserted.GiaDonVi,
        @TrangThai = inserted.TrangThai
    FROM inserted
    LEFT JOIN KhachHang ON inserted.MaKhachHang = KhachHang.MaKhachHang
    LEFT JOIN SanPham ON inserted.MaSanPham = SanPham.MaSanPham;

    -- Kiểm tra xem khách hàng đã tồn tại hay chưa
    SELECT @MaKhachHang = MaKhachHang
    FROM KhachHang
    WHERE SoDienThoai = @SoDienThoai OR Email = @Email;

    -- Nếu khách hàng chưa tồn tại, thêm mới
    IF @MaKhachHang IS NULL
    BEGIN
        INSERT INTO KhachHang (Ho, Ten, Email, SoDienThoai, DiaChi)
        VALUES (@Ho, @Ten, @Email, @SoDienThoai, @DiaChi);
        
        SET @MaKhachHang = SCOPE_IDENTITY(); -- Lấy ID của khách hàng vừa thêm
    END

    -- Lấy ID của sản phẩm dựa trên tên sản phẩm
    SELECT @MaSanPham = MaSanPham
    FROM SanPham
    WHERE TenSanPham = @TenSanPham;

    -- Tính tổng tiền cho đơn hàng
    SET @TongTien = @SoLuong * @GiaDonVi;

    -- Thêm đơn hàng
    INSERT INTO DonHang (MaKhachHang, NgayDatHang, TongTien, TrangThai)
    VALUES (@MaKhachHang, GETDATE(), @TongTien, @TrangThai);

    SET @MaDonHang = SCOPE_IDENTITY(); -- Lấy ID của đơn hàng vừa thêm

    -- Thêm chi tiết đơn hàng
    INSERT INTO ChiTietDonHang (MaDonHang, MaSanPham, SoLuong, GiaDonVi)
    VALUES (@MaDonHang, @MaSanPham, @SoLuong, @GiaDonVi);
END;


DROP TRIGGER trg_AddOrderWithCustomerCheck
-- Chạy thủ tục lưu trữ



SELECT * FROM KhachHang;
SELECT * FROM DonHang;
SELECT * FROM ChiTietDonHang;