package Project1_6713221;

//6713115 Kornchanok Phutrakul
//6713117 Nuttha Limkhunthammo
//6713221 jakkarin roemtangsakul

import java.util.InputMismatchException;
import java.util.Scanner;

public class Gamecontrol {
    private inputHandler input;
    private boolean solved = false;
    private Board board;
    private int i=0;
    public Gamecontrol() {
        input = new inputHandler();
    }
    public inputHandler getInput() {return input;}
    public void runMannualMode() // เริ่มรับค่าinput ศูนรวมจิตใจ
    {
        int boadsize = input.getN();// เอาตัวแปรที่ไปสร้างบอร์ดมาใส่
        board = new Board(boadsize);
        System.out.print("Initial >> ");
        board.display();
        while (!solved) {
            System.out.printf("Step %3d >> Enter Marble ID or A to switch to auto mode = ", i);
            String marbleid = input.getMarbleid(); // เอาค่าไปสลับบอร์ดต่อ
            if (marbleid.equalsIgnoreCase("A")) {
                runAutoMode();
                return;
            }
            if (!board.isMovable(marbleid)) {
                System.out.println("Cannot move " + marbleid);
            } else {
                String move = board.move(marbleid);
                System.out.println(move);
                board.display();
                solved = board.isGoal();
                i++;
            }
        }
        if(solved) System.out.println("Done !!!");
    }

    public void runAutoMode() {
        Solver solver = new Solver();
        boolean hasSolution = solver.solve(board);
        if (!hasSolution) {
            System.out.println("No solution !!");
            return;
        }
        int autoStep = 1;
        for (Moverecord record : solver.getSolution()) {
            board.move(record.getMarbleId());
            System.out.printf("Auto %3d >> %s%n", autoStep++, record.getMarbleId() + " (" + record.getMoveType() + ")");
            board.display();
        }
        System.out.println("Done !!!");
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