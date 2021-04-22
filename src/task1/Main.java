package task1;

import task1.entities.DataBase;
import task1.util.DBCommands;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line;
        DataBase dataBase = new DataBase();

        while (true){
            line = scanner.nextLine();
            DBCommands.chooseAction(line, dataBase);
            if (line.equals("exit")) break;
        }
    }
}
