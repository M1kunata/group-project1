package Project1_6713221;

import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Gamecontrol {
    private inputHandler input;
    private boolean solved = false;
    private boolean cansolve = false;
    private int i=0;
    public Gamecontrol() {
        input = new inputHandler();
    }
    public inputHandler getInput() {return input;}
    public void runMannualMode() // เริ่มรับค่าinput ศูนรวมจิตใจ
    {
        int boadsize = input.getN();// เอาตัวแปรที่ไปสร้างบอร์ดมาใส่
        while (!solved) {
            System.out.printf("Step %3d >> Enter Marble ID or A to switch to auto mode = ", i);
            String marbleid = input.getMarbleid(); // เอาค่าไปสลับบอร์ดต่อ
            if (marbleid.equalsIgnoreCase("A"))
                break;
            i++;
        }
        while (cansolve&&!solved) // เข็คว่าแก้ได้จริงรึเปล่าได้ค่อยปลิ้นออกมา
        {

        }
        if(solved) System.out.println("Done !!!");
    }
}
class inputHandler{ // ตัวรับinput ต่างๆ จาก user
    private Scanner in;
    public inputHandler() {
        in = new Scanner(System.in);
    }
    public int getN()
    {
        int input=0;
        while(input<2) {
                try{
                System.out.printf("Enter number of white marbles = ");
                input = in.nextInt();
                    in.nextLine();// เคลียร์ enter
            }catch (InputMismatchException e){
                System.out.println("type only number!!!");
                in.next(); //clear แสกนเนอร์
                }
            }
        return input;
    }
    public String getMarbleid()
    {
        String input="";
        input = in.nextLine();
        return input;
    }
    public String getrestart()
    {
        String input="";
        System.out.printf("%s\n","*".repeat(30));
        System.out.println("type any button to play again.else type q:");
        input = in.nextLine();
        return input;
    }
}