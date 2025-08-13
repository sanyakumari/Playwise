package search;

import models.Song;
import java.util.HashMap;
import java.util.Map;

/**
 * [cite_start]Provides instant song lookup using a HashMap. [cite: 40, 41]
 * [cite_start]Maps song_id or title to song metadata for O(1) average time lookup. [cite: 42, 44, 47]
 * [cite_start]It should be synced with updates in the playlist engine. [cite: 45]
 * Time and Space complexities are annotated.
 */
public class SongHashMap {
    // Maps song ID to Song object
    private Map<String, Song> songIdMap;
    // Maps song title to Song object (might have collisions if titles are not unique,
    // for simplicity, this will store the first encountered song for a title)
    private Map<String, Song> songTitleMap;

    public SongHashMap() {
        this.songIdMap = new HashMap<>();
        this.songTitleMap = new HashMap<>();
    }

    /**
     * Adds a song to the lookup maps.
     * Time Complexity: O(1) on average.
     * Space Complexity: O(1) for adding one element.
     * @param song The song to add.
     */
    public void addSong(Song song) {
        songIdMap.put(song.getId(), song);
        // Only put if title doesn't exist, or you could handle multiple songs with same title
        if (!songTitleMap.containsKey(song.getTitle().toLowerCase())) {
            songTitleMap.put(song.getTitle().toLowerCase(), song);
        }
    }

    /**
     * Removes a song from the lookup maps.
     * Time Complexity: O(1) on average.
     * Space Complexity: O(1).
     * @param songId The ID of the song to remove.
     * @return The removed Song object, or null if not found.
     */
    public Song removeSong(String songId) {
        Song removedSong = songIdMap.remove(songId);
        if (removedSong != null) {
            // Remove from title map only if it's the specific song we removed
            // This logic assumes a single song per title in the title map.
            if (songTitleMap.containsKey(removedSong.getTitle().toLowerCase()) &&
                songTitleMap.get(removedSong.getTitle().toLowerCase()).getId().equals(songId)) {
                songTitleMap.remove(removedSong.getTitle().toLowerCase());
            }
        }
        return removedSong;
    }

    /**
     * Searches for a song by its ID or title.
     * Priority given to ID search for uniqueness.
     * [cite_start]Time Complexity: O(1) on average. [cite: 42]
     * Space Complexity: O(1).
     * @param query The song ID or title to search for.
     * @return The found Song object, or null if not found.
     */
    public Song searchSong(String query) {
        // Try searching by ID first (assuming IDs are unique)
        if (songIdMap.containsKey(query)) {
            return songIdMap.get(query);
        }
        // Then try searching by title (case-insensitive)
        if (songTitleMap.containsKey(query.toLowerCase())) {
            return songTitleMap.get(query.toLowerCase());
        }
        return null;
    }

    /**
     * Returns the total number of unique songs stored.
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     * @return The number of songs.
     */
    public int size() {
        return songIdMap.size();
    }
}