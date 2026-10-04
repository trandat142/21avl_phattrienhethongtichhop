import java.util.Scanner;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
public class bai4{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println(">> Kiem tra so chan & le <<");
        System.out.println("Vui long nhap so can kiem tra ");
        int so = scanner.nextInt();
        if(so %2==0){
            System.out.println("So " + so + " la so chan.");

        }
        else{
           System.out.println("So " + so + " la so le");
        }
    }
}