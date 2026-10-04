import java.io.File;

public class bai8 {
    public boolean deleteEmptyFolder (String source){
        File folder = new File(source);
        //kiem tra neu folder ton tai thi xoa
        if (folder.exists())
        {
            folder.delete();
            System.out.println("folder ton tai, xoa folder thanh cong");
            return true;
        }
        else
        {
            System.out.println("folder khong ton tai");
        }
        return false;
    }
}
