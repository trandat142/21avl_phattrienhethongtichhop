package Lab2.Bai2;

public class Nguoi {
    private  String hoTen;
    private  int namSinh;
    private  String diaChi;

    //constructor
    public  Nguoi(String hoTen,int namSinh, String diaChi){
        this.hoTen=hoTen;
        this.namSinh=namSinh;
        this.diaChi=diaChi;
    }
    public String getHoTen(){
        return hoTen;
    }
    public void setHoTen(String hoTen){
        this.hoTen=hoTen;
    }
    public  int getNamSinh(){
        return namSinh;
    }
    public void setNamSinh(int namSinh){
        this.namSinh=namSinh;
    }
   
    public String getDiaChi(){
        return diaChi;
    }
     public void setgetDiaChi(String diaChi){
        this.diaChi=diaChi;
    }
    public int tinhTuoi(){
        int namHienTai=2026;
        return namHienTai - namSinh;
    }

    public  void hienThiThongTin(){
        System.out.println("Ho ten: "+hoTen
            + "\nNam sinh:"+namSinh
            +"\nTuoi:"+tinhTuoi()
            +"\nDia Chi:"+diaChi
        );
    }

}
