package task1.entities;

public class PlayList {
    private String name;

    private task1.entities.PlaylistCategory playlistCategory;

    public PlayList(String name) {
        this.name = name;
    }

    public PlayList(String name, task1.entities.PlaylistCategory playlistCategory) {
        this.name = name;
        this.playlistCategory = playlistCategory;
    }

    public task1.entities.PlaylistCategory getPlaylistCategory() {
        return playlistCategory;
    }

    public void setPlaylistCategory(task1.entities.PlaylistCategory playlistCategory) {
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
