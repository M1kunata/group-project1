package Project1_6713221;

//6713115 Kornchanok Phutrakul
//6713117 Nuttha Limkhunthammo
//6713221 jakkarin roemtangsakul

import java.io.*;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException
    {
        PrintStream buffered = new PrintStream(
            new BufferedOutputStream(System.out, 65536), false);
        System.setOut(buffered);

        main mainapp = new main();
        mainapp.gamestart();

        System.out.flush();
    }
    public void gamestart() {
        String restart="";
        while(!restart.equalsIgnoreCase("q")) {
            System.out.println("UwU GAME START!!!");
            System.out.println("><><<><><><><><><><>");
            Gamecontrol game = new Gamecontrol();
            game.runMannualMode();
            restart=game.getInput().getrestart();
        }
    }
}