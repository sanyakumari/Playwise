package ratings;

import models.Song;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the Binary Search Tree for song ratings.
 * [cite_start]Each node holds a rating bucket (1 to 5) and stores multiple songs with that rating. [cite: 33, 34]
 */
class BSTNode {
    int rating; // The rating this node represents (e.g., 1, 2, 3, 4, 5)
    List<Song> songs; // List of songs that have this rating
    BSTNode left;
    BSTNode right;

    public BSTNode(int rating, Song song) {
        this.rating = rating;
        this.songs = new ArrayList<>();
        this.songs.add(song);
        this.left = null;
        this.right = null;
    }

    // Getters
    public int getRating() {
        return rating;
    }

    public List<Song> getSongs() {
        return songs;
    }
}