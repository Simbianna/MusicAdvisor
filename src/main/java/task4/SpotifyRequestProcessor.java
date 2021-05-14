package task4;

import task4.advisor.JsonProcessor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static task4.advisor.JsonProcessor.printErrorMessage;
import static task4.advisor.Main.getAccessServerPath;
import static task4.advisor.Main.getResourceServerPath;


public class SpotifyRequestProcessor {

    private static final String REDIRECT_URL = "http://localhost:8080";
    private static final String CLIENT_ID = "";
    private static final String SECRET = "";

    public static void getSpotifyAccess(String code, User user) {
        System.out.println("making http request for access_token...");

        HttpClient client = HttpClient.newBuilder().build();
        String body = "grant_type=authorization_code&" +
                "code=" + code + "&redirect_uri=" + REDIRECT_URL
                + "&client_id=" + CLIENT_ID + "&client_secret=" + SECRET;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(getAccessServerPath() + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (checkStatusCodeIsOk(response.statusCode())) {
                user.setAccessToken(JsonProcessor.parseAccessToken(response.body()));
                user.setRefreshToken(JsonProcessor.parseRefreshToken(response.body()));
                System.out.println("Success!");
            } else printErrorMessage(response.body());

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static void getFeatured(User user) {
        String path = "/v1/browse/featured-playlists";
        HttpResponse<String> response = getRequestWithAuthHeader(user, path);
        System.out.println(JsonProcessor.parseFeatured(response.body()));
    }

    public static void getNew(User user) {
        String path = "/v1/browse/new-releases";
        HttpResponse<String> response = getRequestWithAuthHeader(user, path);
        System.out.println(JsonProcessor.parseNewAlbums(response.body()));

    }

    public static void getCategories(User user) {
        String path = "/v1/browse/categories";
        HttpResponse<String> response = getRequestWithAuthHeader(user, path);
        System.out.println(JsonProcessor.parseCategories(response.body()));
    }

    public static void getPlaylistsByCategory(User user, String category) {
        String catPath = "/v1/browse/categories";
        HttpResponse<String> catResponse = getRequestWithAuthHeader(user, catPath);
        String categoryId = JsonProcessor.getCategoryId(catResponse.body(), category);
        if (categoryId == null) {
            System.out.println("Unknown category name.");
        } else {
            catPath = "/v1/browse/categories/" + categoryId + "/playlists";
            HttpResponse<String> response = getRequestWithAuthHeader(user, catPath);
           if (checkStatusCodeIsOk(response.statusCode())) {
               System.out.println(JsonProcessor.parsePlayListsByCategory(response.body()));
           }
           else System.out.println("Unknown category name.");
        }
    }

    private static HttpResponse<String> getRequestWithAuthHeader(User user, String path) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + user.getAccessToken())
                .uri(URI.create(getResourceServerPath() + path))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401) {
                refreshToken(user);
            }
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }

    public static void refreshToken(User user) {
        HttpClient client = HttpClient.newBuilder().build();
        String body = "grant_type=refresh_token&" +
                "refresh_token=" + user.getRefreshToken();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(getAccessServerPath() + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            user.setAccessToken(JsonProcessor.parseAccessToken(response.body()));
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }

    public static boolean checkStatusCodeIsOk(int code) {
        return code >= 200 && code < 400;
    }

}
