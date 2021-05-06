package task2;


import task2.entities.*;
import task2.entities.Album;
import task2.entities.DataBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DBCommands {
    private static final String API_URL = "https://accounts.spotify.com";
    private static final String REDIRECT_URL = "http://localhost:8080";

    public static void chooseAction(String action, DataBase db, User user) {
        if (action != null) {
            if (action.equals("auth")) auth(user);
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

    private static List<Album> getNewReleases(DataBase db) {
        return db.getAlbums().stream().filter(Album::isNew).collect(Collectors.toList());
    }

    private static List<PlayList> getFeatured(DataBase db) {
        return db.getFeatured();
    }

    private static List<PlayList> getPlayListByCategory(DataBase db, PlaylistCategory category) {
        return db.getAllPlayLists().stream().filter(p -> p.getPlaylistCategory() == category).collect(Collectors.toList());
    }

    public static void printNewAlbums(DataBase db) {
        System.out.println("---NEW RELEASES---");
        for (Album a : getNewReleases(db)) {
            System.out.println(a);
        }
    }

    public static void printFeatured(DataBase db) {
        System.out.println("---FEATURED---");
        for (PlayList p : getFeatured(db)) {
            System.out.println(p.getName());
        }
    }

    public static void auth(User user) {
        user.setAuthorized(true);
        System.out.println(API_URL
                + "/authorize?client_id="
                + user.getClientId()
                + "&redirect_uri="
                + REDIRECT_URL
                + "&response_type=code");
        System.out.println("---SUCCESS---");
    }

    public static void printCategories() {
        System.out.println("---CATEGORIES---");
        Arrays.stream(PlaylistCategory.values())
                .forEach(v -> System.out.println(v.getVal()));
    }

    public static void printPlaylistByCategory(DataBase db, PlaylistCategory cat) {
        System.out.printf("---%s PLAYLISTS---\n", cat.toString());
        for (PlayList p : getPlayListByCategory(db, cat)) {
            System.out.println(p);
        }

    }

    private static void printExit() {
        System.out.println("---GOODBYE!---");
    }


}
