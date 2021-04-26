package task2;

import task2.entities.DataBase;
import task2.entities.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line;
        DataBase dataBase = new DataBase();
        User user = new User();
        user.setClientId("af1a7946d23d4077b7cfe8d45eef8d38");

        while (true){
            line = scanner.nextLine();
            DBCommands.chooseAction(line, dataBase,user);
            if (line.equals("exit")) break;
        }
    }
}
