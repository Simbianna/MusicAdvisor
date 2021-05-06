package task3;


import task3.databases.SpotifyMockDb;
import task3.service.UserService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;


public class Main {
    static Properties secureProperties = new Properties();
    private static final String SECURE_PROPERTIES_FILEPATH = "src/main/resources/data.properties";
    private static final String SERVER_PATH = "https://accounts.spotify.com";

    static {
        loadProperties();
    }

    public static void main(String[] args) {
        if (secureProperties == null) {
            System.out.println("Load properties and Start Program");
        } else {
            UserService service = new UserService();
            Scanner scanner = new Scanner(System.in);
            String line;
            SpotifyMockDb spotifyMockDb = new SpotifyMockDb();
            String serverPath = getServerPath(args);
            MainController mainController = new MainController(secureProperties);
            System.out.println("---Welcome!---");

            while (true) {
            //    printWelcome();
                line = scanner.nextLine();
                //   Controller.chooseAction(line, serverPath, spotifyMockDb, userDb, user);
                mainController.chooseAction(line, serverPath, spotifyMockDb, service);
                if (line.equals("exit")) {
                    break;
                }
            }
            scanner.close();
        }
    }

    private static String getServerPath(String[] args) {

        if (args != null && args.length > 0) {
            int position = -1;

            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-access")) {
                    position = i + 1;
                    break;
                }
            }

            if (position != -1 && position < args.length) {
                return args[position];
            } else return SERVER_PATH;
        } else return SERVER_PATH;
    }

    private static void loadProperties() {
        try {
            FileInputStream fis = new FileInputStream(SECURE_PROPERTIES_FILEPATH);
            secureProperties.load(fis);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void printWelcome() {
        System.out.println("---Choose Action---");
        System.out.println(
                "auth\n" +
                        "new\n" +
                        "featured\n" +
                        "categories\n" +
                        "playlists");
    }
}
