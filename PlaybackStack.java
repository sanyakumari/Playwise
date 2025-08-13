package playback;

import models.Song;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * [cite_start]Manages playback history using a Stack (LIFO). [cite: 26]
 * Internally uses ArrayList for simplicity, but adheres to stack behavior.
 * Time and Space complexities are annotated.
 */
public class PlaybackStack {
    private List<Song> history; // Acts as the stack

    public PlaybackStack() {
        this.history = new ArrayList<>();
    }

    /**
     * Pushes a song onto the playback history stack.
     * Time Complexity: O(1) (amortized for ArrayList add)
     * Space Complexity: O(1) for adding one element
     * @param song The song to add to history.
     */
    public void push(Song song) {
        history.add(song);
    }

    /**
     * Pops the last played song from the history stack.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * @return The last played song.
     * @throws EmptyStackException if the history is empty.
     */
    public Song pop() {
        if (isEmpty()) {
            return null; // Or throw new EmptyStackException("Playback history is empty.");
        }
        return history.remove(history.size() - 1);
    }

    /**
     * Peeks at the last played song without removing it.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * @return The last played song.
     * @throws EmptyStackException if the history is empty.
     */
    public Song peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return history.get(history.size() - 1);
    }

    /**
     * Checks if the playback history is empty.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return history.isEmpty();
    }

    /**
     * Returns the current size of the playback history.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * @return The number of songs in history.
     */
    public int size() {
        return history.size();
    }

    /**
     * Returns a list of the most recent songs played, without modifying the stack.
     * Time Complexity: O(k) where k is the number of songs requested.
     * Space Complexity: O(k) for the new list.
     * @param count The number of recent songs to retrieve.
     * @return A list of recent songs, in order from oldest to newest of the 'recent' set.
     */
    public List<Song> getRecentSongs(int count) {
        List<Song> recent = new ArrayList<>();
        int startIndex = Math.max(0, history.size() - count);
        for (int i = startIndex; i < history.size(); i++) {
            recent.add(history.get(i));
        }
        return recent;
    }
}