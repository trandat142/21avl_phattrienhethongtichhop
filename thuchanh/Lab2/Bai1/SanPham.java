package Lab2.Bai1;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private double donGia;
    private int soLuong;
    //constructor: tao doi tuong SanPham va gan gia tri ban dau cho 4 thuoc tinh
    public SanPham(String maSanPham,String tenSanPham, double donGia, int soLuong)
    {
        this.maSanPham =maSanPham;
        this.tenSanPham=tenSanPham;
        this.donGia=donGia;
        this.soLuong=soLuong;
    }
    public String getMaSanPham()
    {
        return maSanPham;
    }
    public String getTenSanPham(){
        return tenSanPham;
    }
    public double getDonGia(){
        return donGia;
    }
    public  int getSoLuong(){
        return soLuong;
    }
    //Thanh tien = Don gia * so luong hien co
    public double tinhThanhTien(){
        return donGia*soLuong;
    }
    public void nhapHang(int soLuongNhap){
        if(soLuongNhap>0){
            soLuong+=soLuongNhap; //cong them vao ton kho
            System.out.println("Nhap hang thanh cong: + " + soLuongNhap);
        }
        else{
            System.out.println("So luong nhap khong hop le!");
        }
    }
    public boolean banHang(int soLuongBan){
        if(soLuongBan<=0){
            System.out.println("So luong ban khong hop le!");
            return  false;
        }
        if(soLuongBan > soLuong){
            System.out.println("Khong du hang de ban");
            return false;
        }
        soLuong-=soLuongBan;
        return true;

    }
    public  void  hienThiThongTin(){
        System.out.println("Ma: "+maSanPham
        + "\nTen: " + tenSanPham
        + "\nDon Gia: " + donGia
        + "\nSo Luong: "+ soLuong
        + "\nThanh Tien: " + tinhThanhTien());
    }
}

