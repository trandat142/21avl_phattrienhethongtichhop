import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class bai12 {
    public boolean copyFile(String source, String dest) throws FileNotFoundException, IOException{
        File sourceFile = new File(source);
        File destFile = new File(dest);

            if(sourceFile.exists()){
                FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos= new FileOutputStream(destFile);
                byte[]arr=new byte[1024];
                while((fis.read(arr))!=-1){
                    fos.write(arr);
                    fos.flush();
                }
                fis.close();
                fos.close();
                System.out.println("copy thanh cong");
                return true;

            }else{
                System.out.println("file nguon khong ton tai");
                return false;
            }
        }
        public static void main(String[] args) throws FileNotFoundException, IOException {
    bai12 b = new bai12();

    b.copyFile(
        "E:\\IUH\\PhatTrienTichHopHeThong\\ThucHanh\\Lab1\\demo.txt",
        "E:\\IUH\\PhatTrienTichHopHeThong\\ThucHanh\\Lab1\\demo_copy.txt"
    );
}
    }
    

