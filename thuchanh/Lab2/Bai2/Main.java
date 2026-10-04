package Lab2.Bai2;

public class Main {
    public static  void main(String[] args){
        SinhVien sv1 = new SinhVien("Tran Van Dat", 2004, "Ha Noi",  "SV001", "CNTT", 8.7);
        SinhVien sv2 = new SinhVien("Bui Tra My", 2005, "Da Nang",
                "SV002", "Kế toán", 6.2);
        GiangVien gv1 = new GiangVien("Ton Ngoc Bao Tran", 1985, "TP.HCM",
                "GV001", "Toan", 8000000, 2.5);
        GiangVien gv2 = new GiangVien("Nguyen Minh Hai", 1990, "Can Tho",
                "GV002", "Tin", 7500000, 3.0);
            
           System.out.println("--- Thong tin sinh vien ---");
        sv1.hienThiThongTin();
        sv2.hienThiThongTin();   
          System.out.println("\n--- Thong tin giang vien ---");
        gv1.hienThiThongTin();
        gv2.hienThiThongTin(); 
            
            
            }
}
