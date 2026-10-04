package Lab2.Bai2;


public class SinhVien extends Nguoi{
        private String maSinhVien;
        private String nganhHoc;
        private double diemTrungBinh;
        
        public SinhVien(String hoTen, int namSinh, String diaChi,
            String maSinhVien, String nganhHoc, double diemTrungBinh){
                super(hoTen,namSinh,diaChi);
                this.maSinhVien=maSinhVien;
                this.nganhHoc=nganhHoc;
                this.diemTrungBinh=diemTrungBinh;
            
            }
    public  String xepLoai(){
        if(diemTrungBinh>=8.5){
            return "Gioi";
        }
        else if(diemTrungBinh>=7){
            return  "Kha";
        }
        else if (diemTrungBinh>=5.0){
            return  "Trung binh";
        }
        else{
            return "Yeu";
        }
    }
    @Override 
    public void hienThiThongTin(){
        super.hienThiThongTin();
        System.out.println("Ma SV:" +maSinhVien
        + "\nNganh hoc:"+nganhHoc
        + "\nDiem TB:" +diemTrungBinh
        + "\nXep loai"+xepLoai()
        );
    }
        
    }
    

