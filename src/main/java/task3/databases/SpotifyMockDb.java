package task3.databases;

import task3.entities.Album;
import task3.entities.Artist;
import task3.entities.PlayList;
import task3.entities.PlaylistCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpotifyMockDb {

    private List<Album> albums = new ArrayList<>();

    private List<PlayList> featured = new ArrayList<>();

    private List<PlayList> allPlayLists = new ArrayList<>();


    public SpotifyMockDb() {
        Artist sia = new Artist("Sia");
        Artist diplo = new Artist("Diplo");
        Artist labrinth = new Artist("Labrinth");
        Artist lilPeep = new Artist("Lil Peep");
        Artist patd = new Artist("Panic! At The Disco");
        Artist slipknot = new Artist("Slipknot");

        Album mountains = new Album("Mountains", Arrays.asList(sia, diplo, labrinth));
        Album runaway = new Album("Runaway", Collections.singletonList(lilPeep));
        Album tgs = new Album("The Greatest Show", Collections.singletonList(patd));
        Album aol = new Album("All Out Life", Collections.singletonList(slipknot));

        albums.add(mountains);
        albums.add(runaway);
        albums.add(tgs);
        albums.add(aol);

        featured.add(new PlayList("Mellow Morning"));
        featured.add(new PlayList("Wake Up and Smell the Coffee"));
        featured.add(new PlayList("Monday Motivation"));
        featured.add(new PlayList("Songs to Sing in the Shower"));

        allPlayLists.add(new PlayList("Walk Like A Badass", PlaylistCategory.MOOD));
        allPlayLists.add(new PlayList("Rage Beats", PlaylistCategory.MOOD));
        allPlayLists.add(new PlayList("Arab Mood Booster", PlaylistCategory.MOOD));
        allPlayLists.add(new PlayList("Sunday Stroll", PlaylistCategory.MOOD));

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
