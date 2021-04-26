package task2.entities;


import java.util.List;

public class Artist {

    private String name;

    private List<Album> albums;

    public Artist(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
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
