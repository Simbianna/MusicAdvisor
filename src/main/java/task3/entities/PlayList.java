package task3.entities;

public class PlayList {
    private String name;

    private PlaylistCategory playlistCategory;

    public PlayList(String name) {
        this.name = name;
    }

    public PlayList(String name, PlaylistCategory playlistCategory) {
        this.name = name;
        this.playlistCategory = playlistCategory;
    }

    public PlaylistCategory getPlaylistCategory() {
        return playlistCategory;
    }

    public void setPlaylistCategory(PlaylistCategory playlistCategory) {
        this.playlistCategory = playlistCategory;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
