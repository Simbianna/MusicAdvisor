package task1.entities;

public enum PlaylistCategory {
    TOP_LISTS("Top Lists"),
    POP("Pop"),
    MOOD("Mood"),
    LATIN("Latin");

    public final String val;

    PlaylistCategory(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
