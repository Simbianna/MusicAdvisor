package task1.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataBase {

    private List<Album> albums = new ArrayList<>();

    private List<PlayList> featured = new ArrayList<>();

    private List<PlayList> allPlayLists = new ArrayList<>();


    public DataBase() {
        task1.entities.Artist sia = new task1.entities.Artist("Sia");
        task1.entities.Artist diplo = new task1.entities.Artist("Diplo");
        task1.entities.Artist labrinth = new task1.entities.Artist("Labrinth");
        task1.entities.Artist lilPeep = new task1.entities.Artist("Lil Peep");
        task1.entities.Artist patd = new task1.entities.Artist("Panic! At The Disco");
        task1.entities.Artist slipknot = new task1.entities.Artist("Slipknot");

        task1.entities.Album mountains = new task1.entities.Album("Mountains", Arrays.asList(sia, diplo, labrinth));
        task1.entities.Album runaway = new task1.entities.Album("Runaway", Collections.singletonList(lilPeep));
        task1.entities.Album tgs = new task1.entities.Album("The Greatest Show", Collections.singletonList(patd));
        task1.entities.Album aol = new task1.entities.Album("All Out Life", Collections.singletonList(slipknot));

        albums.add(mountains);
        albums.add(runaway);
        albums.add(tgs);
        albums.add(aol);

        featured.add(new PlayList("Mellow Morning"));
        featured.add(new PlayList("Wake Up and Smell the Coffee"));
        featured.add(new PlayList("Monday Motivation"));
        featured.add(new PlayList("Songs to Sing in the Shower"));

        allPlayLists.add(new PlayList("Walk Like A Badass", task1.entities.PlaylistCategory.MOOD));
        allPlayLists.add(new PlayList("Rage Beats", task1.entities.PlaylistCategory.MOOD));
        allPlayLists.add(new PlayList("Arab Mood Booster", task1.entities.PlaylistCategory.MOOD));
        allPlayLists.add(new PlayList("Sunday Stroll", task1.entities.PlaylistCategory.MOOD));

    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<PlayList> getFeatured() {
        return featured;
    }

    public List<PlayList> getAllPlayLists() {
        return allPlayLists;
    }
}
