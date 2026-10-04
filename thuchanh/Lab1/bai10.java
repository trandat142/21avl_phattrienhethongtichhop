import java.io.File;
import java.io.IOException;
public class bai10 {
    public boolean deleteListFileInfolder(String source) throws IOException {
        File folder = new File(source);
        //folder ton tai
        if(folder.exists()){
            //danh sach file
    
            File[] listFile = folder.listFiles();
                if(listFile != null && listFile.length != 0){
                for (File f: listFile){
                    if (f.isFile()){
                        f.delete();
                    }
                    if(f.isDirectory()){
                        deleteListFileInfolder(f.getAbsolutePath());
                    }
                }
            }
            folder.delete();
            System.out.println("Delete folder thanh cong");
            return true;
        }
        else{
            System.out.println("folder khong ton tai");
            return false;
        }
    }
    public static void main(String[] args) throws IOException{
        bai10 bai10=new bai10();
        bai10.deleteListFileInfolder("E:\\IUH\\PhatTrienTichHopHeThong\\ThucHanh\\DemoXoa");
    }
    
}
