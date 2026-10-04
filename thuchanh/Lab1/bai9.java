import java.io.File;

public class bai9 {
    public boolean deleteFileInfolder (String source){
        File folder = new File(source);
        //folder ton tai
        if(folder.exists()){
            File[] listFile = folder.listFiles();
            if(listFile.length !=0){
                for (File f: listFile){
                    //file thi xoa
                    if (f.isFile()){
                        f.delete();
                    }
                }
            }
            folder.delete();
            System.out.println("Delete folder thanh cong!");
            return true;
        }
        else{
            System.out.println("folder khong ton tai");
            return false;
        }
    }
}
