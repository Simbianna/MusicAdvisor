package task4.advisor;

import com.sun.net.httpserver.HttpServer;
import task4.SpotifyRequestProcessor;
import task4.User;
import task4.UserDb;

import java.io.IOException;
import java.net.InetSocketAddress;

import static task4.SpotifyRequestProcessor.getSpotifyAccess;
import static task4.advisor.Main.getAccessServerPath;


public class MainController {

    private static final String REDIRECT_URL = "http://localhost:8080";
    private static final String CLIENT_ID = "";

    public static void chooseAction(String action, User user, UserDb userDb) {
        if (action != null) {
            if (action.equals("auth")) auth(user, userDb);
            if (!userDb.getCode(user).equals("-1") && !userDb.getCode(user).equals("null")) {
                if (action.equals("new")) SpotifyRequestProcessor.getNew(user);
                else if (action.equals("featured")) SpotifyRequestProcessor.getFeatured(user);
                else if (action.equals("categories")) SpotifyRequestProcessor.getCategories(user);
                else if (action.contains("playlists")) {

                    String cat = action
                            .replace("playlists ", "")
                            .trim();
                    System.out.println(cat);
                    SpotifyRequestProcessor.getPlaylistsByCategory(user, cat);
                }
            } else System.out.println("Please, provide access for application.");
        }
    }

    public static void auth(User user, UserDb userDb) {
        HttpServer server = createServer(user, userDb);
        System.out.println(server);
        if (server != null) {
            server.start();
            System.out.println("use this link to request the access code:");
            System.out.println(getAccessServerPath()
                    + "/authorize?client_id="
                    + CLIENT_ID
                    + "&redirect_uri="
                    + REDIRECT_URL
                    + "&response_type=code");
            System.out.println("waiting for code...");

            String userCode = userDb.getCode(user);
            while (userCode.equals("null") || userCode.equals("-1")) {
                userCode = userDb.getCode(user);
                if (userCode.equals("-1")) {
                    userDb.setCode(user, "null");
                    break;
                }
            }
            System.out.println(userCode);
            server.stop(0);
            userCode = userDb.getCode(user);

            if (!userCode.equals("null") && !userCode.equals("-1")) {
                user.setAuthorized(true);
                getSpotifyAccess(userCode, user);
            }
        }
    }

    public static HttpServer createServer(User user, UserDb userDb) {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/", exchange -> {

                String query = exchange.getRequestURI().getQuery();
                String result;
                if (query != null && query.contains("error=")) {
                    result = "Authorization code not found. Try again.";
                    exchange.sendResponseHeaders(200, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                    exchange.close();

                } else {
                    result = "Got the code. Return back to your program.";
                    exchange.sendResponseHeaders(200, result.length());
                    userDb.setCode(user, query.replace("code=", ""));
                    user.setAuthorized(true);
                    System.out.println("code received");
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                    exchange.close();
                }
            });
            return server;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }
}
