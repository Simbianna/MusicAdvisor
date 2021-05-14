package task4.advisor;


import task4.User;
import task4.UserDb;

import java.util.Scanner;

public class Main {
    private static String accessServerPath = "https://accounts.spotify.com";
    private static String resourceServerPath = "https://api.spotify.com";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        UserDb userDb = new UserDb();
        userDb.addNewUser(user, "null");
        setAccessServerPath(args);
        setResourceServerPath(args);
        String line = scanner.nextLine();

        while (true) {
            MainController.chooseAction(line, user, userDb);
            if (line.equals("exit")) break;
            line = scanner.nextLine();
        }
    }

    private static void setAccessServerPath(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-access")) {
                    if (i + 1 < args.length) {
                        accessServerPath = args[i + 1];
                        break;
                    }
                }
            }
        }
    }

    private static void setResourceServerPath(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-resource")) {
                    if (i + 1 < args.length) {
                        resourceServerPath = args[i + 1];
                        break;
                    }
                }
            }
        }
    }

    public static String getAccessServerPath() {
        return accessServerPath;
    }

    public static void setAccessServerPath(String accessServerPath) {
        Main.accessServerPath = accessServerPath;
    }

    public static String getResourceServerPath() {
        return resourceServerPath;
    }

    public static void setResourceServerPath(String resourceServerPath) {
        Main.resourceServerPath = resourceServerPath;
    }
}
