package Lab2.Bai2;

public class GiangVien extends Nguoi {
    private String maGiangVien;
    private String chuyenMon;
    private double luongCoBan;
    private double hesoLuong;

    public GiangVien(String hoTen, int namSinh, String diaChi, String maGiangVien, String chuyenMon, double luongCoBan, double hesoLuong){
        super(hoTen, namSinh, diaChi);
        this.maGiangVien=maGiangVien;
        this.chuyenMon=chuyenMon;
        this.luongCoBan=luongCoBan;
        this.hesoLuong=hesoLuong;
    }

    public double tinhLuong(){
        return luongCoBan*hesoLuong;
    }
    @Override 
    public void hienThiThongTin(){
        super.hienThiThongTin();
        System.out.println("Ma GV:"+maGiangVien
        + "\nChuyen mon" +chuyenMon
        +"\nLuong"+tinhLuong()

        );
    }
}
