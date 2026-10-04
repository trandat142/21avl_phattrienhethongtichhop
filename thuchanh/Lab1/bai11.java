import java.io.File;

public class bai11 {
    public void findFile(String source, String key){
        File file = new File(source);
        if(file.exists()){
            if(file.isFile()){
                if(file.getName().endsWith(key)){
                    System.out.println(file.getAbsolutePath());
                }
                
            }
            File[] listFile=file.listFiles();
            if(listFile!=null){
                 for(File f: listFile){
                findFile(f.getAbsolutePath(),key);
            }
            }
          
        
        }
        else{
            System.out.println("source khong ton tai");
        }
    }
    public static void main(String[] args) {
    bai11 b = new bai11();
    b.findFile("E:\\IUH\\PhatTrienTichHopHeThong\\ThucHanh\\Lab1", ".java");
}
    
    
}
