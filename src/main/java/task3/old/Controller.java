package task3.old;

import com.sun.net.httpserver.HttpServer;
import task3.databases.SpotifyMockDb;
import task3.databases.UserDb;
import task3.entities.Album;
import task3.entities.PlayList;
import task3.entities.PlaylistCategory;
import task3.entities.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Deprecated
public class Controller {
    // private static final String API_URL = "https://accounts.spotify.com";
    private static final String REDIRECT_URL = "http://localhost:8080";
    private static final String CLIENT_ID = "";
    //TODO put secret
    private static final String SECRET = "";


    public static void chooseAction(String action, String authServerPath, SpotifyMockDb db, UserDb userDb, User user) {
        if (action != null) {
            if (action.equals("auth")) auth(user, userDb, authServerPath);
            if (user.isAuthorized()) {
                if (action.equals("new")) printNewAlbums(db);
                else if (action.equals("featured")) printFeatured(db);
                else if (action.equals("categories")) printCategories();
                else if (action.contains("playlists")) {

                    String cat = action
                            .replace("playlists ", "")
                            .trim()
                            .replaceAll(" ", "_")
                            .toUpperCase();

                    printPlaylistByCategory(db,
                            PlaylistCategory.valueOf(cat));
                } else if (action.equals("exit")) printExit();
            } else System.out.println("Please, provide access for application.");
        }
    }

    public static void auth(User user, UserDb userDb, String authServerPath) {
        HttpServer server = createServerAndSaveUserCodeInDB(user, userDb);
        if (server != null) {
            server.start();
            System.out.println("use this link to request the access code:");
            System.out.println(authServerPath
                    + "/authorize?client_id="
                    + CLIENT_ID
                    + "&redirect_uri="
                    + REDIRECT_URL
                    + "&response_type=code");
            System.out.println("waiting for code...");

            String userCode = getUserCode(user, userDb);
            if (userCode.equals("null")) {
                while (userCode.equals("null")) {
                    userCode = getUserCode(user, userDb);
                }
            }
            server.stop(1);
            getSpotifyAccess(userCode, authServerPath);
        }
    }

    private static String getUserCode(User user, UserDb userDb) {
        if (userDb.containsUser(user) && !userDb.getCode(user).equals("null")) {
            return userDb.getCode(user);
        } else return "null";
    }

    public static HttpServer createServerAndSaveUserCodeInDB(User user, UserDb userDb) {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);

            server.createContext("/", exchange -> {

                String query = exchange.getRequestURI().getQuery();
                String result;
                if (query == null || query.contains("error=")) {
                    userDb.addNewUser(user, "null");
                    result = "Authorization code not found. Try again.";
                    exchange.sendResponseHeaders(200, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();

                } else {
                    result = "Got the code. Return back to your program.";
                    exchange.sendResponseHeaders(200, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(query);
                    userDb.addNewUser(user, query.replace("code=", ""));
                    user.setAuthorized(true);
                    System.out.println("code received");

                }
            });
            return server;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }


    private static void getSpotifyAccess(String code, String serverPath) {
        System.out.println("making http request for access_token...\n" +
                "response:");

        HttpClient client = HttpClient.newBuilder().build();
        String body = "grant_type=authorization_code&" +
                "code=" + code + "&redirect_uri=" + REDIRECT_URL
                + "&client_id=" + CLIENT_ID + "&client_secret=" + SECRET;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(serverPath + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            System.out.println("---SUCCESS---");

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }


    private static List<Album> getNewReleases(SpotifyMockDb db) {
        return db.getAlbums().stream().filter(Album::isNew).collect(Collectors.toList());
    }

    private static List<PlayList> getFeatured(SpotifyMockDb db) {
        return db.getFeatured();
    }

    private static List<PlayList> getPlayListByCategory(SpotifyMockDb db, PlaylistCategory category) {
        return db.getAllPlayLists().stream().filter(p -> p.getPlaylistCategory() == category).collect(Collectors.toList());
    }

    public static void printNewAlbums(SpotifyMockDb db) {
        System.out.println("---NEW RELEASES---");
        for (Album a : getNewReleases(db)) {
            System.out.println(a);
        }
    }

    public static void printFeatured(SpotifyMockDb db) {
        System.out.println("---FEATURED---");
        for (PlayList p : getFeatured(db)) {
            System.out.println(p.getName());
        }
    }


    public static void printCategories() {
        System.out.println("---CATEGORIES---");
        Arrays.stream(PlaylistCategory.values())
                .forEach(v -> System.out.println(v.getVal()));
    }

    public static void printPlaylistByCategory(SpotifyMockDb db, PlaylistCategory cat) {
        System.out.printf("---%s PLAYLISTS---\n", cat.toString());
        for (PlayList p : getPlayListByCategory(db, cat)) {
            System.out.println(p);
        }

    }

    private static void printExit() {
        System.out.println("---GOODBYE!---");
    }
}
