import java.util.Scanner;
public class bai3 {
    public static void main (String[] args){
        Scanner scanner = new Scanner (System.in);
        System.out.print("Vui long nhap so hang thu nhat: ");
        int soA =scanner.nextInt();
        System.out.print("Vui long nhap so hang thu hai: ");
        int soB= scanner.nextInt();
        int kq=soA+soB;
        System.out.println("Tinh tong:" + soA + " + "+ soB + "=" + kq);
    }
}
