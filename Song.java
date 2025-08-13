package models;

/**
 * Represents a song with its metadata.
 */
public class Song {
    private String id; // Unique identifier for the song
    private String title;
    private String artist;
    private long durationMs; // Duration in milliseconds
    private int rating; // Rating from 1 to 5, primarily for BST

    public Song(String id, String title, String artist, long durationMs, int rating) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationMs = durationMs;
        this.rating = rating;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public int getRating() {
        return rating;
    }

    // Setters (if needed, e.g., to update rating)
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Song{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", artist='" + artist + '\'' +
               ", durationMs=" + durationMs +
               ", rating=" + rating +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id.equals(song.id); // Songs are equal if their IDs are equal
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}