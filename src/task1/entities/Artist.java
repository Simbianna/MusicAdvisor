package task1.entities;

import java.util.List;

public class Artist {

    private String name;

    private List<task1.entities.Album> albums;

    public Artist(String name) {
        this.name = name;
    }

    public List<task1.entities.Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<task1.entities.Album> albums) {
        this.albums = albums;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
