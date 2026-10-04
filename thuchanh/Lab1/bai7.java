import java.io.File;

public class bai7 {
    private void deleteFile(String source){
        //new file
        File file = new File(source);
        //check file da ton tai
        //new ton tai
        if(file.exists()){
            System.out.println("File ton tai");
            file.delete();
            System.out.println("Xoa file thanh cong");

        }
        else{
            System.out.println("File khong ton tai");
        }

    }
    public static void main(String[] args){
        bai7 bai7= new bai7();
        bai7.deleteFile("D:/HojcJava/demo.txt");
    } 
}
