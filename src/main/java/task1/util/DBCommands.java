package task1.util;

import task1.entities.Album;
import task1.entities.DataBase;
import task1.entities.PlayList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DBCommands {

    public static void chooseAction(String action, DataBase db) {
        if (action != null) {
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
                        task1.entities.PlaylistCategory.valueOf(cat));
            } else if (action.equals("exit")) printExit();
        }
    }

    private static List<Album> getNewReleases(DataBase db) {
        return db.getAlbums().stream().filter(task1.entities.Album::isNew).collect(Collectors.toList());
    }

    private static List<PlayList> getFeatured(DataBase db) {
        return db.getFeatured();
    }

    private static List<PlayList> getPlayListByCategory(DataBase db, task1.entities.PlaylistCategory category) {
        return db.getAllPlayLists().stream().filter(p -> p.getPlaylistCategory() == category).collect(Collectors.toList());
    }

    public static void printNewAlbums(DataBase db) {
        System.out.println("---NEW RELEASES---");
        for (task1.entities.Album a : getNewReleases(db)) {
            System.out.println(a);
        }
    }

    public static void printFeatured(DataBase db) {
        System.out.println("---FEATURED---");
        for (PlayList p : getFeatured(db)) {
            System.out.println(p.getName());
        }
    }

    public static void printCategories() {
        System.out.println("---CATEGORIES---");
        Arrays.stream(task1.entities.PlaylistCategory.values())
                .forEach(v -> System.out.println(v.getVal()));
    }

    public static void printPlaylistByCategory(DataBase db, task1.entities.PlaylistCategory cat) {
        System.out.printf("---%s PLAYLISTS---\n", cat.toString());
        for (PlayList p : getPlayListByCategory(db, cat)) {
            System.out.println(p);
        }

    }

    private static void printExit() {
        System.out.println("---GOODBYE!---");
    }


}
