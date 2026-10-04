package Lab2.Bai1;

public class Main {
    public static void main(String [] args){
        SanPham sp1 = new SanPham("SP1","Ban Phim",250000,10);
        SanPham sp2=new SanPham("SP1", "Chuot", 100000, 20);
        
        System.out.println("=================Thong tin ban dau=================");
        sp1.hienThiThongTin();
        sp2.hienThiThongTin();

        System.out.println("=================Nhap them hang cho sp1=================");
        sp1.nhapHang(5);
        sp1.hienThiThongTin();

        System.out.println("================= Ban hang thanh cong =================");
        boolean ketQua1=sp1.banHang(3);
        if (ketQua1==true) {
        System.out.println("Ban hang thanh cong!");
        sp1.hienThiThongTin();
        }
        else{
             System.out.println("Ban hang khong thanh cong!");
              
        }

        System.out.println("\n ================= Ban vuot so luong ton kho =================");
        boolean ketQua2=sp2.banHang(100);
        if(ketQua2==false){
                    System.out.println("Ban hang khong thanh cong!");
        }
        else{
         System.out.println("Ban hang thanh cong! "+ ketQua2);
        sp2.hienThiThongTin();
        }
      
    }
}
