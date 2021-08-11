import java.io.IOException;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws IOException {


        memoryAllocation sch= new memoryAllocation();

        System.out.println("************* Table *************");
        System.out.println("\n 1- First-Fit");
        System.out.println("\n 2- Best-Fit ");
        System.out.println("\n 3- Worst-Fit  ");

        System.out.println("\n************* Table *************");

        Scanner scanner = new Scanner(System.in);
        int select = 0;
        while (select != 5) {
            //sch.getinputs();
            System.out.println("Please enter your choice: (1,2,3)");

            select = scanner.nextInt();

            if (select == 1) {
                System.out.println("---- FIRST FIT -----");

                sch.getinputs();
                sch.firstFit();

            }else if(select==2){
                System.out.println("---- BEST FIT -----");
                sch.getinputs();
                sch.bestFit();

            }else if(select == 3){
                System.out.println("---- WORST FIT -----");
                sch.getinputs();
                sch.worstFit();

            }else{
                break;
            }

        }
    }
}
