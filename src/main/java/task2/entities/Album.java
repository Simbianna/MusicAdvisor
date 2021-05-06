package task2.entities;

import java.util.List;

public class Album {

    private String name;

    private List<Artist> artists;

    private boolean isNew = true;

    public Album(String name) {
        this.name = name;
    }

    public Album(String name, List<Artist> artists) {
        this.name = name;
        this.artists = artists;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return name + " " + artists;
    }
}
