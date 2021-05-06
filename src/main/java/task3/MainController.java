package task3;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import task3.databases.SpotifyMockDb;
import task3.entities.Album;
import task3.entities.PlayList;
import task3.entities.PlaylistCategory;
import task3.entities.User;
import task3.service.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class MainController {
    // private static final String API_URL = "https://accounts.spotify.com";
    CurrentUserParams currentUserParams = new CurrentUserParams();
    private final String REDIRECT_URL = "http://localhost:8080";
    private final String clientId;
    private final String secret;


    public MainController(Properties properties) {
        clientId = properties.getProperty("clientId");
        secret = properties.getProperty("secret");
    }

    public void chooseAction(String action, String authServerPath, SpotifyMockDb db, UserService service) {
        int userId = Integer.parseInt(currentUserParams.getUserID());
        if (action != null) {
            if (action.equals("auth")) {
                if (userId <= 0) {
                    auth(service, authServerPath);
                } else System.out.println("Already authenticated");
            }
            userId = Integer.parseInt(currentUserParams.getUserID());
            if (userId > 0) {
                User user = service.findUser(userId);

                //TODO check user exists

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
            }
        } else System.out.println("Please, provide access for application.");
    }

    public void auth(UserService service, String authServerPath) {
        HttpServer server = createAuthServer();
        if (server != null) {
            server.start();
            System.out.println("use this link to request the access code:");
            System.out.println(authServerPath
                    + "/authorize?client_id="
                    + clientId
                    + "&redirect_uri="
                    + REDIRECT_URL
                    + "&response_type=code");
            System.out.println("waiting for code...");

            String userCode = currentUserParams.getUserCode();
            long startTime = System.currentTimeMillis();

            while (userCode.equals("null") || userCode.equals("-1")) {
                userCode = currentUserParams.getUserCode();
                if (System.currentTimeMillis() - startTime > 120000) {
                    System.out.println("Time is up! try again");
                    break;
                }
                if (userCode.equals("-1")) {
                    System.out.println("Denied. Back to main");
                    currentUserParams.setUserCode("null");
                    break;
                }
            }
            server.stop(1);

            if (!userCode.equals("null") && !userCode.equals("-1")) {
                System.out.println("code received");
                User user = getUserByCode(userCode, service);
                currentUserParams.setUserID(user.getId());
                System.out.println("get Access");
                getSpotifyAccess(userCode, authServerPath);
            }
        } else System.out.println("Server does not response");
    }

    private HttpServer createAuthServer() {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            HttpContext context = server.createContext("/");
            HttpRequestHandler requestHandler = new HttpRequestHandler(currentUserParams);
            context.setHandler(requestHandler);
            return server;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

    private User getUserByCode(String userCode, UserService service) {
        User user;
        if ((user = service.findUserByUserCode(userCode)) != null) {
            System.out.println("User Found");
            return user;
        } else {
            user = new User();
            user.setAuthorized(true);
            user.setUserCode(userCode);
            service.saveUser(user);
        }
        return user;
    }


    private void getSpotifyAccess(String code, String serverPath) {
        System.out.println("making http request for access_token...\n" +
                "response:");

        HttpClient client = HttpClient.newBuilder().build();
        String body = "grant_type=authorization_code&" +
                "code=" + code + "&redirect_uri=" + REDIRECT_URL
                + "&client_id=" + clientId + "&client_secret=" + secret;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(serverPath + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            if (response.body().contains("access_token")) {
                System.out.println("---SUCCESS---");
            } else System.out.println("---ERROR---");

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private List<Album> getNewReleases(SpotifyMockDb db) {
        return db.getAlbums().stream().filter(Album::isNew).collect(Collectors.toList());
    }

    private List<PlayList> getFeatured(SpotifyMockDb db) {
        return db.getFeatured();
    }

    private List<PlayList> getPlayListByCategory(SpotifyMockDb db, PlaylistCategory category) {
        return db.getAllPlayLists().stream().filter(p -> p.getPlaylistCategory() == category).collect(Collectors.toList());
    }

    public void printNewAlbums(SpotifyMockDb db) {
        System.out.println("---NEW RELEASES---");
        for (Album a : getNewReleases(db)) {
            System.out.println(a);
        }
    }

    public void printFeatured(SpotifyMockDb db) {
        System.out.println("---FEATURED---");
        for (PlayList p : getFeatured(db)) {
            System.out.println(p.getName());
        }
    }

    public void printCategories() {
        System.out.println("---CATEGORIES---");
        Arrays.stream(PlaylistCategory.values())
                .forEach(v -> System.out.println(v.getVal()));
    }

    public void printPlaylistByCategory(SpotifyMockDb db, PlaylistCategory cat) {
        System.out.printf("---%s PLAYLISTS---\n", cat.toString());
        for (PlayList p : getPlayListByCategory(db, cat)) {
            System.out.println(p);
        }
    }


    private static void printUnAuthorised() {
        System.out.println("For authorisation print \"auth\"");
    }

    private static void printAuthorisedWelcome() {
        System.out.println("---Choose Action---");
        System.out.println("new\n" +
                "featured\n" +
                "categories" +
                "playlists");
    }

    private void printExit() {
        System.out.println("---GOODBYE!---");
    }

}
