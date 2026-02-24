package Project1_6713221;

import java.util.Scanner;

public class main {
    public static void main(String[] args)
    {
        main mainapp = new main();
        mainapp.gamestart();
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
